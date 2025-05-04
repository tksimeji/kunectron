package com.tksimeji.kunectron.hooks;

import com.tksimeji.kunectron.element.ItemElement;
import com.tksimeji.kunectron.policy.ItemSlotPolicy;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ItemContainerGuiHooks extends ContainerGuiHooks {
    default @Nullable ItemElement useElement(final int index) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void useElement(final int index, final @Nullable ItemElement element) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default @NotNull Map<Integer, ItemElement> useElements() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default @NotNull ItemSlotPolicy usePolicy(final int index) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default @NotNull ItemSlotPolicy usePolicy(final int index, final boolean player) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void usePolicy(final int index, final @NotNull ItemSlotPolicy policy) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void usePolicy(final int index, final @NotNull ItemSlotPolicy policy, final boolean player) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default @NotNull ItemSlotPolicy useDefaultPolicy() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void useDefaultPolicy(final @NotNull ItemSlotPolicy defaultPolicy) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default @NotNull ItemSlotPolicy usePlayerDefaultPolicy() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void usePlayerDefaultPolicy(final @NotNull ItemSlotPolicy playerDefaultPolicy) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default boolean useIsEmpty() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }
}
