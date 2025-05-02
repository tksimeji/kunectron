package com.tksimeji.kunectron.hooks;

import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.controller.AdvancementToastGuiController;
import com.tksimeji.kunectron.controller.GuiController;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface AdvancementToastGuiHooks extends AdvancementToastGuiHooksBase {
    @Override
    default @NotNull Player usePlayer() {
        return controller().getPlayer();
    }

    @Override
    default @NotNull ItemStack useIcon() {
        return controller().getIcon();
    }

    @Override
    default @NotNull Component useMessage() {
        return controller().getMessage();
    }

    private @NotNull AdvancementToastGuiController controller() {
        final GuiController controller = Kunectron.getGuiController(this);
        if (controller instanceof AdvancementToastGuiController advancementToastGuiController) {
            return advancementToastGuiController;
        }
        throw new IllegalStateException("No gui controller found.");
    }
}
