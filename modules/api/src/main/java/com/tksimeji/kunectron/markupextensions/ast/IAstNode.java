package com.tksimeji.kunectron.markupextensions.ast;

import com.tksimeji.kunectron.markupextensions.context.Context;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
interface IAstNode<T> {
    @NotNull T evaluate(final @NotNull Context<?> ctx);

    @NotNull Object evaluateDeep(final @NotNull Context<?> ctx);
}
