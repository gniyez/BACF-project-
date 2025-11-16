package project;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
/**
 * Controller class for Career Service Staff operations.
 * Handles company registration, approval/rejection of companies and internships,
 * and processing of student withdrawal requests.
 */
public class CareerServiceController implements FilterOptions{
    private final List<User> users;
    private final InternshipController internshipController;
    private final ApplicationController appController;
/**
     * Constructs a CareerServiceController with given users and controllers.
     * 
     * @param users               list of users
     * @param internshipController controller for internships
     * @param appController       controller for applications
     */
    public CareerServiceController(List<User> users, InternshipController internshipController, ApplicationController appController) {
        this.users = users;
        this.internshipController = internshipController;
        this.appController = appController;
    }
    /**
     * Returns all users.
     * 
     * @return list of users
     */ 
    public List<User> getUsers() {
        return users;
    }
 /**
     * Adds a new user to the system.
     * 
     * @param user the user to add
     */
    public void addUser(User user) {
        users.add(user);
    }
  /**
 * Checks whether a given user ID belongs to a valid Career Service Staff.
 *
 * @param staffID ID of the staff to validate
 * @return true if the user is a CareerServiceStaff with the given ID, false otherwise
 */  
    private boolean isStaff(String staffID) {
        for (User user : users) {
            if ((user instanceof CareerServiceStaff) && user.getUserID().equals(staffID)) {
                return true;
            }
        }
        System.out.println("Invalid staff ID: " + staffID);
        return false;
    }
/**
     * Registers a new company representative.
     * 
     * @param email       email of the representative
     * @param name        name of the representative
     * @param companyName name of the company
     * @param department  department
     * @param position    position in the company
     * @return "PENDING APPROVAL" status after registration
     */
    public String registerCompany(String email, String name, String companyName, 
            String department, String position) {
		CompanyRepresentative rep = new CompanyRepresentative(email, name, companyName, department, position);
		users.add(rep);
		return "PENDING APPROVAL";
	}
    /**
     * Approves a company representative if executed by valid staff.
     * 
     * @param staffID staff ID approving the company
     * @param repID   representative ID to approve
     * @return true if approved, false otherwise
     */
    public boolean approveCompany(String staffID, String repID){
        if (!isStaff(staffID)) return false;

        for (User user : users) {
            if (user instanceof CompanyRepresentative && user.getUserID().equals(repID)) {
                ((CompanyRepresentative) user).setStatus(Status.APPROVED.name());
                return true;
            }
        }
        System.out.println("Company representative not found: " + repID);
        return false;
    }
    
    /**
     * Rejects a company representative if executed by valid staff.
     * 
     * @param staffID staff ID rejecting the company
     * @param repID   representative ID to reject
     * @return true if rejected, false otherwise
     */
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
    /**
     * Approves a student's withdrawal request.
     * 
     * @param staffID staff ID approving the withdrawal
     * @param app     application to approve withdrawal
     * @return true if withdrawal approved, false otherwise
     */
    public boolean approveWithdrawal(String staffID, Application app) {
        if (!isStaff(staffID)) return false;
        
        if (!app.isWithdrawalRequested()) {
            System.out.println("No pending withdrawal request for this application.");
            return false;
        }
        
        appController.processApprovedWithdrawal(app);
        System.out.println("Withdrawal approved for application: " + app.getApplicationID());
        WithdrawalPolicy.recordWithdrawal(app.getStudent().getUserID(), app.getInternship()); 
        return true;

    }
 /**
     * Rejects a student's withdrawal request.
     * 
     * @param staffID staff ID rejecting the withdrawal
     * @param app     application to reject withdrawal
     * @return true if withdrawal rejected, false otherwise
     */
    public boolean rejectWithdrawal(String staffID, Application app) {
        if (!isStaff(staffID)) return false;
        
        if (!app.isWithdrawalRequested()) {
            System.out.println("No pending withdrawal request for this application.");
            return false;
        }
        
        app.setWithdrawalRequested(false); // 
        System.out.println("Withdrawal rejected for application: " + app.getApplicationID());
        return true;
    }
/**
     * Returns all applications with pending withdrawal requests.
     * 
     * @return list of applications pending withdrawal
     */
    public List<Application> getPendingWithdrawalRequests() {
        List<Application> pendingRequests = new ArrayList<>();
        for (Application app : appController.getApplications()) {
            if (app.isWithdrawalRequested()) {
                pendingRequests.add(app);
            }
        }
        return pendingRequests;
    }
    /**
     * Approves an internship posted by a company.
     * 
     * @param staffID   staff ID approving
     * @param internship internship to approve
     * @return true if approved, false otherwise
     */
    public boolean approveInternship(String staffID, Internship internship) {
        if (!isStaff(staffID)) return false;

        internship.setInternshipStatus(Status.APPROVED.name());
        internship.setVisibility(true);
        System.out.println("Internship approved: " + internship.getInternshipTitle());
        return true;
    }
 /**
     * Rejects an internship posted by a company.
     * 
     * @param staffID   staff ID rejecting
     * @param internship internship to reject
     * @return true if rejected, false otherwise
     */
    public boolean rejectInternship(String staffID, Internship internship) {
        if (!isStaff(staffID)) return false;

        internship.setInternshipStatus("REJECTED");
        internship.setVisibility(false);
        System.out.println("Internship rejected: " + internship.getInternshipTitle());
        return true;
    }
     /**
     * Finds an internship by its title.
     * 
     * @param title title of the internship
     * @return Internship if found, null otherwise
     */
    public Internship findInternshipByTitle(String title) {
        for (Internship internship : internshipController.getInternships()) {
            if (internship.getInternshipTitle().equalsIgnoreCase(title)) {
                return internship;
            }
        }
        return null;
    }
    /**
     * Generates a report of internships with optional filters.
     * 
     * @param statusFilter filter by internship status
     * @param majorFilter  filter by preferred major
     * @param levelFilter  filter by internship level
     */
    public void generateReport(String statusFilter, String majorFilter, String levelFilter) {
    	System.out.println("=== Internship Opportunities Report ===");
        List<Internship> filtered = internshipController.getInternships();

        if (statusFilter != null && !statusFilter.isBlank()) {
            filtered = filtered.stream()
                .filter(i -> statusFilter.equalsIgnoreCase(i.getInternshipStatus()))
                .collect(Collectors.toList());
        }
        if (majorFilter != null && !majorFilter.isBlank()) {
            filtered = filtered.stream()
                .filter(i -> majorFilter.equalsIgnoreCase(i.getPreferredMajor()))
                .collect(Collectors.toList());
        }
        if (levelFilter != null && !levelFilter.isBlank()) {
            filtered = filtered.stream()
                .filter(i -> levelFilter.equalsIgnoreCase(i.getLevel()))
                .collect(Collectors.toList());
        }
        if (filtered.isEmpty()) {
            System.out.println("No internships found matching the specified criteria.");
            return;
        }
        for (int i = 0; i < filtered.size(); i++) {
            Internship internship = filtered.get(i);
            System.out.println((i+1) + ") " + internship.getInternshipTitle());
            System.out.println("Company: " + internship.getCompanyName());
            System.out.println("Level: " + internship.getLevel());
            System.out.println("Major: " + internship.getPreferredMajor());
            System.out.println("Open Date: " + internship.getOpenDate());
            System.out.println("Closing Date: " + internship.getCloseDate());
            System.out.println("Slots: " + internship.getSlots());
            System.out.println("Status: " + internship.getInternshipStatus());
            System.out.println("Visible: " + internship.getVisibility());
            System.out.println("Description: " + internship.getInternshipDescription());
            System.out.println(); 
        }
        System.out.println("=== End of Report ===");
    }
}
