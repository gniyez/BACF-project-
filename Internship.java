import java.sql.Date;

public class Internship{
    private String title;
    private String description;
    private String level;
    private String preferredMajor;
    private String status;
    private Date openDate;
    private Date closeDate;
    private String companyName;
    private boolean isVisible;
    private int slots = 10;
   

    public Internship(String title, String description, String level, String preferredMajor, String status,
        Date openDate, Date closeDate, String companyName, int slots){
        this.title = title;
        this.description = description;
        this.level = level;
        this.preferredMajor = preferredMajor;
        this.status = status;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.companyName = companyName;
        
        if (slots > 10) {
            this.slots = 10;
        } else {
            this.slots = slots;
        }

    }

    public String getInternshipTitle(){return title;}
    public void setInternshipTitle(String title){this.title = title;}

    public String getInternshipDescription(){return description;}
    public void setInternshipDescription(String description){this.description = description;}

    public String getLevel() {return level;}
    public void setLevel(String level) {this.level = level;}

    public String getPreferredMajor() {return preferredMajor;}
    public void setPreferredMajor(String preferredMajor) {this.preferredMajor = preferredMajor;}

    public String getInternshipStatus(){return status;}
    public void setInternshipStatus(String status){this.status = status;}

    public Date getOpenDate() {return openDate;}
    public void setOpenDate(Date openDate) {this.openDate = openDate;}

    public Date getCloseDate() {return closeDate;}
    public void setCloseDate(Date closeDate) {this.closeDate = closeDate;}

    public String getCompanyName() {return companyName;}
    public void setCompanyName(String companyName) {this.companyName = companyName;}

    public boolean getVisibility() {return isVisible;}
    public void setVisibility(boolean isVisible) {this.isVisible = isVisible;}

    public int getSlots() {return slots;}
    public void setSlots(int slots) {
        if (slots > 10) {
            this.slots = 10;
        } else {
            this.slots = slots;
        }
    }
    public void setStatus(String status){
        this.status=status;
    }
    public String getStatus(){
         return status;
    }

}

