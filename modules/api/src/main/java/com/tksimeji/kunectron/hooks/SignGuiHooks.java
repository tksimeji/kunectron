package com.tksimeji.kunectron.hooks;

import com.tksimeji.kunectron.SignGui;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface SignGuiHooks extends SignGuiHooksBase {
    @Override
    default @NotNull Player usePlayer() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    @Override
    default @NotNull SignGui.SignType useType() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    @Override
    default @NotNull DyeColor useTextColor() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    @Override
    default boolean useGlowing() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    @Override
    default void useClose() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }
}
