package com.tksimeji.kunectron.util;

import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.markupextensions.MarkupExtensionsException;
import com.tksimeji.kunectron.markupextensions.context.Context;
import net.kyori.adventure.text.*;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

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

    public static @NotNull Component[] splitAt(final @NotNull Component component, final int index) {
        final Spliterator<Component> spliterator = component.spliterator(ComponentIteratorType.DEPTH_FIRST);
        final List<TextComponent> parts = StreamSupport.stream(spliterator, false).map(part -> (TextComponent) part).toList();

        final List<Component> beforeParts = new ArrayList<>();
        final List<Component> afterParts = new ArrayList<>();
        int cumulative = 0;

        for (final TextComponent part : parts) {
            final String partText = part.content();
            final int partLength = partText.length();

            if (cumulative + partLength < index) {
                beforeParts.add(part);
            } else if (cumulative >= index) {
                afterParts.add(part);
            } else {
                final int intraIndex = index - cumulative;
                final String beforeText = partText.substring(0, intraIndex);
                final String afterText = partText.substring(index);

                final TextComponent beforeComponent = Component.text(beforeText).style(part.style());
                final TextComponent afterComponent = Component.text(afterText).style(part.style());
                beforeParts.add(beforeComponent);
                afterParts.add(afterComponent);
            }

            cumulative++;
        }

        final TextComponent.Builder beforeCombined = Component.text();
        for (final Component part : beforeParts) {
            beforeCombined.append(part);
        }

        final TextComponent.Builder afterCombined = Component.text();
        for (final Component part : afterParts) {
            afterCombined.append(part);
        }

        return new Component[] {beforeCombined.build(), afterCombined.build()};
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
