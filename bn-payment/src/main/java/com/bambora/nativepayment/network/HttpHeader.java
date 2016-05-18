package com.bambora.nativepayment.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A wrapper of a {@link HashMap} that simplifies creation and logging of an HTTP header.
 */
public class HttpHeader extends HashMap<String, List<String>> {

    public HttpHeader() {
        // Default constructor
    }

    /**
     * Instantiates an {@link HttpHeader} based on an existing Map.
     *
     * @param map   A {@link Map}
     */
    public HttpHeader(Map<? extends String, ? extends List<String>> map) {
        super(map);
    }

    /**
     * Adds a header field with given key an value. Will not create duplicate values for a key.
     *
     * @param key   Header field key
     * @param value Header field value
     */
    public void addField(String key, String value) {
        List<String> valueSet = get(key);
        if (valueSet != null && !valueSet.contains(value)) {
            valueSet.add(value);
        } else {
            List<String> newValueSet = new ArrayList<>();
            newValueSet.add(value);
            put(key, newValueSet);
        }
    }

    /**
     * Creates a String suitable for logging the header data.
     *
     * @return A {@link String} including all keys and their values.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String key : keySet()) {
            List<String> valueSet = get(key);
            builder
                    .append("\t\t")
                    .append(key)
                    .append(": ");
            for (String value : valueSet) {
                builder.append(value).append(", ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
