package com.tksimeji.kunectron.markupextensions;

import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public interface MarkupExtensionsParser extends MarkupExtensionsParserBase {
    static @NotNull MarkupExtensionsParser markupExtensionsParser() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }
}
