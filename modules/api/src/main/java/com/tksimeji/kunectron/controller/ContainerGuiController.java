package com.tksimeji.kunectron.controller;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public interface ContainerGuiController<I extends Inventory> extends GuiController {
    static void from() {
    }

    @NotNull Player getPlayer();

    @NotNull I getInventory();

    @NotNull Locale getLocale();

    void close();
}
