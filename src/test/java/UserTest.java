import User.User;
import org.junit.*;

public class UserTest {

    @Test
    public void constructorCreatesUserCorrectly() {
        String firstName = "Sauron";
        String lastName = "Irons";
        String email = "sauron@gmail.com";
        String userName = "sauron1";
        String password = "123";

        User user1 = new User(firstName, lastName, email, userName, password);

        Assert.assertEquals(firstName, user1.getFirstName());
        Assert.assertEquals(lastName, user1.getLastName());
        Assert.assertEquals(email, user1.getEmail());
        Assert.assertEquals(userName, user1.getUserName());
        Assert.assertEquals(password, user1.getPassword());
        Assert.assertFalse(user1.getIsLoggedIn());
        Assert.assertFalse(user1.getIsSeller());
    }

    @Test
    public void loginSetsIsLoggedInFlagToTrue() {
        String firstName = "Sauron";
        String lastName = "Irons";
        String email = "sauron@gmail.com";
        String userName = "sauron1";
        String password = "123";

        User user1 = new User(firstName, lastName, email, userName, password);
        user1.login();

        Assert.assertTrue(user1.getIsLoggedIn());
    }

    @Test
    public void logoutSetsIsLoggedInFlagToFalse() {
        String firstName = "Sauron";
        String lastName = "Irons";
        String email = "sauron@gmail.com";
        String userName = "sauron1";
        String password = "123";

        User user1 = new User(firstName, lastName, email, userName, password);
        user1.login();
        user1.logout();

        Assert.assertFalse(user1.getIsLoggedIn());
    }

    @Test
    public void becomeSellerSetsUserIsSellerToTrue() {
        String firstName = "Sauron";
        String lastName = "Irons";
        String email = "sauron@gmail.com";
        String userName = "sauron1";
        String password = "123";

        User user1 = new User(firstName, lastName, email, userName, password);
        user1.becomeSeller();

        Assert.assertTrue(user1.getIsSeller());
    }

    @Test
    public void revokeSellerSetsUserIsSellerToFalse() {
        String firstName = "Sauron";
        String lastName = "Irons";
        String email = "sauron@gmail.com";
        String userName = "sauron1";
        String password = "123";

        User user1 = new User(firstName, lastName, email, userName, password);
        user1.becomeSeller();
        user1.revokeSeller();

        Assert.assertFalse(user1.getIsSeller());
    }


}