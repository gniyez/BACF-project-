package project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class CompanyUI implements FilterOptions{
    private final LogInController logInController;
    private final InternshipController internshipController;
    private final ApplicationController appController;
    private CompanyRepresentative currentUser;
    private final Scanner scanner;
    private final List<User> users; 
    private final Set<String> userIDs; //added for fast lookup
    private String currentFilterCriteria = null;
    private String currentFilterValue = null;
    private Application selectedApp = null;
    
    public CompanyUI(LogInController logInController, ApplicationController appController, 
            InternshipController internshipController, List<User> users) {  
        this.logInController = logInController;                //--- UPDATE --- specify generic type
        this.appController = appController;
        this.internshipController = internshipController;
        this.scanner = new Scanner(System.in);
        this.currentUser = null;
        this.users = users;

        this.userIDs = new HashSet<>(); //initialise userID set for fast lookup
        for (User user:users){
            userIDs.add(user.getUserID());
        }

    }

    public void start(){
        while (true) {
            System.out.println("\n=== COMPANY REPRESENTATIVE PORTAL ===");
            System.out.println("1. Login");
            System.out.println("2. Register New Account");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1 -> login();
                case 2 -> register();
                case 0 -> { 
                    System.out.println("Returning to main menu...");
                    return; 
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void login(){
        System.out.println("\n=== COMPANY REPRESENTATIVE LOGIN ===");
        System.out.println("Enter your User ID (email):");
        String userID = scanner.nextLine();
        System.out.println("Enter your Password:");
        String password = scanner.nextLine();
        
        boolean loginSuccess = logInController.login(userID, password);
        
        if (loginSuccess) {
            User loggedInUser = logInController.getCurrentUser();
            if (loggedInUser instanceof CompanyRepresentative companyRepresentative) {
                this.currentUser = companyRepresentative;
                showMainMenu();
            } else {
                System.out.println("Access denied. Not a company representative.");
            }
        }
    }

    private void register(){
        System.out.println("\n=== COMPANY REPRESENTATIVE REGISTRATION ===");
        
        //Email format validation 
        String email;
        do { 
            System.out.print("Enter your email: ");
            email = scanner.nextLine().trim();
            if(email.isEmpty()){
                System.out.println("Error. Empty email address");
            } else if(!isValidEmail(email)){
                System.out.println("Error. Invalid email address");
                email = "";
            }
        } while (email.isEmpty());


       //Check if email already exists
        if (userIDs.contains(email)){ // 0 1 lookup
            System.out.println("Error. A representative with this email already exists");
            return;
        }

        //for each field, validates for empty inputs (added)
        
        //Non empty fields for name, company, department, position
        String name;
        do { 
            System.out.println("Enter your name: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()){
            System.out.println("Error: Name cannot be empty");}
        } while (name.isEmpty());
        
        String companyName;
        do { 
            System.out.println("Enter company name: ");
            companyName = scanner.nextLine().trim();
            if (companyName.isEmpty()) {
            System.out.println("Error. Company name cannot be empty");}
        } while (companyName.isEmpty());
        
        String department;
        do { 
            System.out.print("Enter department: ");
            department = scanner.nextLine().trim();
                    if (department.isEmpty()){
            System.out.println("Error. Department cannot be empty.");}            
        } while (department.isEmpty());
        
        String position;
        do { 
            System.out.print("Enter position: ");
            position = scanner.nextLine().trim();
            if (position.isEmpty()){
            System.out.println("Error. position cannot be empty");}
        } while (position.isEmpty());
        
        //Create and register the new representative
        CompanyRepresentative rep = new CompanyRepresentative(email, name, companyName, department, position);
        users.add(rep); 
        userIDs.add(email); //add to set for future lookups
        System.out.println("Registration successful!");
        System.out.println("Please wait for approval from Career Center Staff before logging in.");

    }
    //added email format validation 
    private boolean isValidEmail(String email){
        return email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    private void showMainMenu(){
        while (true){
            System.out.println("COMPANY REPRESENTATIVE MENU");
            System.out.println("═".repeat(50));
            System.out.println("1. Create New Internship");
            System.out.println("2. View Applications");
            System.out.println("3. Edit Internship");          
            System.out.println("4. Delete Internship"); 
            System.out.println("5. Approve/Reject Applications");
            System.out.println("6. View My Internships");
            System.out.println("7. Toggle Internship Visibility");
            System.out.println("8. Filter Internships");
            System.out.println("9. Change Password");
            System.out.println("10. View Applications by Suitability Score");
            System.out.println("0. Logout");
            System.out.println("═".repeat(50));
            
            

            int choice = -1;
            while (true) { 
                System.out.print("Choose an option: ");
                String input = scanner.nextLine();
                try {
                    choice = Integer.parseInt(input.trim());
                    if (choice >= 0 && choice <= 10) break;
                    System.out.println("Invalid choice. Must be between 0 and 10");
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number");
                }
            }

            switch(choice){
                case 1 -> createInternship();
                case 2 -> viewApplications();
                case 3 -> editInternship();     
                case 4 -> deleteInternship();
                case 5 -> manageApplications();
                case 6 -> viewMyInternships();
                case 7 -> toggleVisibility();
                case 8 -> filterInternships();
                case 9 -> changePassword();
                case 10 -> showBestApplicantsByScore();
                case 0 -> {
                    System.out.println("Goodbye!");
                    logInController.logout();
                    return;}
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    

    private void createInternship(){
        try{
            //collect all required info
            String title;
            do { 
                System.out.println("Enter internship title:");
                title = scanner.nextLine().trim();
                if (title.isEmpty()){
                    System.out.println("Title cannot be empty");
                }   
            } while (title.isEmpty());
            
            String description;
            do { 
                System.out.println("Enter internship description");
                description = scanner.nextLine().trim();
                if (description.isEmpty()){
                    System.out.println("Description cannot be empty");
                }
            } while (description.isEmpty());

            //level selection
            
            int levelChoice = -1;
            while (true) { 
                System.out.println("Select internship level:");
                System.out.println("1. Basic (Year 1-4)");
                System.out.println("2. Intermediate (Year 3-4)");
                System.out.println("3. Advanced (Year 3-4)");
                System.out.println("Enter choice (1-3):");
                String input = scanner.nextLine();    

                try {
                    levelChoice = Integer.parseInt(input.trim());
                    if(levelChoice >= 1 && levelChoice <=3) break;
                    System.out.println("Invalid choice. Select 1,2 or 3");

                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number 1-3");
                }
            }
            
            String level = switch(levelChoice){
                case 1 -> InternshipLevel.BASIC.name();
                case 2 -> InternshipLevel.INTERMEDIATE.name();
                case 3 -> InternshipLevel.ADVANCED.name();
                default -> throw new IllegalArgumentException("Invalid level choice");
            };

            String preferredMajor;
            do { 
                System.out.println("Enter preferred major: ");
                preferredMajor = scanner.nextLine().trim();
                if(preferredMajor.isEmpty()){
                    System.out.println("Preferred major cannot be empty");
                }
            } while (preferredMajor.isEmpty());

            if (DuplicateInternshipGuard.isDuplicate(internshipController, currentUser, title, level)) {
            System.out.println("Duplicate internship detected: you already have an internship with this title and level.");
            return;
        }
            //Validate dates in correct format. Closing date must be after Open;
            LocalDate openDate = null;
            while (openDate == null){
                System.out.println("Enter opening date (YYYY-MM-DD):");
                String input = scanner.nextLine().trim();
                try {
                    openDate = LocalDate.parse(input);
                } catch (Exception e) {
                    System.out.println("Invalid date format. Please enter in YYYY-MM-DD");
                }    
            }

            LocalDate closeDate = null;
            while(closeDate == null){
                System.out.println("Enter closing date (YYYY-MM-DD):");
                String input = scanner.nextLine().trim();
                try {
                    LocalDate tmp = LocalDate.parse(input);
                    if(tmp.isAfter(openDate)){
                        closeDate = tmp;
                    }
                    else{
                        System.out.println("Closing date must be after opening date");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid date format. Please use YYYY-MM-DD");
                }
            }
        
            //Number of slots validation 
            int slots = 0;
            while (true) { 
                System.out.println("Enter number of slots (max 10):");
                String input = scanner.nextLine().trim();
                try{
                    slots = Integer.parseInt(input);
                    if(slots >= 1 && slots <= 10) break;
                    System.out.println("Number of slots must be from 1 to 10");

                } catch (NumberFormatException e){
                    System.out.println("Please enter a valid number of slots");
                }
            }
            
        internshipController.createInternship(currentUser, title, description, level, preferredMajor, openDate, closeDate, slots);

        System.out.println("Internship created successfully!");
    } catch (IllegalArgumentException e) {
        System.out.println("Error creating internship: " + e.getMessage());
    }
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

    private void editInternship() {
        List<Internship> myInternships = getMyInternships();
        
        if (myInternships.isEmpty()) {
            System.out.println("No internships to edit.");
            return;
        }
        
        System.out.println("\n=== EDIT INTERNSHIP ===");
        System.out.println("Your Internships:");
        for (int i = 0; i < myInternships.size(); i++) {
            Internship intern = myInternships.get(i);
            String editable = Status.PENDING.matches(intern.getInternshipStatus()) ? "EDITABLE" : "APPROVED";
            System.out.println((i+1) + ") " + intern.getInternshipTitle() + 
                              " | Status: " + intern.getInternshipStatus() + " | " + editable);
        }
        

        int choice = -1;
        while (true) { 
            System.out.println("Choose internship to edit: ");    
            String input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input.trim());
                if(choice >=1 && choice <=myInternships.size()) break;
                System.out.println("Invalid selection. Enter number from the list.");

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }
            
            Internship selected = myInternships.get(choice - 1);
            
            
            if (!Status.PENDING.matches(selected.getInternshipStatus())) {
                System.out.println("Cannot edit internship. Only PENDING internships can be edited.");
                return;
            }
            
            //Show current values and allow editing
            System.out.println("\nEditing: " + selected.getInternshipTitle());
            System.out.println("(Press Enter to keep current value)");
            
            System.out.print("Current Title: " + selected.getInternshipTitle() + "\nNew Title: ");
            String newTitle = scanner.nextLine();
            if (!newTitle.isEmpty()) selected.setInternshipTitle(newTitle);
            
            System.out.print("Current Description: " + selected.getInternshipDescription() + "\nNew Description: ");
            String newDescription = scanner.nextLine();
            if (!newDescription.isEmpty()) selected.setInternshipDescription(newDescription);
            
            System.out.print("Current Level: " + selected.getLevel() + "\nNew Level: ");
            String newLevel = scanner.nextLine();
            if (!newLevel.isEmpty()) selected.setLevel(newLevel.toUpperCase());
            
            System.out.print("Current Preferred Major: " + selected.getPreferredMajor() + "\nNew Preferred Major: ");
            String newMajor = scanner.nextLine();
            if (!newMajor.isEmpty()) selected.setPreferredMajor(newMajor);
            
            System.out.print("Current Slots: " + selected.getSlots() + "\nNew Slots: ");
            String slotsInput = scanner.nextLine();
            if (!slotsInput.isEmpty()) {
                int newSlots;
                try {
                    newSlots = Integer.parseInt(slotsInput);
                    if (newSlots < 1  || newSlots >10){
                        System.out.println("Slots must be between 1 and 10. Keeping previous value");

                    }
                    else{
                        selected.setSlots(newSlots);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number. Keeping previous value");
                }}
            
            System.out.println("Internship updated successfully!");
            
               
     }
    

    private void deleteInternship() {
        List<Internship> myInternships = getMyInternships();
        
        if (myInternships.isEmpty()) {
            System.out.println("No internships to delete.");
            return;
        }
        
        System.out.println("\n=== DELETE INTERNSHIP ===");
        for (int i = 0; i < myInternships.size(); i++) {
            Internship intern = myInternships.get(i);
            System.out.println((i+1) + ") " + intern.getInternshipTitle() + 
                              " | Status: " + intern.getInternshipStatus() + " | ");
        }
        
        int choice = -1;
        while (true){
            System.out.print("Choose internship to delete: ");
            String input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input.trim());
                if (choice >= 1 && choice <= myInternships.size()) break;
                System.out.println("Invalid selection. Enter valid number from the list");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number");
            }
        }
            
            Internship selected = myInternships.get(choice - 1);
            
            System.out.print("Are you sure you want to delete '" + selected.getInternshipTitle() + "'? (Y/N): ");
            String confirm = scanner.nextLine();
            
            if (confirm.equalsIgnoreCase("Y")) {
                internshipController.getInternships().remove(selected);
                System.out.println("Internship deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        
            
    }


    private List<Internship> getMyInternships() {
        List<Internship> myInternships = new ArrayList<>();
        for (Internship internship : internshipController.getInternships()) {
            if (currentUser.getCompanyName().equalsIgnoreCase(internship.getCompanyName())) {
                myInternships.add(internship);
            }
        }
        return myInternships;
    }
    
    private void manageApplications(){
        viewApplications();
        
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

        
        String appID;
        while(selectedApp == null){
            System.out.println("Enter Application ID to approve/reject:");
            appID = scanner.nextLine().trim();
            if(appID.isEmpty()){
                System.out.println("Application ID cannot be empty");
                continue;
            }
            for (Application app : companyApplications){
                if (app.getApplicationID().equals(appID) && app.getInternship().getCompanyName().equalsIgnoreCase(currentUser.getCompanyName())) {
                    selectedApp = app;
                    break;
                }
            }
            if(selectedApp == null){
                System.out.println("Application not found. Please try again");
            }
        }

        int choice = -1;
        while(true){
            System.out.println("1. Approve Application");
            System.out.println("2. Reject Application");
            System.out.print("Choose (1-2): ");
            String input = scanner.nextLine().trim();
            try {
                choice = Integer.parseInt(input);
                if (choice == 1 || choice == 2) break;
                System.out.println("Invalid choice. Enter 1 or 2");
            } catch (NumberFormatException e) {
                System.out.println("Please enter 1 or 2");
            }
        }

        switch (choice) {
            case 1 -> appController.approveApplication(currentUser, selectedApp);
            case 2 -> appController.rejectApplication(currentUser, selectedApp);
            default -> System.out.println("Invalid.");
        }
    }

    private void viewMyInternships(){ 
    	List<Internship> myInternships = getMyInternships();
        
        //Apply saved filters
        if (currentFilterCriteria != null && currentFilterValue != null) {
            myInternships = this.filter(myInternships, currentFilterCriteria, currentFilterValue);
        }
        
        //Sort alphabetically
        myInternships.sort(Comparator.comparing(Internship::getInternshipTitle, String.CASE_INSENSITIVE_ORDER));
        
        displayCompanyInternships(myInternships, "MY INTERNSHIPS");
    }
    
    private void displayCompanyInternships(List<Internship> internships, String title) {
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
        System.out.println("=======================");
    }
    private void toggleVisibility(){
        try {
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
            int pick = -1;
            while (true) { 
                System.out.print("Choose internship to toggle: ");
                String input = scanner.nextLine().trim();
                try {
                    pick = Integer.parseInt(input);
                    if (pick >=1 && pick <=mine.size()) break;
                    System.out.println("Invalid selection. Please enter a number from the list");

                } catch (NumberFormatException e) { 
                    System.out.println("Please enter a valid number.");
                }    
            }
            int idx = pick - 1;
            internshipController.toggleVisibility(mine.get(idx));
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }
    
    
        
    private void filterInternships() {
        
        String criteria;

        do{
            System.out.println("Enter filter criteria (status/preferredmajors/internshiplevel/closingdate/opendate/companyname/visibility):");
            System.out.println("Or type 'clear' to remove filters");
            criteria = scanner.nextLine().trim();
            if(criteria.isEmpty()){
                System.out.println("Criteria cannot be empty. Please try again.");
            }
        } while (criteria.isEmpty());
        
        if ("clear".equalsIgnoreCase(criteria)) {
            currentFilterCriteria = null;
            currentFilterValue = null;
            System.out.println("Filters cleared.");
            return;
        }
        

        String value;
        do{
            System.out.println("Enter value to filter by:");
            value = scanner.nextLine();
            if (value.isEmpty()){
                System.out.println("Value cannot be empty.Please try again");

            }

        }while (value.isEmpty());
       
        
        //Save filter settings
        currentFilterCriteria = criteria;
        currentFilterValue = value;
        
        System.out.println("Filter applied: " + criteria + " = " + value);
        System.out.println("Filter settings saved. Use 'View My Internships' to see filtered results.");
    }
    
    private void changePassword() {
        System.out.println("\n=== CHANGE PASSWORD ===");
        
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();
        

        String newPassword;
        do{
            System.out.println("Enter new password");
            newPassword = scanner.nextLine();
            if (newPassword.isEmpty()){
                System.out.println("Error: new password cannot be empty");

            }
        } while (newPassword.isEmpty());

        String confirmPassword;
        do{
            System.out.print("Confirm new password: ");
            confirmPassword = scanner.nextLine();
            if(!newPassword.equals(confirmPassword)){
                System.out.println("Error: New passwords do not match.");
            }
        } while (!newPassword.equals(confirmPassword));
        
       
        String currentUserID = currentUser.getUserID();
        logInController.changePassword(currentUserID, currentPassword, newPassword);
            System.out.println("Returning to main menu...");
         }

    private void showBestApplicantsByScore() {
        //lists the company rep's internship offerings and prompts them to select one
        List<Internship> internships = internshipController.getInternshipsForCompany(currentUser.getCompanyName());
        if (internships.isEmpty()) {
            System.out.println("You have no internships.");
            return;
        }

        System.out.println("Select an internship:");
        for (int i = 0; i < internships.size(); i++) {
            Internship in = internships.get(i);
            System.out.println((i + 1) + ". " + in.getInternshipTitle() + " [" + in.getLevel() + "]");
        }

        int selection = -1;
        while (selection < 1 || selection > internships.size()) {
            System.out.print("Enter internship number: ");
            try {
                selection = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
        Internship selectedInternship = internships.get(selection - 1);

        // 2. Get, sort, display applicants
        List<Application> candidateApps = appController.getApplicationsForInternship(selectedInternship);
        candidateApps.sort((a, b) -> {
            int scoreA = MatchingScoreUtil.score(a.getStudent(), selectedInternship);
            int scoreB = MatchingScoreUtil.score(b.getStudent(), selectedInternship);
            return Integer.compare(scoreB, scoreA);
        });

        System.out.println("\nApplicants for " + selectedInternship.getInternshipTitle() + ":");
        for (Application app : candidateApps) {
            int score = MatchingScoreUtil.score(app.getStudent(), selectedInternship);
            System.out.println(app.getStudent().getName() + ", Major: " + app.getStudent().getMajor() +
                ", Year: " + app.getStudent().getYearOfStudy() + " [Score: " + score + "]");
        }
    }

    }