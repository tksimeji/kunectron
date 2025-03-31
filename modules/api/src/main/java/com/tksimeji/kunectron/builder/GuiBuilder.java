package com.tksimeji.kunectron.builder;

import com.tksimeji.kunectron.hooks.AdvancementToastGuiHooks;
import com.tksimeji.kunectron.hooks.Hooks;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public interface GuiBuilder<B extends IGuiBuilder<B, H>, H extends Hooks> extends IGuiBuilder<B, H> {
    static @NotNull AdvancementToastGuiHooks advancementToast() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    static @NotNull AnvilGuiBuilder anvil() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    static @NotNull ChestGuiBuilder chest() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    static @NotNull MerchantGuiBuilder merchant() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    static @NotNull ScoreboardGuiBuilder scoreboard() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }
}
