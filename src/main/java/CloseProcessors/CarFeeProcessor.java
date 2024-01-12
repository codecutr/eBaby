package CloseProcessors;

import Auction.Auction;

public class CarFeeProcessor extends CloseProcessorDecorator {

    public CarFeeProcessor() {

    }
    public CarFeeProcessor(CloseProcessor feeProcessor){
        super(feeProcessor);
    }
    public void process(Auction auction) {
        double highestBid = auction.getHighestBid();

        if (highestBid >= 50000) {
            auction.setBuyerFinalAmount(highestBid * 1.04 + 1000);
        } else {
            auction.setBuyerFinalAmount(highestBid + 1000);
        }
        super.process(auction);
    }
}
