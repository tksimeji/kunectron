package com.tksimeji.kunectron.markupextension.token;

import org.jetbrains.annotations.NotNull;

public interface TokenFactory extends ITokenFactory {
    static @NotNull TokenFactory tokenFactory() {
        return new TokenFactoryImpl();
    }
}
