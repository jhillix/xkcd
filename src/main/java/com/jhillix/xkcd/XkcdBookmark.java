package com.jhillix.xkcd;

import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


/**
 * Allows the user to add a bookmark for a xkcd webcomic by using Java's Preferences API.
 *
 * @author jhillix
 */
public class XkcdBookmark {

    private static Logger LOG = Logger.getLogger(XkcdBookmark.class);

    public static final String OK = "Bookmark successfully added!";

    public static final String NG = "You must enter a valid xkcd title!";

    private Preferences preferences;

    private String lineSeperator = System.lineSeparator();

    public XkcdBookmark() {
        // Use this class as a node in which the preferences can be stored.
        preferences = Preferences.userRoot().node(this.getClass().getName());
    }

    /**
     * Keeping it simple for now and only allowing one bookmark addition at a time. Future improvement is to allow user
     * to specify multiple titles at one time. Probably could be implemented better, but hey baby steps.y
     *
     * @param title that the user entered
     * @param xkcds list of Xkcd objects that was parsed earlier
     * @return      a OK or NG response to notify user
     */
    public String addBookmark(final String title, final List<Xkcd> xkcds) {
        Xkcd bookmark = new Xkcd();

        // Assume the worst.
        String response = NG;

        // Verify that the title the user entered is a valid title. If not, then return back with a reason why.
        for (Xkcd xkcd : xkcds) {
            if (xkcd.getTitle().toLowerCase().equals(title.toLowerCase())) {
                bookmark = xkcd;
                response = OK;
                break;
            }
        }

        // Add the bookmark to user preferences.
        if (OK.equals(response)) {
            String realTitle = bookmark.getTitle();

            preferences.put(realTitle + "-title", realTitle);
            preferences.put(realTitle + "-link", bookmark.getLink());
            preferences.put(realTitle + "-pubDate", bookmark.getPubDate());
            preferences.put(realTitle + "-description", bookmark.getDescription());
        }

        return response;
    }

    public void showBookmarks() {
        if (preferences != null) {
            try {
                for (String pref : preferences.keys()) {
                    // Only show the title.
                    if (pref.endsWith("-title")) {
                        System.out.println(pref.substring(0, pref.lastIndexOf("-title")));
                    }
                }
            } catch (BackingStoreException ex) {
                LOG.error(ex.getStackTrace());
            }
        } else {
            System.out.println("No bookmarks have been added.");
        }
    }

    public boolean hasBookmarks() {
        boolean bool = false;
        try {
            if (preferences != null && preferences.keys().length > 0) {
                bool = true;
            }
        } catch (BackingStoreException ex) {
            LOG.error(ex.getStackTrace());
        }

        return bool;
    }

    public void getBookmark(final String bookmarks) {
        if (hasBookmarks()) {
            List<Xkcd> xkcds = new ArrayList<>();
            for (String bookmark : bookmarks.split(",")) {
                // Is this bookmark valid?
                bookmark = bookmark.trim();
                String test = preferences.get(bookmark + "-title", "Could not locate a bookmark with that title.");
                if ("Could not locate a bookmark with that title.".equals(test)) {
                    System.out.println(test + " (" + bookmark + ")" + lineSeperator);
                    continue;
                }

                // Populate a Xkcd object from the users bookmarks.
                Xkcd xkcd = new Xkcd();
                xkcd.setTitle(preferences.get(bookmark + "-title", ""));
                xkcd.setLink(preferences.get(bookmark + "-link", ""));
                xkcd.setPubDate(preferences.get(bookmark + "-pubDate", ""));
                xkcd.setDescription(preferences.get(bookmark + "-description", ""), false);

                // Add it to the List.
                xkcds.add(xkcd);
            }

            // Format it.
            System.out.println(new XkcdFormatter().format(xkcds));
            System.out.println("End of bookmarks." + lineSeperator);
        }
    }
}
