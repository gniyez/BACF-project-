public interface StudentApplications{
    void applyInternship(Student student, Internship internship);
    void withdrawApplication(Student student, Application app);
    void acceptPlacement(Student student, Application app);
}