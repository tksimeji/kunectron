package com.tksimeji.kunectron.markupextensions.token;

import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public interface TokenFactory extends TokenFactoryBase {
    static @NotNull TokenFactory tokenFactory() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }
}
