package com;

import java.util.Locale;

public class Utilities {
    public static final int MAX_CARD_COST = 8;

    public static final String[] AVAILABLE_LANGUAGES = new String[] {"English", "Deutsch"};
    public static final String CHOOSE_LANGUAGE_MESSAGE = "Pick a language (Wählen Sie eine Sprache aus): ";

    public static final String ENGLISH_BUNDLE = "language";
    public static final String GERMAN_BUNDLE = "language_de";

    public static final Locale ENGLISH_LOCALE = Locale.ENGLISH;
    public static final Locale GERMAN_LOCALE = Locale.GERMAN;

    public static String getBundleName(String languageSelection) {
        return switch (languageSelection) {
            case "English" -> ENGLISH_BUNDLE;
            case "Deutsch" -> GERMAN_BUNDLE;
            default -> ENGLISH_BUNDLE;
        };
    }

    public static Locale getLocale(String languageSelection) {
        return switch (languageSelection) {
            case "English" -> ENGLISH_LOCALE;
            case "Deutsch" -> GERMAN_LOCALE;
            default ->  ENGLISH_LOCALE;
        };
    }

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
