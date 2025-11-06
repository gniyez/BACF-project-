import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class CompanyUI implements FilterOptions, LogIn{
    private InternshipController internshipController;
    private ApplicationController appController;
    private CompanyRepresentative currentUser;
    private Scanner scanner;
    private List<Internship> internships;
    private List<Application> applications;

    public CompanyUI(LogInController logInController,ApplicationController appController, InternshipController internshipcontroller CompanyRepresentative currentUser){
        this.loginController = logInController;
        this.appController = appController;
        this.internshipcontroller = internshipcontroller;
        this.currentUser = currentUser;
        this.scanner = new Scanner(System.in);
        this.internships = new ArrayList<>();
        this.applications = new ArrayList<>();
    }

    public void start(){
        System.out.println("COMPANY REPRESENATIVE LOGIN");
        System.out.println("═".repeat(30));
        System.out.println("Enter your User ID:");
        String userID = scanner.nextLine();
        System.out.println("Enter your Password:");
        String password = scanner.nextLine();
        logInController.logIn(userID, password);
       
        showMainMenu();
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

            switch(choice){
                case 1 -> createInternship();
                case 2 -> viewApplications();
                case 3 -> manageApplications();
                case 4 -> viewMyInternships();
                case 5 -> toggleVisibility();
                case 6 -> filterApplications();
                case 0 -> {
                    logout();
                    System.out.prinln("Goodbye!");
                    return;
                deafult -> System.out.println("Invalid choice. Please try again.");
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
            String level = switch(levelChoice){
                case 1 -> "BASIC";
                case 2 -> "INTERMEDIATE";
                case 3 -> "ADVANCED";
                default -> throw new IllegalArgumentException("Invalid level choice");
            };

            System.out.println("Enter preferred major:");
            String preferredMajor = scanner.nextLine();

            System.out.println("Enter opening date (YYYY-MM-DD):");
            Date openDate = Date.valueOf(scanner.nextLine());

            System.out.println("Enter closing date (YYYY-MM-DD):");
            Date closeDate = Date.valueOf(scanner.nextLine());

            System.out.println("Enter number of slots (max 10):");
            int slots = scanner.nextInt();
            slots = Math.min(slots, 10); //enforce max slots

            internshipController.createInternship(currentUser, title, description, level, preferredMajor, openDate, closeDate, slots);

            System.out.println("Internship created successfully!");
           
        } catch (Exception e){
            System.out.println("Error creating internship: " + e.getMessage());
        }

    }

    private void viewApplications(){
        List<Application> all = appController.getApplications(); 
        for (Application app : all) {
            if (app.getInternship().getCompanyName().equalsIgnoreCase(currentUser.getCompanyName())) {
                any = true;
                System.out.println(app.getApplicationID() + " | "
                        + app.getStudent().getStudentID() + " | "
                        + app.getInternship().getInternshipTitle() + " | "
                        + app.getStatus());
            }
        }
        if (!any) System.out.println("No applications for your internships.");
    }
    }

    private void manageApplications(){
        viewApplications();
        
        if (applications.isEmpty()) {
            return;
        }

        System.out.println("Enter Application ID to approve/reject:");
        String appID = scanner.nextLine();

        Application selectedApp = null;
        for (Application app : appController.getApplications()) {
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
                        + " | Status: " + i.getStatus()
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

        if (internships.isEmpty()) {
            return;
        }

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

    private void filterApplications(String criteria, String value){
        
    }
}


