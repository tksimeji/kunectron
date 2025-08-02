package com.tksimeji.kunectron;

import com.tksimeji.kunectron.adapter.Adapter;
import com.tksimeji.kunectron.controller.ContainerGuiController;
import com.tksimeji.kunectron.controller.GuiController;
import com.tksimeji.kunectron.markupextensions.MarkupExtensionsParser;
import com.tksimeji.kunectron.type.GuiType;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Set;

public final class Kunectron {
    public static <T> @NotNull T create(final @NotNull T gui) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    public static <T> @NotNull T create(final @NotNull T gui, final @NotNull Class<? extends Annotation> annotation) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    public static @NotNull Set<Object> getGuis() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    public static @NotNull Set<Object> getGuis(final @NotNull GuiType<?, ?> type) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    public static <T> @NotNull Set<T> getGuis(final @NotNull Class<T> javaClass) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    public static @Nullable GuiController getGuiController(final @NotNull Object gui) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    public static @NotNull GuiController getGuiControllerOrThrow(final @NotNull Object gui) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    public static <T extends Inventory> @Nullable ContainerGuiController<T> getGuiController(final @NotNull T inventory) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    public static <T extends Inventory> @NotNull ContainerGuiController<T> getGuiControllerOrThrow(final @NotNull T inventory) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    public static @NotNull Set<GuiController> getGuiControllers(final @NotNull GuiType<? ,?> type) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    public static <T extends GuiController> @NotNull Set<T> getGuiControllers(final @NotNull Class<T> javaClass) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    public static @NotNull Set<GuiController> getGuiControllers() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    public static void deleteGuiController(final @NotNull GuiController controller) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    public static <A extends Annotation> @Nullable GuiType<A, ?> getGuiType(final @NotNull Class<A> annotation) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    public static @NotNull Set<GuiType<?, ?>> getGuiTypes() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    public static void registerGuiType(final @NotNull GuiType<?, ?> type, final @NotNull Plugin plugin) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    public static @Nullable Adapter getAdapter(final @NotNull String minecraftVersion) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    public static @NotNull Set<Adapter> getAdapters() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    public static void registerAdapter(final @NotNull Adapter adapter) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    public static @NotNull MarkupExtensionsParser getMarkupExtensionsParser() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    public static void setMarkupExtensionsParser(final @NotNull MarkupExtensionsParser markupExtensionsParser) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }
}
