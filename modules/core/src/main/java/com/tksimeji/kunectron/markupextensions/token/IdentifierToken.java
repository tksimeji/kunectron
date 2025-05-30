package com.tksimeji.kunectron.markupextensions.token;

import org.jetbrains.annotations.NotNull;

final class IdentifierToken extends TokenImpl {
    public IdentifierToken(final @NotNull String value) {
        super(value);
    }

    @Override
    public boolean isIdentifier() {
        return true;
    }
}
