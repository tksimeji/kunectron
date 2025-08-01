package com.tksimeji.kunectron.type;

import com.tksimeji.kunectron.SignGui;
import com.tksimeji.kunectron.controller.SignGuiController;
import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

@Singleton
public class SignGuiType implements GuiType<SignGui, SignGuiController> {
    private static final @NotNull SignGuiType instance = new SignGuiType();

    public static @NotNull SignGuiType instance() {
        return instance;
    }

    @Override
    public @NotNull Class<SignGui> getAnnotationClass() {
        return SignGui.class;
    }

    @Override
    public @NotNull Class<SignGuiController> getControllerClass() {
        return SignGuiController.class;
    }

    @Override
    public @NotNull SignGuiController createController(final @NotNull Object gui, @NotNull SignGui annotation) {
        return new SignGuiController(gui);
    }
}
