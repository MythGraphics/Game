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

import game.HasUIImage;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class Message {

    final String text;
    final HasUIImage source;

    public Message(String text, HasUIImage source) {
        if (text == null) {
            throw new NullPointerException("Message text is NULL.");
        }
        this.text = text;
        if (source != null) {
            this.source = source;
        } else {
            this.source = new HasUIImage() {
                @Override
                public BufferedImage getImg() { return null; }
                @Override
                public ImageIcon getIcon() { return null; }
                @Override
                public String toString() { return ""; }
            };
        }
    }

    public String getText() {
        return text;
    }

    public HasUIImage getSource() {
        return source;
    }

    @Override
    public String toString() {
        return source + ": " + text;
    }

}
