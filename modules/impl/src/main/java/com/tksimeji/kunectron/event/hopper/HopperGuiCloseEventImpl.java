package com.tksimeji.kunectron.event.hopper;

import com.tksimeji.kunectron.event.GuiEventImpl;
import com.tksimeji.kunectron.event.HopperGuiEvents;
import org.jetbrains.annotations.NotNull;

public final class HopperGuiCloseEventImpl extends GuiEventImpl implements HopperGuiEvents.CloseEvent {
    public HopperGuiCloseEventImpl(final @NotNull Object gui) {
        super(gui);
    }
}
