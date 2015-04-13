package com.jhillix.xkcd;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.unbescape.html.HtmlEscape;
import java.io.InputStream;
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
    public void parse(final InputStream inputStream) {

        try {
            LOG.info("Creating XML reader.");
            document = new SAXReader().read(inputStream);

            List<Node> nodes = document.selectNodes("/rss/channel/item");

            LOG.info("Parsing RSS feed.");
            String description;
            int start;
            int end;
            System.out.println("\n\"xkcd updates every Monday, Wednesday, and Friday.\"\n");
            for (Node node : nodes) {
                System.out.println("Title: " + node.selectSingleNode("title").getText());
                System.out.println("Link: " + node.selectSingleNode("link").getText());

                // Yeah, this part could of been done with RegEx :P
                description = node.selectSingleNode("description").getText();
                start = (description.indexOf("title") + 7);
                end = (description.indexOf("alt") - 2);
                System.out.println("Description: " + HtmlEscape.unescapeHtml(description.substring(start, end)));

                System.out.println("Pub Date: " + node.selectSingleNode("pubDate").getText() + "\n");
            }
        } catch (DocumentException ex) {
            LOG.warn(ex.getStackTrace());
        }
    }
}
