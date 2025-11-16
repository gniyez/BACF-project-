package project;

/**
 * Enumeration for internship levels
 * Using this enum helps ensure type safety and prevents typos
 */
public enum InternshipLevel{
    BASIC,
    INTERMEDIATE,
    ADVANCED;

    /**
     * Checks if this internship level matches the given string value.
     * 
     * @param value the string value to compare
     * @return true if the string matches this internship level, false otherwise
     */
    public boolean matches(String value){
        return this.name().equalsIgnoreCase(value);
    }

    /**
     * Safely converts a string to an InternshipLevel enum.
     * Returns BASIC if the value is null or does not match any level.
     *
     * @param value the string to convert
     * @return the matching InternshipLevel, or BASIC as a default
     */
    public static InternshipLevel fromString(String value){
        if (value == null) return BASIC;
        try {
            return InternshipLevel.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return BASIC;
        }
    }
}