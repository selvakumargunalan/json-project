package com.risksense.converters;

import com.risksense.controller.XMLJSONConverter;

/**
 * Factory class for creating instances of {@link XMLJSONConverterI}.
 */
public final class ConverterFactory {

    /**
     * You should implement this method having it return your version of
     * {@link com.risksense.converters.XMLJSONConverterI}.
     *
     * @return {@link com.risksense.converters.XMLJSONConverterI} implementation
     * you created.
     */
    public static final XMLJSONConverterI createXMLJSONConverter() {
        // Todo: Implement this method please.
        return new XMLJSONConverter();
    }
}
