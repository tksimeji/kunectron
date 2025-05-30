package com.tksimeji.kunectron.markupextensions.ast;

import com.tksimeji.kunectron.markupextensions.context.Context;
import org.jetbrains.annotations.NotNull;

public final class IntegerNumberNode implements NumberNode<Integer> {
    private final int value;

    public IntegerNumberNode(final int value) {
        this.value = value;
    }

    public IntegerNumberNode(final @NotNull String value) {
        this(Integer.parseInt(value));
    }

    @Override
    public @NotNull Integer getValue() {
        return value;
    }

    @Override
    public @NotNull Integer evaluate(@NotNull Context<?> ctx) {
        return value;
    }

    @Override
    public @NotNull String toString() {
        return String.valueOf(value);
    }
}
