package com.tksimeji.kunectron.markupextensions.context;

import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public interface Context<T> extends ContextBase<T> {
    static <T> @NotNull Context<T> context(final @NotNull T object) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    static <T> @NotNull MutableContext<T> mutable(final @NotNull T object) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }
}
