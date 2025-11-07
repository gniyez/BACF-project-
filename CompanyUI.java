import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class CompanyUI implements FilterOptions{
    private LogInController logInController;
    private InternshipController internshipController;
    private ApplicationController appController;
    private CompanyRepresentative currentUser;
    private Scanner scanner;
   

    public CompanyUI(LogInController logInController, ApplicationController appController, 
            InternshipController internshipController) {  
		this.logInController = logInController;
		this.appController = appController;
		this.internshipController = internshipController;
		this.scanner = new Scanner(System.in);
		this.currentUser = null;  
	}

    public void start(){
        System.out.println("COMPANY REPRESENATIVE LOGIN");
        System.out.println("═".repeat(30));
        System.out.println("Enter your User ID:");
        String userID = scanner.nextLine();
        System.out.println("Enter your Password:");
        String password = scanner.nextLine();
        boolean loginSuccess = logInController.login(userID, password);
        
        if (loginSuccess) {
            User loggedInUser = logInController.getCurrentUser();
            if (loggedInUser instanceof CompanyRepresentative) {
                this.currentUser = (CompanyRepresentative) loggedInUser;
                System.out.println("Welcome, " + currentUser.getName() + "!");
                showMainMenu();
            } else {
                System.out.println("Access denied. Not a student.");
            }
        } else {
            System.out.println("Login failed. Invalid credentials.");
        }
    }

    private void showMainMenu(){
        while (true){
            System.out.println("COMPANY REPRESENTATIVE MENU");
            System.out.println("═".repeat(50));
            System.out.println("1.  Create New Internship");
            System.out.println("2.  View Applications");
            System.out.println("3.  Approve/Reject Applications");
            System.out.println("4.  View My Internships");
            System.out.println("5.  Toggle Internship Visibility");
            System.out.println("6.  Filter Applications");
            System.out.println("0.  Exit");
            System.out.println("═".repeat(50));

            System.out.println("Choose an option (0-6):");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice){
                case 1 -> createInternship();
                case 2 -> viewApplications();
                case 3 -> manageApplications();
                case 4 -> viewMyInternships();
                case 5 -> toggleVisibility();
                case 6 -> filterInternships();
                case 0 -> {
                    logInController.logout();
                    System.out.println("Goodbye!");
                    return;}
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    

    private void createInternship(){
        try{
            //collect all required info
            System.out.println("Enter internship title:");
            String title = scanner.nextLine();
            System.out.println("Enter internship description:");
            String description = scanner.nextLine();

            //level selection
            System.out.println("Select internship level:");
            System.out.println("1. Basic (Year 1-4)");
            System.out.println("2. Intermediate (Year 3-4)");
            System.out.println("3. Advanced (Year 3-4)");
            System.out.println("Enter choice (1-3):");
            int levelChoice = scanner.nextInt();
            scanner.nextLine();
            
            String level = switch(levelChoice){
                case 1 -> "BASIC";
                case 2 -> "INTERMEDIATE";
                case 3 -> "ADVANCED";
                default -> throw new IllegalArgumentException("Invalid level choice");
            };

            System.out.println("Enter preferred major:");
            String preferredMajor = scanner.nextLine();

            System.out.println("Enter opening date (YYYY-MM-DD):");
            LocalDate openDate = LocalDate.parse(scanner.nextLine());

            System.out.println("Enter closing date (YYYY-MM-DD):");
            LocalDate closeDate = LocalDate.parse(scanner.nextLine());
            
            System.out.println("Enter number of slots (max 10):");
            int slots = scanner.nextInt();
            slots = Math.min(slots, 10); //enforce max slots

            internshipController.createInternship(currentUser, title, description, level, preferredMajor, openDate, closeDate, slots);

            System.out.println("Internship created successfully!");
           
        } catch (Exception e){
            System.out.println("Error creating internship: " + e.getMessage());}
        }

 

    private void viewApplications(){
        List<Application> all = appController.getApplications(); 
        boolean any = false;
        for (Application app : all) {
            if (app.getInternship().getCompanyName().equalsIgnoreCase(currentUser.getCompanyName())) {
                any = true;
                System.out.println(app.getApplicationID() + " | "
                        + app.getStudent().getUserID() + " | "
                        + app.getInternship().getInternshipTitle() + " | "
                        + app.getStatus());
            }
        }
        if (!any) System.out.println("No applications for your internships.");
    }

    private void manageApplications(){
        viewApplications();
        
     // Fix: Use appController.getApplications() instead of empty applications list
        List<Application> companyApplications = new ArrayList<>();
        for (Application app : appController.getApplications()) {
            if (app.getInternship().getCompanyName().equalsIgnoreCase(currentUser.getCompanyName())) {
                companyApplications.add(app);
            }
        }
        
        if (companyApplications.isEmpty()) { 
            System.out.println("No applications to manage.");
            return;
        }

        System.out.println("Enter Application ID to approve/reject:");
        String appID = scanner.nextLine();

        Application selectedApp = null;
        for (Application app : companyApplications) {
            if (app.getApplicationID().equals(appID) && app.getInternship().getCompanyName().equalsIgnoreCase(currentUser.getCompanyName())) {
                selectedApp = app;
                break;
            }
        }
        if (selectedApp == null) {
            System.out.println("Application not found.");
            return;
        }

        System.out.println("1. Approve Application");
        System.out.println("2. Reject Application");
        System.out.print("Choose (1-2): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) appController.approveApplication(currentUser, selectedApp);
        else if (choice == 2) appController.rejectApplication(currentUser, selectedApp);
        else System.out.println("Invalid.");
    }

    private void viewMyInternships(){ 
        List<Internship> all = internshipController.getInternships();      
        boolean any = false;
        for (Internship i : all) {
            if (currentUser.getCompanyName().equalsIgnoreCase(i.getCompanyName())) {
                any = true;
                System.out.println(i.getInternshipTitle()
                        + " | Status: " + i.getInternshipStatus()
                        + " | Visible: " + i.getVisibility()
                        + " | Level: " + i.getLevel()
                        + " | Slots: " + i.getSlots()
                        + " | Open: " + i.getOpenDate() + " to " + i.getCloseDate());
            }
        }
        if (!any) System.out.println("No internships yet.");
    }


    private void toggleVisibility(){
        viewMyInternships();

        List<Internship> all = internshipController.getInternships();
        List<Internship> mine = new ArrayList<>();
        for (Internship i : all) {
            if (currentUser.getCompanyName().equalsIgnoreCase(i.getCompanyName())) {
                mine.add(i);
            }
        }
        if (mine.isEmpty()) {
            System.out.println("No internships to toggle.");
            return;
        }
        for (int idx = 0; idx < mine.size(); idx++) {
            System.out.println((idx + 1) + ") " + mine.get(idx).getInternshipTitle()
                    + " (visible=" + mine.get(idx).getVisibility() + ")");
        }
        System.out.print("Choose internship to toggle: ");
        try {
            int pick = scanner.nextInt();         
            int idx = pick - 1;
            if (idx < 0 || idx >= mine.size()) {
                System.out.println("Invalid index.");
                return;
            }
            internshipController.toggleVisibility(mine.get(idx));          
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }
    
        
    private void filterInternships() {
    	 System.out.println("Enter filter criteria (status/preferredmajors/internshiplevel/etc):");
    	    String criteria = scanner.nextLine();
    	    System.out.println("Enter value to filter by:");
    	    String value = scanner.nextLine();
    	    
   
    	    List<Internship> companyInternships = new ArrayList<>();
    	    List<Internship> allInternships = internshipController.getInternships();
    	    for (Internship internship : allInternships) {
    	        if (currentUser.getCompanyName().equalsIgnoreCase(internship.getCompanyName())) {
    	            companyInternships.add(internship);
    	        }
    	    }
    	    
    	    List<Internship> filtered = this.filter(companyInternships, criteria, value);
    	    
    	    // Fix: Display filtered results
    	    System.out.println("Filtered Internships:");
    	    if (filtered.isEmpty()) {
    	        System.out.println("No internships match the filter criteria.");
    	    } else {
    	        for (Internship internship : filtered) {
    	            System.out.println(internship.getInternshipTitle()
    	                    + " | Status: " + internship.getInternshipStatus()
    	                    + " | Visible: " + internship.getVisibility()
    	                    + " | Level: " + internship.getLevel()
    	                    + " | Company: " + internship.getCompanyName());
    	        }
    	    }
    	}
}


