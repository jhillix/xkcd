package com.jhillix.xkcd;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.List;

/**
 * Formats output for the console.
 *
 * @author jhillix
 */
public class XkcdFormatter {

    private static Logger LOG = Logger.getLogger(XkcdFormatter.class);

    public XkcdFormatter() {}

    /**
     * Takes a List composed of Xkcd objects and passes it to FormattedOutput.vm for output.
     *
     * @param xkcds list to output
     * @return      the formatted output
     */
    public String format(final List<Xkcd> xkcds) {
        LOG.info("Formatting output.");
        Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("FormattedOutput.vm"));

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("xkcds", xkcds);
        StringWriter stringWriter = new StringWriter();

        try {
            Velocity.evaluate(velocityContext, stringWriter, "", reader);
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }

        return stringWriter.toString();
    }
}
