/*
 *
 */

package graphic.map;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import java.awt.Graphics2D;

public interface Renderable {

    void draw(Graphics2D g2d, int offsetX, int offsetY);
    
}