package com.tksimeji.kunectron.markupextensions;

import com.tksimeji.kunectron.markupextensions.ast.AstNode;
import com.tksimeji.kunectron.markupextensions.token.Token;
import com.tksimeji.kunectron.markupextensions.token.Tokenizer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@ApiStatus.Internal
interface MarkupExtensionsParserBase {
    @NotNull AstNode<?> parse(final @NotNull String input);

    @NotNull AstNode<?> parse(final @NotNull String input, final @NotNull Tokenizer tokenizer);

    @NotNull AstNode<?> parse(final @NotNull Collection<Token> tokens);
}
