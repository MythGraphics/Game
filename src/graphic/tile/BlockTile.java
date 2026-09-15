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

import graphic.CanFireProjectile;
import graphic.DirectionalImage;
import graphic.HasImage;
import graphic.map.Block;
import graphic.map.GameMap;
import graphic.map.IsBlockType;
import graphic.map.Renderable;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

// is a sprite
public class BlockTile extends Block implements Renderable, CanFireProjectile {

    private HasImage image;
    private DirectionalImage projectileImage;

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

    @Override
    public DirectionalImage getProjectileImage() {
        return projectileImage;
    }

    public void setProjectileImage(DirectionalImage projectileImage) {
        this.projectileImage = projectileImage;
    }

    @Override
    public boolean canFireProjectile() {
        return projectileImage != null;
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

}
