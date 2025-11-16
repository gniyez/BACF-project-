package project;

/**
 * Interface defining login-related operations for users.
 */
public interface LogIn{
    /**
     * Attempts to log in a user with the given credentials.
     *
     * @param userID   the user ID (usually email)
     * @param password the user's password
     * @return true if login succeeds, false otherwise
     */
    boolean login(String userID, String password);
    /**
     * Logs out the current user.
     */
    void logout();
    /**
     * Changes the password of a user.
     *
     * @param userID      the user ID
     * @param oldPassword the current password
     * @param newPassword the new password
     */
    void changePassword(String userID, String oldPassword, String newPassword);
}
