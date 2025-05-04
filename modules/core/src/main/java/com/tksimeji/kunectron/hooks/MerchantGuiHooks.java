package com.tksimeji.kunectron.hooks;

import com.google.common.base.Preconditions;
import com.tksimeji.kunectron.controller.MerchantGuiController;
import com.tksimeji.kunectron.element.TradeElement;
import com.tksimeji.kunectron.hooks.impl.ContainerGuiHooksImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface MerchantGuiHooks extends ContainerGuiHooksImpl<MerchantGuiController> {
    default @Nullable TradeElement useElement(final int index) {
        return controller().getElement(index);
    }

    default void useElement(final int index, final @NotNull TradeElement element) {
        Preconditions.checkArgument(element != null, "Element cannot be null.");
        controller().setElement(index, element);
    }

    default void useAddElement(final @NotNull TradeElement element) {
        Preconditions.checkArgument(element != null, "Element cannot be null.");
        controller().addElement(element);
    }

    default void useRemoveElement(final int index) {
        controller().removeElement(index);
    }

    default void useInsertElement(final int index, final @NotNull TradeElement element) {
        Preconditions.checkArgument(element != null, "Element cannot be null.");
        controller().insertElement(index, element);
    }

    default @NotNull List<TradeElement> useElements() {
        return controller().getElements();
    }
}
