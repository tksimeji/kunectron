package com.tksimeji.kunectron.markupextensions.ast;

import com.tksimeji.kunectron.markupextensions.context.Context;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class MemberAccessNode implements AstNode<Object> {
    private final @NotNull AstNode<?> node;
    private final @NotNull String member;

    public MemberAccessNode(final @NotNull AstNode<?> node, final @NotNull String member) {
        this.node = node;
        this.member = member;
    }

    @Override
    public @NotNull Object evaluate(final @NotNull Context<?> ctx) {
        return Optional.ofNullable(Context.context(node.evaluate(ctx)).getMember(member)).orElse("null");
    }
}
