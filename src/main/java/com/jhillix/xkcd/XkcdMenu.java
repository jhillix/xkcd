package com.jhillix.xkcd;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;


public class XkcdMenu {

    private static Logger LOG = Logger.getLogger(XkcdMenu.class);

    public XkcdMenu() {}

    public String showMenu() {
        Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("menu.vm"));

        VelocityContext velocityContext = new VelocityContext();
        StringWriter stringWriter = new StringWriter();

        try {
            Velocity.evaluate(velocityContext, stringWriter, "", reader);
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }

        return stringWriter.toString();
    }
}
