package com.tksimeji.kunectron.markupextensions.ast;

import com.tksimeji.kunectron.markupextensions.context.Context;
import org.jetbrains.annotations.NotNull;

public interface AstNode<T> extends AstNodeBase<T> {
    @Override
    @NotNull T evaluate(final @NotNull Context<?> ctx);

    @Override
    default @NotNull Object evaluateDeep(final @NotNull Context<?> ctx) {
        if (this instanceof PrimitiveNode<?> primitiveNode) {
            return primitiveNode.getValue();
        }

        if (this instanceof BinaryOpNode binaryOpNode) {
            AstNode<?> node = binaryOpNode.evaluate(ctx);
            while (node instanceof BinaryOpNode) {
                node = ((BinaryOpNode) node).evaluate(ctx);
            }
            return node.evaluateDeep(ctx);
        }

        return evaluate(ctx);
    }
}
