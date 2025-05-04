package com.tksimeji.kunectron.type;

import com.tksimeji.kunectron.*;
import com.tksimeji.kunectron.controller.GuiController;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

public interface GuiType<A extends Annotation, C extends GuiController> extends GuiTypeBase<A, C> {
    static @NotNull GuiType<AdvancementToastGui, ?> advancementToast() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    static @NotNull GuiType<AnvilGui, ?> anvil() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    static @NotNull GuiType<ChestGui, ?> chest() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    static @NotNull GuiType<DispenserGui, ?> dispenser() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    static @NotNull GuiType<MerchantGui, ?> merchant() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    static @NotNull GuiType<ScoreboardGui, ?> scoreboard() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    static @NotNull GuiType<SignGui, ?> sign() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }
}
