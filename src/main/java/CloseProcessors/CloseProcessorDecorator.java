package CloseProcessors;

import Auction.*;

public class CloseProcessorDecorator extends CloseProcessor {
    private CloseProcessor nextFeeProcessor;

    public CloseProcessorDecorator(){}

    public CloseProcessorDecorator(CloseProcessor feeProcessor){
        nextFeeProcessor =  feeProcessor;

    }
    public void process(Auction auction){
        if(nextFeeProcessor != null){
            nextFeeProcessor.process(auction);
        }
    }
}
