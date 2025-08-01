package com.tksimeji.kunectron.hooks.impl;

import com.tksimeji.kunectron.controller.ContainerGuiController;
import com.tksimeji.kunectron.hooks.ContainerGuiHooks;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public interface ContainerGuiHooksImpl<T extends ContainerGuiController<?>> extends ContainerGuiHooks, HooksImpl<T> {
    @Override
    default @NotNull Player usePlayer() {
        return controller().getPlayer();
    }

    @Override
    default @NotNull Locale useLocale() {
        return controller().getLocale();
    }

    @Override
    default void useClose() {
        controller().close();
    }
}
