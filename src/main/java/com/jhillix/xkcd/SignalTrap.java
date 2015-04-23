package com.jhillix.xkcd;

import org.fusesource.jansi.AnsiConsole;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import static org.fusesource.jansi.Ansi.ansi;


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
        // Reset our console color.
        System.out.println(ansi().reset());
        // Uninstall AnsiConsole.
        AnsiConsole.systemUninstall();
        // Exit.
        System.exit(0);
    }
}
