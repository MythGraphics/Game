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

import graphic.AnimatedBlock;
import graphic.Animation;
import graphic.MoveableSprite;
import graphic.Sprite;
import static graphic.io.BinaryIO.SPRITE;
import static graphic.io.BinaryIO.loadImage;
import graphic.io.TilesetUtility;
import static graphic.map.BlockType.*;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class DefaultSpaceMap extends GameMap {

    public final static String[] DEFAULT_TILE_MAP = {
        "vxxxxxxxxxxxxxxxxxxxxxxxxx#xxxxxxxxxxxxxxxxxxxxxxxxxV",
        "Y                                                   y",
        "Y vxxxxxxxV vxxxxxxxV vxxxxxxxV vxxxxxxxV vxxxxxxxV y",
        "Y Y       y Y       y Y       y Y       y Y       y y",
        "Y Y   E   y Y   E   y Y   E   y Y   E   y Y   E   y y",
        "Y Y       y Y       y Y       y Y       y Y       y y",
        "Y W XXXXXXw WXXXXXX w WXXX XXXw W XXXXXXw WXXXXXX w y",
        "Y                                                   y",
        "Y vxxxxxxxV vxxxxxxxV vxxxxxxxV vxxxxxxxV vxxxxxxxV y",
        "Y Y       y Y       y Y       y Y       y Y       y y",
        "Y Y   E   y Y   E   y Y   E   y Y   E   y Y   E   y y",
        "Y Y       y Y       y Y       y Y       y Y       y y",
        "Y W XXXXXXw WXXXXXX w WXXX XXXw W XXXXXXw WXXXXXX w y",
        "Y                                                   y",
        "Y vxxxxxxxV vxxxxxxxV vxxxxxxxV vxxxxxxxV vxxxxxxxV y",
        "Y Y       y Y       y Y       y Y       y Y       y y",
        "Y Y   E   y Y   E   y Y   E   y Y   E   y Y   E   y y",
        "Y Y       y Y       y Y       y Y       y Y       y y",
        "Y W XXXXXXw WXXXXXX w WXXX XXXw W XXXXXXw WXXXXXX w y",
        "Y                                                   y",
        "Y I                                                 y",
        "Y                         P                         y",
        "WXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXw",
    };
    public final static Color AMBIENT_COLOR = new Color(50, 50, 50);

    private Animation[] playerAni;
    private Animation enemyAni;

    private final Map<BlockType, Image> imgMap = new HashMap<>();

    public DefaultSpaceMap() {
        this(DEFAULT_TILE_MAP);
    }

    public DefaultSpaceMap(String[] tileMap) {
        super(tileMap);
    }

    @Override
    public Color getAmbientColor() {
        return AMBIENT_COLOR;
    }

    @Override
    void loadSprites() {
        Image[][] tileset = TilesetUtility.getAnimationSet(
            loadImage("tilesets/spaceship/creatures2.png"), 0, 0, 32, 32, 3
        );
        Image[] corpseset = TilesetUtility.getSpriteSet(
            loadImage("tilesets/spaceship/creatures2.png"), new Point(3*32, 0), 0, 0, 32, -1
        );
        imgMap.put(ENVIRONMENT_A, loadImage(SPRITE+"land/Straw1.png"));
        imgMap.put(WALL0, loadImage("tilesets/spaceship/wall1.png"));
        imgMap.put(WALL1, loadImage("tilesets/spaceship/wall2.png"));
        imgMap.put(WALL2, loadImage("tilesets/spaceship/wall3.png"));
        imgMap.put(WALL3, loadImage("tilesets/spaceship/wall4.png"));
        imgMap.put(WALL4, loadImage("tilesets/spaceship/wall5.png"));
        imgMap.put(WALL5, loadImage("tilesets/spaceship/wall6.png"));
        imgMap.put(WALL6, loadImage("tilesets/spaceship/wall7.png"));
        imgMap.put(WALL7, loadImage("tilesets/spaceship/wall8.png"));
        imgMap.put(SPACE, loadImage("tilesets/spaceship/floor.png"));
        imgMap.put(CORPSE, corpseset[5]);
        enemyAni = new Animation(tileset[5], true);
        playerAni = Animation.buildDirectionalAnimationSet( TilesetUtility.getAnimationSet(
            loadImage("tilesets/spaceship/spacemarine.png"), 0, 0, 32, 32, 3
        ));
        for (Animation a : playerAni) {
            a.slowDown();
        }
    }

    @Override
    Block getBlock(BlockType bType, int x, int y, int tileSize) {
        switch (bType) {
            case PLAYER:
                return new MoveableSprite( playerAni, x, y, tileSize, PLAYER, getMaxPoint() );
            case ENVIRONMENT_A:
            case WALL0:
            case WALL1:
            case WALL2:
            case WALL3:
            case WALL4:
            case WALL5:
            case WALL6:
            case WALL7:
            case WALL8:
            case EXIT:
            case SPACE:
            case SPACEHOLDER:
                return new Sprite( imgMap.get(bType), x, y, tileSize, bType );
            case ENEMY:
                // da sich mehrere Gegner die selbe Animation teilen, diese kopieren
                AnimatedBlock enemy = new AnimatedBlock( enemyAni.copy(), x, y, tileSize, ENEMY );
                enemy.setDeadImage( imgMap.get( CORPSE ));
                return enemy;
        }
        return super.getBlock(bType, x, y, tileSize);
    }

}
