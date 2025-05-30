package com.tksimeji.kunectron.markupextensions.ast;

import com.tksimeji.kunectron.markupextensions.context.Context;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class IdentifierNode implements AstNode<Object> {
    private final @NotNull String name;

    public IdentifierNode(final @NotNull String name) {
        this.name = name;
    }

    @Override
    public @NotNull Object evaluate(final @NotNull Context<?> ctx) {
        return Optional.ofNullable(ctx.getState(name)).orElse("null");
    }
}
