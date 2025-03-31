package com.tksimeji.kunectron.builder;

import com.tksimeji.kunectron.AdvancementToastGui;
import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.hooks.AdvancementToastGuiHooks;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdvancementToastGuiBuilderImpl extends IGuiBuilderImpl<AdvancementToastGuiBuilder, AdvancementToastGuiHooks> implements AdvancementToastGuiBuilder {
    private @NotNull AdvancementToastGui.AdvancementType type = AdvancementToastGui.AdvancementType.TASK;

    private @NotNull ItemStack icon = ItemStack.of(Material.GRASS_BLOCK);

    private @NotNull Component message = Component.empty();

    @Override
    public @NotNull AdvancementToastGuiBuilder type(final @NotNull AdvancementToastGui.AdvancementType type) {
        this.type = type;
        return this;
    }

    @Override
    public @NotNull AdvancementToastGuiBuilder icon(final @NotNull ItemStack icon) {
        this.icon = icon;
        return this;
    }

    @Override
    public @NotNull AdvancementToastGuiBuilder icon(final @NotNull ItemType icon) {
        return icon(icon.createItemStack());
    }

    @Override
    public @NotNull AdvancementToastGuiBuilder icon(final @NotNull Material icon) {
        return icon(ItemStack.of(icon));
    }

    @Override
    public @NotNull AdvancementToastGuiBuilder message(final @NotNull ComponentLike message) {
        this.message = message.asComponent();
        return this;
    }

    @Override
    public @NotNull AdvancementToastGuiHooks build(final @NotNull Player player) {
        return Kunectron.create(new Gui(player, type, icon, message, handlers), AdvancementToastGui.class);
    }

    @AdvancementToastGui
    private static final class Gui extends AbstractGui<AdvancementToastGuiHooks> implements AdvancementToastGuiHooks {
        @AdvancementToastGui.Player
        private final @NotNull Player player;

        @AdvancementToastGui.Type
        private final @NotNull AdvancementToastGui.AdvancementType type;

        @AdvancementToastGui.Icon
        private final @NotNull ItemStack icon;

        @AdvancementToastGui.Message
        private final @NotNull Component message;

        public Gui(final @NotNull Player player, final @NotNull AdvancementToastGui.AdvancementType type, final @NotNull ItemStack icon, final @NotNull ComponentLike message, final @NotNull List<HandlerInfo> handlers) {
            super(handlers);

            this.player = player;
            this.type = type;
            this.icon = icon;
            this.message = message.asComponent();
        }
    }
}
