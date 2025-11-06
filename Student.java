public class Student extends User{
    private String major;
    private int yearOfStudy;
    private int maxApps = 3;

    public Student(String userID, String name, String password, String major, int yearOfStudy){
        super(userID, name, password);
        this.major = major;
        this.yearOfStudy = yearOfStudy;
        
    }
    public String getStudentID(){
        return studentID;
    }
    public String getStudentID() {
           return getUserID();
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

    public void displayRole(){
        System.out.println("I am a student");
    }

}
