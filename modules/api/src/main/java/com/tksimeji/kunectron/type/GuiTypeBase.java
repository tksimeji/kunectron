package com.tksimeji.kunectron.type;

import com.tksimeji.kunectron.controller.GuiController;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

@ApiStatus.Internal
interface GuiTypeBase<A extends Annotation, C extends GuiController> {
    @NotNull Class<A> getAnnotationClass();

    @NotNull Class<C> getControllerClass();

    @NotNull C createController(final @NotNull Object gui, final @NotNull A annotation);
}
