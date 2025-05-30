package com.tksimeji.kunectron.controller.impl;

import com.tksimeji.kunectron.IndexGroup;
import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.controller.GuiController;
import com.tksimeji.kunectron.event.CancellableEvent;
import com.tksimeji.kunectron.event.GuiEvent;
import com.tksimeji.kunectron.event.GuiHandler;
import com.tksimeji.kunectron.event.bukkit.KunectronGuiEventCallEvent;
import com.tksimeji.kunectron.event.bukkit.KunectronGuiEventCalledEvent;
import com.tksimeji.kunectron.markupextensions.context.Context;
import com.tksimeji.kunectron.markupextensions.context.MutableContext;
import com.tksimeji.kunectron.util.Classes;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.IntStream;

public abstract class GuiControllerImpl implements GuiController {
    public static @NotNull Set<Integer> parseIndexGroup(final @NotNull IndexGroup indexGroup) {
        final HashSet<Integer> indexes = new HashSet<>(Arrays.stream(indexGroup.indexes()).boxed().toList());

        if (indexGroup.indexFrom() >= 0 && indexGroup.indexFrom() <= indexGroup.indexTo()) {
            indexes.addAll(IntStream.rangeClosed(indexGroup.indexFrom(), indexGroup.indexTo()).boxed().toList());
        }

        for (final int expectIndex : indexGroup.expectIndexes()) {
            indexes.remove(expectIndex);
        }

        return indexes;
    }

    public static @NotNull Set<Integer> parseIndexGroup(final int[] value, final @NotNull IndexGroup[] indexGroups) {
        final Set<Integer> indexes = new HashSet<>(Arrays.stream(value).boxed().toList());

        for (final IndexGroup indexGroup : indexGroups) {
            indexes.addAll(parseIndexGroup(indexGroup));
        }

        return indexes;
    }

    protected final @NotNull Object gui;

    protected final @NotNull MutableContext<?> markupExtensionContext;

    protected final @NotNull Set<Method> handlers = new LinkedHashSet<>();

    public GuiControllerImpl(final @NotNull Object gui) {
        this.gui = gui;
        markupExtensionContext = Context.mutable(gui);

        Classes.getMethods(gui.getClass()).stream()
                .filter(method -> method.isAnnotationPresent(GuiHandler.class) &&
                        method.getParameters().length == 1 &&
                        GuiEvent.class.isAssignableFrom(method.getParameterTypes()[0]))
                .sorted(Comparator.comparingInt(method -> method.getAnnotation(GuiHandler.class).priority()))
                .forEach(handlers::add);
    }

    @Override
    public @NotNull Object getGui() {
        return gui;
    }

    @Override
    public @NotNull Context<?> getContext() {
        return markupExtensionContext;
    }

    @Override
    public void setState(final @NotNull String name, final @Nullable Object value) {
        markupExtensionContext.setState(name, value);
    }

    @Override
    public boolean callEvent(final @NotNull GuiEvent event) {
        final List<Method> handlers = this.handlers.stream().filter(handler -> handler.getParameterTypes()[0].isAssignableFrom(event.getClass())).toList();

        if (!new KunectronGuiEventCallEvent(event, this).callEvent()) {
            if (event instanceof CancellableEvent cancellableEvent) {
                return cancellableEvent.isCancelled();
            }

            return false;
        }

        for (final Method handler : handlers) {
            final GuiHandler annotation = handler.getAnnotation(GuiHandler.class);

            if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && annotation.ignoreCancelled()) {
                continue;
            }

            handler.setAccessible(true);

            Runnable runnable = () -> {
                try {
                    handler.invoke(gui, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            };

            if (annotation.async()) {
                Bukkit.getScheduler().runTaskAsynchronously(Kunectron.plugin(), runnable);
            } else if (!Bukkit.isPrimaryThread()) {
                Bukkit.getScheduler().runTask(Kunectron.plugin(), runnable);
            } else {
                runnable.run();
            }
        }

        new KunectronGuiEventCalledEvent(event, this).callEvent();

        return event instanceof Cancellable && ((Cancellable) event).isCancelled();
    }

    protected final  <A extends Annotation, T> @NotNull Optional<Pair<T, A>> getDeclaration(final @NotNull Object gui, final @NotNull Class<A> annotation, final @NotNull Class<T> aClass) {
        return getDeclarations(gui, annotation, aClass).stream().findFirst();
    }

    protected final  <A extends Annotation, T> @NotNull Pair<T, A> getDeclarationOrDefault(final @NotNull Object gui, final @NotNull Class<A> annotation, final @NotNull Class<T> aClass, final @NotNull T defaultValue) {
        return getDeclaration(gui, annotation, aClass).orElseGet(() -> Pair.of(defaultValue, null));
    }

    protected final <A extends Annotation, T> @NotNull Pair<T, A> getDeclarationOrThrow(final @NotNull Object gui, final @NotNull Class<A> annotation, final @NotNull Class<T> aClass) {
        final Pair<T, A> pair = getDeclaration(gui, annotation, aClass).orElse(null);
        if (pair == null) {
            throw new NullPointerException(String.format("Required declaration field @%s %s not found.", annotation.getName(), aClass.getName()));
        }
        return pair;
    }

    @SuppressWarnings("unchecked")
    protected final <A extends Annotation, T> @NotNull List<Pair<T, A>> getDeclarations(final @NotNull Object gui, final @NotNull Class<A> annotation, final @NotNull Class<T> aClass) {
        return Classes.getFields(gui.getClass()).stream()
                .filter(field -> field.isAnnotationPresent(annotation))
                .peek(field -> field.setAccessible(true))
                .filter(field -> {
                    try {
                        final Object value = field.get(gui);
                        return value != null && aClass.isAssignableFrom(value.getClass());
                    } catch (final IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(field -> {
                    try {
                        return Pair.of((T) field.get(gui), field.getAnnotation(annotation));
                    } catch (final IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }
}
