package com.tksimeji.kunectron.listener;

import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.controller.ContainerGuiController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

public final class ContainerGuiListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClose(final @NotNull InventoryCloseEvent event) {
        final ContainerGuiController<?> controller =  Kunectron.getGuiController(event.getInventory());
        if (controller != null) {
            controller.close();
        }
    }
}
