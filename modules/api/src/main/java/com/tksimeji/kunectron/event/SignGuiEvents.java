package com.tksimeji.kunectron.event;

import org.jetbrains.annotations.NotNull;

public final class SignGuiEvents {
    private SignGuiEvents() {
        throw new UnsupportedOperationException();
    }

    public interface InitEvent extends GuiEvent {
    }

    public interface CloseEvent extends GuiEvent {
        @NotNull String[] getLines();

        @NotNull String getFirstLine();

        @NotNull String getSecondLine();

        @NotNull String getThirdLine();

        @NotNull String getFourthLine();
    }
}
