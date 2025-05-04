package com.tksimeji.kunectron.builder;

import com.google.common.base.Preconditions;
import com.tksimeji.kunectron.MerchantGui;
import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.element.TradeElement;
import com.tksimeji.kunectron.hooks.AnvilGuiHooks;
import com.tksimeji.kunectron.hooks.MerchantGuiHooks;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class MerchantGuiBuilderImpl extends ContainerGuiBuilderImpl<MerchantGuiBuilder, MerchantGuiHooks> implements MerchantGuiBuilder {
    private final @NotNull List<TradeElement> elements = new ArrayList<>();

    @Override
    public @NotNull MerchantGuiBuilder element(final @NotNull TradeElement element) {
        Preconditions.checkArgument(element != null, "Element cannot be null.");
        elements.add(element);
        return this;
    }

    @Override
    public @NotNull MerchantGuiHooks build(final @NotNull Player player) {
        Preconditions.checkArgument(player != null, "Player cannot be null.");

        final Gui gui = Kunectron.create(new Gui(player, title, handlers), MerchantGui.class);

        for (final TradeElement element : elements) {
            gui.useAddElement(element);
        }
        return gui;
    }

    @MerchantGui(serverSideTranslation = true)
    private static final class Gui extends AbstractGui<AnvilGuiHooks> implements MerchantGuiHooks {
        @MerchantGui.Player
        private final @NotNull Player player;

        @MerchantGui.Title
        private final @NotNull Component title;

        public Gui(final @NotNull Player player, final @NotNull ComponentLike title, final @NotNull List<HandlerInfo> handlers) {
            super(handlers);

            this.player = player;
            this.title = title.asComponent();
        }
    }
}