package com.tksimeji.kunectron.type;

import com.tksimeji.kunectron.AdvancementToastGui;
import com.tksimeji.kunectron.controller.AdvancementToastGuiController;
import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

@Singleton
public final class AdvancementToastGuiType implements GuiType<AdvancementToastGui, AdvancementToastGuiController> {
    private static final @NotNull AdvancementToastGuiType instance = new AdvancementToastGuiType();

    public static @NotNull AdvancementToastGuiType instance() {
        return instance;
    }

    private AdvancementToastGuiType() {
    }

    @Override
    public @NotNull Class<AdvancementToastGui> getAnnotationClass() {
        return AdvancementToastGui.class;
    }

    @Override
    public @NotNull Class<AdvancementToastGuiController> getControllerClass() {
        return AdvancementToastGuiController.class;
    }

    @Override
    public @NotNull AdvancementToastGuiController createController(final @NotNull Object gui, final @NotNull AdvancementToastGui annotation) {
        return new AdvancementToastGuiController(gui);
    }
}
