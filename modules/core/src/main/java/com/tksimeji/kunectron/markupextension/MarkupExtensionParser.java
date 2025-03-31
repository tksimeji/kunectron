package com.tksimeji.kunectron.markupextension;

import org.jetbrains.annotations.NotNull;

public interface MarkupExtensionParser extends IMarkupExtensionParser {
    static @NotNull MarkupExtensionParser markupExtensionParser() {
        return new MarkupExtensionParserImpl();
    }
}
