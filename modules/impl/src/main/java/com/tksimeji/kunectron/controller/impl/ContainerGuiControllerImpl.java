package com.tksimeji.kunectron.controller.impl;

import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.controller.ContainerGuiController;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public abstract class ContainerGuiControllerImpl<I extends Inventory> extends GuiControllerImpl implements ContainerGuiController<I> {
    public ContainerGuiControllerImpl(@NotNull Object gui) {
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
                Kunectron.getGuiControllers(ContainerGuiController.class).stream()
                        .noneMatch(controller -> controller.getPlayer().equals(getPlayer()))) {
            getPlayer().closeInventory();
        }

        getPlayer().updateInventory();
    }
}
