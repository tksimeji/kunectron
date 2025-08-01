package com.tksimeji.kunectron.event.bukkit;

import com.tksimeji.kunectron.controller.GuiController;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class KunectronGuiDeleteEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    private final @NotNull Object gui;

    private final @NotNull GuiController controller;

    private boolean cancelled;

    public KunectronGuiDeleteEvent(final @NotNull Object gui, final @NotNull GuiController controller) {
        super(true);
        this.gui = gui;
        this.controller = controller;
    }

    public @NotNull Object getGui() {
        return gui;
    }

    public @NotNull GuiController getController() {
        return controller;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Deprecated
    @Override
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
