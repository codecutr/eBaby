package CloseProcessors;

import Auction.Auction;
import Services.PostOffice;

public class NotifyNoSaleProcessor extends CloseProcessorDecorator{
    public NotifyNoSaleProcessor(){

    }
    public NotifyNoSaleProcessor(CloseProcessor closeProcessor){
        super(closeProcessor);
    }
    public void process(Auction auction) {

        PostOffice po = PostOffice.getInstance();
        po.sendEMail(auction.getSeller().getEmail(), "Your item didn't sell.");
        super.process(auction);
    }
}
