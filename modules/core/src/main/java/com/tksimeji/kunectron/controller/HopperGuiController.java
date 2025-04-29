package com.tksimeji.kunectron.controller;

import com.tksimeji.kunectron.*;
import com.tksimeji.kunectron.element.ItemElement;
import com.tksimeji.kunectron.event.hopper.HopperGuiClickEventImpl;
import com.tksimeji.kunectron.event.hopper.HopperGuiCloseEventImpl;
import com.tksimeji.kunectron.event.hopper.HopperGuiInitEventImpl;
import com.tksimeji.kunectron.policy.ItemSlotPolicy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.translation.GlobalTranslator;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class HopperGuiController extends AbstractItemContainerGuiController<Inventory> {
    private final @NotNull Player player;

    private final @NotNull Inventory inventory;

    public HopperGuiController(final @NotNull Object gui) {
        super(gui);

        player = getDeclarationOrThrow(gui, HopperGui.Player.class, Player.class).getLeft();

        Component title = getDeclarationOrDefault(gui, HopperGui.Title.class, ComponentLike.class, Component.empty()).getLeft().asComponent();
        if (title instanceof TranslatableComponent translatableComponent &&
                GlobalTranslator.translator().canTranslate(translatableComponent.key(), getLocale())) {
            title = GlobalTranslator.render(title, getLocale());
        }

        inventory = Bukkit.createInventory(null, InventoryType.HOPPER, title);

        Bukkit.getScheduler().runTask(Kunectron.plugin(), () -> player.openInventory(inventory));

        getDeclaration(gui, HopperGui.DefaultPolicy.class, ItemSlotPolicy.class).ifPresent(declaration -> {
            setDefaultPolicy(declaration.getLeft());
        });

        getDeclaration(gui, HopperGui.PlayerDefaultPolicy.class, ItemSlotPolicy.class).ifPresent(declaration -> {
            setPlayerDefaultPolicy(declaration.getLeft());
        });

        for (Pair<ItemElement, HopperGui.Element> declaration : getDeclarations(gui, HopperGui.Element.class, ItemElement.class)) {
            for (final int index : parseAnnotation(declaration.getRight())) {
                setElement(index, declaration.getLeft());
            }
        }

        for (Pair<ItemSlotPolicy, HopperGui.Policy> declaration : getDeclarations(gui, HopperGui.Policy.class, ItemSlotPolicy.class)) {
            for (final int index : parseAnnotation(declaration.getRight())) {
                setPolicy(index, declaration.getLeft());
            }
        }
    }

    @Override
    public void init() {
        callEvent(new HopperGuiInitEventImpl(gui));
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public void click(int index, @NotNull Action action, @NotNull Mouse mouse) {
        callEvent(new HopperGuiClickEventImpl(gui, index, getElement(index), action, mouse));
    }

    @Override
    public void close() {
        callEvent(new HopperGuiCloseEventImpl(gui));
        super.close();
    }

    private @NotNull Set<Integer> parseAnnotation(final @NotNull HopperGui.Element annotation) {
        return parseIndexGroup(annotation.index(), annotation.groups());
    }

    private @NotNull Set<Integer> parseAnnotation(final @NotNull HopperGui.Policy annotation) {
        return parseIndexGroup(annotation.index(), annotation.groups(), annotation.player());
    }
}
