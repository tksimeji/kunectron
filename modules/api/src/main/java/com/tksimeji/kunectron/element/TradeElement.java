package com.tksimeji.kunectron.element;

import com.tksimeji.kunectron.event.MerchantGuiEvents;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Locale;

public interface TradeElement extends Element<TradeElement> {
    @NotNull ItemStack result();

    @Contract("_ -> this")
    @NotNull TradeElement result(final @NotNull ItemStack result);

    @NotNull Ingredients ingredients();

    @Contract("_ -> this")
    @NotNull TradeElement ingredient(final @NotNull ItemStack ingredient);

    @Contract("_, _ -> this")
    @NotNull TradeElement ingredients(final @NotNull ItemStack ingredient1, final @Nullable ItemStack ingredient2);

    int maxUses();

    @Contract("_ -> this")
    @NotNull TradeElement maxUses(final @Range(from = 0, to = Integer.MAX_VALUE) int maxUses);

    boolean canSelect();

    @Contract("_ -> this")
    @NotNull TradeElement canSelect(final boolean canSelect);

    boolean canPurchase();

    @Contract("_ -> this")
    @NotNull TradeElement canPurchase(final boolean canUse);

    @Nullable TradeElement.SelectHandler selectHandler();

    @Contract("_ -> this")
    @NotNull TradeElement selectHandler(final @NotNull TradeElement.SelectHandler1 handler);

    @Contract("_ -> this")
    @NotNull TradeElement selectHandler(final @NotNull TradeElement.SelectHandler2 handler);

    @Nullable TradeElement.PurchaseHandler purchaseHandler();

    @Contract("_ -> this")
    @NotNull TradeElement purchaseHandler(final @NotNull TradeElement.PurchaseHandler1 handler);

    @Contract("_ -> this")
    @NotNull TradeElement purchaseHandler(final @NotNull TradeElement.PurchaseHandler2 handler);

    @NotNull MerchantRecipe createMerchantRecipe(final @Nullable Locale locale);

    boolean equals(final @Nullable MerchantRecipe object);

    interface SelectHandler {
    }

    @FunctionalInterface
    interface SelectHandler1 extends SelectHandler {
        void onSelect();
    }

    @FunctionalInterface
    interface SelectHandler2 extends SelectHandler {
        void onSelect(@NotNull MerchantGuiEvents.SelectEvent event);
    }

    interface PurchaseHandler {
    }

    @FunctionalInterface
    interface PurchaseHandler1 extends PurchaseHandler {
        void onPurchase();
    }

    @FunctionalInterface
    interface PurchaseHandler2 extends PurchaseHandler {
        void onPurchase(@NotNull MerchantGuiEvents.PurchaseEvent event);
    }

    interface Ingredients extends Iterable<ItemStack> {
        @NotNull ItemStack getIngredient1();

        @Nullable ItemStack getIngredient2();

        boolean hasIngredient2();
    }
}
