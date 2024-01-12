package Auction;

import java.time.LocalDateTime;

import CloseProcessors.*;
import Notifiers.*;
import User.User;

public class Auction {

    enum State {
        NOTSTARTED,
        OPEN,
        CLOSED
    }

    public enum Category {
        OTHER,
        CAR,
        DLSOFTWARE
    }

    private User seller;
    private String itemName;
    private double startPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int auctionID;

    private double highestBid;

    private User currentBidder;

    private State state;

    private Category category;
    private double buyerFinalAmount;
    private double sellerFinalAmount;

    public Auction(User seller, String itemName, double startPrice, LocalDateTime startTime, LocalDateTime endTime, int auctionID, Category category) {
        this.seller = seller;
        this.itemName = itemName;
        this.startPrice = startPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.auctionID = auctionID;
        this.state = State.NOTSTARTED;
        this.category = category;
    }

    public User getSeller() { return seller; }

    public String getItemName() { return itemName; }

    public double getStartPrice() { return startPrice; }

    public LocalDateTime getStartTime() { return startTime; }

    public LocalDateTime getEndTime() { return endTime; }

    public int getAuctionID() { return auctionID; }

    public State getAuctionState() { return state; }

    public double getHighestBid() { return highestBid; }

    public User getCurrentBidder() { return currentBidder; }
    public double getSellerFinalAmount() { return sellerFinalAmount; }
    public double getBuyerFinalAmount() { return buyerFinalAmount; }

    public Category getCategory() { return category; }

    public void setBuyerFinalAmount(double amount) { buyerFinalAmount = amount; }

    public void setSellerFinalAmount(double amount) { sellerFinalAmount = amount; }

    public void onStart() {
        this.state = State.OPEN;
    }

    public void onClose() {
        this.state = State.CLOSED;
        CloseProcessorFactory.getInstance(this).process(this);
    }

    public void bid(User user, double bid) throws Exception {
        try {
            validateBid(user, bid);
            highestBid = bid;
            currentBidder = user;
        }catch (Exception e) {
            throw e;
        }
    }

    private void validateBid(User user, double bid) throws Exception {
        if (this.state != State.OPEN){
            throw new Exception("One cannot bid on non-Open auctions");
        }
        if (!user.getIsLoggedIn()) {
            throw new Exception("Bidder must be authenticated");
        }
        if (currentBidder == null){
            if (bid < startPrice) {
                throw new Exception("Bid price is less than starting price");
            }
        } else{
            if (bid <= highestBid) {
                throw new Exception("Bid price must be more higher than the most highest bid");
            }
        }
        if (user.getUserName().equals(this.seller.getUserName())){
            throw new Exception("Seller cannot bid on their own item");
        }
    }
}
