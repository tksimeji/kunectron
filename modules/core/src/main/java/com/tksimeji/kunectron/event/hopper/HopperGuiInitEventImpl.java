package com.tksimeji.kunectron.event.hopper;

import com.tksimeji.kunectron.event.GuiEventImpl;
import com.tksimeji.kunectron.event.HopperGuiEvents;
import org.jetbrains.annotations.NotNull;

public final class HopperGuiInitEventImpl extends GuiEventImpl implements HopperGuiEvents.InitEvent {
    public HopperGuiInitEventImpl(final @NotNull Object gui) {
        super(gui);
    }
}
