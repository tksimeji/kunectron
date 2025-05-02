package com.tksimeji.kunectron.controller;

import com.tksimeji.kunectron.Action;
import com.tksimeji.kunectron.Mouse;
import com.tksimeji.kunectron.element.ItemElement;
import com.tksimeji.kunectron.policy.ItemSlotPolicy;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ItemContainerGuiController<I extends Inventory> extends ContainerGuiController<I>, TickableGuiController {
    @Nullable ItemElement getElement(final int index);

    @NotNull Map<Integer, ItemElement> getElements();

    void setElement(final int index, final @Nullable ItemElement element);

    @NotNull ItemSlotPolicy getPolicy(final int index);

    @NotNull Map<Integer, ItemSlotPolicy> getPolicies();

    void setPolicy(final int index, final @NotNull ItemSlotPolicy policy);

    @NotNull ItemSlotPolicy getDefaultPolicy();

    void setDefaultPolicy(final @NotNull ItemSlotPolicy policy);

    @NotNull ItemSlotPolicy getPlayerDefaultPolicy();

    void setPlayerDefaultPolicy(final @NotNull ItemSlotPolicy policy);

    int getSize();

    boolean isValidIndex(final int index);

    @Nullable ItemStack createItemStack(final @Nullable ItemElement element);

    void click(final int index, final @NotNull Action action, final @NotNull Mouse mouse);
}
