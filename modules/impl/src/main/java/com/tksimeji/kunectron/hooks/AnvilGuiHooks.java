package com.tksimeji.kunectron.hooks;

import com.tksimeji.kunectron.controller.AnvilGuiController;
import com.tksimeji.kunectron.element.ItemElement;
import com.tksimeji.kunectron.hooks.impl.ItemContainerGuiHooksImpl;
import org.jetbrains.annotations.Nullable;

public interface AnvilGuiHooks extends ItemContainerGuiHooksImpl<AnvilGuiController> {
    default @Nullable ItemElement useFirstElement() {
        return controller().getFirstElement();
    }

    default void useFirstElement(final @Nullable ItemElement element) {
        controller().setFirstElement(element);
    }

    default @Nullable ItemElement useSecondElement() {
        return controller().getSecondElement();
    }

    default void useSecondElement(final @Nullable ItemElement element) {
        controller().setSecondElement(element);
    }

    default @Nullable ItemElement useResultElement() {
        return controller().getResultElement();
    }

    default void useResultElement(final @Nullable ItemElement element) {
        controller().setResultElement(element);
    }
}
