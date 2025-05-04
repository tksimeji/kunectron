package com.tksimeji.kunectron.hooks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public interface ScoreboardGuiHooks extends Hooks {
    default @NotNull Set<Player> usePlayers() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void useAddPlayer(final @NotNull Player player) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void useRemovePlayer(final @NotNull Player player) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default boolean useIsPlayer(final @NotNull Player player) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default @NotNull Component useTitle() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void useTitle(final @NotNull ComponentLike title) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default @NotNull Component useLine(final int index) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void useLine(final int index, final @NotNull ComponentLike line) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void useAddLine(final @NotNull ComponentLike line) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void useRemoveLine(final int index) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void useRemoveLines() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void useInsertLine(final int index, final @NotNull ComponentLike line) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void useClearLine(final int index) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void useClearLines() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default @NotNull List<Component> useLines() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default int useSize() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    default void useClose() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }
}
