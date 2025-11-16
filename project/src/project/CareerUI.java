
package project;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
/**
 * CareerUI is the user interface class for Career Service Staff to manage
 * company representatives, internships, withdrawal requests, and reports.
 */
public class CareerUI implements FilterOptions{
    private final CareerServiceController csController;
    private final InternshipController internshipController;
    private final LogInController logInController;
    private final Scanner sc;
    private CareerServiceStaff currentUser;
    
    private String currentFilterCriteria = null;
    private String currentFilterValue = null;
/**
     * Constructs a CareerUI with the required controllers.
     * @param csController Career service controller
     * @param internshipController Internship controller
     * @param logInController Login controller
     */
    public CareerUI(CareerServiceController csController, InternshipController internshipController, LogInController logInController) {
        this.csController = csController;
        this.internshipController = internshipController;
        this.logInController = logInController;
        this.sc = new Scanner(System.in);
    }
  /**
     * Starts the UI by prompting login for career service staff.
     */  
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
            if (loggedInUser instanceof CareerServiceStaff careerServiceStaff) {
                this.currentUser = careerServiceStaff;
                showStaffMenu();
            } else {
                System.out.println("Access denied. Not a career service staff member.");
            }
        }
    }
 /**
     * Displays the main menu for staff and handles menu selection.
     */   
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

            int choice = -1;
            while (true) { 
                System.out.print("Choose an option: ");
                String input = sc.nextLine().trim();
                try {
                    choice = Integer.parseInt(input);
                    if (choice >= 0 && choice <=11) break;
                    System.out.println("Invalid choice. Please enter a number between 0 and 11");

                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number");
                }
            }

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
 /**
     * Approves a company representative based on user input.
     */
    private void approveCompanyFlow() {
        System.out.print("Enter company representative email to approve: ");
        String repID;
        do { 
            System.out.println("Enter company representative email to approve: ");
            repID = sc.nextLine().trim();
            if (repID.isEmpty()){
                System.out.println("ID cannot be empty");
            }
        } while (repID.isEmpty());
        csController.approveCompany(currentUser.getUserID(),repID);
    }
/**
     * Rejects a company representative based on user input.
     */
    private void rejectCompanyFlow() {
        System.out.print("Enter company representative email to reject: ");
        String repID;
        do{
            System.out.print("Enter company representative email to reject");
            repID = sc.nextLine().trim();
            if (repID.isEmpty()){
                System.out.println("ID cannot be empty");

            }

        }
        while(repID.isEmpty());
        csController.rejectCompany(currentUser.getUserID(), repID);
    }
/**
     * Approves a pending internship selected by the staff.
     */
    private void approveInternshipFlow() {
    	List<Internship> pendingInternships = new ArrayList<>();
        for (Internship i : internshipController.getInternships()) {
            if (Status.PENDING.matches(i.getInternshipStatus())) {
                pendingInternships.add(i);
            }
        }
        
        if (pendingInternships.isEmpty()) {
            System.out.println("No pending internships to approve.");
            return;
        }
        
       
        System.out.println("Pending Internships:");
        for (int i = 0; i < pendingInternships.size(); i++) {
            Internship internship = pendingInternships.get(i);
            System.out.println((i+1) + ") " + internship.getInternshipTitle() + 
                              " | " + internship.getCompanyName() + 
                              " | Visible=" + internship.getVisibility());
        }
        

        int choice = -1;
        while (true) { 
            System.out.print("Enter number to approve: ");
            String input = sc.nextLine().trim();
            try {
                choice = Integer.parseInt(input);
                if(choice >= 1 && choice <= pendingInternships.size()) break;
                System.out.println("Invalid selection. Enter number from the list");

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }

        }
            
            Internship selectedInternship = pendingInternships.get(choice - 1);
            csController.approveInternship(currentUser.getUserID(), selectedInternship);
    }
 /**
     * Rejects a pending internship selected by the staff.
     */
    private void rejectInternshipFlow() {
        List<Internship> pendingInternships = new ArrayList<>();
        for (Internship i : internshipController.getInternships()) {
            if (Status.PENDING.matches(i.getInternshipStatus())) {
                pendingInternships.add(i);
            }
        }
        
        if (pendingInternships.isEmpty()) {
            System.out.println("No pending internships to reject.");
            return;
        }
        
       
        System.out.println("Pending Internships:");
        for (int i = 0; i < pendingInternships.size(); i++) {
            Internship internship = pendingInternships.get(i);
            System.out.println((i+1) + ") " + internship.getInternshipTitle() + 
                              " | " + internship.getCompanyName() + 
                              " | Visible=" + internship.getVisibility());
        }
        
        int choice = -1;
        while (true) { 
            System.out.print("Enter number to reject: ");
            String input = sc.nextLine().trim();
            try{
                choice = Integer.parseInt(input);
                if(choice >= 1 && choice <= pendingInternships.size()) break;
                System.out.println("Invalid selection. Enter number from the list");

            } catch (NumberFormatException e){
                System.out.println("Please enter a valid number");   
            }
        }
            
            Internship selectedInternship = pendingInternships.get(choice - 1);
            csController.rejectInternship(currentUser.getUserID(), selectedInternship);
    }
  /**
     * Generates an internship report with optional status, major, and level filters.
     */  
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
   
    /**
     * Lists pending company representatives awaiting approval or rejection.
     */
    private void listPendingReps(){
        boolean any=false;
        List<User> users = csController.getUsers();
        for (User u:users){
            if (u instanceof CompanyRepresentative r){
                if (!Status.APPROVED.matches(r.getStatus()) && !Status.REJECTED.matches(r.getStatus())) {
                    any = true;
                    System.out.println(r.getUserID() + " | " + r.getCompanyName() + " | status=" + r.getStatus());
                }
            }
        }
        if (!any) System.out.println("No pending reps.");
    }
    /**
     * Lists pending internships awaiting approval or rejection.
     */
    private void listPendingInternships() {             
        List<Internship> pendingInternships = new ArrayList<>();
        for (Internship internship : internshipController.getInternships()) {
            if (Status.PENDING.matches(internship.getInternshipStatus())) {
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
    /**
     * Filters internships based on criteria and value provided by staff.
     */
    private void filterInternships(){
        String criteria;
        do{
            System.out.println("Enter filter criteria (status/preferredmajors/internshiplevel/closingdate/opendate/companyname/visibility):");
            System.out.println("Or type 'clear' to remove filters");
            criteria = sc.nextLine().trim();
            if (criteria.isEmpty()){
                System.out.println("Criteria cannot be empty. please try again");

            }
        } while(criteria.isEmpty());
        
        if ("clear".equalsIgnoreCase(criteria)) {
            currentFilterCriteria = null;
            currentFilterValue = null;
            System.out.println("Filters cleared.");
            return;
        }
        
        String value;
        do{
            System.out.println("Enter filter value");
            value = sc.nextLine().trim();
            if(value.isEmpty()){
                System.out.println("Fitler value cannot be empty, please try again");

            }

        } while (value.isEmpty());
        
        //Save filter settings
        currentFilterCriteria = criteria;
        currentFilterValue = value;
        
        System.out.println("Filter applied: " + criteria + " = " + value);
        System.out.println("Filter settings saved. Use 'View All Internships' to see filtered results.");
    }
   /**
     * Manages pending withdrawal requests and allows approval or rejection.
     */ 
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
        
        int choice = -1;
        while(true){
            System.out.print("Choose request to manage");
            String input = sc.nextLine().trim();
            try {
                choice = Integer.parseInt(input);
                if(choice >= 1 && choice <= pendingRequests.size()) break;
                System.out.println("Invalid selection. Choose a number from the list");

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }
        
            Application selectedApp = pendingRequests.get(choice - 1);
            
            
            int action = -1;
            while(true){
                System.out.println("1. Approve Withdrawal");
                System.out.println("2. Reject Withdrawal");
                System.out.print("Choose action: ");
                String input = sc.nextLine().trim();
                try {
                    action = Integer.parseInt(input);
                    if (action == 1 || action == 2) break;
                    System.out.println("Invalid action. Enter 1 or 2");
                } catch (NumberFormatException e) {
                    System.out.println("Please enter 1 or 2");
                }
                
            }
            
            if (action == 1) {
                csController.approveWithdrawal(currentUser.getUserID(), selectedApp);
            } else if (action == 2) {
                csController.rejectWithdrawal(currentUser.getUserID(), selectedApp);
            } 
        }   
    
     /**
     * Displays all internships with optional filters applied.
     */
    
    private void viewAllInternships() {
        List<Internship> allInternships = internshipController.getInternships();
        
        //Apply saved filters
        if (currentFilterCriteria != null && currentFilterValue != null) {
            allInternships = this.filter(allInternships, currentFilterCriteria, currentFilterValue);
        }  

        //Sort alphabetically
        allInternships.sort(Comparator.comparing(Internship::getInternshipTitle, String.CASE_INSENSITIVE_ORDER));
        displayAllInternships(allInternships, "ALL INTERNSHIP OPPORTUNITIES");
    }
    /**
     * Helper method to display a list of internships with details.
     * @param internships List of internships to display
     * @param title Title of the display section
     */
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
        System.out.println("=====================================");
    }
    
    /**
     * Allows the staff to change their password.
     */
    private void changePassword() {
        System.out.println("\n=== CHANGE PASSWORD ===");
        
        System.out.print("Enter current password: ");
        String currentPassword = sc.nextLine();
        

        String newPassword;
        do { 
            System.out.print("Enter new password: ");
            newPassword = sc.nextLine();
            if(newPassword.isEmpty()){
              System.out.println("Error: New password cannot be empty.");  
            }    
        } while (newPassword.isEmpty());
        
        String confirmPassword;
        do { 
            System.out.println("Confirm new password");
            confirmPassword = sc.nextLine();
            if(!newPassword.equals(confirmPassword)){
                System.out.println("Error: new passwords do not match. Please try again");
            }
        } while (!newPassword.equals(confirmPassword));
        
        if (newPassword.isEmpty()) {
            System.out.println("Error: New password cannot be empty.");
            return;
        }
        
        String currentUserID = currentUser.getUserID();
        logInController.changePassword(currentUserID, currentPassword, newPassword);
        System.out.println("Returning to main menu...");
    }  
}
