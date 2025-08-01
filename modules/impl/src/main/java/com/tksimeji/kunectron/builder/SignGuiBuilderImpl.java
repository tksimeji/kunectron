package com.tksimeji.kunectron.builder;

import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.SignGui;
import com.tksimeji.kunectron.hooks.SignGuiHooks;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SignGuiBuilderImpl extends IGuiBuilderImpl<SignGuiBuilder, SignGuiHooks> implements SignGuiBuilder {
    private @NotNull SignGui.SignType type = SignGui.SignType.OAK;

    private @NotNull DyeColor textColor = DyeColor.BLACK;

    private boolean glowing = false;

    private final @NotNull String[] lines = {null, null, null, null};

    @Override
    public @NotNull SignGuiBuilder type(final @NotNull SignGui.SignType type) {
        this.type = type;
        return this;
    }

    @Override
    public @NotNull SignGuiBuilder textColor(final @NotNull DyeColor textColor) {
        this.textColor = textColor;
        return this;
    }

    @Override
    public @NotNull SignGuiBuilder glowing(boolean glowing) {
        this.glowing = glowing;
        return this;
    }

    @Override
    public @NotNull SignGuiBuilder line(final @NotNull String line) {
        for (int index = 0; index < lines.length; index++) {
            if (lines[index] == null) {
                return line(index, line);
            }
        }
        return this;
    }

    @Override
    public @NotNull SignGuiBuilder line(final int index, final @NotNull String line) {
        lines[index] = line;
        return this;
    }

    @Override
    public @NotNull SignGuiBuilder lines(final @NotNull String... lines) {
        int index = 0;
        for (final String line : lines) {
            line(index++, line);
            if (index >= 4) {
                break;
            }
        }
        return this;
    }

    @Override
    public @NotNull SignGuiHooks build(final @NotNull Player player) {
        return Kunectron.create(new Gui(player, type, textColor, glowing, lines, handlers));
    }

    @SignGui
    private static final class Gui extends AbstractGui<SignGuiHooks> implements SignGuiHooks {
        @SignGui.Player
        private final @NotNull Player player;

        @SignGui.Type
        private final @NotNull SignGui.SignType type;

        @SignGui.TextColor
        private final @NotNull DyeColor textColor;

        @SignGui.Glowing
        private final boolean glowing;

        @SignGui.Line
        private final @Nullable String firstLine;

        @SignGui.Line
        private final @Nullable String secondLine;

        @SignGui.Line
        private final @Nullable String thirdLine;

        @SignGui.Line
        private final @Nullable String fourthLine;

        public Gui(final @NotNull Player player, final @NotNull SignGui.SignType type, final @NotNull DyeColor textColor, final boolean glowing, final @NotNull String[] lines, final @NotNull List<HandlerInfo> handlers) {
            super(handlers);
            this.player = player;
            this.type = type;
            this.textColor = textColor;
            this.glowing = glowing;
            this.firstLine = lines[0];
            this.secondLine = lines[1];
            this.thirdLine = lines[2];
            this.fourthLine = lines[3];
        }
    }
}
