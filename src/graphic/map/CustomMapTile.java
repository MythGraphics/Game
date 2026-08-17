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

import graphic.HasImage;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class CustomMapTile implements IsMapTile, HasImage {

    private final char mapChar;
    private final BufferedImage img;
    private final InteractionType iType;
    private final boolean passable;

    public CustomMapTile(char mapChar, BufferedImage img, InteractionType iType, boolean passable) {
        this.mapChar  = mapChar;
        this.img      = img;
        this.iType    = iType;
        this.passable = passable;
    }

    @Override
    public char getMapChar() {
        return mapChar;
    }

    @Override
    public InteractionType getInteractionType() {
        return iType;
    }

    @Override
    public boolean isPassable() {
        return passable;
    }

    @Override
    public Image getImage() {
        return img;
    }

}
