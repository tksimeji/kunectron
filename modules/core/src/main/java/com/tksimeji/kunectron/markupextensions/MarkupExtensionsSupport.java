package com.tksimeji.kunectron.markupextensions;

import com.tksimeji.kunectron.markupextensions.context.Context;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public interface MarkupExtensionsSupport {
    @ApiStatus.Internal
    @Nullable Context<?> getContext();

    @ApiStatus.Internal
    void setContext(final @Nullable Context<?> ctx);
}
