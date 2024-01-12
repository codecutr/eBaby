package User;

import java.util.HashSet;
import java.util.Set;


public class RegisteredUsers {

    Set<User> userList;
    private static RegisteredUsers myUsers;
    public static RegisteredUsers getInstance() {
        if(myUsers == null){
            myUsers = new RegisteredUsers();
        }
        return myUsers;
    }

    private RegisteredUsers() {
        userList = new HashSet<User>();
    }

    public void registerUser(User user) {
        userList.add(user);
    }

    public void removeUser (String userName){
        User foundUser = findByUserName(userName);

        if(foundUser != null){
            userList.remove(foundUser);
        }
    }

    public User findByUserName(String userName) {
        for (User user : userList) {
            if (user.getUserName().equals(userName))
                return user;
        }
        return null;
    }

    public void loginUser(String userName, String password) throws Exception {
        User foundUser = findByUserName(userName);
        if (foundUser != null) {
            if (password.equals(foundUser.getPassword())) {
                foundUser.login();
            } else {
                throw new Exception("Bad credentials");
            }
        } else {
            throw new Exception("Bad credentials");
        }
    }

    public void logoutUser(String userName) throws Exception {
        User foundUser = findByUserName(userName);

        if (foundUser != null) {
            foundUser.logout();
        } else {
            throw new Exception("User not found");
        }
    }

    public void makeUserSeller(String userName) throws Exception {
        User foundUser = findByUserName(userName);

        if (foundUser != null) {
            foundUser.becomeSeller();
        } else {
            throw new Exception("User not found");
        }
    }

    public void revokeUserSeller(String userName) throws Exception {
        User foundUser = findByUserName(userName);

        if (foundUser != null) {
            foundUser.revokeSeller();
        } else {
            throw new Exception("User not found");
        }
    }
}
