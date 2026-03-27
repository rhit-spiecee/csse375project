package com;

import java.util.Arrays;
import java.util.Locale;

public enum Language {
    ENGLISH("English", "language",    Locale.ENGLISH),
    GERMAN ("Deutsch", "language_de", Locale.GERMAN);


    public final String displayName;
    public final String bundleName;
    public final Locale locale;

    Language(String displayName, String bundleName, Locale locale) {
        this.displayName = displayName;
        this.bundleName  = bundleName;
        this.locale      = locale;
    }

    public static Language fromDisplayName(String displayName) {
        return Arrays.stream(values())
                .filter(l -> l.displayName.equals(displayName))
                .findFirst()
                .orElse(ENGLISH);
    }

    public static String[] displayNames() {
        return Arrays.stream(values())
                .map(l -> l.displayName)
                .toArray(String[]::new);
    }
}
