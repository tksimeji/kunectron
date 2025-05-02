package com.tksimeji.kunectron.type;

import com.tksimeji.kunectron.controller.GuiController;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

public interface GuiType<A extends Annotation, C extends GuiController> extends GuiTypeBase<A, C> {
    static @NotNull GuiType<?, ?> advancementToast() {
        return AdvancementToastGuiType.instance();
    }

    static @NotNull GuiType<?, ?> anvil() {
        return AnvilGuiType.instance();
    }

    static @NotNull GuiType<?, ?> chest() {
        return ChestGuiType.instance();
    }

    static @NotNull GuiType<?, ?> merchant() {
        return MerchantGuiType.instance();
    }

    static @NotNull GuiType<?, ?> scoreboard() {
        return ScoreboardGuiType.instance();
    }

    static @NotNull GuiType<?, ?> sign() {
        return SignGuiType.instance();
    }
}
