public class CompanyUI implements FilterOptions, LogIn{
    private LogInController logInController;
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

            Date openDate = new Date();
            System.out.println("Enter closing date (YYYY-MM-DD):");
            String closingDateStr = scanner.nextLine();
            Date closeDate = Date.valueOf(closingDateStr);

            System.out.println("Enter number of slots (max 10):");
            int slots = scanner.nextInt();
            slots = Math.min(slots, 10); //enforce max slots

            Internship internship = new Internship(title, description, level, preferredMajor, "PENDING",openDate, closeDate, currentUser.getCompanyName(), slots);

            internship.setVisible(true);
            internships.add(internship);

            System.out.println("Internship created successfully!");
           
        } catch (Exception e){
            System.out.println("Error creating internship: " + e.getMessage());
        }

    }

    private void viewApplications(){
        System.out.println("═".repeat(80));
        System.out.println("APPLICATIONS RECEIVED");
        System.out.println("═".repeat(80));
        
        if (applications.isEmpty()) {
            System.out.println(" No applications received yet.");
            return;
        }

        System.out.printf("%-15s %-20s %-25s %-10s%n", 
            "APPLICATION ID", "STUDENT", "INTERNSHIP", "STATUS");
        System.out.println("-".repeat(80));
        
        for (int i = 0; i < applications.size(); i++) {
            Application app = applications.get(i);
            System.out.printf("%-15s %-20s %-25s %-10s%n",
                app.getApplicationID().substring(0, Math.min(12, app.getApplicationID().length())),
                app.getStudent.getName(),
                app.getInternship.getTitle().substring(0, Math.min(22, app.getInternship.getTitle().length())),
                app.getStatus());
        }
    }

    private void manageApplications(){
        viewApplications();
        
        if (applications.isEmpty()) {
            return;
        }

        System.out.println("Enter Application ID to approve/reject:");
        String appID = scanner.nextLine();

        Application selectedApp = findApplicationByID(appID);
        if (selectedApp == null) {
            System.out.println("Application not found.");
            return;
        }

        System.out.println("1. Approve Application");
        System.out.println("2. Reject Application");
        System.out.print("Choose (1-2): ");
        int choice = scanner.nextInt();

        if (choice == 1) internshipController.approveApplication(currentUser, selectedApp);
        else if (choice == 2) internshipController.rejectApplication(currentUser, selectedApp);
        else System.out.println("Cancelled.");
    }

    private void viewMyInternships(){ 
        if (internships.isEmpty()) {
            System.out.println("No internships created yet.");
            return;
        }

        for (Internship i : internships) {
            System.out.println("- " + i.getInternshipTitle() + " (" + i.getLevel() + ")");
            }
    }


    private void toggleVisibility(){
        viewMyInternships();

        if (internships.isEmpty()) {
            return;
        }

        System.out.println("Enter internship number to toggle visibility:");
        int internshipNum = scanner.nextInt();
         try {
            Internship selectedInternship = internships.get(internshipNum - 1);
            boolean newVisibility = !selectedInternship.isVisible(); // Toggle the current visibility
            selectedInternship.setVisible(newVisibility);
            System.out.println("Internship visibility updated to: " + (newVisibility ? "Visible" : "Hidden"));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid internship number.");
        }
    }

    private void filterApplications(String criteria, String value){
        
    }


    //helper method 
    private Application findApplicationById(String appId) {
        for (Application app : applications) {
            if (app.getApplicationID().equals(appId)) {
                return app;
            }
        }
        return null;
    }
}


