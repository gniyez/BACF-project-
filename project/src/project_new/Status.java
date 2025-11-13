package project;

/**
 * Enumeration for all application and internship status values.
 * Ensures type safety and prevents typos in status comparisons.
 */

public enum Status{
    PENDING,
    APPROVED,
    REJECTED,
    SUCCESSFUL,
    UNSUCCESSFUL,
    WITHDRAWN,
    ACCEPTED,
    FILLED;

//to check if string matches status, case insensitive 
public boolean matches(String value){
    return this.name().equalsIgnoreCase(value);
}

/**
     * Safely converts a string to a Status enum
     * @param value the string to convert
     * @return the matching Status, or PENDING as default
     */ 
public static Status fromString(String value){
    if (value == null) return PENDING;
    try {
        return Status.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
        return PENDING;
    }
}
}