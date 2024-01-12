import Auction.Auction;
import User.RegisteredUsers;
import User.User;
import junit.framework.TestCase;
import org.junit.*;

import java.time.LocalDateTime;

public class RegisteredUsersTest {

    User user1;
    RegisteredUsers userList;

    @Before
    public void setUp() {

        String firstName = "Sauron";
        String lastName = "Irons";
        String email = "sauron@gmail.com";
        String userName = "sauron1";
        String password = "123";

        user1 = new User(firstName, lastName, email, userName, password);

        userList =  RegisteredUsers.getInstance();
        userList.registerUser(user1);
    }

    @After
    public void tearDown() {
        userList.removeUser(user1.getUserName());
    }

    @Test
    public void registerUserAddsUserToCollection()
    {
        User foundUser = userList.findByUserName(user1.getUserName());
        Assert.assertEquals (foundUser.getUserName(), user1.getUserName());
    }

    @Test
    public void removeRegisterUserRemovesUserFromCollection()
    {
        String userName = "sauron1";
        userList.removeUser(userName);
        User foundUser = userList.findByUserName(userName);
        Assert.assertEquals(null,foundUser);
    }

    @Test
    public void findUserByUserNameReturnsUserObject(){
        User foundUser = userList.findByUserName( user1.getUserName());
        Assert.assertEquals(foundUser.getUserName(),  user1.getUserName());
    }
    @Test
    public void findUserByUserNameReturnsNullWhenUserIsNotThere(){
        RegisteredUsers userList =  RegisteredUsers.getInstance();
        User foundUser = userList.findByUserName("badusername");
        Assert.assertEquals(foundUser, null);
    }

    @Test
    public void loginSetsIsLoggedInFlagToTrueOnUserObject() {
        try {
            userList.loginUser(user1.getUserName(), user1.getPassword());
            User foundUser = userList.findByUserName(user1.getUserName());
            Assert.assertTrue(foundUser.getIsLoggedIn());
        } catch (Exception e) {
            Assert.fail();
        }

    }

    @Test
    public void loginSetsIsLoggedInFlagToFalseWhenPasswordIsWrong() {
        try {
            userList.loginUser(user1.getUserName(), "badpassword");
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "Bad credentials");
        }

    }

    @Test
    public void loginSetsIsLoggedInFlagToFalseWhenUserNameDoesNotExist() {
        try {
            userList.loginUser("badusername", user1.getPassword());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "Bad credentials");
        }

    }

    @Test
    public void logoutSetsIsLoggedInFlagToFalseWhenUserLogsOut() {
        try {
            userList.loginUser(user1.getUserName(), user1.getPassword());
            userList.logoutUser(user1.getUserName());
            User foundUser = userList.findByUserName(user1.getUserName());
            Assert.assertFalse(foundUser.getIsLoggedIn());
        } catch (Exception e) {
            Assert.fail();
        }

    }
    @Test
    public void logoutThrowsAnExceptionWhenUserDoesNotExist() {
        String userName = "sauron2";
        RegisteredUsers userList =  RegisteredUsers.getInstance();
        try {
            userList.logoutUser(userName);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "User not found");
        }
    }

    @Test
    public void makeRegisteredUserSellerSetsIsSellerFlagToTrueOnUserObject() {
        try {
            userList.makeUserSeller(user1.getUserName());
            User foundUser = userList.findByUserName(user1.getUserName());
            Assert.assertTrue(foundUser.getIsSeller());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void makeRegisteredUserThrowsAnExceptionWhenUserDoesNotExist() {
        String userName = "sauron2";
        RegisteredUsers userList =  RegisteredUsers.getInstance();
        try {
            userList.makeUserSeller(userName);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "User not found");
        }
    }


    @Test
    public void revokeSellerFromRegisteredUserSellerSetsIsSellerFlagToFalseOnUserObject() {
        try {
            userList.makeUserSeller(user1.getUserName());
            userList.revokeUserSeller(user1.getUserName());
            User foundUser = userList.findByUserName(user1.getUserName());
            Assert.assertFalse(foundUser.getIsSeller());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void revokeUserSellerThrowsAnExceptionWhenUserDoesNotExist() {
        String userName = "sauron2";
        RegisteredUsers userList =  RegisteredUsers.getInstance();
        try {
            userList.revokeUserSeller(userName);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "User not found");
        }
    }

//    @Test
//    public void getUserListReturnsTheRegisteredUserList() {
//        RegisteredUsers userList = RegisteredUsers.getUserList();
//    }
}