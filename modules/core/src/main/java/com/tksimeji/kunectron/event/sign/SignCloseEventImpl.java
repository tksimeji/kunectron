package com.tksimeji.kunectron.event.sign;

import com.tksimeji.kunectron.event.GuiEventImpl;
import com.tksimeji.kunectron.event.SignGuiEvents;
import org.jetbrains.annotations.NotNull;

public final class SignCloseEventImpl extends GuiEventImpl implements SignGuiEvents.CloseEvent {
    private final @NotNull String[] lines;

    public SignCloseEventImpl(final @NotNull Object gui, final @NotNull String[] lines) {
        super(gui);
        this.lines = lines;
    }

    @Override
    public @NotNull String[] getLines() {
        return lines;
    }

    @Override
    public @NotNull String getFirstLine() {
        return lines[0];
    }

    @Override
    public @NotNull String getSecondLine() {
        return lines[1];
    }

    @Override
    public @NotNull String getThirdLine() {
        return lines[2];
    }

    @Override
    public @NotNull String getFourthLine() {
        return lines[3];
    }
}
