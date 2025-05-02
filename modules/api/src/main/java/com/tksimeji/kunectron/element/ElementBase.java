package com.tksimeji.kunectron.element;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
interface ElementBase<T> extends Cloneable {
    @NotNull T clone();
}
