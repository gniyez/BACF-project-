package project;

import java.util.List;
import java.util.stream.Collectors;

public interface FilterOptions {
    
    //Default method implementation
    default List<Internship> filter(List<Internship> internships, String criteria, String value) {
        return internships.stream().filter(internship -> {
            switch(criteria.toLowerCase()) {
                case "status":
                    return String.valueOf(internship.getInternshipStatus()).equalsIgnoreCase(value);
                case "preferredmajors":
                    return String.valueOf(internship.getPreferredMajor()).equalsIgnoreCase(value);
                case "internshiplevel":
                    return String.valueOf(internship.getLevel()).equalsIgnoreCase(value);
                case "closingdate":
                    return String.valueOf(internship.getCloseDate()).equalsIgnoreCase(value);
                case "opendate":
                    return String.valueOf(internship.getOpenDate()).equalsIgnoreCase(value);
                case "companyname":
                    return String.valueOf(internship.getCompanyName()).equalsIgnoreCase(value);
                case "visibility":
                    return String.valueOf(internship.getVisibility()).equalsIgnoreCase(value);
                default:
                    return true; //no filter if criteria unknown
            }
        }).collect(Collectors.toList());
    }
}