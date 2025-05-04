package com.tksimeji.kunectron.hooks;

import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface AdvancementToastGuiHooks extends Hooks {
    default @NotNull Player usePlayer() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default @NotNull ItemStack useIcon() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default @NotNull Component useMessage() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }
}
