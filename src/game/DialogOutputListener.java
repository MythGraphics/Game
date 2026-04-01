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

public interface DialogOutputListener {

    void show(HasDialog dialog);
    void show(Message msg);

}
