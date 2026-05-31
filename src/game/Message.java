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

public class Message {

    final String text;
    final InteractiveObject source;

    public Message(String text, InteractiveObject source) {
        if ( text == null ) {
            throw new NullPointerException("Message text is NULL.");
        }
        this.text = text;
        if (source != null) {
            this.source = source;
        } else {
            this.source = new InteractiveObject("");
        }
    }

    public String getText() {
        return text;
    }

    public InteractiveObject getSource() {
        return source;
    }

}
