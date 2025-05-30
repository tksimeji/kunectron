package com.tksimeji.kunectron.markupextensions.token;

import org.jetbrains.annotations.NotNull;

final class NumberToken extends TokenImpl {
    public NumberToken(final @NotNull String value) {
        super(value);
    }

    @Override
    public boolean isNumber() {
        return true;
    }
}
