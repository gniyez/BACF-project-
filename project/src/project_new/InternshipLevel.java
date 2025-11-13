package project;

public enum InternshipLevel{
    BASIC,
    INTERMEDIATE,
    ADVANCED;

    public boolean matches(String value){
        return this.name().equalsIgnoreCase(value);
    }

    //Same as Status, converts string to enum; returns matching internship level
    //or BASIC as default 
    public static InternshipLevel fromString(String value){
        if (value == null) return BASIC;
        try {
            return InternshipLevel.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return BASIC;
        }
    }
}