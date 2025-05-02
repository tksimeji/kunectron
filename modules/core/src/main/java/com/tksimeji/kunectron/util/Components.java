package com.tksimeji.kunectron.util;

import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.markupextension.MarkupExtensionException;
import com.tksimeji.kunectron.markupextension.context.Context;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

    public static @NotNull Component markupExtension(final @NotNull Component component, final @NotNull Context<?> ctx) {
        final List<Component> children = new ArrayList<>();
        for (final Component child : component.children()) {
            children.add(markupExtension(child, ctx));
        }

        if (component instanceof TextComponent textComponent) {
            return markupExtension0(textComponent, ctx).children(children);
        }

        if (component instanceof TranslatableComponent translatableComponent) {
            final List<Component> arguments = new ArrayList<>();
            for (final TranslationArgument argument : translatableComponent.arguments()) {
                arguments.add(markupExtension(argument.asComponent(), ctx));
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
                final String result = Kunectron.getMarkupExtensionParser().parse(innerText).evaluateDeep(ctx).toString();
                matcher.appendReplacement(builder, Matcher.quoteReplacement(result));
            } catch (final MarkupExtensionException e) {
                matcher.appendReplacement(builder, Matcher.quoteReplacement(e.getMessage()));
            }
        }
        matcher.appendTail(builder);

        return textComponent.content(builder.toString());
    }
}
