package com.tksimeji.kunectron.markupextensions.token;

import org.jetbrains.annotations.NotNull;

public final class UnaryOperatorToken extends TokenImpl {
    public UnaryOperatorToken(@NotNull String value) {
        super(value);
    }

    @Override
    public boolean isUnaryOperator() {
        return true;
    }
}
