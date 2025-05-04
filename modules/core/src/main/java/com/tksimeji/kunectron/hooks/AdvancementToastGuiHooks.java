package com.tksimeji.kunectron.hooks;

import com.tksimeji.kunectron.controller.AdvancementToastGuiController;
import com.tksimeji.kunectron.hooks.impl.HooksImpl;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface AdvancementToastGuiHooks extends HooksImpl<AdvancementToastGuiController> {
    default @NotNull Player usePlayer() {
        return controller().getPlayer();
    }

    default @NotNull ItemStack useIcon() {
        return controller().getIcon();
    }

    default @NotNull Component useMessage() {
        return controller().getMessage();
    }
}
