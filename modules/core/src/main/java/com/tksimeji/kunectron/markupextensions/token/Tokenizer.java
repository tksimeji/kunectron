package com.tksimeji.kunectron.markupextensions.token;

import org.jetbrains.annotations.NotNull;

public interface Tokenizer extends TokenizerBase {
    static @NotNull Tokenizer tokenizer() {
        return new TokenizerImpl();
    }
}
