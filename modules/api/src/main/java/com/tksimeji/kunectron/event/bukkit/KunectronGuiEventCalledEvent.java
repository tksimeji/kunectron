package com.tksimeji.kunectron.event.bukkit;

import com.tksimeji.kunectron.controller.GuiController;
import com.tksimeji.kunectron.event.GuiEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class KunectronGuiEventCalledEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    private final @NotNull GuiEvent event;

    private final @NotNull GuiController controller;

    public KunectronGuiEventCalledEvent(final @NotNull GuiEvent event, final @NotNull GuiController controller) {
        super(true);
        this.event = event;
        this.controller = controller;
    }

    public @NotNull GuiEvent getEvent() {
        return event;
    }

    public @NotNull Object getGui() {
        return event.getGui();
    }

    public @NotNull GuiController getController() {
        return controller;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
