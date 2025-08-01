package com.tksimeji.kunectron.type;

import com.tksimeji.kunectron.HopperGui;
import com.tksimeji.kunectron.controller.HopperGuiController;
import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

@Singleton
public final class HopperGuiType implements GuiType<HopperGui, HopperGuiController> {
    private static final @NotNull HopperGuiType instance = new HopperGuiType();

    public static @NotNull HopperGuiType instance() {
        return instance;
    }

    @Override
    public @NotNull Class<HopperGui> getAnnotationClass() {
        return HopperGui.class;
    }

    @Override
    public @NotNull Class<HopperGuiController> getControllerClass() {
        return HopperGuiController.class;
    }

    @Override
    public @NotNull HopperGuiController createController(final @NotNull Object gui, final @NotNull HopperGui annotation) {
        return new HopperGuiController(gui, annotation);
    }
}
