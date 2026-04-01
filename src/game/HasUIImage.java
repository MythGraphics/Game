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

import java.awt.Dimension;
import java.awt.Image;
import javax.swing.ImageIcon;

public interface HasUIImage {

    public final static Dimension DEFAULT_ICON_DIMENSION = new Dimension(32, 32);

    public Image getImg(); // Bild/Hintergrund für TextBox
    public ImageIcon getIcon();

}
