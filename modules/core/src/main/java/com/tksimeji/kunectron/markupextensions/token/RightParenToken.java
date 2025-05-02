package com.tksimeji.kunectron.markupextensions.token;

final class RightParenToken extends TokenImpl {
    public RightParenToken() {
        super(")");
    }

    @Override
    public boolean isRightParen() {
        return true;
    }
}
