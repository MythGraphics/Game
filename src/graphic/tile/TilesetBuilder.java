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

import graphic.Alignment;
import static graphic.Alignment.HORIZONTAL;
import static graphic.Alignment.VERTICAL;
import graphic.Direction;
import static graphic.Direction.*;
import graphic.HasImage;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class TilesetBuilder {

    final Point cursor = new Point(0,0);

    private final BufferedImage image;

    private int offsetX = 0, offsetY = 0, spaceX = 0, spaceY = 0;
    private int tileSizeX, tileSizeY;
    private Direction d = RIGHT;

    public TilesetBuilder(BufferedImage image, int tileSize) {
        this(image, tileSize, tileSize);
    }

    public TilesetBuilder(BufferedImage image, Dimension dim) {
        this(image, dim.width, dim.height);
    }

    public TilesetBuilder(BufferedImage image, int tileSizeX, int tileSizeY) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null.");
        }
        this.image = image;
        this.tileSizeX = tileSizeX;
        this.tileSizeY = tileSizeY;
    }

    public void setOffset(Dimension offset) {
        setOffset(offset.width, offset.height);
    }

    public void setOffset(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        cursor.x = offsetX;
        cursor.y = offsetY;
    }

    public void setSpace(Dimension space) {
        setSpace(space.width, space.height);
    }

    public void setSpace(int spaceX, int spaceY) {
        this.spaceX = spaceX;
        this.spaceY = spaceY;
    }

    public void setTileSize(int tileSizeX, int tileSizeY) {
        this.tileSizeX = tileSizeX;
        this.tileSizeY = tileSizeY;
    }

    public void setTileSize(Dimension tileSize) {
        setTileSize(tileSize.width, tileSize.height);
    }

    public void setTileSize(int tileSize) {
        setTileSize(tileSize, tileSize);
    }

    public Dimension getTileSize() {
        return new Dimension(tileSizeX, tileSizeY);
    }

    public boolean isQuadratic() {
        return tileSizeX == tileSizeY;
    }

    public void setDirection(Direction d) {
        this.d = d;
    }

    public Direction getDirection() {
        return d;
    }

    public Dimension calcTileSize(int numberOfTilesX, int numberOfTilesY) {
        return new Dimension( calcTileSize( numberOfTilesX, HORIZONTAL ), calcTileSize( numberOfTilesY, VERTICAL ));
    }

    public int calcTileSize(int numberOfTiles, Alignment a) {
        switch (a) {
            case HORIZONTAL -> {
                int width = image.getWidth();
                width -= offsetX + spaceX * (numberOfTiles-1);
                return width / numberOfTiles;
            }
            case VERTICAL -> {
                int height = image.getHeight();
                height -= offsetY + spaceY * (numberOfTiles-1);
                return height / numberOfTiles;
            }
            default -> {
                return -1;
            }
        }
    }

    public Point getCursor() {
        return cursor;
    }

    public void resetCursor() {
        switch (d) {
            case RIGHT, DOWN -> {
                cursor.x = offsetX;
                cursor.y = offsetY;
            }
            case LEFT, UP -> {
                cursor.x = image.getWidth()  - offsetX;
                cursor.y = image.getHeight() - offsetY;
            }
        }
    }

    public void setCursorOnStart() {
        switch (d) {
            case RIGHT -> cursor.x = offsetX;
            case LEFT  -> cursor.x = image.getWidth() - offsetX;
            case DOWN  -> cursor.y = offsetY;
            case UP    -> cursor.y = image.getHeight() - offsetY;
        }
    }

    public BufferedImage nextImage() {
        if ( !hasNext() ) {
            return null;
        }

        BufferedImage subimg = image.getSubimage(cursor.x, cursor.y, tileSizeX, tileSizeY);
        moveCursorNext();
        return subimg;
    }

    public HasImage next() {
        return new TileBuilder.Tile( nextImage() );
    }

    public BufferedImage[] getTileSet(int number) {
        BufferedImage[] array = new BufferedImage[number];
        for (int i = 0; i < number; ++i) {
            if ( hasNext() ) {
                array[i] = nextImage();
            } else {
                throw new IndexOutOfBoundsException("Tile number " + (i+1) + " not available.");
            }
        }
        return array;
    }

    public void moveCursorRight() {
        cursor.x += spaceX + tileSizeX;
    }

    public void moveCursorLeft() {
        cursor.x -= spaceX + tileSizeX;
    }

    public void moveCursorDown() {
        cursor.y += spaceY + tileSizeY;
    }

    public void moveCursorUp() {
        cursor.y -= spaceY + tileSizeY;
    }

    public void moveCursor(Direction d) {
        switch (d) {
            case UP    -> moveCursorUp();
            case DOWN  -> moveCursorDown();
            case LEFT  -> moveCursorLeft();
            case RIGHT -> moveCursorRight();
        }
    }

    public void moveCursorNext() {
        switch(d) {
            case RIGHT -> moveCursorRight();
            case LEFT  -> moveCursorLeft();
            case UP    -> moveCursorUp();
            case DOWN  -> moveCursorDown();
        }
    }

    public boolean hasNext() {
        boolean a = cursor.x >= 0 && cursor.x + tileSizeX <= image.getWidth();
        boolean b = cursor.y >= 0 && cursor.y + tileSizeY <= image.getHeight();
        return a && b;
    }

}
