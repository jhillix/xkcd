package com.jhillix.xkcd;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


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
            // Retrieve the data.
            InputStream inputStream = new XkcdRSS().feed();

            // Parse the data.
            List<Xkcd> xkcds = new XkcdRSSParser().parse(inputStream);

            // Format the data.
            System.out.println(new XkcdFormatter().format(xkcds));

        } catch (IOException ex) {
            LOG.error(ex.getStackTrace());
        }
    }
}
