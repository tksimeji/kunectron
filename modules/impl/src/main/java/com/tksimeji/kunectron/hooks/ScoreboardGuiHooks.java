package com.tksimeji.kunectron.hooks;

import com.google.common.base.Preconditions;
import com.tksimeji.kunectron.controller.ScoreboardGuiController;
import com.tksimeji.kunectron.hooks.impl.HooksImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface ScoreboardGuiHooks extends HooksImpl<ScoreboardGuiController> {
    default @NotNull Set<Player> usePlayers() {
        return controller().getPlayers();
    }

    default void useAddPlayer(final @NotNull Player player) {
        Preconditions.checkArgument(player != null, "Player cannot be null.");
        controller().addPlayer(player);
    }

    default void useRemovePlayer(final @NotNull Player player) {
        Preconditions.checkArgument(player != null, "Player cannot be null.");
        controller().removePlayer(player);
    }

    default boolean useIsPlayer(final @NotNull Player player) {
        return controller().isPlayer(player);
    }

    default @NotNull Component useTitle() {
        return controller().getTitle();
    }

    default void useTitle(final @NotNull ComponentLike title) {
        Preconditions.checkArgument(title != null, "Title cannot be null.");
        controller().setTitle(title);
    }

    default @Nullable Component useLine(final int index) {
        return controller().getLine(index);
    }

    default void useLine(final int index, final @NotNull ComponentLike line) {
        Preconditions.checkArgument(line != null, "Line cannot be null.");
        controller().setLine(index, line);
    }

    default void useAddLine(final @NotNull ComponentLike line) {
        Preconditions.checkArgument(line != null, "Line cannot be null.");
        controller().addLine(line);
    }

    default void useRemoveLine(final int index) {
        controller().removeLine(index);
    }

    default void useRemoveLines() {
        controller().removeLines();
    }

    default void useInsertLine(final int index, final @NotNull ComponentLike line) {
        Preconditions.checkArgument(line != null, "Line cannot be null.");
        controller().insertLine(index, line);
    }

    default void useClearLine(final int index) {
        controller().clearLine(index);
    }

    default void useClearLines() {
        controller().clearLines();
    }

    default @NotNull List<Component> useLines() {
        return controller().getLines();
    }

    default int useSize() {
        return controller().getSize();
    }

    default void useClose() {
        controller().close();
    }
}
