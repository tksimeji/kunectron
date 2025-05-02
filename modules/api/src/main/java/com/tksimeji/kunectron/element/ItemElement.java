package com.tksimeji.kunectron.element;

import com.tksimeji.kunectron.event.ItemContainerClickEvent;
import com.tksimeji.kunectron.markupextensions.context.Context;
import com.tksimeji.kunectron.policy.ItemSlotPolicy;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

public interface ItemElement extends Element<ItemElement> {
    @NotNull ItemType type();

    @Contract("_ -> this")
    @NotNull ItemElement type(final @NotNull ItemType type);

    @NotNull Material material();

    @Contract("_ -> this")
    @NotNull ItemElement material(final @NotNull Material material);

    @Nullable Component title();

    @Contract("_ -> this")
    @NotNull ItemElement title(final @Nullable ComponentLike title);

    @Contract("_ -> this")
    @NotNull ItemElement title(final @Nullable String title);

    @NotNull List<Component> lore();

    @Contract("_ -> this")
    @NotNull ItemElement lore(final @NotNull Collection<Component> lore);

    @Contract("_ -> this")
    @NotNull ItemElement lore(final @NotNull ComponentLike... lore);

    @Contract("_ -> this")
    @NotNull ItemElement lore(final @NotNull String... lore);

    @Contract("_, _ -> this")
    @NotNull ItemElement setLoreLine(final int index, final @NotNull Component line);

    @Contract("_, _ -> this")
    @NotNull ItemElement setLoreLine(final int index, final @NotNull String line);

    @Contract("_ -> this")
    @NotNull ItemElement addLoreLine(final @NotNull Component line);

    @Contract("_ -> this")
    @NotNull ItemElement addLoreLine(final @NotNull String line);

    @Contract("_ -> this")
    @NotNull ItemElement removeLoreLine(final int index);

    @Contract("_, _ -> this")
    @NotNull ItemElement insertLoreLine(final int index, final @NotNull Component line);

    @Contract("_, _ -> this")
    @NotNull ItemElement insertLoreLine(final int index, final @NotNull String line);

    @Contract("_ -> this")
    @NotNull ItemElement clearLoreLine(final int index);

    @Contract("-> this")
    @NotNull ItemElement clearLoreLines();

    @Contract("-> this")
    @NotNull ItemElement clearLore();

    int loreWidth();

    @Contract("_ -> this")
    @NotNull ItemElement loreWidth(final int loreWidth);

    @Range(from = 1, to = Integer.MAX_VALUE) int amount();

    @Contract("_ -> this")
    @NotNull ItemElement amount(final @Range(from = 1, to = Integer.MAX_VALUE) int amount);

    int customModelData();

    @Contract("_ -> this")
    @NotNull ItemElement customModelData(final int customModelData);

    @Nullable Key itemModel();

    @Contract("_ -> this")
    @NotNull ItemElement itemModel(@Nullable Keyed itemModel);

    boolean aura();

    @Contract("_ -> this")
    @NotNull ItemElement aura(final boolean aura);

    boolean hideAdditionalTooltip();

    @Contract("_ -> this")
    @NotNull ItemElement hideAdditionalTooltip(final boolean hideAdditionalTooltip);

    @Nullable ItemSlotPolicy policy();

    @Contract("_ -> this")
    @NotNull ItemElement policy(final @Nullable ItemSlotPolicy policy);

    @Nullable Sound sound();

    @Range(from = 0, to = Integer.MAX_VALUE) float soundVolume();

    @Range(from = 0, to = Integer.MAX_VALUE) float soundPitch();

    @Contract("_ -> this")
    @NotNull ItemElement sound(final @Nullable Sound sound);

    @Contract("_, _, _ -> this")
    @NotNull ItemElement sound(final @Nullable Sound sound, final @Range(from = 0, to = Integer.MAX_VALUE) float volume, final @Range(from = 0, to = 2) float pitch);

    @Nullable Handler handler();

    @Contract("_ -> this")
    @NotNull ItemElement handler(final @Nullable Handler1 handler);

    @Contract("_ -> this")
    @NotNull ItemElement handler(final @Nullable Handler2 handler);

    @NotNull ItemStack createItemStack();

    @NotNull ItemStack createItemStack(final @Nullable Locale locale, final @Nullable Context<?> ctx);

    interface Handler {
    }

    @FunctionalInterface
    interface Handler1 extends Handler {
        void onClick();
    }

    @FunctionalInterface
    interface Handler2 extends Handler {
        void onClick(@NotNull ItemContainerClickEvent event);
    }
}
