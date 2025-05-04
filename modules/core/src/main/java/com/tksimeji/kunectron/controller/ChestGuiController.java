package com.tksimeji.kunectron.controller;

import com.tksimeji.kunectron.Action;
import com.tksimeji.kunectron.ChestGui;
import com.tksimeji.kunectron.Mouse;
import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.controller.impl.ItemContainerGuiControllerImpl;
import com.tksimeji.kunectron.event.chest.ChestGuiClickEventImpl;
import com.tksimeji.kunectron.event.chest.ChestGuiCloseEventImpl;
import com.tksimeji.kunectron.event.chest.ChestGuiInitEventImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public final class ChestGuiController extends ItemContainerGuiControllerImpl<Inventory> {
    private final @NotNull Player player;

    private final @NotNull Inventory inventory;

    public ChestGuiController(final @NotNull Object gui, final @NotNull ChestGui annotation) {
        super(gui, annotation.autoReload(), annotation.serverSideTranslation(), annotation.markupExtensions());

        player = getDeclarationOrThrow(gui, ChestGui.Player.class, Player.class).getLeft();
        inventory = Bukkit.createInventory(null,
                getDeclarationOrDefault(gui, ChestGui.Size.class, ChestGui.ChestSize.class, ChestGui.ChestSize.SIZE_54).getLeft().toInt(),
                titleFromField(ChestGui.Title.class));

        elementsFromFields(ChestGui.Element.class, (aAnnotation) -> parseIndexGroup(aAnnotation.index(), aAnnotation.groups()));
        policiesFromFields(ChestGui.Policy.class, (aAnnotation) -> parseIndexGroup(aAnnotation.index(), aAnnotation.groups(), aAnnotation.player()));
        defaultPolicyFromField(ChestGui.DefaultPolicy.class);
        defaultPolicyFromField(ChestGui.PlayerDefaultPolicy.class);

        Bukkit.getScheduler().runTask(Kunectron.plugin(), () -> player.openInventory(inventory));
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
        super.click(index, action, mouse);
    }

    @Override
    public void close() {
        callEvent(new ChestGuiCloseEventImpl(gui));
        super.close();
    }
}
