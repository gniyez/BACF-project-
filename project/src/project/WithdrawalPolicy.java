package project;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

//Adds business rules: a small cooldown before resubmitting to the same internship
public final class WithdrawalPolicy {
    private WithdrawalPolicy(){}

    private static final int COOLDOWN_DAYS_SAME_INTERNSHIP = 7;
    // studentID + internshipTitle -> last withdrawal date
    private static final Map<String, LocalDate> lastWithdrawal = new HashMap<>();

    public static boolean canReapply(String studentID, Internship internship) {
        String key = key(studentID, internship);
        LocalDate last = lastWithdrawal.get(key);
        if (last == null) return true;
        return !LocalDate.now().isBefore(last.plusDays(COOLDOWN_DAYS_SAME_INTERNSHIP));
    }

    public static void recordWithdrawal(String studentID, Internship internship) {
        lastWithdrawal.put(key(studentID, internship), LocalDate.now());
    }

    private static String key(String studentID, Internship i) {
        return studentID + "::" + i.getCompanyName() + "::" + i.getInternshipTitle();
    }
}
