package com.tksimeji.kunectron.controller;

import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.SignGui;
import com.tksimeji.kunectron.controller.impl.GuiControllerImpl;
import com.tksimeji.kunectron.event.sign.SignCloseEventImpl;
import com.tksimeji.kunectron.event.sign.SignInitEventImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class SignGuiController extends GuiControllerImpl {
    private final @NotNull Player player;

    private final @NotNull SignGui.SignType type;

    private final @NotNull DyeColor textColor;

    private final boolean glowing;

    public SignGuiController(final @NotNull Object gui) {
        super(gui);

        player = getDeclarationOrThrow(gui, SignGui.Player.class, Player.class).getLeft();
        type = getDeclarationOrDefault(gui, SignGui.Type.class, SignGui.SignType.class, SignGui.SignType.OAK).getLeft();
        textColor = getDeclarationOrDefault(gui, SignGui.TextColor.class, DyeColor.class, DyeColor.BLACK).getLeft();
        glowing = getDeclarationOrDefault(gui, SignGui.Glowing.class, boolean.class, false).getLeft();

        final String[] lines = {null, null, null, null};

        final List<Pair<String, SignGui.Line>> lineDeclarations = getDeclarations(gui, SignGui.Line.class, String.class);

        lineDeclarations.stream().filter(declaration -> declaration.getRight().index() != -1).forEach(declaration -> {
            lines[declaration.getRight().index()] = declaration.getLeft();
        });

        lineDeclarations.stream().filter(declaration -> declaration.getRight().index() == -1).forEach(declaration -> {
            for (int index = 0; index < lines.length; index++) {
                if (lines[index] == null) {
                    lines[index] = declaration.getLeft();
                    break;
                }
            }
        });

        for (int index = 0; index < lines.length; index++) {
            if (lines[index] == null) {
                lines[index] = "";
            }
        }

        Kunectron.adapter().openSign(player, type, textColor, glowing, lines, finalLines -> {
            Bukkit.getScheduler().runTask(Kunectron.plugin(), task -> {
                callEvent(new SignCloseEventImpl(gui, finalLines));
            });
            return null;
        });
    }

    @Override
    public void init() {
        callEvent(new SignInitEventImpl(gui));
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull SignGui.SignType getType() {
        return type;
    }

    public @NotNull DyeColor getTextColor() {
        return textColor;
    }

    public boolean isGlowing() {
        return glowing;
    }

    public void close() {
        Kunectron.adapter().closeSign(player);
    }
}
