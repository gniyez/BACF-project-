package project;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines the withdrawal policy for internship applications.
 * Adds a cooldown period before a student can reapply to the same internship after withdrawal.
 */
public final class WithdrawalPolicy {
    private WithdrawalPolicy(){}
    
    /**
     * Number of days a student must wait before reapplying to the same internship
     * after a withdrawal.
     */
    private static final int COOLDOWN_DAYS_SAME_INTERNSHIP = 7;
   
    /**
     * Stores the last withdrawal date for each student and internship combination.
     * The key is made up of the student ID, company name and internship title.
     */    
    private static final Map<String, LocalDate> lastWithdrawal = new HashMap<>();

    /**
     * Checks whether the given student can reapply to the specified internship
     * based on the cooldown period.
     *
     * @param studentID  student matriculation ID
     * @param internship the internship the student wants to reapply to
     * @return true if the student can reapply, false if still within the cooldown period
     */
    public static boolean canReapply(String studentID, Internship internship) {
        String key = key(studentID, internship);
        LocalDate last = lastWithdrawal.get(key);
        if (last == null) return true;
        return !LocalDate.now().isBefore(last.plusDays(COOLDOWN_DAYS_SAME_INTERNSHIP));
    }

    /**
     * Records a withdrawal for the given student and internship,
     * using the current date as the last withdrawal date.
     * 
     * @param studentID  student matriculation ID
     * @param internship the internship the student is withdrawing from
     */
    public static void recordWithdrawal(String studentID, Internship internship) {
        lastWithdrawal.put(key(studentID, internship), LocalDate.now());
    }

    /**
     * Builds a unique key for identifying the withdrawal record of a student
     * for a specific internship.
     * 
     * @param studentID  student matriculation ID
     * @param i          the internship
     * @return the combined string key
     */
    private static String key(String studentID, Internship i) {
        return studentID + "::" + i.getCompanyName() + "::" + i.getInternshipTitle();
    }
}
