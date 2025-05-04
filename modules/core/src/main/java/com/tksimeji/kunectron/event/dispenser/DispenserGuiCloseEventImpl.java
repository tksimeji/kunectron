package com.tksimeji.kunectron.event.dispenser;

import com.tksimeji.kunectron.event.DispenserGuiEvents;
import com.tksimeji.kunectron.event.GuiEventImpl;
import org.jetbrains.annotations.NotNull;

public final class DispenserGuiCloseEventImpl extends GuiEventImpl implements DispenserGuiEvents.CloseEvent {
    public DispenserGuiCloseEventImpl(final @NotNull Object gui) {
        super(gui);
    }
}
