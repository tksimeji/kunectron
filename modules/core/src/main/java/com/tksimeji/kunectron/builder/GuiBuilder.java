package com.tksimeji.kunectron.builder;

import com.tksimeji.kunectron.hooks.Hooks;
import org.jetbrains.annotations.NotNull;

public interface GuiBuilder<B extends GuiBuilderBase<B, H>, H extends Hooks> extends GuiBuilderBase<B, H> {
    static @NotNull AdvancementToastGuiBuilder advancementToast() {
        return new AdvancementToastGuiBuilderImpl();
    }

    static @NotNull AnvilGuiBuilder anvil() {
        return new AnvilGuiBuilderImpl();
    }

    static @NotNull ChestGuiBuilder chest() {
        return new ChestGuiBuilderImpl();
    }

    static @NotNull DispenserGuiBuilder dispenser() {
        return new DispenserGuiBuilderImpl();
    }

    static @NotNull HopperGuiBuilder hopper() {
        return new HopperGuiBuilderImpl();
    }

    static @NotNull MerchantGuiBuilder merchant() {
        return new MerchantGuiBuilderImpl();
    }

    static @NotNull ScoreboardGuiBuilder scoreboard() {
        return new ScoreboardGuiBuilderImpl();
    }

    static @NotNull SignGuiBuilder sign() {
        return new SignGuiBuilderImpl();
    }
}
