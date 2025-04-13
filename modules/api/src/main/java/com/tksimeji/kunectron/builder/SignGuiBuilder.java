package com.tksimeji.kunectron.builder;

import com.tksimeji.kunectron.SignGui;
import com.tksimeji.kunectron.hooks.SignGuiHooks;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface SignGuiBuilder extends GuiBuilder<SignGuiBuilder, SignGuiHooks> {
    @Contract("_ -> this")
    @NotNull SignGuiBuilder type(final @NotNull SignGui.SignType type);

    @Contract("_ -> this")
    @NotNull SignGuiBuilder textColor(final @NotNull DyeColor textColor);

    @Contract("_ -> this")
    @NotNull SignGuiBuilder glowing(final boolean glowing);

    @Contract("_ -> this")
    @NotNull SignGuiBuilder line(final @NotNull String line);

    @Contract("_, _ -> this")
    @NotNull SignGuiBuilder line(final int index, final @NotNull String line);

    @Contract("_ -> this")
    @NotNull SignGuiBuilder lines(final @NotNull String... lines);

    @Contract("_ -> this")
    @NotNull SignGuiHooks build(final @NotNull Player player);
}
