package com.tksimeji.kunectron.hooks;

import com.tksimeji.kunectron.SignGui;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
interface SignGuiHooksBase extends Hooks {
    @NotNull Player usePlayer();

    @NotNull SignGui.SignType useType();

    @NotNull DyeColor useTextColor();

    boolean useGlowing();

    void useClose();
}
