package com.tksimeji.kunectron.markupextensions.operator;

import com.tksimeji.kunectron.markupextensions.MarkupExtensionsException;
import com.tksimeji.kunectron.markupextensions.context.Context;
import com.tksimeji.kunectron.markupextensions.ast.*;
import org.jetbrains.annotations.NotNull;

public final class TimesOperator implements BinaryOperator<NumberNode<?>> {
    @Override
    public @NotNull String getOperator() {
        return "*";
    }

    @Override
    public @NotNull NumberNode<?> evaluate(final @NotNull Context<?> ctx, final @NotNull Object left, final @NotNull Object right) {
        if (!(left instanceof Number leftNumber) || !(right instanceof Number rightNumber)) {
            throw new MarkupExtensionsException(String.format("Invalid operand combination: %s * %s", left.getClass().getName(), right.getClass().getName()));
        }

        if (left instanceof Integer leftInteger && right instanceof Integer rightInteger) {
            return new IntegerNumberNode(leftInteger * rightInteger);
        }

        return new DoubleNumberNode(leftNumber.doubleValue() * rightNumber.doubleValue());
    }
}
