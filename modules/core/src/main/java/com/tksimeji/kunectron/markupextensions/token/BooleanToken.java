package com.tksimeji.kunectron.markupextensions.token;

import org.jetbrains.annotations.NotNull;

final class BooleanToken extends TokenImpl {
    public BooleanToken(final @NotNull String value) {
        super(value);
    }

    @Override
    public boolean isBoolean() {
        return true;
    }
}
