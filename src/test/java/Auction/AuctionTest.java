package Auction;

import Services.*;
import User.User;
import org.junit.*;
import User.RegisteredUsers;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;

public class AuctionTest {

    Auction auction1;

    User user1;
    User user2;
    RegisteredUsers userList;

    int auctionID;

    AuctionLogger auctionLogger = AuctionLogger.getInstance();

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

        String firstName2 = "Meg";
        String lastName2 = "Fryling";
        String email2 = "megfryling@gmail.com";
        String userName2 = "mfryling";
        String password2 = "1234";

        user2 = new User(firstName2, lastName2, email2, userName2, password2);
        userList.registerUser(user2);

        String itemName = "Cookies";
        double startPrice = 2.00;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusDays(1);
        int auctionID = 1;

        auction1 = new Auction(user1, itemName, startPrice, startTime, endTime, auctionID, Auction.Category.OTHER);

        Assert.assertEquals(user1, auction1.getSeller());
        Assert.assertEquals(itemName, auction1.getItemName());
        Assert.assertEquals(startPrice, auction1.getStartPrice(), 0.01);
        Assert.assertEquals(startTime, auction1.getStartTime());
        Assert.assertEquals(endTime, auction1.getEndTime());
        Assert.assertEquals(auctionID, auction1.getAuctionID());
    }

    @After
    public void tearDown() {
        userList.removeUser(user1.getUserName());
        userList.removeUser(user2.getUserName());
        auctionLogger.clearLog("logfile.txt");
    }

    @Test
    public void startAuctionChangesTheAuctionStateToStart(){
        Assert.assertEquals(Auction.State.NOTSTARTED, auction1.getAuctionState());
        auction1.onStart();
        Assert.assertEquals(Auction.State.OPEN, auction1.getAuctionState());
    }

    @Test
    public void openAuctionAcceptsBids(){
        auction1.onStart();
        double newBid = 2.00;
        try {
            user2.login();
            auction1.bid(user2, newBid);
            Assert.assertEquals(newBid, auction1.getHighestBid(), 0.01);
        }catch (Exception e) {
            Assert.fail("This should not have thrown an exception, " + e.getMessage());
        }

    }

    @Test
    public void nonOpenAuctionDoesNotAcceptBids(){
        double newBid = 2.00;
        try{
            auction1.bid(user1, newBid);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals("One cannot bid on non-Open auctions", e.getMessage());
        }
    }

    @Test
    public void startedAuctionOnlyAcceptsBidsFromAuthenticatedUser() {
        double newBid = 2.00;
        try{
            user2.login();
            auction1.onStart();
            auction1.bid(user2, newBid);
            Assert.assertEquals(newBid, auction1.getHighestBid(), 0.01);
        } catch (Exception e){
            Assert.fail("This shouldn't have thrown an exception");
        }
    }

    @Test
    public void startedAuctionDoesNotAcceptBidsFromUnauthenticatedUser() {
        double newBid = 2.00;
        try{
            auction1.onStart();
            auction1.bid(user1, newBid);
            Assert.fail("This should have thrown an exception");
        } catch (Exception e){
            Assert.assertEquals("Bidder must be authenticated", e.getMessage());
        }
    }

    @Test
    public void startedAuctionCanBeClosed() {
        auction1.onStart();
        auction1.onClose();
        Assert.assertEquals(Auction.State.CLOSED, auction1.getAuctionState());
    }

    @Test
    public void closedAuctionNotifiesSellerIfYesSell() {
        auction1.onStart();
        try {
            user2.login();
            auction1.bid(user2, 3.00);
            PostOffice po = PostOffice.getInstance();
            auction1.onClose();

            boolean foundEmail = po.doesLogContain(user1.getEmail(), "You sold your item hero!  way to go.");
            Assert.assertTrue(foundEmail);

        }catch (Exception e){
            Assert.fail("this should not have thrown an exception, " + e.getMessage());
        }
    }

    @Test
    public void closedAuctionNotifiesSellerIfNoSell() {
        auction1.onStart();
        try {
            PostOffice po = PostOffice.getInstance();
            auction1.onClose();

            boolean foundEmail = po.doesLogContain(user1.getEmail(), "Your item didn't sell.");
            Assert.assertTrue(foundEmail);

        }catch (Exception e){
            Assert.fail("this should not have thrown an exception, " + e.getMessage());
        }
    }

    @Test
    public void closedAuctionNotifiesWinnerIfHighestBid() {
        auction1.onStart();
        try {
            user2.login();
            auction1.bid(user2, 3.00);
            PostOffice po = PostOffice.getInstance();
            auction1.onClose();

            boolean foundEmail = po.doesLogContain(user2.getEmail(), "You won your item hero!  way to go.");
            Assert.assertTrue(foundEmail);

        }catch (Exception e){
            Assert.fail("this should not have thrown an exception, " + e.getMessage());
        }
    }

    @Test
    public void bidIsRejectedIfLowerThanStartingPrice() {
        auction1.onStart();
        try {
            user2.login();
            auction1.bid(user2, 1.00);
            Assert.fail("This should have thrown an exception");
        }catch (Exception e){
            Assert.assertEquals("Bid price is less than starting price", e.getMessage());
        }
    }

    @Test
    public void bidIsRejectedIfLowerThanHighestBid() {
        auction1.onStart();
        try {
            user2.login();
            auction1.bid(user2, 4.00);
            auction1.bid(user2, 3.00);
            Assert.fail("This should have thrown an exception");
        }catch (Exception e){
            Assert.assertEquals("Bid price must be more higher than the most highest bid", e.getMessage());
        }
    }

    @Test
    public void bidIsRejectedIfEqualToTheHighestBid() {
        auction1.onStart();
        try {
            user2.login();
            auction1.bid(user2, 4.00);
            auction1.bid(user2, 4.00);
            Assert.fail("This should have thrown an exception");
        }catch (Exception e){
            Assert.assertEquals("Bid price must be more higher than the most highest bid", e.getMessage());
        }
    }

    @Test
    public void bidIsRejectedIfBuyerIsTheSeller() {
        auction1.onStart();
        try {
            user1.login();
            auction1.bid(user1, 4.00);
            Assert.fail("This should have thrown an exception");
        }catch (Exception e){
            Assert.assertEquals("Seller cannot bid on their own item", e.getMessage());
        }
    }

    @Test
    public void bidIsAcceptedIfBuyerIsNotTheSeller() {
        auction1.onStart();
        try {
            user2.login();
            auction1.bid(user2, 4.00);
        }catch (Exception e){
            Assert.fail("This should not have thrown an exception");
        }
    }

    @Test
    public void transactionFeeIs2Percent() {
        auction1.onStart();
        try {
            user2.login();
            auction1.bid(user2, 4.00);
        }catch (Exception e){
            Assert.fail("This should not have thrown an exception");
        }
        auction1.onClose();
        Assert.assertEquals(3.92, auction1.getSellerFinalAmount(),.001);
    }

    @Test
    public void testFeesForDownloadableSoftware(){
        LocalDateTime startDate = LocalDateTime.now();
        Auction softwareAuction = new Auction(user1, "Software", 0.0, startDate, startDate.plusDays(1), 2, Auction.Category.DLSOFTWARE);
        softwareAuction.onStart();

        try {
            user2.login();
            softwareAuction.bid(user2, 4.00);
        } catch (Exception e){
            Assert.fail("This should not have thrown an exception");
        }

        softwareAuction.onClose();
        Assert.assertEquals(4.00, softwareAuction.getBuyerFinalAmount(),.001);
    }

    @Test
    public void testFeesForOther(){
        LocalDateTime startDate = LocalDateTime.now();
        Auction softwareAuction = new Auction(user1, "Non Software", 0.0, startDate, startDate.plusDays(1), 2, Auction.Category.OTHER);
        softwareAuction.onStart();

        try {
            user2.login();
            softwareAuction.bid(user2, 4.00);
        } catch (Exception e){
            Assert.fail("This should not have thrown an exception");
        }

        softwareAuction.onClose();
        Assert.assertEquals(14.00, softwareAuction.getBuyerFinalAmount(),.001);
    }

    @Test
    public void testFeesForCarUnder50k(){
        LocalDateTime startDate = LocalDateTime.now();
        Auction carAuction = new Auction(user1, "Ford F150", 0.0, startDate, startDate.plusDays(1), 2, Auction.Category.CAR);
        carAuction.onStart();

        try {
            user2.login();
            carAuction.bid(user2, 4.00);
        } catch (Exception e){
            Assert.fail("This should not have thrown an exception");
        }

        carAuction.onClose();
        Assert.assertEquals(1004.00, carAuction.getBuyerFinalAmount(),.001);

    }

    @Test
    public void testFeesForCarOver50k(){
        LocalDateTime startDate = LocalDateTime.now();
        Auction carAuction = new Auction(user1, "Tesla", 0.0, startDate, startDate.plusDays(1), 2, Auction.Category.CAR);
        carAuction.onStart();
        double bidAmount = 50000.00;
        try {
            user2.login();
            carAuction.bid(user2, bidAmount);
        } catch (Exception e){
            Assert.fail("This should not have thrown an exception");
        }

        carAuction.onClose();
        Assert.assertEquals(bidAmount*1.04+1000.00, carAuction.getBuyerFinalAmount(),.001);

    }
    @Test
    public void testLoggingCarSale() {
        LocalDateTime startDate = LocalDateTime.now();
        Auction carAuction = new Auction(user1, "Tesla", 0.0, startDate, startDate.plusDays(1), 2, Auction.Category.CAR);
        carAuction.onStart();
        try {
            user2.login();
            carAuction.bid(user2, 3.00);

            carAuction.onClose();

            boolean foundMessage = auctionLogger.findMessage("logfile.txt","Car sale!");
            Assert.assertTrue(foundMessage);

        } catch (Exception e){
            Assert.fail("this should not have thrown an exception, " + e.getMessage());
        }
    }

    @Test
    public void testLoggingSaleOver10K() {
        LocalDateTime startDate = LocalDateTime.now();
        Auction carAuction = new Auction(user1, "Tesla", 5.0, startDate, startDate.plusDays(1), 2, Auction.Category.OTHER);
        carAuction.onStart();
        try {
            user2.login();
            carAuction.bid(user2, 10000.00);

            carAuction.onClose();

            boolean foundMessageSoldOver10k = auctionLogger.findMessage("logfile.txt","Big sale (to everyone except money bags meg!)!");
            Assert.assertTrue(foundMessageSoldOver10k);

        } catch (Exception e){
            Assert.fail("this should not have thrown an exception, " + e.getMessage());
        }
    }

    @Test
    public void testLoggingCarSaleOver10K() {
        LocalDateTime startDate = LocalDateTime.now();
        Auction carAuction = new Auction(user1, "Tesla", 5.0, startDate, startDate.plusDays(1), 2, Auction.Category.CAR);
        carAuction.onStart();
        try {
            user2.login();
            carAuction.bid(user2, 50000.00);

            carAuction.onClose();

            boolean foundMessageCarSold = auctionLogger.findMessage("logfile.txt","Car sale!");
            Assert.assertTrue(foundMessageCarSold);
            boolean foundMessageSoldOver10k = auctionLogger.findMessage("logfile.txt","Big sale (to everyone except money bags meg!)!");
            Assert.assertTrue(foundMessageSoldOver10k);

        } catch (Exception e){
            Assert.fail("this should not have thrown an exception, " + e.getMessage());
        }
    }

    @Test
    public void testNoLogging() {
        LocalDateTime startDate = LocalDateTime.now();
        Auction carAuction = new Auction(user1, "Tesla", 5.0, startDate, startDate.plusDays(1), 2, Auction.Category.OTHER);
        carAuction.onStart();
        try {
            user2.login();
            carAuction.bid(user2, 990.00);

            carAuction.onClose();

            auctionLogger.log("logfile.txt", "Starting log");

            boolean foundMessageCarSold = auctionLogger.findMessage("logfile.txt","Car sale!");
            Assert.assertFalse(foundMessageCarSold);
            boolean foundMessageSoldOver10k = auctionLogger.findMessage("logfile.txt","Big sale (to everyone except money bags meg!)!");
            Assert.assertFalse(foundMessageSoldOver10k);

        } catch (Exception e){
            Assert.fail("this should not have thrown an exception, " + e.getMessage());
        }
    }

}