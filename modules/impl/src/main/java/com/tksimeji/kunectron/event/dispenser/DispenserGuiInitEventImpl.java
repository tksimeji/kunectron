package com.tksimeji.kunectron.event.dispenser;

import com.tksimeji.kunectron.event.DispenserGuiEvents;
import com.tksimeji.kunectron.event.GuiEventImpl;
import org.jetbrains.annotations.NotNull;

public final class DispenserGuiInitEventImpl extends GuiEventImpl implements DispenserGuiEvents.InitEvent {
    public DispenserGuiInitEventImpl(final @NotNull Object gui) {
        super(gui);
    }
}
