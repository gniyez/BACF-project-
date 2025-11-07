import java.util.Scanner;
import java.util.List;

public class CareerUI implements FilterOptions{
    private CareerServiceController csController;
    private InternshipController internshipController;
    private LogInController logInController;
    private Scanner sc;
    private CareerServiceStaff currentUser;

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
        } else {
            System.out.println("Login failed. Invalid credentials.");
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
            System.out.println("9. Logout");

            System.out.print("Enter your choice: ");
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
                   
                case 9 ->{
                	System.out.println("Logging out...");
                	return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void approveCompanyFlow() {
        System.out.print("Enter company rep ID to approve: ");
        String repID = sc.nextLine();
        csController.approveCompany(currentUser.getUserID(),repID);
    }

    private void rejectCompanyFlow() {
        System.out.print("Enter company rep ID to reject: ");
        String repID = sc.nextLine();
        csController.rejectCompany(currentUser.getUserID(), repID);
    }

    private void approveInternshipFlow() {
        System.out.print("Enter internship title to approve: ");
        String title = sc.nextLine();
        Internship internship = csController.findInternshipByTitle(title);
        if (internship != null) {
            csController.approveInternship(currentUser.getUserID(), internship);
        } else {
            System.out.println("Internship not found.");
        }
    }

    private void rejectInternshipFlow() {
        System.out.print("Enter internship title to reject: ");
        String title = sc.nextLine();
        Internship internship = csController.findInternshipByTitle(title);
        if (internship != null) {
            csController.rejectInternship(currentUser.getUserID(), internship);
        } else {
            System.out.println("Internship not found.");
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
                    System.out.println("RepID=" + r.getUserID() + " | " + r.getCompanyName() + " | status=" + r.getStatus());
                }
            }
        }
        if (!any) System.out.println("No pending reps.");
    }
    
    private void listPendingInternships() {             
        boolean any = false;
        for (Internship i : internshipController.getInternships()) {
            if ("PENDING".equals(i.getInternshipStatus())) {
                any = true;
                System.out.println(i.getInternshipTitle() + " | " + i.getCompanyName() + " | Visible=" + i.getVisibility());
            }
        }
        if (!any) System.out.println("No pending internships.");
    }   
    
    private void filterInternships(){
        System.out.println("Enter filter criteria: ");
        String criteria = sc.nextLine();
        System.out.println("Enter filter value: ");
        String value = sc.nextLine();
        List<Internship> filtered = internshipController.filter(criteria, value);
        if (filtered.isEmpty()) {
            System.out.println("No internships found matching the specified criteria.");
            return;
        }
        for (Internship internship : filtered) {
            System.out.println("\nTitle: " + internship.getInternshipTitle());
            System.out.println("Company: " + internship.getCompanyName());
            System.out.println("Level: " + internship.getLevel());
            System.out.println("Preferred Major: " + internship.getPreferredMajor());
            System.out.println("Status: " + internship.getInternshipStatus());
            System.out.println("Visibility: " + internship.getVisibility());
          
        }
    }
    
}
