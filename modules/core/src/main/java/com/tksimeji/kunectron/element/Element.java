package com.tksimeji.kunectron.element;

import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.UUID;

public interface Element<T> extends ElementBase<T> {
    static @NotNull ItemElement item(final @NotNull ItemType type) {
        Preconditions.checkArgument(type != null, "Item type cannot be null.");
        return new ItemElementImpl(type);
    }

    static @NotNull ItemElement item(final @NotNull Material material) {
        Preconditions.checkArgument(material != null, "Material cannot be null.");
        Preconditions.checkArgument(material.isItem(), "Material." + material.name() + " is not an item.");
        return new ItemElementImpl(material);
    }

    static @NotNull ItemElement item(final @NotNull ItemStack itemStack) {
        Preconditions.checkArgument(itemStack != null, "Item stack cannot be null.");
        return new ItemElementImpl(itemStack.clone());
    }

    static @NotNull PlayerHeadElement playerHead() {
        return new PlayerHeadElementImpl();
    }

    static @NotNull PlayerHeadElement playerHead(final @Nullable String name) {
        return playerHead().name(name);
    }

    static @NotNull PlayerHeadElement playerHead(final @Nullable UUID uuid) {
        return playerHead().uuid(uuid);
    }

    static @NotNull PlayerHeadElement playerHead(final @Nullable OfflinePlayer player) {
        return playerHead().player(player);
    }

    static @NotNull PlayerHeadElement playerHead(final @Nullable URL url) {
        return playerHead().url(url);
    }

    static @NotNull TradeElement trade(@NotNull ItemStack result, @NotNull ItemStack ingredient) {
        Preconditions.checkArgument(result != null, "Result cannot be null.");
        Preconditions.checkArgument(ingredient != null, "Ingredient cannot be null.");
        return trade(result, ingredient, null);
    }

    static @NotNull TradeElement trade(@NotNull ItemStack result, @NotNull ItemStack ingredient1, @Nullable ItemStack ingredient2) {
        Preconditions.checkArgument(result != null, "Result cannot be null.");
        Preconditions.checkArgument(ingredient1 != null, "Ingredient1 cannot be null.");
        return new TradeElementImpl(result, ingredient1, ingredient2);
    }
}
