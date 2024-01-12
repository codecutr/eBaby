package CloseProcessors;

import Auction.Auction;

public class SoftwareFeeProcessor extends CloseProcessorDecorator {

    public SoftwareFeeProcessor() {
    }
    public SoftwareFeeProcessor(CloseProcessor feeProcessor){
        super(feeProcessor);
    }
    public void process(Auction auction) {

       auction.setBuyerFinalAmount(auction.getHighestBid());
       super.process(auction);

    }
}
