package CloseProcessors;

import Auction.Auction;
import Services.AuctionLogger;

public class LogProcessor extends CloseProcessorDecorator{
    public LogProcessor(){

    }
    public LogProcessor(CloseProcessor closeProcessor){
        super(closeProcessor);
    }
    public void process(Auction auction) {

        AuctionLogger auctionLogger = AuctionLogger.getInstance();

        if (auction.getCategory() == Auction.Category.CAR) {
            auctionLogger.log("logfile.txt", "Car sale!");
        }
        if(auction.getHighestBid() >= 10000.0){
            auctionLogger.log("logfile.txt", "Big sale (to everyone except money bags meg!)!");
        }
        super.process(auction);
    }
}
