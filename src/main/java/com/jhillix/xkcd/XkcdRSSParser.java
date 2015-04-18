package com.jhillix.xkcd;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Parses the XML and prints the highlights to the console.
 *
 * @author jhillix
 */
public class XkcdRSSParser {

    private static Logger LOG = Logger.getLogger(XkcdRSSParser.class);

    private Document document;

    public XkcdRSSParser() {}

    /**
     * Consumer method that takes an InputStream and parses out the important bits to then be displayed to the user via
     * the console.
     *
     * @param inputStream
     */
    public List<Xkcd> parse(final InputStream inputStream) {
        List<Xkcd> xkcdList = new ArrayList<>();

        try {
            LOG.info("Creating XML reader.");
            document = new SAXReader().read(inputStream);

            List<Node> nodes = document.selectNodes("/rss/channel/item");

            LOG.info("Parsing RSS feed.");

            for (Node node : nodes) {
                Xkcd xkcdElement = new Xkcd();

                xkcdElement.setTitle(node.selectSingleNode("title").getText());
                xkcdElement.setLink(node.selectSingleNode("link").getText());
                xkcdElement.setDescription(node.selectSingleNode("description").getText());
                xkcdElement.setPubDate(node.selectSingleNode("pubDate").getText());

                xkcdList.add(xkcdElement);
            }
        } catch (DocumentException ex) {
            LOG.warn(ex.getStackTrace());
        }

        return xkcdList;
    }
}
