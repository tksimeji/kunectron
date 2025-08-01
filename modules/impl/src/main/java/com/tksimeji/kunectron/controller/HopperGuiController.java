package com.tksimeji.kunectron.controller;

import com.tksimeji.kunectron.Action;
import com.tksimeji.kunectron.HopperGui;
import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.Mouse;
import com.tksimeji.kunectron.controller.impl.ItemContainerGuiControllerImpl;
import com.tksimeji.kunectron.event.hopper.HopperGuiClickEventImpl;
import com.tksimeji.kunectron.event.hopper.HopperGuiCloseEventImpl;
import com.tksimeji.kunectron.event.hopper.HopperGuiInitEventImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public final class HopperGuiController extends ItemContainerGuiControllerImpl<Inventory> {
    private final @NotNull Player player;

    private final @NotNull Inventory inventory;

    public HopperGuiController(final @NotNull Object gui, final @NotNull HopperGui annotation) {
        super(gui, annotation.autoReload(), annotation.serverSideTranslation(), annotation.markupExtensions());

        player = getDeclarationOrThrow(gui, HopperGui.Player.class, Player.class).getLeft();
        inventory = Bukkit.createInventory(null, InventoryType.HOPPER, titleFromField(HopperGui.Title.class));

        elementsFromFields(HopperGui.Element.class, (aAnnotation) -> parseIndexGroup(aAnnotation.index(), aAnnotation.groups()));
        policiesFromFields(HopperGui.Policy.class, (aAnnotation) -> parseIndexGroup(aAnnotation.index(), aAnnotation.groups(), aAnnotation.player()));
        defaultPolicyFromField(HopperGui.DefaultPolicy.class);
        playerDefaultPolicyFromField(HopperGui.PlayerDefaultPolicy.class);

        Bukkit.getScheduler().runTask(Kunectron.plugin(), () -> player.openInventory(inventory));
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
    public void click(final int index, final @NotNull Action action, final @NotNull Mouse mouse) {
        callEvent(new HopperGuiClickEventImpl(gui, index, getElement(index), action, mouse));
        super.click(index, action, mouse);
    }

    @Override
    public void close() {
        callEvent(new HopperGuiCloseEventImpl(gui));
        super.close();
    }
}
