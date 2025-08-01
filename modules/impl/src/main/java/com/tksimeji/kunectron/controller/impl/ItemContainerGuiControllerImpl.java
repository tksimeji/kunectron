package com.tksimeji.kunectron.controller.impl;

import com.tksimeji.kunectron.Action;
import com.tksimeji.kunectron.IndexGroup;
import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.Mouse;
import com.tksimeji.kunectron.controller.ItemContainerGuiController;
import com.tksimeji.kunectron.element.ItemElement;
import com.tksimeji.kunectron.policy.ItemSlotPolicy;
import com.tksimeji.kunectron.policy.Policy;
import com.tksimeji.kunectron.util.Components;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class ItemContainerGuiControllerImpl<I extends Inventory> extends ContainerGuiControllerImpl<I> implements ItemContainerGuiController<I> {
    private @NotNull Component title = Component.empty();
    private @NotNull Component displayedTitle = title;

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

    public ItemContainerGuiControllerImpl(
            final @NotNull Object gui,
            final @NotNull ItemSlotPolicy defaultPolicy,
            final @NotNull ItemSlotPolicy playerDefaultPolicy,
            final boolean autoReload,
            final boolean serverSideTranslation,
            final boolean markupExtensions
    ) {
        super(gui);
        this.defaultPolicy = defaultPolicy;
        this.playerDefaultPolicy = playerDefaultPolicy;
        this.autoReload = autoReload;
        this.serverSideTranslation = serverSideTranslation;
        this.markupExtensions = markupExtensions;
    }

    @Override
    public @NotNull Component getTitle() {
        return title;
    }

    @Override
    public void setTitle(final @NotNull Component title) {
        this.title = title;
        sendTitle();
    }

    @Override
    public void sendTitle() {
        final Component title = markupExtensions ? Components.markupExtensions(this.title, markupExtensionContext) : this.title;
        if (title.equals(displayedTitle)) return;
        Kunectron.adapterOrThrow().sendInventoryTitleUpdate(getPlayer().getOpenInventory(), title);
        displayedTitle = title;
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
        final ItemStack oldItemStack = getInventory().getItem(index);

        if (!isValidIndex(index) || (element == null && oldItemStack == null)) {
            return;
        }

        final ItemStack itemStack = createItemStack(element);

        if (element != null && Objects.equals(itemStack, oldItemStack)) {
            return;
        }

        elements.put(index, element != null ? element.clone() : null);
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
        if (element == null) {
            return;
        }
        final Sound sound = element.sound();
        if (sound != null) {
            getPlayer().playSound(sound);
        }
    }

    @Override
    public void tick() {
        if (!autoReload) return;
        if (markupExtensions) {
            sendTitle();
        }
        for (final Map.Entry<Integer, ItemElement> entry : getElements().entrySet()) {
            setElement(entry.getKey(), entry.getValue());
        }
    }

    protected @NotNull Component titleFromField(final @NotNull Class<? extends Annotation> annotation) {
        final Component value = getDeclarationOrDefault(gui, annotation, ComponentLike.class, Component.empty()).getLeft().asComponent();
        final Component title1 = markupExtensions ? Components.markupExtensions(value, markupExtensionContext) : value;
        this.title = value;
        return title1;
    }

    protected <A extends Annotation> void elementsFromFields(final @NotNull Class<A> annotation, final Function<A, Set<Integer>> parser) {
        for (Pair<ItemElement, A> declaration : getDeclarations(gui, annotation, ItemElement.class)) {
            for (int index : parser.apply(declaration.getRight())) {
                setElement(index, declaration.getLeft());
            }
        }
    }

    protected <A extends Annotation> void policiesFromFields(final @NotNull Class<A> annotation, final Function<A, Set<Integer>> parser) {
        for (Pair<ItemSlotPolicy, A> declaration : getDeclarations(gui, annotation, ItemSlotPolicy.class)) {
            for (int index : parser.apply(declaration.getRight())) {
                setPolicy(index, declaration.getLeft());
            }
        }
    }

    protected void defaultPolicyFromField(final @NotNull Class<? extends Annotation> annotation) {
        getDeclaration(gui, annotation, ItemSlotPolicy.class).ifPresent(declaration -> {
            setDefaultPolicy(declaration.getLeft());
        });
    }

    protected void playerDefaultPolicyFromField(final @NotNull Class<? extends Annotation> annotation) {
        getDeclaration(gui, annotation, ItemSlotPolicy.class).ifPresent(declaration -> {
            setPlayerDefaultPolicy(declaration.getLeft());
        });
    }

    protected @NotNull Set<Integer> parseIndexGroup(final @NotNull IndexGroup indexGroup, final boolean player) {
        return parseIndexGroup(indexGroup).stream().map(index -> player ? index + getInventory().getSize() : index).collect(Collectors.toSet());
    }

    protected @NotNull Set<Integer> parseIndexGroup(final int[] value, final @NotNull IndexGroup[] indexGroups, final boolean player) {
        return parseIndexGroup(value, indexGroups).stream().map(index -> player ? index + getInventory().getSize() : index).collect(Collectors.toSet());
    }
}
