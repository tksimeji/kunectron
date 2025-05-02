package com.tksimeji.kunectron.markupextensions.token;

import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public interface Tokenizer extends TokenizerBase {
    static @NotNull Tokenizer tokenizer() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }
}
