/**
 * Contains custom exception classes used throughout the student management system.
 * <p>
 * These exceptions provide a structured way to signal and handle error conditions
 * such as validation failures or data access issues. They are typically used in the
 * service and domain layers to enforce business rules and encapsulate infrastructure-level
 * errors.
 * </p>
 *
 * <ul>
 *     <li>{@link pl.edu.wit.studentManagement.exceptions.ValidationException} — Represents user input or business rule validation failures, with localization support.</li>
 *     <li>{@link pl.edu.wit.studentManagement.exceptions.DataAccessException} — Wraps lower-level persistence or I/O errors as unchecked exceptions.</li>
 * </ul>
 *
 * This package helps separate application logic from error-handling mechanisms and enables
 * consistent exception propagation and messaging.
 */
package pl.edu.wit.studentManagement.exceptions;
