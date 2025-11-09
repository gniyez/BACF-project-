import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class CareerUI implements FilterOptions{
    private CareerServiceController csController;
    private InternshipController internshipController;
    private LogInController logInController;
    private Scanner sc;
    private CareerServiceStaff currentUser;
    
    private String currentFilterCriteria = null;
    private String currentFilterValue = null;

    public CareerUI(CareerServiceController csController, InternshipController internshipController, LogInController logInController) {
        this.csController = csController;
        this.internshipController = internshipController;
        this.logInController = logInController;
        this.sc = new Scanner(System.in);
    }
    
    public void start(){
        System.out.println("STAFF LOGIN");
        System.out.println("═".repeat(30));
        System.out.println("Enter your User ID:");
        String userID = sc.nextLine();
        System.out.println("Enter your Password:");
        String password = sc.nextLine();
        
        boolean loginSuccess = logInController.login(userID, password);
        
        if (loginSuccess) {
            // Get the current user from login controller
            User loggedInUser = logInController.getCurrentUser();
            if (loggedInUser instanceof CareerServiceStaff) {
                this.currentUser = (CareerServiceStaff) loggedInUser;
                showStaffMenu();
            } else {
                System.out.println("Access denied. Not a career service staff member.");
            }
        }
    }
    
    private void showStaffMenu() {
        while (true) {
            System.out.println("\n=== Career Center Staff Menu ===");
            System.out.println("1. Approve Company Representative");
            System.out.println("2. Reject Company Representative");
            System.out.println("3. Approve Internship");
            System.out.println("4. Reject Internship");
            System.out.println("5. Generate Internship Report");
            System.out.println("6.List of pending companies");
            System.out.println("7.List of pending internships");
            System.out.println("8. Filter Internships");
            System.out.println("9. Manage Withdrawal Requests");
            System.out.println("10. View internship opportunities");
            System.out.println("11. Change Password");
            System.out.println("0. Logout");

            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> approveCompanyFlow();
                    
                case 2 -> rejectCompanyFlow();
                   
                case 3 -> approveInternshipFlow();
                    
                case 4 -> rejectInternshipFlow();
                   
                case 5 -> generateReportFlow();
                    
                case 6 -> listPendingReps();
                   
                case 7 ->listPendingInternships();
                   
                case 8 -> filterInternships();
                
                case 9 -> manageWithdrawalRequests(); 
                
                case 10 -> viewAllInternships(); 
                
                case 11 -> changePassword();
                   
                case 0 -> {
                    System.out.println("Goodbye!");
                    logInController.logout();
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void approveCompanyFlow() {
        System.out.print("Enter company representative email to approve: ");
        String repID = sc.nextLine();
        csController.approveCompany(currentUser.getUserID(),repID);
    }

    private void rejectCompanyFlow() {
        System.out.print("Enter company representative email to reject: ");
        String repID = sc.nextLine();
        csController.rejectCompany(currentUser.getUserID(), repID);
    }

    private void approveInternshipFlow() {
    	List<Internship> pendingInternships = new ArrayList<>();
        for (Internship i : internshipController.getInternships()) {
            if ("PENDING".equals(i.getInternshipStatus())) {
                pendingInternships.add(i);
            }
        }
        
        if (pendingInternships.isEmpty()) {
            System.out.println("No pending internships to approve.");
            return;
        }
        
        // Show numbered list
        System.out.println("Pending Internships:");
        for (int i = 0; i < pendingInternships.size(); i++) {
            Internship internship = pendingInternships.get(i);
            System.out.println((i+1) + ") " + internship.getInternshipTitle() + 
                              " | " + internship.getCompanyName() + 
                              " | Visible=" + internship.getVisibility());
        }
        
        System.out.print("Enter number to approve: ");
        try {
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline
            
            if (choice < 1 || choice > pendingInternships.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Internship selectedInternship = pendingInternships.get(choice - 1);
            csController.approveInternship(currentUser.getUserID(), selectedInternship);
            
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a number.");
            sc.nextLine(); // clear invalid input
        }
    }

    private void rejectInternshipFlow() {
    	List<Internship> pendingInternships = new ArrayList<>();
        for (Internship i : internshipController.getInternships()) {
            if ("PENDING".equals(i.getInternshipStatus())) {
                pendingInternships.add(i);
            }
        }
        
        if (pendingInternships.isEmpty()) {
            System.out.println("No pending internships to reject.");
            return;
        }
        
        // Show numbered list
        System.out.println("Pending Internships:");
        for (int i = 0; i < pendingInternships.size(); i++) {
            Internship internship = pendingInternships.get(i);
            System.out.println((i+1) + ") " + internship.getInternshipTitle() + 
                              " | " + internship.getCompanyName() + 
                              " | Visible=" + internship.getVisibility());
        }
        
        System.out.print("Enter number to reject: ");
        try {
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline
            
            if (choice < 1 || choice > pendingInternships.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Internship selectedInternship = pendingInternships.get(choice - 1);
            csController.rejectInternship(currentUser.getUserID(), selectedInternship);
            
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a number.");
            sc.nextLine(); // clear invalid input
        }
    }

    private void generateReportFlow() {
        System.out.print("Enter status filter (or leave blank): ");
        String status = sc.nextLine();
        if (status.isEmpty()) status = null;

        System.out.print("Enter preferred major filter (or leave blank): ");
        String major = sc.nextLine();
        if (major.isEmpty()) major = null;

        System.out.print("Enter internship level filter (or leave blank): ");
        String level = sc.nextLine();
        if (level.isEmpty()) level = null;

        csController.generateReport(status, major, level);
    }
    
    private void listPendingReps(){
        boolean any=false;
        List<User> users = csController.getUsers();
        for (User u:users){
            if (u instanceof CompanyRepresentative){
                CompanyRepresentative r=(CompanyRepresentative)u;
                if (!"APPROVED".equals(r.getStatus()) && !"REJECTED".equals(r.getStatus())) {
                    any = true;
                    System.out.println(r.getUserID() + " | " + r.getCompanyName() + " | status=" + r.getStatus());
                }
            }
        }
        if (!any) System.out.println("No pending reps.");
    }
    
    private void listPendingInternships() {             
        List<Internship> pendingInternships = new ArrayList<>();
        for (Internship internship : internshipController.getInternships()) {
            if ("PENDING".equals(internship.getInternshipStatus())) {
                pendingInternships.add(internship);
            }
        }
        
        if (pendingInternships.isEmpty()) {
            System.out.println("No pending internships.");
            return;
        }
        
        System.out.println("\n=== PENDING INTERNSHIPS ===");
        for (int i = 0; i < pendingInternships.size(); i++) {
            Internship internship = pendingInternships.get(i);
            System.out.println((i+1) + ") " + internship.getInternshipTitle());
            System.out.println("Company: " + internship.getCompanyName());
            System.out.println("Level: " + internship.getLevel());
            System.out.println("Preferred Major: " + internship.getPreferredMajor());
            System.out.println("Open Date: " + internship.getOpenDate());
            System.out.println("Closing Date: " + internship.getCloseDate());
            System.out.println("Slots: " + internship.getSlots());
            System.out.println("Visible: " + internship.getVisibility());
            System.out.println("Description: " + internship.getInternshipDescription());
            System.out.println(); 
        }
        System.out.println("===========================");
    }
    
    private void filterInternships(){
        System.out.println("Enter filter criteria (status/preferredmajors/internshiplevel/closingdate/opendate/companyname/visibility):");
        System.out.println("Or type 'clear' to remove filters");
        String criteria = sc.nextLine();
        
        if ("clear".equalsIgnoreCase(criteria)) {
            currentFilterCriteria = null;
            currentFilterValue = null;
            System.out.println("Filters cleared.");
            return;
        }
        
        System.out.println("Enter filter value: ");
        String value = sc.nextLine();
        
        // Save filter settings
        currentFilterCriteria = criteria;
        currentFilterValue = value;
        
        System.out.println("Filter applied: " + criteria + " = " + value);
        System.out.println("Filter settings saved. Use 'View All Internships' to see filtered results.");
    }
    
    private void manageWithdrawalRequests() {
        List<Application> pendingRequests = csController.getPendingWithdrawalRequests();
        
        if (pendingRequests.isEmpty()) {
            System.out.println("No pending withdrawal requests.");
            return;
        }
        
        System.out.println("\n=== PENDING WITHDRAWAL REQUESTS ===");
        for (int i = 0; i < pendingRequests.size(); i++) {
            Application app = pendingRequests.get(i);
            System.out.println((i+1) + ") " + app.getApplicationID() + 
                              " | Student: " + app.getStudent().getName() +
                              " | Internship: " + app.getInternship().getInternshipTitle() +
                              " | Current Status: " + app.getStatus() +
                              " | Company: " + app.getInternship().getCompanyName());
        }
        
        System.out.print("Choose request to manage: ");
        try {
            int choice = sc.nextInt();
            sc.nextLine();
            
            if (choice < 1 || choice > pendingRequests.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Application selectedApp = pendingRequests.get(choice - 1);
            
            System.out.println("1. Approve Withdrawal");
            System.out.println("2. Reject Withdrawal");
            System.out.print("Choose action: ");
            
            int action = sc.nextInt();
            sc.nextLine();
            
            if (action == 1) {
                csController.approveWithdrawal(currentUser.getUserID(), selectedApp);
            } else if (action == 2) {
                csController.rejectWithdrawal(currentUser.getUserID(), selectedApp);
            } else {
                System.out.println("Invalid action.");
            }
            
        } catch (Exception e) {
            System.out.println("Invalid input.");
            sc.nextLine();
        }
    }
    
    
    private void viewAllInternships() {
        List<Internship> allInternships = internshipController.getInternships();
        
        // Apply saved filters
        if (currentFilterCriteria != null && currentFilterValue != null) {
            allInternships = this.filter(allInternships, currentFilterCriteria, currentFilterValue);
        }
        
        // Sort alphabetically
        allInternships.sort(Comparator.comparing(Internship::getInternshipTitle, String.CASE_INSENSITIVE_ORDER));
        
        // Use the display method instead of duplicating code
        displayAllInternships(allInternships, "ALL INTERNSHIP OPPORTUNITIES");
    }
    
    private void displayAllInternships(List<Internship> internships, String title) {
        if (internships.isEmpty()) {
            System.out.println("No internships found.");
            if (currentFilterCriteria != null) {
                System.out.println("Try changing or clearing your filters.");
            }
            return;
        }
        
        System.out.println("\n=== " + title + " ===");
        if (currentFilterCriteria != null) {
            System.out.println("Active Filter: " + currentFilterCriteria + " = " + currentFilterValue);
        }
        
        for (int i = 0; i < internships.size(); i++) {
            Internship internship = internships.get(i);
            System.out.println((i+1) + ") " + internship.getInternshipTitle());
            System.out.println("   Company: " + internship.getCompanyName());
            System.out.println("   Level: " + internship.getLevel());
            System.out.println("   Major: " + internship.getPreferredMajor());
            System.out.println("   Open Date: " + internship.getOpenDate());
            System.out.println("   Closing Date: " + internship.getCloseDate());
            System.out.println("   Slots: " + internship.getSlots());
            System.out.println("   Status: " + internship.getInternshipStatus());
            System.out.println("   Visible: " + internship.getVisibility());
            System.out.println("   Description: " + internship.getInternshipDescription());
            System.out.println(); 
        }
        System.out.println("=====================================");
    }
    
    
    private void changePassword() {
        System.out.println("\n=== CHANGE PASSWORD ===");
        
        System.out.print("Enter current password: ");
        String currentPassword = sc.nextLine();
        
        System.out.print("Enter new password: ");
        String newPassword = sc.nextLine();
        
        System.out.print("Confirm new password: ");
        String confirmPassword = sc.nextLine();
        

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Error: New passwords do not match.");
            return;
        }
        
        if (newPassword.isEmpty()) {
            System.out.println("Error: New password cannot be empty.");
            return;
        }
        
       
        String currentUserID = currentUser.getUserID();
        logInController.changePassword(currentUserID, currentPassword, newPassword);
        System.out.println("Returning to main menu...");
    }
    
}