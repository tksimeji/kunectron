package com.tksimeji.kunectron.builder;

import com.tksimeji.kunectron.event.GuiEvent;
import com.tksimeji.kunectron.hooks.Hooks;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
interface GuiBuilderBase<B extends GuiBuilderBase<B, H>, H extends Hooks> {
    @NotNull <E extends GuiEvent> B handler(final @NotNull Class<E> event, final @NotNull GuiBuilderBase.HandlerFunction1<E> function);

    @NotNull <E extends GuiEvent> B handler(final @NotNull Class<E> event, final @NotNull GuiBuilderBase.HandlerFunction2<E, H> function);

    @NotNull <E extends GuiEvent> B handlerAsync(final @NotNull Class<E> event, final @NotNull GuiBuilderBase.HandlerFunction1<E> function);

    @NotNull <E extends GuiEvent> B handlerAsync(final @NotNull Class<E> event, final @NotNull GuiBuilderBase.HandlerFunction2<E, H> function);

    interface HandlerFunction {
    }

    @FunctionalInterface
    interface HandlerFunction1<E extends GuiEvent> extends HandlerFunction {
        void onEvent(@NotNull E event);
    }

    @FunctionalInterface
    interface HandlerFunction2<E extends GuiEvent, H> extends HandlerFunction {
        void onEvent(@NotNull E event, @NotNull H hooks);
    }
}