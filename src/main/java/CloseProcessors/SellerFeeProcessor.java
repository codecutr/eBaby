package CloseProcessors;

import Auction.Auction;

public class SellerFeeProcessor extends CloseProcessorDecorator {

    public SellerFeeProcessor(){

    }
    public SellerFeeProcessor(CloseProcessor feeProcessor){
        super(feeProcessor);
    }
    public void process(Auction auction) {

        auction.setSellerFinalAmount(auction.getHighestBid() * .98);
        super.process(auction);

    }

}
