package com.tksimeji.kunectron.builder;

import com.google.common.base.Preconditions;
import com.tksimeji.kunectron.ChestGui;
import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.element.ItemElement;
import com.tksimeji.kunectron.hooks.ChestGuiHooks;
import com.tksimeji.kunectron.policy.ItemSlotPolicy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class ChestGuiBuilderImpl extends ItemContainerGuiBuilderImpl<ChestGuiBuilder, ChestGuiHooks> implements ChestGuiBuilder {
    private @NotNull ChestGui.ChestSize size = ChestGui.ChestSize.SIZE_54;

    @Override
    public @NotNull ChestGuiBuilder size(final @NotNull ChestGui.ChestSize size) {
        Preconditions.checkArgument(size != null, "Size cannot be null.");
        this.size = size;
        return this;
    }

    @Override
    public @NotNull ChestGuiHooks build(final @NotNull Player player) {
        Preconditions.checkArgument(player != null, "Player cannot be null.");
        final Gui gui = Kunectron.create(new Gui(player, title, size, handlers), ChestGui.class);

        for (final Map.Entry<Integer, ItemElement> entry : elements.entrySet()) {
            gui.useElement(entry.getKey(), entry.getValue());
        }

        for (final Map.Entry<Integer, ItemSlotPolicy> entry : policies.entrySet()) {
            gui.usePolicy(entry.getKey(), entry.getValue());
        }

        Optional.ofNullable(defaultPolicy).ifPresent(gui::useDefaultPolicy);
        Optional.ofNullable(playerDefaultPolicy).ifPresent(gui::usePlayerDefaultPolicy);
        return gui;
    }

    @ChestGui(autoReload = true, serverSideTranslation = true, markupExtensions = true)
    private static final class Gui extends AbstractGui<ChestGuiHooks> implements ChestGuiHooks {
        @ChestGui.Player
        private final @NotNull Player player;

        @ChestGui.Title
        private final @NotNull Component title;

        @ChestGui.Size
        private final @NotNull ChestGui.ChestSize size;

        public Gui(final @NotNull Player player, final @NotNull ComponentLike title, final @NotNull ChestGui.ChestSize size, final @NotNull List<HandlerInfo> handlers) {
            super(handlers);
            this.player = player;
            this.title = title.asComponent();
            this.size = size;
        }
    }
}