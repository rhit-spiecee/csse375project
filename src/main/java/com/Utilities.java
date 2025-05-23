package com;

import java.util.Locale;

public class Utilities {

    public static final String ENGLISH_BUNDLE = "language";
    public static final String GERMAN_BUNDLE = "language_de";

    public static final Locale ENGLISH_LOCALE = Locale.ENGLISH;
    public static final Locale GERMAN_LOCALE = Locale.GERMAN;

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
