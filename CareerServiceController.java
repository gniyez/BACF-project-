import java.util.List;
import java.util.ArrayList;

public class CareerServiceController{
    private List<User> users;
    private List<Internship> internships;
    private FilterOptions filterOptions;

    public CareerServiceController(List<User> users, List<Internship> internships) {
        this.users = users;
        this.internships = internships;
        this.filterOptions = new DefaultFilterOptions(internships);
    }

    public void addUser(User user) {
        users.add(user);
    }
    private boolean isStaff(String staffID) {
        for (User user : users) {
            if ((user instanceof CareerServiceStaff) && user.getUserID().equals(staffID)) {
                return true;
            }
        }
        System.out.println("Invalid staff ID: " + staffID);
        return false;
    }

    public String registerCompany(String userID, String Company_name, String password, String companyID, String department, String position){
        CompanyRepresentative rep = new CompanyRepresentative(userID, Company_name, password, companyID, department, position);
        users.add(rep);
        return "PENDING APPROVAL";
    }
    public boolean approveCompany(String staffID, String repID){
        if (!isStaff(staffID)) return false;

        for (User user : users) {
            if (user instanceof CompanyRepresentative && user.getUserID().equals(repID)) {
                ((CompanyRepresentative) user).setStatus("APPROVED");
                return true;
            }
        }
        System.out.println("Company representative not found: " + repID);
        return false;
    }
    public boolean rejectCompany(String staffID, String repID){
        if (!isStaff(staffID)) return false;

        for (User user : users) {
            if (user instanceof CompanyRepresentative && user.getUserID().equals(repID)) {
                ((CompanyRepresentative) user).setStatus("REJECTED");
                return true;
            }
        }
        System.out.println("Company representative not found: " + repID);
        return false;

    }
    public boolean approveWithdrawal(String staffID, Application app){
        if (!isStaff(staffID)) return false;
        app.setStatus("WITHDRAWN");
        System.out.println("Withdrawal request approved for application ID: " + app.getApplicationID());
        return true;
    }
    public boolean rejectWithdrawal(String staffID, Application app){
        if (!isStaff(staffID)) return false;
        System.out.println("Withdrawal request rejected for application ID: " + app.getApplicationID());
        return true;
    }
    public boolean approveInternship(String staffID, Internship internship) {
        if (!isStaff(staffID)) return false;

        internship.setInternshipStatus("APPROVED");
        internship.setVisibility(true);
        System.out.println("Internship approved: " + internship.getInternshipTitle());
        return true;
    }

    public boolean rejectInternship(String staffID, Internship internship) {
        if (!isStaff(staffID)) return false;

        internship.setInternshipStatus("REJECTED");
        internship.setVisibility(false);
        System.out.println("Internship rejected: " + internship.getInternshipTitle());
        return true;
    }
    public Internship findInternshipByTitle(String title) {
        for (Internship internship : internships) {
            if (internship.getInternshipTitle().equalsIgnoreCase(title)) {
                return internship;
            }
        }
        return null;
    }
    
    public void generateReport(String statusFilter, String majorFilter, String levelFilter) {
        System.out.println("=== Internship Opportunities Report ===");
        List<Internship> filtered = new ArrayList<>(internships);

        if (statusFilter != null&&!statusFilter.isBlank()) {
            filtered.retainAll(InternshipController.filter("status", statusFilter));
        }
        if (majorFilter != null&&!statusFilter.isBlank()) {
            filtered.retainAll(InternshipController.filter("major", majorFilter));
        }
        if (levelFilter != null &&!statusFilter.isBlank()) {
            filtered.retainAll(InternshipController.filter("level", levelFilter));
        }
        if (filtered.isEmpty()) {
            System.out.println("No internships found matching the specified criteria.");
            return;
        }
        for (Internship internship : filtered) {
            System.out.println("\nTitle: " + internship.getInternshipTitle());
            System.out.println("Company: " + internship.getCompanyName());
            System.out.println("Status: " + internship.getInternshipStatus());
            System.out.println("Level: " + internship.getLevel());
            System.out.println("Preferred Major: " + internship.getPreferredMajor());
            System.out.println("Open Date: " + internship.getOpenDate());
            System.out.println("Close Date: " + internship.getCloseDate());
            System.out.println("Slots: " + internship.getSlots());
            System.out.println("Visible to Students: " + internship.getVisibility());
        }
        System.out.println("=== End of Report ===");
    }
}
