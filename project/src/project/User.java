package project;

/**
 * Abstract class representing a user in the internship management system.
 * Includes common attributes and methods for all user types.
 * 
 * @author Team BACF (Carin, Leyi, Pei Shan, Serene, Zhen Ying)
 * @version 1.0
 * @since 2025
 */

public abstract class User{
    private String userID;
    private String name;
    private String password= "password";

    /**
     * Constructor to initialize User object with userID and name.
     * 
     * @param userID Unique identifier for user
     * @param name Name of user
     */
    public User(String userID,String name){
        this.userID = userID;
        this.name = name;
    }
    
    public String getUserID(){
        return userID;
    }
    public void setUserID(String userID){
        this.userID=userID;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Abstract method to display the role of the user.
     * Implemented by concrete subclasses.
     */
    public abstract void displayRole();
}
