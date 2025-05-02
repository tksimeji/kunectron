package com.tksimeji.kunectron.markupextensions.token;

final class CommaToken extends TokenImpl {
    public CommaToken() {
        super(",");
    }

    @Override
    public boolean isComma() {
        return true;
    }
}
