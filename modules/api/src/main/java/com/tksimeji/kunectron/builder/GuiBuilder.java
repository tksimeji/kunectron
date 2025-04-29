package com.tksimeji.kunectron.builder;

import com.tksimeji.kunectron.hooks.Hooks;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public interface GuiBuilder<B extends IGuiBuilder<B, H>, H extends Hooks> extends IGuiBuilder<B, H> {
    static @NotNull AdvancementToastGuiBuilder advancementToast() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    static @NotNull AnvilGuiBuilder anvil() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    static @NotNull ChestGuiBuilder chest() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    static @NotNull HopperGuiBuilder hopper() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    static @NotNull MerchantGuiBuilder merchant() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    static @NotNull ScoreboardGuiBuilder scoreboard() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    static @NotNull SignGuiBuilder sign() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }
}
