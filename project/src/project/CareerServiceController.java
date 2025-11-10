package project;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class CareerServiceController implements FilterOptions{
    private List<User> users;
    private InternshipController internshipController;
    private ApplicationController appController;

    public CareerServiceController(List<User> users, InternshipController internshipController, ApplicationController appController) {
        this.users = users;
        this.internshipController = internshipController;
        this.appController = appController;
    }
    
    public List<User> getUsers() {
        return users;
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

    public String registerCompany(String email, String name, String companyName, 
            String department, String position) {
		CompanyRepresentative rep = new CompanyRepresentative(email, name, companyName, department, position);
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
    public boolean approveWithdrawal(String staffID, Application app) {
        if (!isStaff(staffID)) return false;
        
        if (!app.isWithdrawalRequested()) {
            System.out.println("No pending withdrawal request for this application.");
            return false;
        }
        
        appController.processApprovedWithdrawal(app);
        System.out.println("Withdrawal approved for application: " + app.getApplicationID());
        return true;
    }

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

    public List<Application> getPendingWithdrawalRequests() {
        List<Application> pendingRequests = new ArrayList<>();
        for (Application app : appController.getApplications()) {
            if (app.isWithdrawalRequested()) {
                pendingRequests.add(app);
            }
        }
        return pendingRequests;
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
        for (Internship internship : internshipController.getInternships()) {
            if (internship.getInternshipTitle().equalsIgnoreCase(title)) {
                return internship;
            }
        }
        return null;
    }
    
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
