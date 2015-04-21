package com.jhillix.xkcd;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Grabs the feed.
 *
 * @author jhillix
 */
public class XkcdRSS {

    private static Logger LOG = Logger.getLogger(XkcdRSS.class);

    private URL url;

    public XkcdRSS() {}

    /**
     * Grabs the RSS feed from xkcd and returns the connection.
     *
     * @return InputStream of the RSS feed
     * @throws IOException
     */
    public InputStream feed() throws IOException {
        LOG.info("Getting xkcd.com RSS feed.");

        try {
            url = new URL("http://xkcd.com/rss.xml");

        } catch (MalformedURLException ex) {
            LOG.warn(ex.getMessage(), ex);
        }

        return url.openStream();
    }
}
