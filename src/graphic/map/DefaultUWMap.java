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

import graphic.AnimationData;
import graphic.AnimationPlayer;
import graphic.MoveableSprite;
import static graphic.io.BinaryIO.*;
import graphic.io.TilesetUtility;
import static graphic.map.DefaultBlockType.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class DefaultUWMap extends UWMap {

    public final static Color AMBIENT_COLOR = new Color(12, 80, 200);

    private BufferedImage[] playerImg;
    private BufferedImage wallImg, bubbleImg;
    private AnimationData npcAniData;

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
    protected BlockTile getBlockTile(int x, int y, int width, int height, IsBlockType bType) {
        switch (bType) {
            case PLAYER:
                return new MoveableSprite(
                    AnimationData.buildDirectionalImageSet(playerImg), PLAYER, x, y, tileSize, getMaxPoint()
                );
            case WALL5:
                return new BlockTile(x, y, tileSize, WALL5, () -> wallImg);
            case BUBBLE:
                return new BlockTile(x, y, tileSize, BUBBLE, () -> bubbleImg);
            case NPC:
                AnimationPlayer npcAni = new AnimationPlayer(npcAniData);
                return new BlockTile(x, y, tileSize, NPC, npcAni);
            default:
                return new BlockTile(x, y, tileSize, bType, null);
        }
    }

}
