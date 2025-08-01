package com.tksimeji.kunectron.hooks.impl;

import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.controller.GuiController;
import com.tksimeji.kunectron.hooks.Hooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface HooksImpl<T extends GuiController> extends Hooks {
    IllegalStateException controllerNotFoundException = new IllegalStateException("No gui controller found.");

    @Override
    default void useState(final @NotNull String name, final @Nullable Object value) {
        controller().setState(name, value);
    }

    @SuppressWarnings("unchecked")
    default @NotNull T controller() {
        final GuiController controller = Kunectron.getGuiController(this);
        if (controller == null) throw controllerNotFoundException;
        try {
            return (T) controller;
        } catch (final ClassCastException e) {
            throw controllerNotFoundException;
        }
    }
}
