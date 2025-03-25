package com.tksimeji.kunectron.event.bukkit;

import com.tksimeji.kunectron.controller.GuiController;
import com.tksimeji.kunectron.event.GuiEvent;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class KunectronGuiEventCallEvent extends org.bukkit.event.Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    private final @NotNull GuiEvent event;

    private final @NotNull GuiController controller;

    private boolean cancelled;

    public KunectronGuiEventCallEvent(final @NotNull GuiEvent event, final @NotNull GuiController controller) {
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

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
