package com.tksimeji.kunectron.markupextensions.operator;

import com.tksimeji.kunectron.markupextensions.MarkupExtensionsException;
import com.tksimeji.kunectron.markupextensions.ast.BooleanNode;
import com.tksimeji.kunectron.markupextensions.context.Context;
import org.jetbrains.annotations.NotNull;

public class OrOperator implements BinaryOperator<BooleanNode> {
    @Override
    public @NotNull String getOperator() {
        return "||";
    }

    @Override
    public @NotNull BooleanNode evaluate(final @NotNull Context<?> ctx, final @NotNull Object left, final @NotNull Object right) {
        if (!(left instanceof Boolean leftBoolean) || !(right instanceof Boolean rightBoolean)) {
            throw new MarkupExtensionsException(String.format("Invalid operand combination: %s && %s", left.getClass().getName(), right.getClass().getName()));
        }
        return new BooleanNode(leftBoolean || rightBoolean);
    }
}
