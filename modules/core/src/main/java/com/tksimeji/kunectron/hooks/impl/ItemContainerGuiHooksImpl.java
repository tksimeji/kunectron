package com.tksimeji.kunectron.hooks.impl;

import com.google.common.base.Preconditions;
import com.tksimeji.kunectron.controller.ItemContainerGuiController;
import com.tksimeji.kunectron.element.ItemElement;
import com.tksimeji.kunectron.hooks.ItemContainerGuiHooks;
import com.tksimeji.kunectron.policy.ItemSlotPolicy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ItemContainerGuiHooksImpl<T extends ItemContainerGuiController<?>> extends ContainerGuiHooksImpl<T>, ItemContainerGuiHooks {
    @Override
    default @Nullable ItemElement useElement(final int index) {
        return controller().getElement(index);
    }

    @Override
    default void useElement(final int index, final @Nullable ItemElement element) {
        controller().setElement(index, element);
    }

    @Override
    default @NotNull Map<Integer, ItemElement> useElements() {
        return controller().getElements();
    }

    @Override
    default @NotNull ItemSlotPolicy usePolicy(final int index) {
        return controller().getPolicy(index);
    }

    @Override
    default @NotNull ItemSlotPolicy usePolicy(final int index, final boolean player) {
        return usePolicy(player ? controller().getSize() + index : index);
    }

    @Override
    default void usePolicy(final int index, final @NotNull ItemSlotPolicy policy) {
        Preconditions.checkArgument(policy != null, "Policy cannot be null.");
        controller().setPolicy(index, policy);
    }

    @Override
    default void usePolicy(final int index, final @NotNull ItemSlotPolicy policy, boolean player) {
        usePolicy(player ? controller().getSize() + index : index, policy);
    }

    @Override
    default @NotNull ItemSlotPolicy useDefaultPolicy() {
        return controller().getDefaultPolicy();
    }

    @Override
    default void useDefaultPolicy(@NotNull ItemSlotPolicy defaultPolicy) {
        Preconditions.checkArgument(defaultPolicy != null, "Default policy cannot be null.");
        controller().setDefaultPolicy(defaultPolicy);
    }

    @Override
    default @NotNull ItemSlotPolicy usePlayerDefaultPolicy() {
        return controller().getPlayerDefaultPolicy();
    }

    @Override
    default void usePlayerDefaultPolicy(@NotNull ItemSlotPolicy playerDefaultPolicy) {
        Preconditions.checkArgument(playerDefaultPolicy != null, "Player default policy cannot be null.");
        controller().setPlayerDefaultPolicy(playerDefaultPolicy);
    }

    @Override
    default boolean useIsEmpty() {
        return useElements().isEmpty();
    }
}
