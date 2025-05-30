package com.tksimeji.kunectron.markupextensions;

import com.tksimeji.kunectron.markupextensions.ast.*;
import com.tksimeji.kunectron.markupextensions.operator.*;
import com.tksimeji.kunectron.markupextensions.token.Token;
import com.tksimeji.kunectron.markupextensions.token.Tokenizer;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

final class MarkupExtensionsParserImpl implements MarkupExtensionsParser {
    static class ParseInfo {
        public final @NotNull List<Token> tokens;

        public int position = 0;

        public ParseInfo(final @NotNull Collection<Token> tokens) {
            this.tokens = new ArrayList<>(tokens);
        }
    }

    private final @NotNull Map<String, BinaryOperator<?>> binaryOperators;
    private final @NotNull Map<String, UnaryOperator<?>> unaryOperators;

    public MarkupExtensionsParserImpl() {
        this(List.of(new PlusOperator(), new MinusOperator(), new TimesOperator(), new DivididedByOperator(), new EqualityOperator(), new InequalityOperator(), new StrictEqualityOperator(), new StrictInequalityOperator(), new AndOperator(), new OrOperator(), new NotOperator()));
    }

    public MarkupExtensionsParserImpl(final @NotNull Collection<Operator<?>> operators) {
        this.binaryOperators = operators.stream()
                .filter(operator -> operator instanceof BinaryOperator<?>)
                .collect(Collectors.toMap(Operator::getOperator, operator -> (BinaryOperator<?>) operator));
        this.unaryOperators = operators.stream()
                .filter(operator -> operator instanceof UnaryOperator<?>)
                .collect(Collectors.toMap(Operator::getOperator, operator -> (UnaryOperator<?>) operator));
    }

    @Override
    public @NotNull AstNode<?> parse(final @NotNull String input) {
        return parse(input, Tokenizer.tokenizer());
    }

    @Override
    public @NotNull AstNode<?> parse(@NotNull String input, @NotNull Tokenizer tokenizer) {
        return parse(tokenizer.tokenize(input));
    }

    public @NotNull AstNode<?> parse(final @NotNull Collection<Token> tokens) {
        final ParseInfo info = new ParseInfo(tokens);
        return parseOr(info);
    }

    private @NotNull AstNode<?> parseOr(final @NotNull ParseInfo info) {
        AstNode<?> leftNode = parseAnd(info);
        while (info.position < info.tokens.size() && info.tokens.get(info.position).isBinaryOperator() && info.tokens.get(info.position).getValue().equals("||")) {
            final String operator = info.tokens.get(info.position++).getValue();
            final AstNode<?> rightNode = parseAnd(info);
            leftNode = new BinaryOpNode(leftNode, binaryOperators.get(operator), rightNode);
        }
        return leftNode;
    }

    private @NotNull AstNode<?> parseAnd(final @NotNull ParseInfo info) {
        AstNode<?> leftNode = parseEquality(info);
        while (info.position < info.tokens.size() && info.tokens.get(info.position).isBinaryOperator() && info.tokens.get(info.position).getValue().equals("&&")) {
            final String operator = info.tokens.get(info.position++).getValue();
            final AstNode<?> rightNode = parseExpression(info);
            leftNode = new BinaryOpNode(leftNode, binaryOperators.get(operator), rightNode);
        }
        return leftNode;
    }

    private @NotNull AstNode<?> parseEquality(final @NotNull ParseInfo info) {
        AstNode<?> leftNode = parseExpression(info);
        while (info.position < info.tokens.size() && info.tokens.get(info.position).isBinaryOperator() && (info.tokens.get(info.position).getValue().equals("==") || info.tokens.get(info.position).getValue().equals("!=") || info.tokens.get(info.position).getValue().equals("===") || info.tokens.get(info.position).getValue().equals("!=="))) {
            final String operator = info.tokens.get(info.position++).getValue();
            final AstNode<?> rightNode = parseExpression(info);
            leftNode = new BinaryOpNode(leftNode, binaryOperators.get(operator), rightNode);
        }
        return leftNode;
    }

    private @NotNull AstNode<?> parseExpression(final @NotNull ParseInfo info) {
        AstNode<?> leftNode = parseTerm(info);
        while (info.position < info.tokens.size() && info.tokens.get(info.position).isBinaryOperator() && (info.tokens.get(info.position).getValue().equals("+") || info.tokens.get(info.position).getValue().equals("-"))) {
            final String operator = info.tokens.get(info.position++).getValue();
            final AstNode<?> rightNode = parseTerm(info);
            leftNode = new BinaryOpNode(leftNode, binaryOperators.get(operator), rightNode);
        }
        return leftNode;
    }

    private @NotNull AstNode<?> parseTerm(final @NotNull ParseInfo info) {
        AstNode<?> leftNode = parseFactor(info);
        while (info.position < info.tokens.size() && info.tokens.get(info.position).isBinaryOperator() && (info.tokens.get(info.position).getValue().equals("*") || info.tokens.get(info.position).getValue().equals("/"))) {
            final String operator = info.tokens.get(info.position++).getValue();
            final AstNode<?> rightNode = parseFactor(info);
            leftNode = new BinaryOpNode(leftNode, binaryOperators.get(operator), rightNode);
        }
        return leftNode;
    }

    private @NotNull AstNode<?> parseFactor(final @NotNull ParseInfo info) {
        final Token token = info.tokens.get(info.position);
        final String tokenValue = token.getValue();

        if (token.isUnaryOperator()) {
            info.position++;
            final AstNode<?> operand = parseFactor(info);
            return new UnaryOpNode(unaryOperators.get("!"), operand);
        }

        info.position++;

        if (token.isBoolean()) {
            return new BooleanNode(tokenValue);
        }

        if (token.isNumber() && tokenValue.contains(".")) {
            return new DoubleNumberNode(tokenValue);
        }

        if (token.isNumber()) {
            return new IntegerNumberNode(tokenValue);
        }

        if (token.isString()) {
            return new StringNode(tokenValue);
        }

        if (token.isIdentifier()) {
            AstNode<?> currentNode;

            if (info.position < info.tokens.size() && info.tokens.get(info.position).isLeftParen()) {
                info.position++;
                final List<AstNode<?>> args = new ArrayList<>();

                if (!info.tokens.get(info.position).isRightParen()) {
                    args.add(parseOr(info));
                    while (info.position < info.tokens.size() && info.tokens.get(info.position).isComma()) {
                        info.position++;
                        args.add(parseOr(info));
                    }
                }

                if (info.position >= info.tokens.size() || !info.tokens.get(info.position).isRightParen()) {
                    throw new RuntimeException("Expected closing parenthesis.");
                }
                info.position++;

                currentNode = new MethodCallNode(tokenValue, args, null);
            } else {
                currentNode = new IdentifierNode(tokenValue);
            }

            while (info.position < info.tokens.size() && info.tokens.get(info.position).isDot()) {
                info.position++;

                if (info.position >= info.tokens.size()) {
                    throw new RuntimeException("Unexpected end of tokens after dot.");
                }

                final Token nextToken = info.tokens.get(info.position);
                final String nextTokenValue = nextToken.getValue();

                if (nextToken.isIdentifier() && info.position + 1 < info.tokens.size() && info.tokens.get(info.position + 1).isLeftParen()) {
                    info.position++;
                    info.position++;
                    final List<AstNode<?>> args = new ArrayList<>();

                    if (!info.tokens.get(info.position).isRightParen()) {
                        args.add(parseOr(info));
                        while (info.position < info.tokens.size() && info.tokens.get(info.position).isComma()) {
                            info.position++;
                            args.add(parseOr(info));
                        }
                    }

                    if (info.position >= info.tokens.size() || !info.tokens.get(info.position).isRightParen()) {
                        throw new RuntimeException("Expected closing parenthesis.");
                    }
                    info.position++;
                    currentNode = new MethodCallNode(nextTokenValue, args, currentNode);
                } else if (nextToken.isIdentifier()) {
                    info.position++;
                    currentNode = new MemberAccessNode(currentNode, nextTokenValue);
                } else {
                    throw new RuntimeException("Unexpected token after dot: " + nextTokenValue);
                }
            }

            return currentNode;
        }

        if (token.isLeftParen()) {
            final AstNode<?> orNode = parseOr(info);
            if (info.position >= info.tokens.size() || !info.tokens.get(info.position).isRightParen()) {
                throw new RuntimeException("Expected closing parenthesis.");
            }
            info.position++;
            return orNode;
        }

        throw new RuntimeException("Unexpected token: " + token.getValue());
    }
}
