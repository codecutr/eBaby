package CloseProcessors;

import Auction.*;
import Notifiers.NotifyNoSale;
import Notifiers.NotifySale;

public class CloseProcessorFactory {

    public static CloseProcessorDecorator getInstance(Auction auction) {
        Auction.Category category = auction.getCategory();


        if (auction.getCurrentBidder() != null) {
            switch (category) {
                case DLSOFTWARE:
                    return new LogProcessor(new NotifySaleProcessor(new SellerFeeProcessor(new SoftwareFeeProcessor())));
                case OTHER:
                    return new LogProcessor(new NotifySaleProcessor(new SellerFeeProcessor(new OtherFeeProcessor())));
                case CAR:
                    return new LogProcessor(new NotifySaleProcessor(new SellerFeeProcessor(new CarFeeProcessor())));
                default:
                    return null;
            }
        } else {
            return new NotifyNoSaleProcessor();
        }
    }
}
