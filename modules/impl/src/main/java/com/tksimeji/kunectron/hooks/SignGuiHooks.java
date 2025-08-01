package com.tksimeji.kunectron.hooks;

import com.tksimeji.kunectron.SignGui;
import com.tksimeji.kunectron.controller.SignGuiController;
import com.tksimeji.kunectron.hooks.impl.HooksImpl;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface SignGuiHooks extends HooksImpl<SignGuiController> {
    default @NotNull Player usePlayer() {
        return controller().getPlayer();
    }

    default @NotNull SignGui.SignType useType() {
        return controller().getType();
    }

    default @NotNull DyeColor useTextColor() {
        return controller().getTextColor();
    }

    default boolean useGlowing() {
        return controller().isGlowing();
    }

    default void useClose() {
        controller().close();
    }
}
