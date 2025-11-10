package project;

import java.time.LocalDate;

public class Internship {
    private String title;
    private String description;
    private String level;
    private String preferredMajor;
    private String status;
    private LocalDate openDate;
    private LocalDate closeDate;
    private String companyName;
    private boolean isVisible;
    private int slots;

    public Internship(String title, String description, String level, String preferredMajor, String status,
    		LocalDate openDate, LocalDate closeDate, String companyName, int slots){
        this.title = title;
        this.description = description;
        this.level = level;
        this.preferredMajor = preferredMajor;
        this.status = status;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.companyName = companyName;
        this.slots = Math.min(slots, 10);
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

    public LocalDate getOpenDate() {return openDate;}
    public void setOpenDate(LocalDate openDate) {this.openDate = openDate;}

    public LocalDate getCloseDate() {return closeDate;}
    public void setCloseDate(LocalDate closeDate) {this.closeDate = closeDate;}

    public String getCompanyName() {return companyName;}
    public void setCompanyName(String companyName) {this.companyName = companyName;}

    public boolean getVisibility() {return isVisible;}
    public void setVisibility(boolean isVisible) {this.isVisible = isVisible;}

    public int getSlots() {return slots;}
    public void setSlots(int slots) { this.slots = Math.min(slots, 10); }
    
    public boolean isOpenForApplication() {
        LocalDate today = LocalDate.now();
        return "APPROVED".equals(status) && 
               !today.isBefore(openDate) && 
               !today.isAfter(closeDate) &&
               slots > 0;
    }

    public boolean isVisibleToStudents() {
        return isVisible && "APPROVED".equals(status);
    }

}

