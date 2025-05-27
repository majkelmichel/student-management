package pl.edu.wit.studentManagement.translations.impl;

import org.junit.jupiter.api.Test;
import pl.edu.wit.studentManagement.entities.fields.StudentField;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for the {@link StudentTranslation} class.
 *
 * <p>These tests verify that the correct Polish and English translations are returned
 * for each {@link StudentField} value, as well as the behavior when a null value is provided.</p>
 *
 * @author Michał Pokrzywka
 */
public class StudentTranslationTest {
    /**
     * Verifies that the Polish translation for FIRST_NAME is correct.
     */
    @Test
    void plFirstNameGetTranslation() {
        StudentTranslation studentTranslation = new StudentTranslation(new Locale("pl"));
        assertEquals("Imię",studentTranslation.getTranslation(StudentField.FIRST_NAME));
    }
    /**
     * Verifies that the Polish translation for LAST_NAME is correct.
     */
    @Test
    void plLastNameGetTranslation() {
        StudentTranslation studentTranslation = new StudentTranslation(new Locale("pl"));
        assertEquals("Nazwisko",studentTranslation.getTranslation(StudentField.LAST_NAME));
    }
    /**
     * Verifies that the Polish translation for ALBUM is correct.
     */
    @Test
    void plAlbumGetTranslation() {
        StudentTranslation studentTranslation = new StudentTranslation(new Locale("pl"));
        assertEquals("Numer albumu",studentTranslation.getTranslation(StudentField.ALBUM));
    }
    /**
     * Verifies Polish translation for null.
     */
    @Test
    void plNullGetTranslation() {
        StudentTranslation studentTranslation = new StudentTranslation(new Locale("pl"));
        assertEquals("Nieznane pole",studentTranslation.getTranslation(null));
    }
    /**
     * Verifies that the English translation for FIRST_NAME is correct.
     */
    @Test
    void enFirstNameGetTranslation() {
        StudentTranslation studentTranslation = new StudentTranslation(new Locale("en"));
        assertEquals("First name",studentTranslation.getTranslation(StudentField.FIRST_NAME));
    }
    /**
     * Verifies that the English translation for LAST_NAME is correct.
     */
    @Test
    void enLastNameGetTranslation() {
        StudentTranslation studentTranslation = new StudentTranslation(new Locale("en"));
        assertEquals("Last name",studentTranslation.getTranslation(StudentField.LAST_NAME));
    }
    /**
     * Verifies that the English translation for ALBUM is correct.
     */
    @Test
    void enAlbumGetTranslation() {
        StudentTranslation studentTranslation = new StudentTranslation(new Locale("en"));
        assertEquals("Student ID",studentTranslation.getTranslation(StudentField.ALBUM));
    }
    /**
     * Verifies English translation for null.
     */
    @Test
    void enNullGetTranslation() {
        StudentTranslation studentTranslation = new StudentTranslation(new Locale("en"));
        assertEquals("Unknown field",studentTranslation.getTranslation(null));
    }
}