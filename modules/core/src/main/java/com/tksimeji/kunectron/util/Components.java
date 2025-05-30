package com.tksimeji.kunectron.util;

import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.markupextensions.MarkupExtensionsException;
import com.tksimeji.kunectron.markupextensions.context.Context;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Components {
    private static final @NotNull Pattern markupExtensionPattern = Pattern.compile("\\{([^}]*)}");

    public static @NotNull Component translate(final @NotNull Component component, final @NotNull Locale locale) {
        if (component instanceof TranslatableComponent translatableComponent) {
            return translate0(translatableComponent, locale);
        }
        final List<Component> children = new ArrayList<>();
        for (final Component child : component.children()) {
            if (child instanceof TranslatableComponent translatableComponent) {
                children.add(translate0(translatableComponent, locale));
            } else {
                children.add(child);
            }
        }
        return component.children(children);
    }

    private static @NotNull Component translate0(final @NotNull TranslatableComponent translatableComponent, final @NotNull Locale locale) {
        if (GlobalTranslator.translator().canTranslate(translatableComponent.key(), locale)) {
            return GlobalTranslator.render(translatableComponent, locale);
        }
        return translatableComponent;
    }

    public static @NotNull Component markupExtensions(final @NotNull Component component, final @NotNull Context<?> ctx) {
        final List<Component> children = new ArrayList<>();
        for (final Component child : component.children()) {
            children.add(markupExtensions(child, ctx));
        }

        if (component instanceof TextComponent textComponent) {
            return markupExtension0(textComponent, ctx).children(children);
        }

        if (component instanceof TranslatableComponent translatableComponent) {
            final List<Component> arguments = new ArrayList<>();
            for (final TranslationArgument argument : translatableComponent.arguments()) {
                arguments.add(markupExtensions(argument.asComponent(), ctx));
            }
            return translatableComponent.arguments(arguments).children(children);
        }

        return component.children(children);
    }

    private static @NotNull TextComponent markupExtension0(final @NotNull TextComponent textComponent, final @NotNull Context<?> ctx) {
        final StringBuilder builder = new StringBuilder();

        final String content = textComponent.content();
        final Matcher matcher = markupExtensionPattern.matcher(content);

        while (matcher.find()) {
            final String innerText = matcher.group(1);
            try {
                final String result = Kunectron.getMarkupExtensionsParser().parse(innerText).evaluateDeep(ctx).toString();
                matcher.appendReplacement(builder, Matcher.quoteReplacement(result));
            } catch (final MarkupExtensionsException e) {
                matcher.appendReplacement(builder, Matcher.quoteReplacement(e.getMessage()));
            }
        }
        matcher.appendTail(builder);

        return textComponent.content(builder.toString());
    }

    public static @NotNull List<Component> flatten(final @NotNull Component component) {
        final List<Component> result = new ArrayList<>();
        result.add(component);
        for (final Component child : component.children()) {
            result.addAll(flatten(child).stream().map(aChild -> aChild.style(component.style())).toList());
        }
        return result;
    }

    public static List<Component> split(final @NotNull Component component, final int length) {
        final List<Component> result = new ArrayList<>();

        final Component[] split = splitAt(component, length);
        final Component part1 = split[0];
        final Component part2 = split[1];

        if (part1 != null) result.add(split[0]);
        if (part2 != null) {
            if (PlainTextComponentSerializer.plainText().serialize(split[1]).length() > length) {
                result.addAll(split(split[1], length));
            } else {
                result.add(split[1]);
            }
        }
        return result;
    }

    public static @NotNull Component[] splitAt(final @NotNull Component component, final int index) {
        final List<Component> components = flatten(component);
        final List<Component> part1Components = new ArrayList<>();
        final List<Component> part2Components = new ArrayList<>();

        int cumulative = 0;
        boolean splitOccurred = false;

        for (final Component partComponent : components) {
            if (partComponent instanceof TextComponent textComponent) {
                final String content = textComponent.content();
                final int length = content.length();

                if (!splitOccurred) {
                    if (cumulative + length <= index) {
                        part1Components.add(partComponent);
                    } else if (cumulative >= index) {
                        part2Components.add(partComponent);
                        splitOccurred = true;
                    } else {
                        final int intraIndex = index - cumulative;
                        final String plainText1 = content.substring(0, intraIndex);
                        final String plainText2 = content.substring(intraIndex);
                        part1Components.add(Component.text(plainText1).style(partComponent.style()));
                        part2Components.add(Component.text(plainText2).style(partComponent.style()));
                        splitOccurred = true;
                    }
                    cumulative += length;
                } else {
                    part2Components.add(partComponent);
                }
            } else {
                if (!splitOccurred) {
                    part1Components.add(partComponent);
                } else {
                    part2Components.add(partComponent);
                }
            }
        }

        Component part1 = null;
        for (final Component part1Component : part1Components) {
            if (part1 == null) {
                part1 = part1Component.children(List.of());
            } else {
                part1 = part1.append(part1Component.children(List.of()));
            }
        }

        Component part2 = null;
        for (final Component part2Component : part2Components) {
            if (part2 == null) {
                part2 = part2Component.children(List.of());
            } else {
                part2 = part2.append(part2Component.children(List.of()));
            }
        }

        return new Component[] { part1, part2 };
    }

    public static boolean isTextComponent(final @NotNull Component component) {
        if (!(component instanceof TextComponent)) {
            return false;
        }
        for (final Component child : component.children()) {
            if (!isTextComponent(child)) {
                return false;
            }
        }
        return true;
    }
}
