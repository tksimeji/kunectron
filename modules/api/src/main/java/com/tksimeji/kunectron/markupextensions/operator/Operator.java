package com.tksimeji.kunectron.markupextensions.operator;

import com.tksimeji.kunectron.markupextensions.ast.AstNode;
import org.jetbrains.annotations.NotNull;

public interface Operator<T extends AstNode<?>> {
    @NotNull String getOperator();
}
