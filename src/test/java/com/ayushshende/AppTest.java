package com.ayushshende;

import org.junit.jupiter.api.Test;

import com.ayushshende.App;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    void testBrandHtmlContainsBrand() {
        String h = App.brandHtml();
        assertNotNull(h);
        assertTrue(h.contains("Ayush Shende"));
    }

    @Test
    void testBrandHtmlIsNotEmpty() {
        String h = App.brandHtml();
        assertTrue(h.length() > 0);
    }
}