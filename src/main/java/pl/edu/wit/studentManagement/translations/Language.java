/**
 * Provides support for application language selection and locale management.
 *
 * <p>
 * Includes the Language enum defining supported locales
 * and utilities for switching between them.
 *
 * @author Michał Pokrzywka
 */
package pl.edu.wit.studentManagement.translations;

import java.util.Locale;

public enum Language {
    POLISH("pl","PL"),
    ENGLISH("en","US");
    private final Locale locale;
    Language(String lang, String country){
        this.locale = new Locale(lang,country);
    }
    public Locale getLocale() {
        return locale;
    }
}
