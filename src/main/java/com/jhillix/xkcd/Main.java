package com.jhillix.xkcd;

import jline.console.ConsoleReader;
import jline.console.completer.StringsCompleter;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.fusesource.jansi.AnsiConsole;
import sun.misc.Signal;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.ansi;


/**
 * Where the process begins.
 *
 * @author jhillix
 */
public class Main {

    private static Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        // Configure Log4J.
        PropertyConfigurator.configure(Main.class.getClassLoader().getResource("log4j.properties"));

        // Give our user that good ol' tabby feel.
        AnsiConsole.systemInstall();

        // Hackish way to control the flow of this program.
        Signal.handle(new Signal("INT"), new SignalTrap());

        // Show the menu. In Color!!!
        XkcdMenu menu = new XkcdMenu();
        System.out.println(ansi().fg(GREEN).a(menu.showMenu()));

        XkcdBookmark xkcdBookmark = new XkcdBookmark();
        List<Xkcd> xkcds = new ArrayList<>();

        try {
            // Instantiate a JLine ConsoleReader to give the user a typical CLI feel with some shaheen!
            ConsoleReader console = new ConsoleReader();

            // Create a Set for holding tab completion Strings. Add some bits to start off with.
            Set<String> tabs = new HashSet<>();
            tabs.add("clear");
            tabs.add("exit");
            tabs.add("q");

            // Add the Set to our ConsoleReader.
            console.addCompleter(new StringsCompleter(tabs));

            // Run forever or until the user sends the interrupt signal (e.g. Ctrl+C).
            while (true) {
                String option = console.readLine("a> ");

                // We need to account for tab completion input here (e.g. whitespace is appended).
                option = option.trim();

                // Add the new value to our Set so the user can use it for tab completion.
                tabs.add(option);
                // TODO: is there a better way to do this??
                console.addCompleter(new StringsCompleter(tabs));

                switch (option) {
                    case "1":
                        // Retrieve the data.
                        InputStream inputStream = new XkcdRSS().feed();

                        // Parse the data.
                        xkcds = new XkcdRSSParser().parse(inputStream);

                        // Format the data.
                        System.out.println(new XkcdFormatter().format(xkcds));
                        break;
                    case "2":
                        // User wants to add a bookmark.
                        if (!xkcds.isEmpty()) {
                            System.out.println(xkcdBookmark.addBookmark(console.readLine("Enter a \"title\" to bookmark: "), xkcds));
                        } else {
                            System.out.println("You must run option \"1\" first.");
                        }
                        break;
                    case "3":
                        // User wants to view bookmarks. If the user has bookmarks show them.
                        xkcdBookmark.showBookmarks();
                        break;
                    case "4":
                        // User wants to read one of their bookmarks.
                        if (xkcdBookmark.hasBookmarks()) {
                            xkcdBookmark.getBookmark(console.readLine("Enter the name of a bookmark or multiple bookmarks separated by a comma: "));
                        } else {
                            System.out.println("You have no bookmarks to read!");
                        }
                        break;
                    case "5":
                        // User wants to remove a bookmark.
                        xkcdBookmark.removeBookmark(console.readLine("Enter the name of a bookmark to delete: "));
                        break;
                    case "6":
                        // User wants to remove all of their bookmarks.
                        xkcdBookmark.removeAllBookmarks();
                        break;
                    case "clear":
                        console.clearScreen();
                        System.out.println(menu.showMenu());
                        break;
                    case "exit":
                    case "q":
                        // Reset our console color.
                        System.out.println(ansi().reset());
                        // Uninstall AnsiConsole.
                        AnsiConsole.systemUninstall();
                        // Shutdown ConsoleReader.
                        console.shutdown();
                        // Exit.
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid option.");
                        break;
                }
            }
        } catch (IOException | InputMismatchException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
