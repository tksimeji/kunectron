package com.tksimeji.kunectron.controller;

import com.tksimeji.kunectron.AdvancementToastGui;
import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.controller.impl.GuiControllerImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class AdvancementToastGuiControllerImpl extends GuiControllerImpl implements AdvancementToastGuiController {
    private final @NotNull AdvancementToastGui.AdvancementType type;

    private final @NotNull Player player;

    private final @NotNull ItemStack icon;

    private final @NotNull Component message;

    public AdvancementToastGuiControllerImpl(final @NotNull Object gui) {
        super(gui);

        type = getDeclarationOrDefault(gui, AdvancementToastGui.Type.class, AdvancementToastGui.AdvancementType.class, AdvancementToastGui.AdvancementType.TASK).getLeft();
        player = getDeclarationOrThrow(gui, AdvancementToastGui.Player.class, Player.class).getLeft();
        icon = getDeclarationOrThrow(gui, AdvancementToastGui.Icon.class, ItemStack.class).getLeft();
        message = getDeclarationOrDefault(gui, AdvancementToastGui.Message.class, ComponentLike.class, Component.empty()).getLeft().asComponent();
    }

    @Override
    public void init() {
        Optional.ofNullable(Kunectron.adapter()).ifPresent(adapter -> adapter.createAdvancementToast(player, type, icon, message, Kunectron.plugin(), () -> {
            Kunectron.deleteGuiController(this);
            return null;
        }));
    }
}
