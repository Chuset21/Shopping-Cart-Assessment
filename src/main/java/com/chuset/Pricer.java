package com.chuset;

import java.util.HashMap;
import java.util.Map;

/**
 * A stub implementation - for this exercise, you may disregard that this is incomplete.
 */
public class Pricer {
    private final Map<String, Integer> pricingDatabase = new HashMap<>(); // stub

    public Pricer() {
        pricingDatabase.put("apple", 100);
        pricingDatabase.put("banana", 200);
    }

    /**
     * Returns the price of the item passed, in Euro-cent. E.g. if an item costs €1, this will return 100
     * If itemType is an unknown string, store policy is that the item is free.
     */
    public Integer getPrice(String itemType) {
        return pricingDatabase.getOrDefault(itemType, 0);
    }

}
