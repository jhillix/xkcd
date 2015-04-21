package com.jhillix.xkcd;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import sun.misc.Signal;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


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

        // Hackish way to control the flow of this program.
        Signal.handle(new Signal("INT"), new SignalTrap());

        // Show the menu.
        System.out.println(new XkcdMenu().showMenu());

        XkcdBookmark xkcdBookmark = new XkcdBookmark();
        List<Xkcd> xkcds = new ArrayList<>();

        // Run forever or until the user sends the interrupt signal (e.g. Ctrl+C).
        while (true) {
            try {
                // Prompt user.
                Scanner integer = new Scanner(System.in);
                Scanner string = new Scanner(System.in);
                System.out.print("Enter an option or \"Ctrl+C\" to quit: ");

                switch (integer.nextInt()) {
                    case 1:
                        // Retrieve the data.
                        InputStream inputStream = new XkcdRSS().feed();

                        // Parse the data.
                        xkcds = new XkcdRSSParser().parse(inputStream);

                        // Format the data.
                        System.out.println(new XkcdFormatter().format(xkcds));
                        break;
                    case 2:
                        // User wants to add a bookmark.
                        if (!xkcds.isEmpty()) {
                            System.out.print("Enter a \"title\" to bookmark: ");
                            System.out.println(xkcdBookmark.addBookmark(string.nextLine(), xkcds));
                        } else {
                            System.out.println("You must run option \"1\" first.");
                        }
                        break;
                    case 3:
                        // User wants to view bookmarks. If the user has bookmarks show them.
                        xkcdBookmark.showBookmarks();
                        break;
                    case 4:
                        // User wants to read one of their bookmarks.
                        if (xkcdBookmark.hasBookmarks()) {
                            System.out.print("Enter the name of a bookmark or multiple bookmarks separated by a comma: ");
                            xkcdBookmark.getBookmark(string.nextLine());
                        } else {
                            System.out.println("You have no bookmarks to read!");
                        }
                        break;
                    case 5:
                        // User wants to remove a bookmark.
                        System.out.print("Enter the name of a bookmark to delete: ");
                        xkcdBookmark.removeBookmark(string.nextLine());
                        break;
                    case 6:
                        // User wants to remove all of their bookmarks.
                        xkcdBookmark.removeAllBookmarks();
                        break;
                    default:
                        System.out.println("Invalid option.");
                        break;
                }
            } catch (IOException | InputMismatchException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        }
    }
}
