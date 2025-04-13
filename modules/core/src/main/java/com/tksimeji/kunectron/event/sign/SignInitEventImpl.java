package com.tksimeji.kunectron.event.sign;

import com.tksimeji.kunectron.event.GuiEventImpl;
import com.tksimeji.kunectron.event.SignGuiEvents;
import org.jetbrains.annotations.NotNull;

public final class SignInitEventImpl extends GuiEventImpl implements SignGuiEvents.InitEvent {
    public SignInitEventImpl(final @NotNull Object gui) {
        super(gui);
    }
}
