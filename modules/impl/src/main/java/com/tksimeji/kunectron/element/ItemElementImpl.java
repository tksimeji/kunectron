package com.tksimeji.kunectron.element;

import com.google.common.base.Preconditions;
import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.markupextensions.context.Context;
import com.tksimeji.kunectron.policy.ItemSlotPolicy;
import com.tksimeji.kunectron.util.Components;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;

public class ItemElementImpl implements ItemElement {
    protected @NotNull ItemStack itemStack;

    protected @Nullable Component title;
    protected @NotNull List<Component> lore = List.of();
    protected int loreWidth = -1;

    protected @Nullable ItemSlotPolicy policy;

    protected @Nullable Sound sound;
    protected float soundVolume = 1.0F;
    protected float soundPitch = 1.0F;

    protected @Nullable Handler handler;

    protected final boolean itemStackMode;

    public ItemElementImpl(final @NotNull ItemType type) {
        this(type.createItemStack(), false);
    }

    public ItemElementImpl(final @NotNull Material material) {
        this(material.asItemType());
    }

    public ItemElementImpl(final @NotNull ItemStack itemStack) {
        this(itemStack, true);
    }

    private ItemElementImpl(final @NotNull ItemStack itemStack, final boolean itemStackMode) {
        Preconditions.checkArgument(itemStack.getItemMeta() != null, "Item elements cannot be created from an item stack that does not have an item meta.");

        this.itemStack = itemStack;
        this.itemStackMode = itemStackMode;

        if (itemStackMode) {
            final ItemMeta itemMeta = itemStack.getItemMeta();
            title = itemMeta.displayName();
            lore = Optional.ofNullable(itemMeta.lore()).orElse(new ArrayList<>());
        } else {
            title((ComponentLike) null);
            amount(itemStack.getAmount());
            hideAdditionalTooltip(true);
        }
    }

    @Override
    public @NotNull ItemType type() {
        return itemStack.getType().asItemType();
    }

    @Override
    public @NotNull ItemElement type(final @NotNull ItemType type) {
        Preconditions.checkArgument(type != null, "Item type cannot be null.");
        itemStack = itemStack.withType(type.createItemStack().getType());
        return this;
    }

    @Override
    public @NotNull Material material() {
        return itemStack.getType();
    }

    @Override
    public @NotNull ItemElement material(final @NotNull Material material) {
        Preconditions.checkArgument(material != null, "Material cannot be null.");
        itemStack = itemStack.withType(material);
        return this;
    }

    @Override
    public @Nullable Component title() {
        return title;
    }

    @Override
    public @NotNull ItemElement title(final @Nullable ComponentLike title) {
        this.title = title != null ? title.asComponent().colorIfAbsent(NamedTextColor.WHITE).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE) : null;

        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(this.title);
        itemStack.setItemMeta(itemMeta);
        updateHideToolTip();
        return this;
    }

    @Override
    public @NotNull ItemElement title(final @Nullable String title) {
        return title(title != null ? Component.text(title) : null);
    }

    @Override
    public @NotNull List<Component> lore() {
        return Collections.unmodifiableList(lore);
    }

    @Override
    public @NotNull ItemElement lore(final @NotNull Collection<Component> lore) {
        Preconditions.checkArgument(lore != null, "Lines cannot be null.");
        this.lore = lore.stream()
                .map(line -> line.asComponent().colorIfAbsent(NamedTextColor.GRAY).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .toList();

        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.lore(this.lore);
        itemStack.setItemMeta(itemMeta);
        updateHideToolTip();
        return this;
    }

    @Override
    public @NotNull ItemElement lore(final @NotNull ComponentLike... lore) {
        Preconditions.checkArgument(lore != null, "Lines cannot be null.");
        return lore(Arrays.stream(lore).map(ComponentLike::asComponent).toList());
    }

    @Override
    public @NotNull ItemElement lore(final @NotNull String... lore) {
        Preconditions.checkArgument(lore != null, "Lines cannot be null.");
        return lore(Arrays.stream(lore).map(Component::text).map(Component::asComponent).toList());
    }

    @Override
    public @NotNull ItemElement setLoreLine(final int index, final @NotNull Component line) {
        Preconditions.checkArgument(index >= 0, index + " is an invalid index.");
        final List<Component> lore = new ArrayList<>(this.lore);
        if (lore.size() <= index) {
            for (int i = lore.size(); i <= index; i++) {
                if (i == index) {
                    lore.set(index, line);
                } else {
                    lore.set(index, Component.empty());
                }
            }
        } else {
            lore.set(index, line);
        }
        lore(lore);
        return this;
    }

    @Override
    public @NotNull ItemElement setLoreLine(final int index, final @NotNull String line) {
        return setLoreLine(index, Component.text(line));
    }

    @Override
    public @NotNull ItemElement addLoreLine(final @NotNull Component line) {
        final List<Component> lore = new ArrayList<>(this.lore);
        lore.add(line);
        lore(lore);
        return this;
    }

    @Override
    public @NotNull ItemElement addLoreLine(final @NotNull String line) {
        return addLoreLine(Component.text(line));
    }

    @Override
    public @NotNull ItemElement removeLoreLine(final int index) {
        final List<Component> lore = new ArrayList<>(this.lore);
        lore.remove(index);
        lore(lore);
        return this;
    }

    @Override
    public @NotNull ItemElement insertLoreLine(final int index, final @NotNull Component line) {
        final List<Component> lore = new ArrayList<>(this.lore);
        lore.add(index, line);
        lore(lore);
        return this;
    }

    @Override
    public @NotNull ItemElement insertLoreLine(final int index, final @NotNull String line) {
        return insertLoreLine(index, Component.text(line));
    }

    @Override
    public @NotNull ItemElement clearLoreLine(final int index) {
        return setLoreLine(index, Component.empty());
    }

    @Override
    public @NotNull ItemElement clearLoreLines() {
        for (int i = 0; i < lore.size(); i++) {
            clearLoreLine(i);
        }
        return this;
    }

    @Override
    public @NotNull ItemElement clearLore() {
        lore(List.of());
        return this;
    }

    @Override
    public int loreWidth() {
        return loreWidth;
    }

    @Override
    public @NotNull ItemElement loreWidth(final int loreWidth) {
        this.loreWidth = loreWidth > 0 ? loreWidth : -1;
        return this;
    }

    @Override
    public @Range(from = 1, to = Integer.MAX_VALUE) int amount() {
        return itemStack.getAmount();
    }

    @Override
    public @NotNull ItemElement amount(final @Range(from = 1, to = Integer.MAX_VALUE) int amount) {
        Preconditions.checkArgument(amount > 0, "Amount cannot be less then or equal to 0.");
        Preconditions.checkArgument(amount <= itemStack.getMaxStackSize(), "Amount must be less than or equal to the maximum stack size of " + itemStack.getMaxStackSize() + ".");
        itemStack.setAmount(amount);
        return this;
    }

    @Override
    public int customModelData() {
        if (itemStack.getItemMeta().hasCustomModelData()) {
            return -1;
        }
        return itemStack.getItemMeta().getCustomModelData();
    }

    @Override
    public @NotNull ItemElement customModelData(final int customModelData) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(0 <= customModelData ? customModelData : null);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    @Override
    public @Nullable Key itemModel() {
        return itemStack.getItemMeta().getItemModel();
    }

    @Override
    public @NotNull ItemElement itemModel(final @Nullable Keyed itemModel) {
        final Key key = itemModel != null ? itemModel.key() : null;

        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setItemModel(key != null ? new NamespacedKey(key.namespace(), key.value()) : null);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    @Override
    public boolean aura() {
        return itemStack.getItemMeta().hasEnchants();
    }

    @Override
    public @NotNull ItemElement aura(final boolean aura) {
        final ItemMeta itemMeta = itemStack.getItemMeta();

        if (aura) {
            itemMeta.addEnchant(Enchantment.INFINITY, 1, false);
        } else {
            itemMeta.removeEnchantments();
        }

        itemStack.setItemMeta(itemMeta);
        return this;
    }

    @Override
    public boolean hideAdditionalTooltip() {
        return !Kunectron.adapterOrThrow().hasAdditionalTooltip(itemStack, Kunectron.plugin());
    }

    @Override
    public @NotNull ItemElement hideAdditionalTooltip(final boolean hideAdditionalTooltip) {
        if (hideAdditionalTooltip) {
            Kunectron.adapterOrThrow().hideAdditionalTooltip(itemStack, Kunectron.plugin());
        } else {
            Kunectron.adapterOrThrow().showAdditionalTooltip(itemStack, Kunectron.plugin());
        }
        return this;
    }

    @Override
    public @Nullable ItemSlotPolicy policy() {
        return policy;
    }

    @Override
    public @NotNull ItemElement policy(final @Nullable ItemSlotPolicy policy) {
        this.policy = policy;
        return this;
    }

    @Override
    public @Nullable Sound sound() {
        return sound;
    }

    @Override
    public @Range(from = 0, to = Integer.MAX_VALUE) float soundVolume() {
        return soundVolume;
    }

    @Override
    public @Range(from = 0, to = 2) float soundPitch() {
        return soundPitch;
    }

    @Override
    public @NotNull ItemElement sound(final @Nullable Sound sound) {
        return sound(sound, soundVolume, soundPitch);
    }

    @Override
    public @NotNull ItemElement sound(final @Nullable Sound sound, final @Range(from = 0, to = Integer.MAX_VALUE) float volume, final @Range(from = 0, to = 2) float pitch) {
        Preconditions.checkArgument(0 <= volume, "Volume cannot be less than 0.");
        Preconditions.checkArgument(0 <= pitch && pitch <= 2, "Pitch cannot be less than 0 or greater than 2.");

        this.sound = sound;
        soundVolume = volume;
        soundPitch = pitch;
        return this;
    }

    @Override
    public @Nullable Handler handler() {
        return handler;
    }

    @Override
    public @NotNull ItemElement handler(final @Nullable Handler1 handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public @NotNull ItemElement handler(final @Nullable Handler2 handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public @NotNull ItemStack createItemStack() {
        return createItemStack(null, null);
    }

    @Override
    public @NotNull ItemStack createItemStack(@Nullable Locale locale, @Nullable Context<?> ctx) {
        final ItemStack itemStack = this.itemStack.clone();
        if (locale == null && ctx == null) {
            return itemStack;
        }

        final ItemMeta itemMeta = itemStack.getItemMeta();

        Component title = this.title;
        if (title != null && ctx != null) {
            title = Components.markupExtensions(title, ctx);
        }
        if (title != null && locale != null) {
            title = Components.translate(title, locale);
        }
        itemMeta.displayName(title);

        List<Component> lore = this.lore.stream().map(component -> {
            if (ctx != null) {
                component = Components.markupExtensions(component, ctx);
            }
            if (locale != null) {
                component = Components.translate(component, locale);
            }
            return component;
        }).toList();
        if (loreWidth != -1) {
            final List<Component> lore2 = new ArrayList<>();
            for (final Component line : lore) {
                lore2.addAll(Components.split(line, loreWidth));
            }
            lore = lore2;
        }
        itemMeta.lore(lore);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public @NotNull ItemElement clone() {
        return clone(new ItemElementImpl(itemStack.clone(), itemStackMode));
    }

    protected <T extends ItemElementImpl> @NotNull T clone(final @NotNull T copy) {
        return clone(copy, null);
    }

    protected <T extends ItemElementImpl> @NotNull T clone(final @NotNull T copy, final @Nullable ItemStack itemStack) {
        if (itemStack != null) {
            copy.itemStack = itemStack;
        }

        copy.title(title);
        copy.lore(lore);
        copy.loreWidth(loreWidth);
        copy.policy(policy);
        copy.sound(sound, soundVolume, soundPitch);

        if (handler instanceof Handler1 handler1) {
            copy.handler(handler1);
        } else if (handler instanceof Handler2 handler2) {
            copy.handler(handler2);
        }

        return copy;
    }

    protected void updateHideToolTip() {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setHideTooltip(title == null && lore.isEmpty());
        itemStack.setItemMeta(itemMeta);
    }
}
