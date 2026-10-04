/*
 *
 */

package graphic.tile;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.HasImage;
import graphic.map.Block;
import graphic.map.GameMap;
import graphic.map.IsBlockType;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

// is a sprite
public class BlockTile extends Block implements IsBlockTile, Cloneable {

    private HasImage image;

    public BlockTile(BlockTile blockTile) {
        this(blockTile.x, blockTile.y, blockTile.width, blockTile.height, blockTile.bType, blockTile);
    }

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

    public BlockTile(Point pos, Dimension dim, IsBlockType bType, HasImage image) {
        this(pos.x, pos.y, dim.width, dim.height, bType, image);
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
        BufferedImage img = getImage();
        if (img != null) {
            setWidth(  img.getWidth()  );
            setHeight( img.getHeight() );
        }
    }

    @Override
    public void draw(Graphics2D g2d, int offsetX, int offsetY) {
        g2d.drawImage( getImage(), x-offsetX, y-offsetY, width, height, null );
    }

    public void destroy(GameMap map) {
        map.remove(this);
    }

    public void change(IsBlockType bType, HasImage image) {
        setBlockType(bType);
        setImage(image);
    }

    @Override
    public BlockTile clone() throws CloneNotSupportedException {
        if ( this.getClass() == BlockTile.class ) {
            return new BlockTile(this);
        } else {
            throw new CloneNotSupportedException( "Clone on " + getClass().getName() + " not supported." );
        }
    }

}
