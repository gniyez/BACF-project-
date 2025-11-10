package project;

public class Student extends User{
    private String major;
    private int yearOfStudy;
    private final int maxApps = 3;

    public Student(String studentID, String name, String major, int yearOfStudy){
        super(studentID, name);
        this.major = major;
        this.yearOfStudy = yearOfStudy;
        
    }

    public String getMajor(){
        return major;
    }

    public void setMajor(String major){
        this.major = major;
    }

    public int getYearOfStudy(){
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy){
        this.yearOfStudy = yearOfStudy;
    }
    public int getMaxApps() {
        return maxApps;
    }
    
    public boolean canApplyForLevel(String internshipLevel) {
        if (yearOfStudy <= 2) {
            return "BASIC".equalsIgnoreCase(internshipLevel);
        } else {
            return true; //Year 3-4 can apply for any level
        }
    }

    public void displayRole(){
        System.out.println("I am a student");
    }

}
