package com.tksimeji.kunectron.markupextension.token;

import org.jetbrains.annotations.NotNull;

public interface Tokenizer extends ITokenizer {
    static @NotNull Tokenizer tokenizer() {
        return new TokenizerImpl();
    }
}
