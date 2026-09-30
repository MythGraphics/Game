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

import graphic.Direction;
import static graphic.Direction.*;
import graphic.DirectionalImage;
import graphic.HasImage;
import graphic.map.Block;
import static graphic.map.DefaultBlockType.MISSILE;
import graphic.map.GameMap;
import graphic.map.IsBlockType;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

// is a sprite
public class BlockTile extends Block implements IsBlockTile, CanFireMissile, Cloneable {

    private HasImage image;
    private DirectionalImage missileImage;

    public BlockTile(BlockTile blockTile) {
        this(blockTile.x, blockTile.y, blockTile.width, blockTile.height, blockTile.bType, blockTile);
        setMissileImage(blockTile.missileImage);
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

    @Override
    public DirectionalImage getMissileImage() {
        return new DirectionalImage(missileImage);
    }

    @Override
    public void fireMissile(GameMap map, Direction d) {
        if ( !hasMissile() ) {
            return;
        }

        int missileX = x;
        int missileY = y;
        switch (d) {
            case RIGHT -> missileX += map.tileSize;
            case LEFT  -> missileX -= map.tileSize;
            case DOWN  -> missileY += map.tileSize;
            case UP    -> missileY -= map.tileSize;
        }
        map.add( new Missile( this, d, missileX, missileY, MISSILE, GameMap.defaultMissileSpeed ));
    }

    @Override
    public boolean hasMissile() {
        return missileImage != null;
    }

    public final void setMissileImage(DirectionalImage missileImage) {
        this.missileImage = missileImage;
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
