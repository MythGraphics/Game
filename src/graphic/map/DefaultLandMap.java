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

import graphic.*;
import static graphic.io.BinaryIO.BINARYIO;
import graphic.io.TilesetUtility;
import static graphic.map.BlockType.*;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class DefaultLandMap extends GameMap {

    public final static String[] DEFAULT_TILE_MAP = {
        "..........XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
        "..........J...                                #",
        ".............. O                              X",
        "......K.......                                X",
        "XXXXXX.....N..                                X",
        "X     ...                                     X",
        "X     ...                                     X",
        "X                                             X",
        "X                                             X",
        "X 11111111 11111111                           X",
        "X 22222222 22222222                           X",
        "X 33333333 33333333                           X",
        "X 44444444 44444444                           X",
        "X 55555555 55555555                           X",
        "X 66666666 66666666                           X",
        "X 77777777 77777777                           X",
        "X 88888888 88888888                           X",
        "X                                             X",
        "X                                             X",
        "X   I T O                                     X",
        "X                                             X",
        "X                    P                        X",
        "WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW",
        "VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV",
    };
    public final static Color AMBIENT_COLOR = new Color(124, 188, 62);

    private final Map<BlockType, BufferedImage> imgMap = new HashMap<>();

    private Image[] playerImg;
    private Animation npcAni, portalAni;

    public DefaultLandMap() {
        this(DEFAULT_TILE_MAP);
    }

    public DefaultLandMap(String[] tileMap) {
        super(tileMap);
    }

    @Override
    public Color getAmbientColor() {
        return AMBIENT_COLOR;
    }

    @Override
    void loadSprites() {
        playerImg = TilesetUtility.getSpriteSetVertical(
            BINARYIO.loadImage(BINARYIO.TILESET+"player/lpc_female_blond/idle2.png"), 0, 4
        );
        npcAni = new Animation( TilesetUtility.getSpriteSetHorizontal(
            BINARYIO.loadImage(BINARYIO.TILESET+"npc/lpc_male_blackbeard/idle2.png"), 0, 2
        ));
        npcAni.slowDown();
        portalAni = new Animation( TilesetUtility.getSpriteSetHorizontal(
            BINARYIO.loadImage(BINARYIO.TILESET+"portal.png"), 0, 4
        ));
        portalAni.slowDown();
        imgMap.put( SPACE,          BINARYIO.loadImage( BINARYIO.SPRITE+"land/Gras1.png" ));
        imgMap.put( WALL5,          BINARYIO.loadImage( BINARYIO.SPRITE+"land/Stone1.png" ));
        imgMap.put( TEXTSIGN,       BINARYIO.loadImage( BINARYIO.SPRITE+"land/Sign1.png" ));
        imgMap.put( ENVIRONMENT_A,  BINARYIO.loadImage( BINARYIO.SPRITE+"land/Straw1.png" ));
        imgMap.put( ENVIRONMENT1,   BINARYIO.loadImage( BINARYIO.SPRITE+"land/Bush1.png" ));
        imgMap.put( ENVIRONMENT2,   BINARYIO.loadImage( BINARYIO.SPRITE+"land/Bush2.png" ));
        imgMap.put( ENVIRONMENT3,   BINARYIO.loadImage( BINARYIO.SPRITE+"land/Bush3.png" ));
        imgMap.put( ENVIRONMENT4,   BINARYIO.loadImage( BINARYIO.SPRITE+"land/Bush4.png" ));
        imgMap.put( ENVIRONMENT5,   BINARYIO.loadImage( BINARYIO.SPRITE+"land/Bush5.png" ));
        imgMap.put( ENVIRONMENT6,   BINARYIO.loadImage( BINARYIO.SPRITE+"land/Bush5_berries.png" ));
        imgMap.put( ENVIRONMENT7,   BINARYIO.loadImage( BINARYIO.SPRITE+"land/Mushroom1.png" ));
        imgMap.put( ENVIRONMENT8,   BINARYIO.loadImage( BINARYIO.SPRITE+"land/Mushroom2.png" ));
        imgMap.put( WALL1,          BINARYIO.loadImage( BINARYIO.SPRITE+"land/Water.png" ));
        imgMap.put( WALL3,          BINARYIO.loadImage( BINARYIO.SPRITE+"land/Water2Land.png" ));
        imgMap.put( ENVIRONMENT_B,  BINARYIO.loadImage( BINARYIO.SPRITE+"land/House.png" ));
        imgMap.put( ENVIRONMENT_C,  BINARYIO.loadImage( BINARYIO.SPRITE+"land/Tree1.png" ));
    }

    @Override
    Block getBlock(BlockType bType, int x, int y, int tileSize) {
        switch (bType) {
            case PLAYER:
                return new MoveableSprite(
                    Animation.buildDirectionalImageSet(playerImg), x, y, tileSize, PLAYER, getMaxPoint()
                );
            case NPC:
                return new AnimatedBlock(npcAni, x, y, tileSize, NPC );
            case PORTAL:
                return new AnimatedBlock(portalAni, x, y, tileSize, PORTAL );
            case SPACE:
            case TEXTSIGN:
            case ENVIRONMENT1:
            case ENVIRONMENT2:
            case ENVIRONMENT3:
            case ENVIRONMENT4:
            case ENVIRONMENT5:
            case ENVIRONMENT6:
            case ENVIRONMENT7:
            case ENVIRONMENT8:
            case WALL1:
            case WALL3:
            case WALL5:
            case ENVIRONMENT_A:
                return new Sprite( imgMap.get( bType ), x, y, tileSize, bType );
            case ENVIRONMENT_B:
                return new Sprite( imgMap.get( ENVIRONMENT_B ), x, y, 128, ENVIRONMENT_B );
            case ENVIRONMENT_C:
                return new Sprite( imgMap.get( ENVIRONMENT_C ), x, y, 128, ENVIRONMENT_C );
        }
        return super.getBlock(bType, x, y, tileSize);
    }

}
