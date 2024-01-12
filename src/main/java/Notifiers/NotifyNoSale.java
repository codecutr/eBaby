package Notifiers;

import Auction.*;
import CloseProcessors.CloseProcessor;
import Services.PostOffice;

public class NotifyNoSale extends Notifier {
    public void sendEmail(Auction auction) {
        PostOffice po = PostOffice.getInstance();
        po.sendEMail(auction.getSeller().getEmail(), "Your item didn't sell.");
    }
}
