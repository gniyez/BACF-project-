package project;

public final class DuplicateInternshipGuard {
    private DuplicateInternshipGuard() {}

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
