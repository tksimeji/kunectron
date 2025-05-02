package com.tksimeji.kunectron.markupextensions.ast;

import com.tksimeji.kunectron.markupextensions.context.Context;
import org.jetbrains.annotations.NotNull;

public final class BooleanNode implements PrimitiveNode<Boolean> {
    private final boolean value;

    public BooleanNode(final boolean value) {
        this.value = value;
    }

    public BooleanNode(final @NotNull String value) {
        this(Boolean.parseBoolean(value));
    }

    @Override
    public @NotNull Boolean getValue() {
        return value;
    }

    @Override
    public @NotNull Boolean evaluate(final @NotNull Context<?> ctx) {
        return value;
    }
}
