package com.tksimeji.kunectron.builder;

import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.event.GuiEvent;
import com.tksimeji.kunectron.event.GuiHandler;
import com.tksimeji.kunectron.hooks.Hooks;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;

abstract class AbstractGui<H extends Hooks> {
    private final @NotNull List<IGuiBuilderImpl.HandlerInfo> handlers;

    public AbstractGui(final @NotNull List<IGuiBuilderImpl.HandlerInfo> handlers) {
        this.handlers = handlers;
    }

    @GuiHandler
    public <E extends GuiEvent> void onEvent(final @NotNull E event) {
        final List<IGuiBuilderImpl.HandlerInfo> handlers = this.handlers.stream()
                .filter(handler -> handler.event().isAssignableFrom(event.getClass()))
                .toList();

        for (final IGuiBuilderImpl.HandlerInfo handler : handlers) {
            final GuiBuilderBase.HandlerFunction function = handler.function();

            if (handler.async()) {
                Bukkit.getScheduler().runTaskAsynchronously(Kunectron.plugin(), () -> {
                    if (function instanceof GuiBuilderBase.HandlerFunction1<?> handlerFunction1) {
                        ((GuiBuilderBase.HandlerFunction1<E>) handlerFunction1).onEvent(event);
                    } else if (function instanceof GuiBuilderBase.HandlerFunction2<?, ?> handlerFunction2) {
                        ((GuiBuilderBase.HandlerFunction2<E, H>) handlerFunction2).onEvent(event, (H) this);
                    }
                });
            } else {
                Bukkit.getScheduler().runTask(Kunectron.plugin(), () -> {
                    if (function instanceof GuiBuilderBase.HandlerFunction1<?> handlerFunction1) {
                        ((GuiBuilderBase.HandlerFunction1<E>) handlerFunction1).onEvent(event);
                    } else if (function instanceof GuiBuilderBase.HandlerFunction2<?, ?> handlerFunction2) {
                        ((GuiBuilderBase.HandlerFunction2<E, H>) handlerFunction2).onEvent(event, (H) this);
                    }
                });
            }
        }
    }
}