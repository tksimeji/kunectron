package com.tksimeji.kunectron.hooks;

import com.tksimeji.kunectron.element.TradeElement;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface MerchantGuiHooks extends ContainerGuiHooks {
    default @Nullable TradeElement useElement(final int index) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void useElement(final int index, final @NotNull TradeElement element) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void useAddElement(final @NotNull TradeElement element) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void useRemoveElement(final int index) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void useInsertElement(final int index, final @NotNull TradeElement element) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default @NotNull List<TradeElement> useElements() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }
}
