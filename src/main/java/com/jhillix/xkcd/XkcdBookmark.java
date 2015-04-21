package com.jhillix.xkcd;

import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


/**
 * Allows the user to add a bookmark for a xkcd webcomic by using Java's Preferences API. A bookmark is referenced by the xkcd
 * webcomic title.
 *
 * @author jhillix
 */
public class XkcdBookmark {

    private static Logger LOG = Logger.getLogger(XkcdBookmark.class);

    public static final String OK = "Bookmark successfully added!";

    public static final String NG = "You must enter a valid xkcd title!";

    private static final String INVALID_BOOKMARK = "Could not locate a bookmark with that title.";

    private Preferences preferences;

    private String lineSeperator = System.lineSeparator();

    public XkcdBookmark() {
        // Use this class as a node in which the preferences can be stored.
        preferences = Preferences.userRoot().node(this.getClass().getName());
    }

    /**
     * Keeping it simple for now and only allowing one bookmark addition at a time. Future improvement is to allow user
     * to specify multiple titles at one time. Probably could be implemented better, but hey baby steps ;)
     *
     * @param title that the user entered
     * @param xkcds list of Xkcd objects that was parsed earlier
     * @return      an OK or NG response to notify user
     */
    public String addBookmark(final String title, final List<Xkcd> xkcds) {
        Xkcd bookmark = new Xkcd();

        // Assume the worst.
        String response = NG;

        // Iterate through the List, verify that the title the user entered is a valid title. If we have a valid title,
        // update the response and grab the Xkcd object associated with the valid title. NOTE: for now we only allow one
        // bookmark at a time. This will obviously need to be refactored when more than one bookmark at a time can be entered.
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

    /**
     * Shows a users saved bookmarks if they exist or a message if none exist.
     */
    public void showBookmarks() {
        if (hasBookmarks()) {
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

    /**
     * Checks if this user has any bookmarks.
     *
     * @return true if bookmarks exist otherwise, false
     */
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

    /**
     * Gets a bookmark or bookmarks if there is more than one and displays them to the user. The user will see a list of
     * all bookmarks they currently have. The user can then copy & paste one of those values or all of them when prompted.
     *
     * @param bookmarks separated by a comma if there is more than one or just a single bookmark
     */
    public void getBookmark(final String bookmarks) {

        if (hasBookmarks()) {
            List<Xkcd> xkcds = new ArrayList<>();
            for (String bookmark : bookmarks.split(",")) {

                bookmark = bookmark.trim();

                // Is this bookmark valid?
                if (!isBookmarkValid(bookmark)) {
                    System.out.println(INVALID_BOOKMARK + " (" + bookmark + ")" + lineSeperator);
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

    /**
     * Checks if a bookmark is valid by checking its existence. Case matters!
     *
     * @param bookmark to check (xkcd webcomic title)
     * @return true if we find a match otherwise false
     */
    public boolean isBookmarkValid(final String bookmark) {
        boolean bool = false;

        String test = preferences.get(bookmark.trim() + "-title", INVALID_BOOKMARK);
        if (!INVALID_BOOKMARK.equals(test)) {
            bool = true;
        }
        return bool;
    }

    /**
     * Removes a single bookmark if it exists.
     */
    public void removeBookmark(final String bookmark) {

        if (hasBookmarks() && isBookmarkValid(bookmark)) {
            preferences.remove(bookmark + "-title");
            preferences.remove(bookmark + "-link");
            preferences.remove(bookmark + "-pubDate");
            preferences.remove(bookmark + "-description");

            System.out.println("Successfully removed " + bookmark);
        } else {
            System.out.println("Either you have no bookmarks or the bookmark you entered is incorrect. Case matters.");
        }
    }

    /**
     * Removes all bookmarks for this user.
     */
    public void removeAllBookmarks() {

        if (hasBookmarks()) {

            try {
                preferences.removeNode();
            } catch (BackingStoreException ex) {
                LOG.error(ex.getStackTrace());
            }

            System.out.println("Successfully removed all bookmarks!");
        } else {
            System.out.println("You have no bookmarks!");
        }
    }
}
