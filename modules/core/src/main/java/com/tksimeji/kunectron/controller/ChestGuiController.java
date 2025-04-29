package com.tksimeji.kunectron.controller;

import com.tksimeji.kunectron.Action;
import com.tksimeji.kunectron.ChestGui;
import com.tksimeji.kunectron.Mouse;
import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.element.ItemElement;
import com.tksimeji.kunectron.event.chest.ChestGuiClickEventImpl;
import com.tksimeji.kunectron.event.chest.ChestGuiCloseEventImpl;
import com.tksimeji.kunectron.event.chest.ChestGuiInitEventImpl;
import com.tksimeji.kunectron.policy.ItemSlotPolicy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.translation.GlobalTranslator;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class ChestGuiController extends AbstractItemContainerGuiController<Inventory> {
    private final @NotNull Player player;

    private final @NotNull Inventory inventory;

    public ChestGuiController(final @NotNull Object gui) {
        super(gui);

        player = getDeclarationOrThrow(gui, ChestGui.Player.class, Player.class).getLeft();

        Component title = getDeclarationOrDefault(gui, ChestGui.Title.class, ComponentLike.class, Component.empty()).getLeft().asComponent();
        if (title instanceof TranslatableComponent translatableComponent &&
                GlobalTranslator.translator().canTranslate(translatableComponent.key(), getLocale())) {
            title = GlobalTranslator.render(title, getLocale());
        }

        inventory = Bukkit.createInventory(null,
                getDeclarationOrDefault(gui, ChestGui.Size.class, ChestGui.ChestSize.class, ChestGui.ChestSize.SIZE_54).getLeft().toInt(),
                title);

        Bukkit.getScheduler().runTask(Kunectron.plugin(), () -> player.openInventory(inventory));

        getDeclaration(gui, ChestGui.DefaultPolicy.class, ItemSlotPolicy.class).ifPresent(declaration -> {
            setDefaultPolicy(declaration.getLeft());
        });

        getDeclaration(gui, ChestGui.PlayerDefaultPolicy.class, ItemSlotPolicy.class).ifPresent(declaration -> {
            setPlayerDefaultPolicy(declaration.getLeft());
        });

        for (Pair<ItemElement, ChestGui.Element> declaration : getDeclarations(gui, ChestGui.Element.class, ItemElement.class)) {
            for (final int index : parseAnnotation(declaration.getRight())) {
                setElement(index, declaration.getLeft());
            }
        }

        for (Pair<ItemSlotPolicy, ChestGui.Policy> declaration : getDeclarations(gui, ChestGui.Policy.class, ItemSlotPolicy.class)) {
            for (final int index : parseAnnotation(declaration.getRight())) {
                setPolicy(index, declaration.getLeft());
            }
        }
    }

    @Override
    public void init() {
        callEvent(new ChestGuiInitEventImpl(gui));
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public void click(final int index, final @NotNull Action action, final @NotNull Mouse mouse) {
        callEvent(new ChestGuiClickEventImpl(gui, index, getElement(index), action, mouse));
    }

    @Override
    public void close() {
        callEvent(new ChestGuiCloseEventImpl(gui));
        super.close();
    }

    private @NotNull Set<Integer> parseAnnotation(final @NotNull ChestGui.Element annotation) {
        return parseIndexGroup(annotation.index(), annotation.groups());
    }

    private @NotNull Set<Integer> parseAnnotation(final @NotNull ChestGui.Policy annotation) {
        return parseIndexGroup(annotation.index(), annotation.groups(), annotation.player());
    }
}
