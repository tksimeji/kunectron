package com.tksimeji.kunectron.markupextensions;

import org.jetbrains.annotations.NotNull;

public interface MarkupExtensionsParser extends IMarkupExtensionsParser {
    static @NotNull MarkupExtensionsParser markupExtensionsParser() {
        return new MarkupExtensionsParserImpl();
    }
}
