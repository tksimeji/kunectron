package com.tksimeji.kunectron.controller;

import com.tksimeji.kunectron.Action;
import com.tksimeji.kunectron.AnvilGui;
import com.tksimeji.kunectron.Mouse;
import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.controller.impl.ItemContainerGuiControllerImpl;
import com.tksimeji.kunectron.element.ItemElement;
import com.tksimeji.kunectron.event.AnvilGuiEvents;
import com.tksimeji.kunectron.event.GuiEvent;
import com.tksimeji.kunectron.event.anvil.AnvilGuiClickEventImpl;
import com.tksimeji.kunectron.event.anvil.AnvilGuiCloseEventImpl;
import com.tksimeji.kunectron.event.anvil.AnvilGuiInitEventImpl;
import com.tksimeji.kunectron.policy.ItemSlotPolicy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.translation.GlobalTranslator;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.AnvilInventory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public final class AnvilGuiController extends ItemContainerGuiControllerImpl<AnvilInventory> {
    private final @NotNull Player player;

    private @NotNull AnvilInventory inventory;

    private @NotNull String text = "";

    private final boolean overwriteResultSlot;

    public AnvilGuiController(final @NotNull Object gui, final @NotNull AnvilGui annotation) {
        super(gui, annotation.autoReload(), annotation.serverSideTranslation(), annotation.markupExtensions());

        player = getDeclarationOrThrow(gui, AnvilGui.Player.class, Player.class).getLeft();
        overwriteResultSlot = annotation.overwriteResultSlot();

        Bukkit.getScheduler().runTask(Kunectron.plugin(), () -> {
            Component title = getDeclarationOrDefault(gui, AnvilGui.Title.class, ComponentLike.class, Component.empty()).getLeft().asComponent();
            if (title instanceof TranslatableComponent translatableComponent &&
                    GlobalTranslator.translator().canTranslate(translatableComponent.key(), getLocale())) {
                title = GlobalTranslator.render(title, getLocale());
            }

            inventory = Kunectron.adapter().createAnvilInventory(player, title);

            getDeclaration(gui, AnvilGui.DefaultPolicy.class, ItemSlotPolicy.class).ifPresent(declaration -> {
                setDefaultPolicy(declaration.getLeft());
            });

            getDeclaration(gui, AnvilGui.PlayerDefaultPolicy.class, ItemSlotPolicy.class).ifPresent(declaration -> {
                setPlayerDefaultPolicy(declaration.getLeft());
            });

            getDeclaration(gui, AnvilGui.FirstElement.class, ItemElement.class).ifPresent(declaration -> {
                setFirstElement(declaration.getLeft());
            });

            getDeclaration(gui, AnvilGui.SecondElement.class, ItemElement.class).ifPresent(declaration -> {
                setSecondElement(declaration.getLeft());
            });

            getDeclaration(gui, AnvilGui.ResultElement.class, ItemElement.class).ifPresent(declaration -> {
                setResultElement(declaration.getLeft());
            });

            for (final Pair<ItemSlotPolicy, AnvilGui.Policy> declaration : getDeclarations(gui, AnvilGui.Policy.class, ItemSlotPolicy.class)) {
                for (final int index : parseAnnotation(declaration.getRight())) {
                    setPolicy(index, declaration.getLeft());
                }
            }
        });
    }

    @Override
    public void init() {
        callEvent(new AnvilGuiInitEventImpl(gui));
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    @Override
    public @NotNull AnvilInventory getInventory() {
        return inventory;
    }

    public @NotNull String getText() {
        return text;
    }

    public @Nullable ItemElement getFirstElement() {
        return getElement(0);
    }

    public void setFirstElement(final @Nullable ItemElement element) {
        setElement(0, element);
    }

    public @Nullable ItemElement getSecondElement() {
        return getElement(1);
    }

    public void setSecondElement(final @Nullable ItemElement element) {
        setElement(1, element);
    }

    public @Nullable ItemElement getResultElement() {
        return getElement(2);
    }

    public void setResultElement(final @Nullable ItemElement element) {
        setElement(2, element);
    }

    public boolean isOverwriteResultSlot() {
        return overwriteResultSlot;
    }

    @Override
    public void click(final int index, final @NotNull Action action, final @NotNull Mouse mouse) {
        callEvent(new AnvilGuiClickEventImpl(gui, index, getElement(index), action, mouse));
        super.click(index, action, mouse);
    }

    @Override
    public void close() {
        callEvent(new AnvilGuiCloseEventImpl(gui, text));
        inventory.clear();
        super.close();
    }

    @Override
    public boolean callEvent(final @NotNull GuiEvent event) {
        if (event instanceof AnvilGuiEvents.TextChangeEvent textChangeEvent) {
            text = textChangeEvent.getText();
        }

        return super.callEvent(event);
    }

    @ApiStatus.Internal
    private @NotNull Set<Integer> parseAnnotation(final @NotNull AnvilGui.Policy annotation) {
        return parseIndexGroup(annotation.index(), annotation.groups(), annotation.player());
    }
}
