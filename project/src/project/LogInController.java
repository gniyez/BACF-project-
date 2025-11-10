package project;

import java.util.List;

public class LogInController implements LogIn {
    private List<User> users;
    private User currentUser;

    public LogInController(List<User> users){
        this.users=users;
        this.currentUser=null;
    }

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
        
        // User exists, now check password
        if (foundUser.getPassword().equals(password)){
            //Check if companyrep account is already approved
            if (foundUser instanceof CompanyRepresentative) {
                CompanyRepresentative rep = (CompanyRepresentative) foundUser;
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

    public void logout(){
        if(currentUser!=null){
            System.out.println("User "+currentUser.getName()+" logged out.");
            currentUser=null;
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

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
    
    public User getCurrentUser(){ // Later use to route them to their UI
        return currentUser;
    }
}
