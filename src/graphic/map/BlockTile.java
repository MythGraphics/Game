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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

// is a sprite
public class BlockTile extends Block implements HasImage, Renderable {

    private HasImage image;

    public BlockTile(int x, int y, IsBlockType bType, HasImage image) {
        this(x, y, 0, bType, image);
        updateDimension();
    }

    public BlockTile(int x, int y, int tileSize, IsBlockType bType, HasImage image) {
        this(x, y, tileSize, tileSize, bType, image);
    }

    public BlockTile(int x, int y, int width, int height, IsBlockType bType, HasImage image) {
        super(x, y, width, height, bType);
        setImage(image);
    }

    @Override
    public BufferedImage getImage() {
        return image.getImage();
    }

    public final void setImage(HasImage image) {
        if (image == null) {
            this.image = () -> null;
        } else {
            this.image = image;
        }
    }

    public final void updateDimension() {
        if ( getImage() != null ) {
            setWidth(  image.getImage().getWidth() );
            setHeight( image.getImage().getHeight() );
        }
    }

    @Override
    public void draw(Graphics2D g2d, int offsetX, int offsetY) {
        g2d.drawImage( getImage(), x-offsetX, y-offsetY, null );
    }

    public void destroy() {
        setBlockType(DefaultBlockType.NONE);
        setImage( () -> null );
    }

    public void change(IsBlockType bType, HasImage image) {
        setBlockType(bType);
        setImage(image);
    }

}
