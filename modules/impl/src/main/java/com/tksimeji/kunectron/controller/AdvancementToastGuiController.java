package com.tksimeji.kunectron.controller;

import com.tksimeji.kunectron.AdvancementToastGui;
import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.controller.impl.GuiControllerImpl;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AdvancementToastGuiController extends GuiControllerImpl {
    private final @NotNull AdvancementToastGui.AdvancementType type;

    private final @NotNull Player player;

    private final @NotNull ItemStack icon;

    private final @NotNull Component message;

    private final @NotNull Key advancementKey = Key.key("kunectron", UUID.randomUUID().toString());

    public AdvancementToastGuiController(final @NotNull Object gui) {
        super(gui);

        type = getDeclarationOrDefault(gui, AdvancementToastGui.Type.class, AdvancementToastGui.AdvancementType.class, AdvancementToastGui.AdvancementType.TASK).getLeft();
        player = getDeclarationOrThrow(gui, AdvancementToastGui.Player.class, Player.class).getLeft();
        icon = getDeclarationOrThrow(gui, AdvancementToastGui.Icon.class, ItemStack.class).getLeft();
        message = getDeclarationOrDefault(gui, AdvancementToastGui.Message.class, ComponentLike.class, Component.empty()).getLeft().asComponent();
    }

    @Override
    public void init() {
        Kunectron.adapterOrThrow().sendAdvancementGranted(player, advancementKey, type, icon, message);
        Bukkit.getScheduler().runTaskLaterAsynchronously(Kunectron.plugin(), () -> {
            Kunectron.adapterOrThrow().sendAdvancementCleanup(player, advancementKey);
            Kunectron.deleteGuiController(this);
        }, 10L);
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull ItemStack getIcon() {
        return icon;
    }

    public @NotNull Component getMessage() {
        return message;
    }
}
