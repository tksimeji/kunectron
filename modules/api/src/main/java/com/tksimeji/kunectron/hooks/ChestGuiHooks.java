package com.tksimeji.kunectron.hooks;

import com.tksimeji.kunectron.element.ItemElement;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ChestGuiHooks extends ChestGuiHooksBase {
    @Override
    default @Nullable ItemElement useElement(final int index) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    @Override
    default void useElement(final int index, final @NotNull ItemElement element) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    @Override
    default void useClearElement(final int index) {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }

    @Override
    default @NotNull Map<Integer, ItemElement> useElements() {
        throw new NotImplementedException("The API module cannot be called at runtime.");
    }
}
