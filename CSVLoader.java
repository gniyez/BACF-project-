import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class CSVLoader {
    
    public List<User> loadUsersFromCSV(String studentFile, String staffFile, String companyFile) {
        List<User> users = new ArrayList<>();
        
        try {
            // Load students
            loadStudentsFromCSV(users, studentFile);
            
            // Load career service staff
            loadStaffFromCSV(users, staffFile);
            
            // Load company representatives
            loadCompanyRepsFromCSV(users, companyFile);
            
            System.out.println("Total users loaded: " + users.size());
            
        } catch (IOException e) {
            System.out.println("Error loading CSV files: " + e.getMessage());
        }
        
        return users;
    }
    
    private void loadStudentsFromCSV(List<User> users, String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;
            int loadedCount = 0;
            
            while ((line = br.readLine()) != null) {
                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                // Split by COMMA (changed from tab)
                String[] data = line.split(",");
                
                if (data.length >= 5) {
                    try {
                        String studentID = data[0].trim();
                        String name = data[1].trim();
                        String major = data[2].trim();
                        int year = Integer.parseInt(data[3].trim());
                        String email = data[4].trim();
                        
                        // Validate student ID format (U + 7 digits + letter)
                        if (!studentID.matches("^U\\d{7}[A-Z]$")) {
                            System.out.println("Warning: Invalid student ID format: " + studentID);
                        }
                        
                        // Create Student object - using studentID as userID
                        Student student = new Student(studentID, name, major, year);
                        users.add(student);
                        loadedCount++;
                        
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid year format in student data: " + line);
                    } catch (Exception e) {
                        System.out.println("Error parsing student data: " + line + " - " + e.getMessage());
                    }
                } else {
                    System.out.println("Invalid student data format (expected 5 columns, got " + data.length + "): " + line);
                    System.out.println("Data: " + java.util.Arrays.toString(data));
                }
            }
            System.out.println("Successfully loaded " + loadedCount + " students");
        }
    }
    
    private void loadStaffFromCSV(List<User> users, String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;
            int loadedCount = 0;
            
            while ((line = br.readLine()) != null) {
                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                // Split by COMMA (changed from tab)
                String[] data = line.split(",");
                
                if (data.length >= 5) {
                    try {
                        String staffID = data[0].trim();
                        String name = data[1].trim();
                        String role = data[2].trim();
                        String department = data[3].trim();
                        String email = data[4].trim();
                        
                        // Create CareerServiceStaff object - using staffID as userID
                        CareerServiceStaff staff = new CareerServiceStaff(staffID, name, department);
                        users.add(staff);
                        loadedCount++;
                        
                    } catch (Exception e) {
                        System.out.println("Error parsing staff data: " + line + " - " + e.getMessage());
                    }
                } else {
                    System.out.println("Invalid staff data format (expected 5 columns, got " + data.length + "): " + line);
                    System.out.println("Data: " + java.util.Arrays.toString(data));
                }
            }
            System.out.println("Successfully loaded " + loadedCount + " staff members");
        }
    }
    
    private void loadCompanyRepsFromCSV(List<User> users, String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;
            int loadedCount = 0;
            
            while ((line = br.readLine()) != null) {
                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                // Split by COMMA (changed from tab)
                String[] data = line.split(",");
                
                if (data.length >= 7) {
                    try {
                        String companyRepID = data[0].trim();
                        String name = data[1].trim();
                        String companyName = data[2].trim();
                        String department = data[3].trim();
                        String position = data[4].trim();
                        String email = data[5].trim();
                        String status = data[6].trim();
                        
                        // Validate email format for company reps
                        if (!email.contains("@")) {
                            System.out.println("Warning: Invalid email format for company rep: " + email);
                        }
                        
                        // Create CompanyRepresentative object - using email as userID
                        CompanyRepresentative rep = new CompanyRepresentative(email, name, companyName, department, position);
                        
                        // Set status if provided and valid
                        if (!status.isEmpty() && (status.equals("APPROVED") || status.equals("REJECTED"))) {
                            rep.setStatus(status);
                        } else if (!status.isEmpty() && !status.equals("PENDING")) {
                            System.out.println("Warning: Invalid status for company rep " + email + ": " + status);
                        }
                        
                        users.add(rep);
                        loadedCount++;
                        
                    } catch (Exception e) {
                        System.out.println("Error parsing company rep data: " + line + " - " + e.getMessage());
                    }
                } else if (data.length > 1) { // If there's some data but not enough columns
                    System.out.println("Invalid company rep data format (expected 7 columns, got " + data.length + "): " + line);
                    System.out.println("Data: " + java.util.Arrays.toString(data));
                }
                // If data.length == 1, it's probably just an empty line or header
            }
            System.out.println("Successfully loaded " + loadedCount + " company representatives");
        } catch (IOException e) {
            System.out.println("Company representatives file not found or inaccessible: " + filename);
            System.out.println("Starting with empty company representatives list.");
        }
    }
    
    public List<Internship> loadInternshipsFromCSV(String filename) {
        List<Internship> internships = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;
            int loadedCount = 0;
            
            while ((line = br.readLine()) != null) {
                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                // Split by COMMA
                String[] data = line.split(",");
                
                if (data.length >= 10) {
                    try {
                        String title = data[0].trim();
                        String description = data[1].trim();
                        String level = data[2].trim();
                        String preferredMajor = data[3].trim();
                        String status = data[4].trim();
                        LocalDate openDate = LocalDate.parse(data[5].trim());
                        LocalDate closeDate = LocalDate.parse(data[6].trim());
                        String companyName = data[7].trim();
                        int slots = Integer.parseInt(data[8].trim());
                        boolean visibility = Boolean.parseBoolean(data[9].trim());
                        
                        Internship internship = new Internship(
                            title, description, level, preferredMajor, status,
                            openDate, closeDate, companyName, slots
                        );
                        internship.setVisibility(visibility);
                        
                        internships.add(internship);
                        loadedCount++;
                        
                    } catch (Exception e) {
                        System.out.println("Error parsing internship data: " + line + " - " + e.getMessage());
                    }
                } else {
                    System.out.println("Invalid internship data format: " + line);
                }
            }
            System.out.println("Successfully loaded " + loadedCount + " internships");
            
        } catch (IOException e) {
            System.out.println("Internship file not found: " + filename);
        }
        
        return internships;
    }
}