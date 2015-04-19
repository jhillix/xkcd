package com.jhillix.xkcd;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;


/**
 * Where process begins.
 *
 * @author jhillix
 */
public class Main {

    private static Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        // Configure Log4J.
        PropertyConfigurator.configure(Main.class.getClassLoader().getResource("log4j.properties"));

        try {
            XkcdBookmark xkcdBookmark = new XkcdBookmark();

            // If the user has bookmarks show them.
            if (xkcdBookmark.hasBookmarks()) {
                System.out.println("Here are your current bookmarks:");
                xkcdBookmark.showBookmarks();
            }

            // Does the user want to read one of their bookmarks?
            Scanner input = new Scanner(System.in);
            System.out.print("Would you like to read a bookmark? <y or n> ");
            if ("y".equals(input.nextLine())) {
                System.out.print("Enter the name of a bookmark or multiple bookmarks separated by a \",\": ");
                xkcdBookmark.getBookmark(input.nextLine());
            }

            // Retrieve the data.
            InputStream inputStream = new XkcdRSS().feed();

            // Parse the data.
            List<Xkcd> xkcds = new XkcdRSSParser().parse(inputStream);

            // Format the data.
            System.out.println(new XkcdFormatter().format(xkcds));

            // Ask the user if they want to bookmark one of the xkcd webcomics.
            System.out.print("Would you like to bookmark a xkcd webcomic? <y or n> ");
            if ("y".equals(input.nextLine())) {
                System.out.print("Enter a \"title\" to bookmark: ");
                System.out.println(xkcdBookmark.addBookmark(input.nextLine(), xkcds));
            }

        } catch (IOException ex) {
            LOG.error(ex.getStackTrace());
        }
    }
}
