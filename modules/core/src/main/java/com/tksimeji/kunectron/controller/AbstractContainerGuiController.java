package com.tksimeji.kunectron.controller;

import com.tksimeji.kunectron.Kunectron;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public abstract class AbstractContainerGuiController<I extends Inventory> extends AbstractGuiController implements ContainerGuiController<I> {
    public AbstractContainerGuiController(@NotNull Object gui) {
        super(gui);
    }

    @Override
    public @NotNull Locale getLocale() {
        return getPlayer().locale();
    }

    @Override
    public void close() {
        Kunectron.deleteGuiController(this);

        if (getPlayer().getOpenInventory().getTopInventory().getType() != InventoryType.CRAFTING &&
                Kunectron.getGuiControllers().stream().noneMatch(controller -> controller instanceof ContainerGuiController<?> containerGuiController && containerGuiController.getPlayer() == getPlayer())) {
            getPlayer().closeInventory();
        }

        getPlayer().updateInventory();
    }
}
