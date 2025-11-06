public class Application{
    
    private Student student;
    private Internship internship;
    private String status; 
    public Application(Student student ,Internship internship,String status){
        this.student=student;
        this.internship=internship;
        this.status='pending';
    }
    public Student getStudent(){
        return student;
    }
    public Internship getInternship(){
        return internship;
    }
    public getStatus(){
        return status;
    }
    public setStatus(String status){
        this.status = status;
    }
}