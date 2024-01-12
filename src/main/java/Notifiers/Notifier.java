package Notifiers;

import Auction.Auction;
import CloseProcessors.CloseProcessorDecorator;

public abstract class Notifier extends CloseProcessorDecorator {

    public abstract void sendEmail(Auction auction);

}
