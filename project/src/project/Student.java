package project;
/**
 * Represents a student user in the internship management system.
 * Inherits from the User class and includes additional attributes
 * A student has a major, year of study, and a maximum number of applications allowed.
 */
public class Student extends User{
    private String major;
    private int yearOfStudy;
    private final int maxApps = 3;

    /**
     * Constructs a new Student with specified ID, name, major, and year of study.
     * @param studentID the student matriculation number.
     * @param name the full name of the student.
     * @param major the major of the student.
     * @param yearOfStudy the current year of study.
     */
    public Student(String studentID, String name, String major, int yearOfStudy){
        super(studentID, name);
        this.major = major;
        this.yearOfStudy = yearOfStudy;  
    }

    /**
     * returns the major of the student.
     * 
     * @return major of the student.
     */
    public String getMajor(){
        return major;
    }
    /**
     * updates the major of the student
     * 
     * @param major new major to set
     */
    public void setMajor(String major){
        this.major = major;
    }

    /**
     * returns the current year of study of the student.
     * 
     * @return year of study of the student.
     */
    public int getYearOfStudy(){
        return yearOfStudy;
    }

    /**
     * updates the year of study of the student
     * 
     * @param yearOfStudy new year of study to set
     */
    public void setYearOfStudy(int yearOfStudy){
        this.yearOfStudy = yearOfStudy;
    }

    /**
     * returns the maximum number of applications allowed for the student.
     * 
     * @return maximum number of applications allowed.
     */
    public int getMaxApps() {
        return maxApps;
    }
    
    /**
     * Checks whether this student can apply for a given internship level
     * Students in year 1-2 can only apply for BASIC level internships,
     * while students in year 3-4 can apply for any level.
     * 
     * @param internshipLevel the level of the internship to check
     * @return true if the student can apply for the given level, false otherwise
     */
    public boolean canApplyForLevel(String internshipLevel) {
        if (yearOfStudy <= 2) {
            return "BASIC".equalsIgnoreCase(internshipLevel);
        } else {
            return true; //Year 3-4 can apply for any level
        }
    }
    /**
    * Displays the role of this user.
    * This implementation prints a simple message indicating that this user is a student.
    */
    @Override
    public void displayRole(){
        System.out.println("I am a student");
    }

}
