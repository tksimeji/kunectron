package com.tksimeji.kunectron.markupextensions.context;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
interface ContextBase<T> {
    @NotNull T getObject();

    @Nullable Object getState(final @NotNull String name);

    @Nullable Object getFunction(final @NotNull String name, final @NotNull Object... args);

    @Nullable Object getMember(final @NotNull String name);
}
