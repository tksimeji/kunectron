package com.tksimeji.kunectron.event.dispenser;

import com.tksimeji.kunectron.Action;
import com.tksimeji.kunectron.Mouse;
import com.tksimeji.kunectron.element.ItemElement;
import com.tksimeji.kunectron.event.DispenserGuiEvents;
import com.tksimeji.kunectron.event.ItemContainerClickEventImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DispenserGuiClickEventImpl extends ItemContainerClickEventImpl implements DispenserGuiEvents.ClickEvent {
    public DispenserGuiClickEventImpl(final @NotNull Object gui, final int index, final @Nullable ItemElement element, final @NotNull Action action, final @NotNull Mouse mouse) {
        super(gui, index, element, action, mouse);
    }
}
