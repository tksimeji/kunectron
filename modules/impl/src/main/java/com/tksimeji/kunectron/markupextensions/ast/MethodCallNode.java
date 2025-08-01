package com.tksimeji.kunectron.markupextensions.ast;

import com.tksimeji.kunectron.markupextensions.context.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public final class MethodCallNode implements AstNode<Object> {
    private final @NotNull String name;
    private final @NotNull List<AstNode<?>> args;
    private final @Nullable AstNode<?> target;

    public MethodCallNode(final @NotNull String name, final @NotNull Collection<AstNode<?>> args) {
        this(name, args, null);
    }

    public MethodCallNode(final @NotNull String name, final @NotNull Collection<AstNode<?>> args, final @Nullable AstNode<?> target) {
        this.name = name;
        this.args = new ArrayList<>(args);
        this.target = target;
    }

    @Override
    public @NotNull Object evaluate(final @NotNull Context<?> ctx) {
        final Object[] args = this.args.stream().map(argument -> argument.evaluateDeep(ctx)).toArray();
        if (target != null) {
            Object targetValue = target.evaluate(ctx);
            return Optional.ofNullable(Context.context(targetValue).getFunction(name, args)).orElse("null");
        } else {
            return Optional.ofNullable(ctx.getFunction(name, args)).orElse("null");
        }
    }
}