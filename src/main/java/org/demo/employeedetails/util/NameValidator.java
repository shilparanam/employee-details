package org.demo.employeedetails.util;

import java.util.regex.Pattern;

public final class NameValidator {
    private static final int MAX_LENGTH = 50;
    // Unicode-aware: start with a letter, then letters, marks, spaces, apostrophes, or hyphens
    private static final String NAME_REGEX = "^[\\p{L}][\\p{L}\\p{M} '\\-]{0,49}$";
    private static final Pattern PATTERN = Pattern.compile(NAME_REGEX);

    private NameValidator() {}

    /**
     * Validate a name field. Throws IllegalArgumentException on invalid input.
     * - value must be non-null and non-blank
     * - trimmed length <= MAX_LENGTH
     * - matches allowed characters (Unicode letters, diacritics, spaces, hyphen, apostrophe)
     */
    public static void validate(String fieldName, String value) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " must not be null");
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(fieldName + " must be at most " + MAX_LENGTH + " characters");
        }
        if (!PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException(fieldName + " contains invalid characters");
        }
    }
}

