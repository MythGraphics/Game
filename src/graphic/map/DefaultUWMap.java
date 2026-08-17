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

import graphic.AnimatedSprite;
import graphic.Animation;
import graphic.MoveableSprite;
import graphic.Sprite;
import static graphic.io.BinaryIO.*;
import graphic.io.TilesetUtility;
import static graphic.map.DefaultMapTile.*;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;

public class DefaultUWMap extends UWMap {

    public final static Color AMBIENT_COLOR = new Color(12, 80, 200);

    private Image[] playerImg;
    private Image wallImg, bubbleImg;
    private Animation npcAni;

    public DefaultUWMap(char[][] tileMap) {
        super(tileMap);
        init();
    }

    @Override
    public Color getAmbientColor() {
        return AMBIENT_COLOR;
    }

    @Override
    protected void loadSprites() {
        playerImg = TilesetUtility.getSpriteSetVertical(
            loadImage(TILESET+"player/lpc_female_blond/idle2.png"), 0, tileSize, 4
        );
        wallImg = loadImage(SPRITE+"land/Stone1.png");
        bubbleImg = TilesetUtility.getSpriteSet(
            loadImage(TILESET+"uw/bubble.png"),
            new Point(0, 0),
            0, 0, 225, 1
        )[0];
    }

    @Override
    Block getBlock(IsMapTile bType, int x, int y, int tileSize) {
        switch (bType) {
            case PLAYER:
                return new MoveableSprite(
                    Animation.buildDirectionalImageSet(playerImg), null, x, y, tileSize, PLAYER, getMaxPoint()
                );
            case WALL5:
                return new Sprite(wallImg, x, y, tileSize, WALL5);
            case BUBBLE:
                return new Sprite(bubbleImg, x, y, tileSize, BUBBLE);
            case NPC:
                return new AnimatedSprite(npcAni, null, x, y, tileSize, NPC);
            default:
                return super.getBlock(bType, x, y, tileSize);
        }
    }

}
