package com.chuset;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShoppingCart implements IShoppingCart {

    public enum Formatting { // Can easily add and implement more options with the helper methods
        NAME_AMOUNT_PRICE,
        PRICE_AMOUNT_NAME,
        PRICE_NAME_AMOUNT
    }

    final Map<String, Integer> contents = new LinkedHashMap<>();
    final Pricer pricer;

    public ShoppingCart(Pricer pricer) {
        if (pricer == null) {
            throw new IllegalArgumentException("pricer cannot be null");
        }
        this.pricer = pricer;
    }

    public ShoppingCart() {
        this(new Pricer());
    }

    public void addItem(String itemType, int number) {
        if (number < 1) {
            throw new UnsupportedOperationException("Cannot add 0 items or a negative amount of items to shopping cart");
        } else if (itemType == null || itemType.isEmpty()) {
            throw new IllegalArgumentException("Item type cannot be null or empty");
        }
        contents.merge(itemType, number, Integer::sum); // TODO implement solution to account for overflow
    }

    public void printReceipt() {
        printReceipt(Formatting.NAME_AMOUNT_PRICE);
    }

    public void printReceipt(Formatting formatting) {
        if (formatting == null) {
            throw new IllegalArgumentException("Formatting argument cannot be null"); // TODO test for null formatting argument
        }

        double totalPrice = 0;
        int totalAmount = 0;
        for (final Map.Entry<String, Integer> entry : contents.entrySet()) {
            final int amount = entry.getValue();
            final String product = entry.getKey();
            final int priceInCents = pricer.getPrice(product) * entry.getValue();
            final double priceInEuro = (double) priceInCents / 100; // Use BigDecimal to avoid losing precision

            totalPrice += priceInEuro;
            totalAmount += amount;

            formatOutput(formatting, product, amount, priceInEuro);
        }
        formatOutput(formatting, "total", totalAmount, totalPrice);
    }

    private void formatOutput(Formatting formatting, String product, int amount, double priceInEuro) {
        switch (formatting) {
            case NAME_AMOUNT_PRICE -> System.out.printf("%s - %d - €%.2f%n", product, amount, priceInEuro);
            case PRICE_AMOUNT_NAME -> System.out.printf("€%.2f - %d - %s%n", priceInEuro, amount, product);
            case PRICE_NAME_AMOUNT -> System.out.printf("€%.2f - %s - %d%n", priceInEuro, product, amount);
        }
    }
}
