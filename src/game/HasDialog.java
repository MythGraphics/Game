/*
 *
 */

package game;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import java.util.LinkedList;

public interface HasDialog {

    // A Dialog is a LinkedList of Messages.
    LinkedList<Message> getDialog();

}
