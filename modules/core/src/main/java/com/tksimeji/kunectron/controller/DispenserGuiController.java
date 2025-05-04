package com.tksimeji.kunectron.controller;

import com.tksimeji.kunectron.Action;
import com.tksimeji.kunectron.DispenserGui;
import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.Mouse;
import com.tksimeji.kunectron.controller.impl.ItemContainerGuiControllerImpl;
import com.tksimeji.kunectron.element.ItemElement;
import com.tksimeji.kunectron.event.dispenser.DispenserGuiClickEventImpl;
import com.tksimeji.kunectron.event.dispenser.DispenserGuiCloseEventImpl;
import com.tksimeji.kunectron.event.dispenser.DispenserGuiInitEventImpl;
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

public final class DispenserGuiController extends ItemContainerGuiControllerImpl<Inventory> {
    private final @NotNull Player player;

    private final @NotNull Inventory inventory;

    public DispenserGuiController(final @NotNull Object gui, final @NotNull DispenserGui annotation) {
        super(gui, annotation.autoReload(), annotation.serverSideTranslation(), annotation.markupExtensions());

        player = getDeclarationOrThrow(gui, DispenserGui.Player.class, Player.class).getLeft();

        Component title = getDeclarationOrDefault(gui, DispenserGui.Title.class, ComponentLike.class, Component.empty()).getLeft().asComponent();
        if (serverSideTranslation &&
                title instanceof TranslatableComponent translatableComponent &&
                GlobalTranslator.translator().canTranslate(translatableComponent.key(), getLocale())) {
            title = GlobalTranslator.render(title, getLocale());
        }

        inventory = Bukkit.createInventory(null, InventoryType.DISPENSER, title);

        getDeclaration(gui, DispenserGui.DefaultPolicy.class, ItemSlotPolicy.class).ifPresent(declaration -> {
            setDefaultPolicy(declaration.getLeft());
        });

        getDeclaration(gui, DispenserGui.PlayerDefaultPolicy.class, ItemSlotPolicy.class).ifPresent(declaration -> {
            setPlayerDefaultPolicy(declaration.getLeft());
        });

        for (Pair<ItemElement, DispenserGui.Element> declaration : getDeclarations(gui, DispenserGui.Element.class, ItemElement.class)) {
            for (final int index : parseAnnotation(declaration.getRight())) {
                setElement(index, declaration.getLeft());
            }
        }

        for (Pair<ItemSlotPolicy, DispenserGui.Policy> declaration : getDeclarations(gui, DispenserGui.Policy.class, ItemSlotPolicy.class)) {
            for (final int index : parseAnnotation(declaration.getRight())) {
                setPolicy(index, declaration.getLeft());
            }
        }

        Bukkit.getScheduler().runTask(Kunectron.plugin(), () -> {
           player.openInventory(inventory);
        });
    }

    @Override
    public void init() {
        callEvent(new DispenserGuiInitEventImpl(gui));
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
    public void click(final int index, final @NotNull Action action, final @NotNull Mouse mouse) {
        callEvent(new DispenserGuiClickEventImpl(gui, index, getElement(index), action, mouse));
        super.click(index, action, mouse);
    }

    @Override
    public void close() {
        callEvent(new DispenserGuiCloseEventImpl(gui));
        super.close();
    }

    private @NotNull Set<Integer> parseAnnotation(final @NotNull DispenserGui.Element annotation) {
        return parseIndexGroup(annotation.index(), annotation.groups());
    }

    private @NotNull Set<Integer> parseAnnotation(final @NotNull DispenserGui.Policy annotation) {
        return parseIndexGroup(annotation.index(), annotation.groups(), annotation.player());
    }
}
