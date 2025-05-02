package com.tksimeji.kunectron.markupextensions.ast;

import com.tksimeji.kunectron.markupextensions.context.Context;
import org.jetbrains.annotations.NotNull;

public final class DoubleNumberNode implements NumberNode<Double> {
    private final double value;

    public DoubleNumberNode(final double value) {
        this.value = value;
    }

    public DoubleNumberNode(final @NotNull String value) {
        this(Double.parseDouble(value));
    }

    @Override
    public @NotNull Double getValue() {
        return value;
    }

    @Override
    public @NotNull Double evaluate(final @NotNull Context<?> ctx) {
        return value;
    }

    @Override
    public @NotNull String toString() {
        return String.valueOf(value);
    }
}
