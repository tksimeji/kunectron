package com.tksimeji.kunectron.markupextensions.ast;

import com.tksimeji.kunectron.markupextensions.context.Context;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public interface AstNode<T> extends AstNodeBase<T> {
    @Override
    @NotNull T evaluate(final @NotNull Context<?> ctx);

    @Override
    default @NotNull Object evaluateDeep(final @NotNull Context<?> ctx) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }
}
