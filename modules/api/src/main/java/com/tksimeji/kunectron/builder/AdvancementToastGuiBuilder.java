package com.tksimeji.kunectron.builder;

import com.tksimeji.kunectron.AdvancementToastGui;
import com.tksimeji.kunectron.hooks.AdvancementToastGuiHooks;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface AdvancementToastGuiBuilder extends GuiBuilder<AdvancementToastGuiBuilder, AdvancementToastGuiHooks> {
    @Contract("_ -> this")
    @NotNull AdvancementToastGuiBuilder type(final @NotNull AdvancementToastGui.AdvancementType type);

    @Contract("_ -> this")
    @NotNull AdvancementToastGuiBuilder icon(final @NotNull ItemStack icon);

    @Contract("_ -> this")
    @NotNull AdvancementToastGuiBuilder icon(final @NotNull ItemType icon);

    @Contract("_ -> this")
    @NotNull AdvancementToastGuiBuilder icon(final @NotNull Material icon);

    @Contract("_ -> this")
    @NotNull AdvancementToastGuiBuilder message(final @NotNull ComponentLike message);

    @Contract("_ -> this")
    @NotNull AdvancementToastGuiBuilder message(final @NotNull String message);

    @NotNull AdvancementToastGuiHooks build(final @NotNull Player player);
}
