package com.tksimeji.kunectron.type;

import com.tksimeji.kunectron.*;
import com.tksimeji.kunectron.controller.GuiController;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

public interface GuiType<A extends Annotation, C extends GuiController> extends GuiTypeBase<A, C> {
    static @NotNull GuiType<AdvancementToastGui, ?> advancementToast() {
        return AdvancementToastGuiType.instance();
    }

    static @NotNull GuiType<AnvilGui, ?> anvil() {
        return AnvilGuiType.instance();
    }

    static @NotNull GuiType<ChestGui, ?> chest() {
        return ChestGuiType.instance();
    }

    static @NotNull GuiType<DispenserGui, ?> dispenser() {
        return DispenserGuiType.instance();
    }

    static @NotNull GuiType<HopperGui, ?> hopper() {
        return HopperGuiType.instance();
    }

    static @NotNull GuiType<MerchantGui, ?> merchant() {
        return MerchantGuiType.instance();
    }

    static @NotNull GuiType<ScoreboardGui, ?> scoreboard() {
        return ScoreboardGuiType.instance();
    }

    static @NotNull GuiType<SignGui, ?> sign() {
        return SignGuiType.instance();
    }
}
