package com.tksimeji.kunectron.controller;

import com.tksimeji.kunectron.Action;
import com.tksimeji.kunectron.DispenserGui;
import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.Mouse;
import com.tksimeji.kunectron.controller.impl.ItemContainerGuiControllerImpl;
import com.tksimeji.kunectron.event.dispenser.DispenserGuiClickEventImpl;
import com.tksimeji.kunectron.event.dispenser.DispenserGuiCloseEventImpl;
import com.tksimeji.kunectron.event.dispenser.DispenserGuiInitEventImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public final class DispenserGuiController extends ItemContainerGuiControllerImpl<Inventory> {
    private final @NotNull Player player;

    private final @NotNull Inventory inventory;

    public DispenserGuiController(final @NotNull Object gui, final @NotNull DispenserGui annotation) {
        super(gui, annotation.autoReload(), annotation.serverSideTranslation(), annotation.markupExtensions());

        player = getDeclarationOrThrow(gui, DispenserGui.Player.class, Player.class).getLeft();
        inventory = Bukkit.createInventory(null, InventoryType.DISPENSER, titleFromField(DispenserGui.Title.class));

        elementsFromFields(DispenserGui.Element.class, (aAnnotation) -> parseIndexGroup(aAnnotation.index(), aAnnotation.groups()));
        policiesFromFields(DispenserGui.Policy.class, (aAnnotation) -> parseIndexGroup(aAnnotation.index(), aAnnotation.groups(), aAnnotation.player()));
        defaultPolicyFromField(DispenserGui.DefaultPolicy.class);
        playerDefaultPolicyFromField(DispenserGui.PlayerDefaultPolicy.class);

        Bukkit.getScheduler().runTask(Kunectron.plugin(), () -> player.openInventory(inventory));
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
}
