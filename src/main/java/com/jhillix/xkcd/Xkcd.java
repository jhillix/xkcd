package com.jhillix.xkcd;

import org.unbescape.html.HtmlEscape;

/**
 * Represents the highlights of the RSS feed.
 *
 * @author jhillix
 */
public class Xkcd {

    private String title;

    private String link;

    private String description;

    private String pubDate;

    public Xkcd() {}

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public void setDescription(final String description) {
        int start = (description.indexOf("title") + 7);
        int end = (description.indexOf("alt") - 2);
        this.description = HtmlEscape.unescapeHtml(description.substring(start, end));
    }

    public void setPubDate(final String pubDate) {
        this.pubDate = pubDate;
    }
}
