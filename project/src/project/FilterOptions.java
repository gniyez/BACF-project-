package project;

import java.util.List;
import java.util.stream.Collectors;
/**
 * Provides default filtering functionality for lists of internships.
 * Classes implementing this interface can use the {@code filter} method
 * to filter internships based on criteria such as status, level, major, dates, company, or visibility.
 */
public interface FilterOptions {
    /**
 * Filters a list of internships based on the given criteria and value.
 *
 * @param internships The list of internships to filter
 * @param criteria    The field to filter by
 * @param value       The value to match
 * @return A list of internships matching the criteria
 */

    //Default method implementation
    default List<Internship> filter(List<Internship> internships, String criteria, String value) {
        return internships.stream().filter(internship -> {
            return switch (criteria.toLowerCase()) {
                case "status" -> String.valueOf(internship.getInternshipStatus()).equalsIgnoreCase(value);
                case "preferredmajors" -> String.valueOf(internship.getPreferredMajor()).equalsIgnoreCase(value);
                case "internshiplevel" -> String.valueOf(internship.getLevel()).equalsIgnoreCase(value);
                case "closingdate" -> String.valueOf(internship.getCloseDate()).equalsIgnoreCase(value);
                case "opendate" -> String.valueOf(internship.getOpenDate()).equalsIgnoreCase(value);
                case "companyname" -> String.valueOf(internship.getCompanyName()).equalsIgnoreCase(value);
                case "visibility" -> String.valueOf(internship.getVisibility()).equalsIgnoreCase(value);
                default -> true;
            }; //no filter if criteria unknown
        }).collect(Collectors.toList());
    }
}