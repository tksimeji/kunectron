package com.tksimeji.kunectron.builder;

import com.google.common.base.Preconditions;
import com.tksimeji.kunectron.HopperGui;
import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.element.ItemElement;
import com.tksimeji.kunectron.hooks.HopperGuiHooks;
import com.tksimeji.kunectron.policy.ItemSlotPolicy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class HopperGuiBuilderImpl extends ItemContainerGuiBuilderImpl<HopperGuiBuilder, HopperGuiHooks> implements HopperGuiBuilder {
    @Override
    public @NotNull HopperGuiHooks build(final @NotNull Player player) {
        Preconditions.checkArgument(player != null, "Player cannot be null.");
        final Gui gui = Kunectron.create(new Gui(player, title, handlers));

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

    @HopperGui
    private static final class Gui extends AbstractGui<HopperGuiHooks> implements HopperGuiHooks {
        @HopperGui.Player
        private final @NotNull Player player;

        @HopperGui.Title
        private final @NotNull Component title;

        public Gui(final @NotNull Player player, final @NotNull ComponentLike title, final @NotNull List<HandlerInfo> handlers) {
            super(handlers);
            this.player = player;
            this.title = title.asComponent();
        }
    }
}
