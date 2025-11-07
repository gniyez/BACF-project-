import java.io.*;
import java.sql.Date;
import java.util.*;

public class CSVLoader {

    // Load Users (Student, CompanyRep, Staff)
    public static List<User> loadUsers(String filePath) {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // skip header line
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1); // keep empty strings

                String type = parts[0].trim();
                String userID = parts[1].trim();
                String name = parts[2].trim();
                String password = parts[3].trim();

                if (type.equalsIgnoreCase("Student")) {
                    String major = parts[4].trim();
                    int year = Integer.parseInt(parts[5].trim());
                    users.add(new Student(userID, name, password, major, year));

                } else if (type.equalsIgnoreCase("CompanyRepresentative")) {
                    String companyID = parts[6].trim();
                    String companyName = parts[7].trim();
                    String department = parts[8].trim();
                    String position = parts[9].trim();
                    users.add(new CompanyRepresentative(userID, name, password, companyID, companyName, department, position));

                } else if (type.equalsIgnoreCase("CareerServiceStaff")) {
                    String staffID = parts[10].trim();
                    users.add(new CareerServiceStaff(userID, name, password, staffID, "Career Office"));
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading users CSV: " + e.getMessage());
        }
        return users;
    }

    // Load Internships
    public static List<Internship> loadInternships(String filePath) {
        List<Internship> internships = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);

                String title = parts[0].trim();
                String description = parts[1].trim();
                String level = parts[2].trim();
                String major = parts[3].trim();
                String status = parts[4].trim();
                Date openDate = Date.valueOf(parts[5].trim());
                Date closeDate = Date.valueOf(parts[6].trim());
                String companyName = parts[7].trim();
                int slots = Integer.parseInt(parts[8].trim());

                internships.add(new Internship(title, description, level, major, status, openDate, closeDate, companyName, slots));
            }
        } catch (Exception e) {
            System.out.println("Error reading internships CSV: " + e.getMessage());
        }
        return internships;
    }
}
