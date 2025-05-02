package com.tksimeji.kunectron.markupextensions.operator;

import com.tksimeji.kunectron.markupextensions.context.Context;
import com.tksimeji.kunectron.markupextensions.ast.AstNode;
import org.jetbrains.annotations.NotNull;

public interface BinaryOperator<T extends AstNode<?>> extends Operator<T> {
    @NotNull T evaluate(final @NotNull Context<?> ctx, final @NotNull Object left, final @NotNull Object right);
}
