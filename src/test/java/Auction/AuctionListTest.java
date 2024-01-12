package Auction;

import User.RegisteredUsers;
import User.User;
import junit.framework.TestCase;
import org.junit.*;
import org.junit.Assert;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class AuctionListTest {

    Auction auction1;
    User user1;
    RegisteredUsers userList;

    int auctionID;

    @Before
    public void setUp() {
        String sellerUserName = "sauron1";
        String itemName = "Cookies";
        double startPrice = 2.00;
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusDays(1);
        auctionID = 1;

        String firstName = "Sauron";
        String lastName = "Irons";
        String email = "sauron@gmail.com";
        String userName = "sauron1";
        String password = "123";

        user1 = new User(firstName, lastName, email, userName, password);
        auction1 = new Auction(user1, itemName, startPrice, startTime, endTime, auctionID, Auction.Category.OTHER);

        userList =  RegisteredUsers.getInstance();
        userList.registerUser(user1);
    }

    @After
    public void tearDown() throws Exception {
        user1.logout();
        user1.revokeSeller();
        userList.removeUser(user1.getUserName());
    }

    @Test
    public void createAuctionSucceedsWithValidator(){
        AuctionList auctionList =  new AuctionList();
        user1.login();
        user1.becomeSeller();
        try{
            auctionList.addAuction(auction1);
            Auction foundAuction = auctionList.findByID(auctionID);
            Assert.assertNotNull(foundAuction);
        } catch (Exception e){
            Assert.fail();
        }
    }

    @Test
    public void createAuctionThrowsExceptionWhenUserIsNotAuthenticated(){
        AuctionList auctionList =  new AuctionList();
        try{
            auctionList.addAuction(auction1);
            Assert.fail();
        } catch (Exception e){
            Assert.assertEquals(e.getMessage(), "User not authenticated");
        }

    }

    @Test
    public void createAuctionThrowsExceptionWhenUserIsNotASeller(){
        AuctionList auctionList =  new AuctionList();
        user1.login();
        try{
            auctionList.addAuction(auction1);
            Assert.fail();
        } catch (Exception e){
            Assert.assertEquals(e.getMessage(), "User is not a Seller");
        }

    }

    @Test
    public void createAuctionThrowsExceptionWhenStartingPriceIsEqualOrLessThanZero(){
        AuctionList auctionList =  new AuctionList();
        user1.login();
        user1.becomeSeller();
        Auction auction2 = new Auction(auction1.getSeller(),auction1.getItemName(),0.0, auction1.getStartTime(),auction1.getEndTime(),auction1.getAuctionID()+1, Auction.Category.OTHER);
        try{
            auctionList.addAuction(auction2);
            Assert.fail();
        } catch (Exception e){
            Assert.assertEquals(e.getMessage(), "Starting Price should be more than zero");
        }
    }

    @Test
    public void createAuctionThrowsExceptionWhenStartingDateIsBeforeNow(){
        AuctionList auctionList =  new AuctionList();
        user1.login();
        user1.becomeSeller();
        Auction auction2 = new Auction(auction1.getSeller(),auction1.getItemName(),auction1.getStartPrice(), auction1.getStartTime().plusDays(-1),auction1.getEndTime(),auction1.getAuctionID()+1, Auction.Category.OTHER);
        try{
            auctionList.addAuction(auction2);
            Assert.fail();
        } catch (Exception e){
            Assert.assertEquals(e.getMessage(), "Starting Time should be in the future!");
        }
    }

    @Test
    public void createAuctionThrowsExceptionWhenStartingDateIsAfterEndDate(){
        AuctionList auctionList =  new AuctionList();
        user1.login();
        user1.becomeSeller();
        Auction auction2 = new Auction(auction1.getSeller(),auction1.getItemName(),auction1.getStartPrice(), auction1.getStartTime(),auction1.getEndTime().plusDays(-2),auction1.getAuctionID()+1, Auction.Category.OTHER);
        try{
            auctionList.addAuction(auction2);
            Assert.fail();
        } catch (Exception e){
            Assert.assertEquals(e.getMessage(), "Starting Time should be before End Time");
        }
    }

    @Test
    public void handleAuctionEventWillStartAnAuctionEventOnTime() {
        LocalDateTime startTime = LocalDateTime.now().plusSeconds(5);
        Auction auction3 = new Auction(user1, "Meg's Bags of Money", 50.0, startTime, startTime.plusSeconds(5), auctionID, Auction.Category.OTHER);
        AuctionList auctionList =  new AuctionList();
        try {
            user1.login();
            user1.becomeSeller();
            auctionList.addAuction(auction3);
        }catch (Exception e){
            Assert.fail("we should have been able to insert an auction in the auction list" + e.getMessage());
        }

        ZonedDateTime zdt = ZonedDateTime.of(startTime.plusSeconds(3), ZoneId.systemDefault());
        long date = zdt.toInstant().toEpochMilli();

        auctionList.handleAuctionEvents(date);
        Assert.assertEquals(Auction.State.OPEN, auction3.getAuctionState());
    }

    @Test
    public void handleAuctionEventWillCloseAnAuctionEventOnTime() {
        LocalDateTime startTime = LocalDateTime.now().plusSeconds(5);
        Auction auction3 = new Auction(user1, "Meg's Bags of Money", 50.0, startTime, startTime.plusSeconds(5), auctionID, Auction.Category.OTHER);
        AuctionList auctionList =  new AuctionList();
        try {
            user1.login();
            user1.becomeSeller();
            auctionList.addAuction(auction3);
        }catch (Exception e){
            Assert.fail("we should have been able to insert an auction in the auction list" + e.getMessage());
        }

        ZonedDateTime zdt = ZonedDateTime.of(startTime.plusSeconds(11), ZoneId.systemDefault());
        long date = zdt.toInstant().toEpochMilli();

        auctionList.handleAuctionEvents(date);
        Assert.assertEquals(Auction.State.CLOSED, auction3.getAuctionState());
    }

    @Test
    public void handleAuctionEventWillNotStartAnAuctionEventBeforeTime() {
        LocalDateTime startTime = LocalDateTime.now().plusSeconds(5);
        Auction auction3 = new Auction(user1, "Meg's Bags of Money", 50.0, startTime, startTime.plusSeconds(5), auctionID, Auction.Category.OTHER);
        AuctionList auctionList =  new AuctionList();
        try {
            user1.login();
            user1.becomeSeller();
            auctionList.addAuction(auction3);
        }catch (Exception e){
            Assert.fail("we should have been able to insert an auction in the auction list" + e.getMessage());
        }

        ZonedDateTime zdt = ZonedDateTime.of(startTime.plusSeconds(-1), ZoneId.systemDefault());
        long date = zdt.toInstant().toEpochMilli();

        auctionList.handleAuctionEvents(date);
        Assert.assertEquals(Auction.State.NOTSTARTED, auction3.getAuctionState());
    }

}