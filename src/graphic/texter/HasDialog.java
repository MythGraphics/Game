/*
 *
 */

package graphic.texter;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import java.util.LinkedList;

@FunctionalInterface
public interface HasDialog {

    // A dialog is a linked list of messages.
    LinkedList<Message> getDialog();

}
