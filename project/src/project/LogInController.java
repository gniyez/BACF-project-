package project;

import java.util.List;

/**
 * Controller that handles login, logout, and password changes.
 * Implements the LogIn interface.
 */
public class LogInController implements LogIn {
    private final List<User> users;
    private User currentUser;

    /**
     * Constructs a new LogInController with the given list of users.
     * 
     * @param users the list of users that can log in to the system
     */
    public LogInController(List<User> users){
        this.users=users;
        this.currentUser=null;
    }
    /**
     * Attempts to log in a user with the given user ID and password.
     * For company representatives, their account must be approved
     * before they are allowed to log in.
     *
     * @param userID  the user ID entered by the user
     * @param password the password entered by the user
     * @return true if login is successful, false otherwise
     */
    @Override
    public boolean login(String userID, String password){
    	User foundUser = null;
    	for(User user:users){
            if(user.getUserID().equals(userID)){
                foundUser = user;
                break;
            }
        }
        
        if (foundUser == null) {
            System.out.println("Login failed. Invalid user ID: " + userID);
            return false;
        }
        
        //User exists, now check password
        if (foundUser.getPassword().equals(password)){
            //Check if companyrep account is already approved
            if (foundUser instanceof CompanyRepresentative rep) {
                if (!rep.isApproved()) {
                    System.out.println("Login failed. Your company representative account is pending approval from Career Center Staff.");
                    return false;
                }
            }
            
            currentUser = foundUser;
            System.out.println("Login successful. Welcome, "+foundUser.getName()+"!");
            return true;
        } else {
            System.out.println("Login failed. Incorrect password.");
            return false;
        }
    }
    /**
     * Logs out the currently logged-in user, if any.
     * Prints a message to indicate the result.
     */
    @Override
    public void logout(){
        if(currentUser!=null){
            System.out.println("User "+currentUser.getName()+" logged out.");
            currentUser=null;
        } else {
            System.out.println("No user is currently logged in.");
        }
    }
    /**
     * Changes the password for the user with the given user ID.
     * Current password must match before updating password.
     * @param userID      the user ID of the user changing their password
     * @param oldPassword the current password of the user
     * @param newPassword the new password to set
     */
    @Override
    public void changePassword(String userID, String oldPassword, String newPassword){
        for(User user : users){
            if(user.getUserID().equals(userID)) {
                if(user.getPassword().equals(oldPassword)){                 
                    user.setPassword(newPassword);
                    System.out.println("Password changed successfully for user " + user.getName() + ".");
                    return;
                } else {
                    System.out.println("Password change failed. Incorrect current password.");
                    return;
                }
            }
        }      
        System.out.println("Password change failed. User not found: " + userID);
    }
    
    /**
     * Returns the currently logged-in user.
     * 
     * @return the current user, or null if no user is logged in
     */
    public User getCurrentUser(){ //Later use to route them to their UI
        return currentUser;
    }
}
