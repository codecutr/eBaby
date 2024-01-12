package CloseProcessors;

import Auction.Auction;
import Services.PostOffice;

public class NotifySaleProcessor extends CloseProcessorDecorator {
    public NotifySaleProcessor(){

    }
    public NotifySaleProcessor(CloseProcessor closeProcessor){
        super(closeProcessor);
    }
    public void process(Auction auction) {

        PostOffice po = PostOffice.getInstance();

        po.sendEMail(auction.getSeller().getEmail(), "You sold your item hero!  way to go.");
        po.sendEMail(auction.getCurrentBidder().getEmail(), "You won your item hero!  way to go.");
        super.process(auction);
    }
}
