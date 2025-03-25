package com.tksimeji.kunectron.hooks;

import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ContainerGuiHooks extends Hooks {
    default @NotNull Player usePlayer() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void useClose() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }
}
