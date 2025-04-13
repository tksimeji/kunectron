package com.tksimeji.kunectron.hooks;

import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.SignGui;
import com.tksimeji.kunectron.controller.GuiController;
import com.tksimeji.kunectron.controller.SignGuiController;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface SignGuiHooks extends ISignGuiHooks {
    @Override
    default @NotNull Player usePlayer() {
        return controller().getPlayer();
    }

    @Override
    default @NotNull SignGui.SignType useType() {
        return controller().getType();
    }

    @Override
    default @NotNull DyeColor useTextColor() {
        return controller().getTextColor();
    }

    @Override
    default boolean useGlowing() {
        return controller().isGlowing();
    }

    @Override
    default void useClose() {
        controller().close();
    }

    private @NotNull SignGuiController controller() {
        final GuiController controller = Kunectron.getGuiController(this);
        if (controller instanceof SignGuiController signGuiController) {
            return signGuiController;
        }
        throw new IllegalStateException("No gui controller found.");
    }
}
