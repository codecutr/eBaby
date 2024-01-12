package Notifiers;

import User.User;

public class NotifierFactory {

    public static Notifier getInstance(User currentBidder) {
        if (currentBidder != null) {
            return new NotifySale();
        } else {
            return new NotifyNoSale();
        }
    }
}
