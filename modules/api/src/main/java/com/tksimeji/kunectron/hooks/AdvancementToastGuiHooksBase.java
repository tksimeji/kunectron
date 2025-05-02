package com.tksimeji.kunectron.hooks;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
interface AdvancementToastGuiHooksBase extends Hooks {
    @NotNull Player usePlayer();

    @NotNull ItemStack useIcon();

    @NotNull Component useMessage();
}
