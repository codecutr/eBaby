package Notifiers;

import Auction.Auction;
import Services.*;

public class NotifySale extends Notifier {

    public void sendEmail(Auction auction) {
        PostOffice po = PostOffice.getInstance();

        po.sendEMail(auction.getSeller().getEmail(), "You sold your item hero!  way to go.");
        po.sendEMail(auction.getCurrentBidder().getEmail(), "You won your item hero!  way to go.");
    }
}
