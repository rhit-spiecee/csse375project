package com;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class LanguageTests {
    @Test
    public void testLanguageMapping() {
        assertEquals(Language.ENGLISH, Language.fromDisplayName("English"));
        assertEquals(Language.DUTCH, Language.fromDisplayName("Nederlands"));
        assertEquals(Language.ENGLISH, Language.fromDisplayName("Invalid"));
    }

    @Test
    public void testDisplayNames() {
        String[] names = Language.displayNames();
        assertEquals(2, names.length);
        assertTrue(Arrays.asList(names).contains("English"));
    }
}