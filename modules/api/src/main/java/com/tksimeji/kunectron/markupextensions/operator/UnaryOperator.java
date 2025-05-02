package com.tksimeji.kunectron.markupextensions.operator;

import com.tksimeji.kunectron.markupextensions.ast.AstNode;
import com.tksimeji.kunectron.markupextensions.context.Context;
import org.jetbrains.annotations.NotNull;

public interface UnaryOperator<T extends AstNode<?>> extends Operator<T> {
    @NotNull T evaluate(final @NotNull Context<?> ctx, final @NotNull Object operand);
}
