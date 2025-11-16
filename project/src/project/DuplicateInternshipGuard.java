package project;
/**
 * Utility to check for duplicate internships by a company representative.
 * Cannot be instantiated.
 */
public final class DuplicateInternshipGuard {
    private DuplicateInternshipGuard() {}
/**
     * Returns true if the representative already has an internship with the same title and level
     * that is not rejected.
     *
     * @param controller the InternshipController
     * @param rep        the company representative
     * @param title      internship title
     * @param level      internship level
     * @return true if duplicate exists, false otherwise
     */
    public static boolean isDuplicate(InternshipController controller, CompanyRepresentative rep, String title, String level) {
        if (controller == null || rep == null || title == null || level == null) return false;
        for (Internship i : controller.getInternships()) {
            if (i == null) continue;
            if (
                rep.getCompanyName().equalsIgnoreCase(i.getCompanyName())
                && title.equalsIgnoreCase(i.getInternshipTitle())
                && level.equalsIgnoreCase(i.getLevel())
                && !Status.REJECTED.matches(i.getInternshipStatus())
            ) {
                return true;
            }
        }
        return false;
    }
}
