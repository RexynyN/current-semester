package bruh;

import java.awt.*;
import java.io.*;

/**
 * A simple Agent that makes you aware
 * of its arrival
 *
 * @version 1.0
 * @author Prof. David Bernstein, James Madison University
 */
public class Kilroy extends Agent {

    /**
     * Explicit Value Constructor
     *
     * @param home The "home" host
     */
    public Kilroy(String home) {
        super(home);
    }

    /**
     * The code that should be executed before this
     * Agent leaves home
     */
    public void beforeDeparture() {
        goTo("localhost");
    }

    /**
     * The code that should be executed when this
     * Agent arrives at a remote Sandbox
     */
    public void onArrival() {
        Frame f;

        f = new Frame("Kilroy");
        f.setLayout(new BorderLayout());
        f.add(new Label("Kilroy Was Here!", Label.CENTER));
        f.setSize(400, 50);
        f.show();
        f.toFront();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {
        }
        ;

        f.dispose();
    }

    /**
     * The code that should be executed when this
     * Agent returns home
     */
    public void onReturn() {

    }
}