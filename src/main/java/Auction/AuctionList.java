package Auction;

import User.RegisteredUsers;
import User.User;
import Services.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

public class AuctionList implements Auctionable {

    Set<Auction> auctionList;

    public AuctionList() {
        auctionList = new HashSet<Auction>();
    }

    public void addAuction(Auction auction) throws Exception{
        validateAuction(auction);
        auctionList.add(auction);
    }

    public Auction findByID(int auctionID) {
        for (Auction auction : auctionList) {
            if (auction.getAuctionID() == auctionID)
                return auction;
        }
        return null;
    }

    public void handleAuctionEvents(long now) {
        for (Auction auction : auctionList) {
            ZonedDateTime zdtStartTime = ZonedDateTime.of(auction.getStartTime(), ZoneId.systemDefault());
            long startDateInMilli = zdtStartTime.toInstant().toEpochMilli();

            if(startDateInMilli <= now){
                auction.onStart();
            }

            ZonedDateTime zdtEndTime = ZonedDateTime.of(auction.getEndTime(), ZoneId.systemDefault());
            long endDateInMilli = zdtEndTime.toInstant().toEpochMilli();

            if(endDateInMilli <= now){
                auction.onClose();
            }
        }
    }

    private void validateAuction(Auction auction) throws Exception{
        RegisteredUsers userList = RegisteredUsers.getInstance();
        User foundUser = auction.getSeller();
        if (!foundUser.getIsLoggedIn()){
            throw new Exception("User not authenticated");
        }
        if (!foundUser.getIsSeller()){
            throw new Exception("User is not a Seller");
        }
        if (auction.getStartPrice() <= 0.0 ){
            throw new Exception("Starting Price should be more than zero");
        }
        LocalDateTime now = LocalDateTime.now();
        if (auction.getStartTime().isBefore(now)){
            throw new Exception("Starting Time should be in the future!");
        }
        if (!auction.getStartTime().isBefore(auction.getEndTime())){
            throw new Exception("Starting Time should be before End Time");
        }
    }
}
