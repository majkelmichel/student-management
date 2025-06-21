package pl.edu.wit.studentManagement.translations;

import java.util.ResourceBundle;

public final class Translator {
    private static Language language = Language.ENGLISH;
    private static ResourceBundle bundle = loadBundle();

    private Translator() {}

    private static ResourceBundle loadBundle() {
        return ResourceBundle.getBundle("messages",language.getLocale());
    }

    public static void setLanguage(Language lang) {
        language = lang;
        bundle = loadBundle();
    }

    public static String translate(String key) {
        return bundle.getString(key);
    }
}
