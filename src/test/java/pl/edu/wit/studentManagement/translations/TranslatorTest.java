/**
 * Unit tests for the Translator class, which handles loading and retrieving
 * localized messages based on the selected Language.
 *
 * <p>
 * Tests verify:
 * <ul>
 *   <li>Default language selection</li>
 *   <li>Language switching</li>
 *   <li>Translation of known keys</li>
 *   <li>Error handling for missing keys</li>
 * </ul>
 *
 * Resource bundles such as messages_en.properties and messages_pl.properties
 * must be present in the classpath for these tests to succeed.
 *
 * @author Michał Pokrzywka
 */
package pl.edu.wit.studentManagement.translations;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TranslatorTest {
    @AfterEach
    void resetLanguageToDefault() {
        Translator.setLanguage(Language.ENGLISH);
    }

    @Test
    void defaultLanguageEnglish() {
        assertEquals(Language.ENGLISH, Translator.getLanguage());
    }

    @Test
    void setLanguageToPolish() {
        Translator.setLanguage(Language.POLISH);
        assertEquals(Language.POLISH, Translator.getLanguage());
        assertEquals("pl", Translator.getLanguage().getLocale().getLanguage());
    }

    @Test
    void translateEnglishTranslation() {
        String result = Translator.translate("app.title");
        assertEquals("Student Management Application", result);
    }

    @Test
    void translatePolishTranslation() {
        Translator.setLanguage(Language.POLISH);
        String result = Translator.translate("app.title");
        assertEquals("Aplikacja do zarządzania studentami", result);
    }

    @Test
    void translateUnknownKeyException() {
        assertThrows(java.util.MissingResourceException.class, () -> {
            Translator.translate("non.existent.key");
        });
    }
}