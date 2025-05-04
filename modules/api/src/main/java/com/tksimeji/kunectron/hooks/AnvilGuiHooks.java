package com.tksimeji.kunectron.hooks;

import com.tksimeji.kunectron.element.ItemElement;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

public interface AnvilGuiHooks extends ItemContainerGuiHooks {
    default @Nullable ItemElement useFirstElement() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void useFirstElement(@Nullable ItemElement element) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default @Nullable ItemElement useSecondElement() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void useSecondElement(@Nullable ItemElement element) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default @Nullable ItemElement useResultElement() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void useResultElement(@Nullable ItemElement element) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }
}
