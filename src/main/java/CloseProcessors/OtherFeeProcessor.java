package CloseProcessors;

import Auction.Auction;

public class OtherFeeProcessor extends CloseProcessorDecorator {

    public OtherFeeProcessor(){

    }
    public OtherFeeProcessor(CloseProcessor feeProcessor){
        super(feeProcessor);
    }
    public void process(Auction auction) {
        double highestBid = auction.getHighestBid();

        auction.setBuyerFinalAmount(highestBid + 10);
        super.process(auction);
    }
}
