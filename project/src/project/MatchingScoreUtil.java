package project;

import java.time.LocalDate;

/**
 * Utility class for computing a matching score between a student and an internship.
 * The score is an integer from 0 to 100 based on eligibility and preference fit.
 */
public final class MatchingScoreUtil {
    private MatchingScoreUtil() {}
    /**
     * Computes a matching score between a student and an internship.
     * The method first checks basic eligibility (level, date range, status, visibility).
     * If the internship is not eligible for the student, the score is 0.
     * Otherwise, the score is built using:
     * major fit, internship level fit, time left until closing date and slot availability.
     *
     * @param student    the student to evaluate
     * @param internship the internship to evaluate
     * @return an integer score between 0 and 100, where higher means a better match
     */    
    public static int score(Student student, Internship internship) {
        int score = 0;

        // Base eligibility checks
        boolean levelOk = student.canApplyForLevel(internship.getLevel());
        boolean dateOk = !LocalDate.now().isBefore(internship.getOpenDate())
                      && !LocalDate.now().isAfter(internship.getCloseDate());
        boolean statusOk = Status.APPROVED.matches(internship.getInternshipStatus());
        boolean visible = internship.getVisibility();

        if (!(levelOk && dateOk && statusOk && visible)) return 0;

        // Major fit
        if (internship.getPreferredMajor().equalsIgnoreCase(student.getMajor())) score += 45;

        // Level fit (closer matches get higher)
        score += switch (internship.getLevel().toUpperCase()) {
            case "BASIC" -> (student.getYearOfStudy() <= 2 ? 30 : 10);
            case "INTERMEDIATE" -> (student.getYearOfStudy() >= 3 ? 30 : 10);
            case "ADVANCED" -> (student.getYearOfStudy() >= 3 ? 35 : 0);
            default -> 0;
        };

        //Time proximity bonus: sooner closing dates rank higher
        long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), internship.getCloseDate());
        if (daysLeft <= 7) score += 15;
        else if (daysLeft <= 21) score += 8;  

        // Availability
        if (internship.getSlots() > 0) score += 10;

        return Math.min(score, 100);
    }
}
