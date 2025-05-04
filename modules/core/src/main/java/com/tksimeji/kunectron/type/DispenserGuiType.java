package com.tksimeji.kunectron.type;

import com.tksimeji.kunectron.DispenserGui;
import com.tksimeji.kunectron.controller.DispenserGuiController;
import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

@Singleton
public final class DispenserGuiType implements GuiType<DispenserGui, DispenserGuiController> {
    private static final @NotNull DispenserGuiType instance = new DispenserGuiType();

    public static @NotNull DispenserGuiType instance() {
        return instance;
    }

    @Override
    public @NotNull Class<DispenserGui> getAnnotationClass() {
        return DispenserGui.class;
    }

    @Override
    public @NotNull Class<DispenserGuiController> getControllerClass() {
        return DispenserGuiController.class;
    }

    @Override
    public @NotNull DispenserGuiController createController(final @NotNull Object gui, final @NotNull DispenserGui annotation) {
        return new DispenserGuiController(gui, annotation);
    }
}
