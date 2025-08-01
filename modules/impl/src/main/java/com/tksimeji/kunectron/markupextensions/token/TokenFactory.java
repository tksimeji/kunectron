package com.tksimeji.kunectron.markupextensions.token;

import org.jetbrains.annotations.NotNull;

public interface TokenFactory extends TokenFactoryBase {
    static @NotNull TokenFactory tokenFactory() {
        return new TokenFactoryImpl();
    }
}
