package com.jhillix.xkcd;

import sun.misc.Signal;
import sun.misc.SignalHandler;


/**
 * Custom handler that will exit the program. Mainly used to control the flow of this application. Maybe not the best/safe
 * way to do this.
 *
 * @author jhillix
 */
public class SignalTrap implements SignalHandler {

    public SignalTrap() {}

    @Override
    public void handle(final Signal signal) {
        System.out.println("Shutting down");
        System.exit(0);
    }
}
