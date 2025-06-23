package pl.edu.wit.studentManagement.translations;

import java.util.ResourceBundle;
/**
 * Handles translation and localization for the student management application.
 *
 * <p>
 * Provides the Translator utility for retrieving localized messages
 * and the Language enum for managing supported locales.
 *
 * Resource bundles (e.g., messages_en.properties, messages_pl.properties) are selected based on the current language.
 *
 * @author Michał Pokrzywka
 */
public final class Translator {
    private static Language language = Language.ENGLISH;
    private static ResourceBundle bundle = loadBundle();

    private Translator() {}

    private static ResourceBundle loadBundle() {
        return ResourceBundle.getBundle("messages",language.getLocale());
    }

    public static Language getLanguage() {
        return language;
    }

    public static void setLanguage(Language lang) {
        language = lang;
        bundle = loadBundle();
    }

    public static String translate(String key) {
        return bundle.getString(key);
    }
}