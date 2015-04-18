package com.jhillix.xkcd;

import org.junit.Test;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * Tests the XkcdRSSParser with a rss.xml file downloaded from xkcd.com.
 *
 * Use "mvn test" to run from the CLI as standalone.
 *
 * @author jhillix
 */
public class XkcdRSSParserTest {

    @Test
    public void testParse() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("rss.xml");

        XkcdRSSParser test = new XkcdRSSParser();

        List<Xkcd> xkcds = test.parse(inputStream);

        // We should have four elements in this List.
        assertEquals(4, xkcds.size());

            // We have full control of the input. Meaning, "rss.xml" is a static file and will always have four entries and
            // always have the static Strings below. Assuming nobody donks with rss.xml ;)
            for (int i = 0; i < xkcds.size(); i++) {
                switch (i) {
                    case 0:
                        assertEquals("Napoleon", xkcds.get(0).getTitle());
                        assertEquals("http://xkcd.com/1510/", xkcds.get(0).getLink());
                        assertEquals("Fri, 10 Apr 2015 04:00:00 -0000", xkcds.get(0).getPubDate());
                        assertEquals("\"Mr. President, what if the unthinkable happens? What if the launch goes wrong, and Napoleon is not stranded on the Moon?\" \"Have Safire write up a speech.\"", xkcds.get(0).getDescription());
                        break;
                    case 1:
                        assertEquals("Scenery Cheat Sheet", xkcds.get(1).getTitle());
                        assertEquals("http://xkcd.com/1509/", xkcds.get(1).getLink());
                        assertEquals("Wed, 08 Apr 2015 04:00:00 -0000", xkcds.get(1).getPubDate());
                        assertEquals("At the boundary between each zone, stories blend together. Somewhere in the New Mexico desert, the Roadrunner is pursued by a tireless Anton Chigurh.", xkcds.get(1).getDescription());
                        break;
                    case 2:
                        assertEquals("Operating Systems", xkcds.get(2).getTitle());
                        assertEquals("http://xkcd.com/1508/", xkcds.get(2).getLink());
                        assertEquals("Mon, 06 Apr 2015 04:00:00 -0000", xkcds.get(2).getPubDate());
                        assertEquals("One of the survivors, poking around in the ruins with the point of a spear, uncovers a singed photo of Richard Stallman. They stare in silence. \"This,\" one of them finally says, \"This is a man who BELIEVED in something.\"", xkcds.get(2).getDescription());
                        break;
                    case 3:
                        assertEquals("Metaball", xkcds.get(3).getTitle());
                        assertEquals("http://xkcd.com/1507/", xkcds.get(3).getLink());
                        assertEquals("Fri, 03 Apr 2015 04:00:00 -0000", xkcds.get(3).getPubDate());
                        assertEquals("Shoot, it landed in the golf course. Gonna be hard to get it down the--oh, never mind, it rolled onto the ice hazard. Face-off!", xkcds.get(3).getDescription());
                        break;
                    default:
                        System.out.println("Something unexpected happened.");
                        break;
                }
            }
    }
}
