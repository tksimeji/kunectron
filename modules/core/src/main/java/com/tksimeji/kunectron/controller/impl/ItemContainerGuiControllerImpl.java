package com.tksimeji.kunectron.controller.impl;

import com.tksimeji.kunectron.Action;
import com.tksimeji.kunectron.IndexGroup;
import com.tksimeji.kunectron.Mouse;
import com.tksimeji.kunectron.controller.ItemContainerGuiController;
import com.tksimeji.kunectron.element.ItemElement;
import com.tksimeji.kunectron.policy.ItemSlotPolicy;
import com.tksimeji.kunectron.policy.Policy;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ItemContainerGuiControllerImpl<I extends Inventory> extends ContainerGuiControllerImpl<I> implements ItemContainerGuiController<I> {
    private final @NotNull Map<Integer, ItemElement> elements = new HashMap<>();

    private final @NotNull Map<Integer, ItemSlotPolicy> policies = new HashMap<>();

    private @NotNull ItemSlotPolicy defaultPolicy;
    private @NotNull ItemSlotPolicy playerDefaultPolicy;

    protected final boolean autoReload;
    protected final boolean serverSideTranslation;
    protected final boolean markupExtensions;

    public ItemContainerGuiControllerImpl(final @NotNull Object gui, final boolean autoReload, final boolean serverSideTranslation, final boolean markupExtensions) {
        this(gui, Policy.itemSlot(false), Policy.itemSlot(false), autoReload, serverSideTranslation, markupExtensions);
    }

    public ItemContainerGuiControllerImpl(final @NotNull Object gui, final @NotNull ItemSlotPolicy defaultPolicy, final @NotNull ItemSlotPolicy playerDefaultPolicy, final boolean autoReload, final boolean serverSideTranslation, final boolean markupExtensions) {
        super(gui);
        this.defaultPolicy = defaultPolicy;
        this.playerDefaultPolicy = playerDefaultPolicy;
        this.autoReload = autoReload;
        this.serverSideTranslation = serverSideTranslation;
        this.markupExtensions = markupExtensions;
    }

    @Override
    public @Nullable ItemElement getElement(final int index) {
        return elements.get(index);
    }

    @Override
    public @NotNull Map<Integer, ItemElement> getElements() {
        return new HashMap<>(elements);
    }

    @Override
    public void setElement(final int index, final @Nullable ItemElement element) {
        final ItemElement aElement = element != null ? element.clone() : null;
        final ItemStack oldItemStack = getInventory().getItem(index);

        if (!isValidIndex(index) || (element == null && oldItemStack == null)) {
            return;
        }

        final ItemStack itemStack = createItemStack(aElement);
        if (element != null && Objects.equals(itemStack, oldItemStack)) {
            return;
        }

        elements.put(index, aElement);
        getInventory().setItem(index, itemStack);
    }

    @Override
    public @NotNull ItemSlotPolicy getPolicy(final int index) {
        final ItemElement element = getElement(index);

        if (element != null) {
            final ItemSlotPolicy elementPolicy = element.policy();

            if (elementPolicy != null) {
                return elementPolicy;
            }
        }

        return Optional.ofNullable(policies.get(index)).orElse(index < 0 || isValidIndex(index) ? defaultPolicy : playerDefaultPolicy);
    }

    @Override
    public @NotNull Map<Integer, ItemSlotPolicy> getPolicies() {
        return new HashMap<>(policies.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> getPolicy(entry.getKey()))));
    }

    @Override
    public void setPolicy(final int index, final @NotNull ItemSlotPolicy policy) {
        policies.put(index, policy);
    }

    @Override
    public @NotNull ItemSlotPolicy getDefaultPolicy() {
        return defaultPolicy;
    }

    @Override
    public void setDefaultPolicy(final @NotNull ItemSlotPolicy defaultPolicy) {
        this.defaultPolicy = defaultPolicy;
    }

    @Override
    public @NotNull ItemSlotPolicy getPlayerDefaultPolicy() {
        return playerDefaultPolicy;
    }

    @Override
    public void setPlayerDefaultPolicy(final @NotNull ItemSlotPolicy playerDefaultPolicy) {
        this.playerDefaultPolicy = playerDefaultPolicy;
    }

    @Override
    public int getSize() {
        return getInventory().getSize();
    }

    @Override
    public boolean isValidIndex(final int index) {
        return index >= 0 && index < getSize();
    }

    @Override
    public @Nullable ItemStack createItemStack(final @Nullable ItemElement element) {
        if (element == null) return null;
        return element.createItemStack(serverSideTranslation ? getLocale() : null, markupExtensions ? markupExtensionContext : null);
    }

    @Override
    public void click(final int index, final @NotNull Action action, final @NotNull Mouse mouse) {
        final ItemElement element = getElement(index);
        if (element != null && element.sound() != null) {
            getPlayer().playSound(getPlayer(), Objects.requireNonNull(element.sound()), element.soundVolume(), element.soundPitch());
        }
    }

    @Override
    public void tick() {
        if (!autoReload) return;
        for (final Map.Entry<Integer, ItemElement> entry : getElements().entrySet()) {
            setElement(entry.getKey(), entry.getValue());
        }
    }

    protected @NotNull Set<Integer> parseIndexGroup(final @NotNull IndexGroup indexGroup, final boolean player) {
        return parseIndexGroup(indexGroup).stream().map(index -> player ? index + getInventory().getSize() : index).collect(Collectors.toSet());
    }

    protected @NotNull Set<Integer> parseIndexGroup(final int[] value, final @NotNull IndexGroup[] indexGroups, final boolean player) {
        return parseIndexGroup(value, indexGroups).stream().map(index -> player ? index + getInventory().getSize() : index).collect(Collectors.toSet());
    }
}
