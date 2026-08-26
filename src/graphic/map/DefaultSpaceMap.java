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
import static graphic.io.TilesetUtility.*;
import static graphic.map.DefaultBlockType.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class DefaultSpaceMap extends GameMap {

    public final static Color AMBIENT_COLOR = new Color(50, 50, 50);

    private AnimationData[] playerAniData;
    private AnimationPlayer[] playerAni;
    private AnimationData enemyAniData;

    private final Map<IsBlockType, BufferedImage> imgMap = new HashMap<>();

    public DefaultSpaceMap(char[][] tileMap) {
        super(tileMap);
        init();
    }

    @Override
    public Color getAmbientColor() {
        return AMBIENT_COLOR;
    }

    @Override
    protected void loadSprites() {
        BufferedImage[][] tileset = scaleDirectionalAnimationSet(
            getAnimationSet(
                loadImage( TILESET+"spaceship/creatures2.png" ), 32, 3
            ), DEFAULT_TILE_SIZE
        );
        BufferedImage[] corpseset = scaleImageSet(
            getSpriteSet(
                loadImage( TILESET+"spaceship/creatures2.png" ), new Point( 3*32, 0 ), 0, 0, 32, -1
            ), DEFAULT_TILE_SIZE
        );
        imgMap.put( ENVIRONMENT0, loadStretchedImage( SPRITE+"land/Straw1.png" ));
        imgMap.put( WALL0, loadScaledImage( SPRITE+"spaceship/wall1.png" ));
        imgMap.put( WALL1, loadScaledImage( SPRITE+"spaceship/wall2.png" ));
        imgMap.put( WALL2, loadScaledImage( SPRITE+"spaceship/wall3.png" ));
        imgMap.put( WALL3, loadScaledImage( SPRITE+"spaceship/wall4.png" ));
        imgMap.put( WALL4, loadScaledImage( SPRITE+"spaceship/wall5.png" ));
        imgMap.put( WALL5, loadScaledImage( SPRITE+"spaceship/wall6.png" ));
        imgMap.put( WALL6, loadScaledImage( SPRITE+"spaceship/wall7.png" ));
        imgMap.put( WALL7, loadScaledImage( SPRITE+"spaceship/wall8.png" ));
        imgMap.put( SPACE, loadScaledImage( SPRITE+"spaceship/floor.png" ));
        imgMap.put( CORPSE_ENEMY,  corpseset[5] );
        imgMap.put( CORPSE_PLAYER, corpseset[3] );
        enemyAniData  = new AnimationData(tileset[5], true);
        playerAniData = AnimationData.buildDirectionalAnimationSet(
            scaleDirectionalAnimationSet(
                getAnimationSet(
                    loadImage( TILESET+"spaceship/spacemarine.png" ), 32, 3
                ), DEFAULT_TILE_SIZE
            )
        );
        playerAni = AnimationPlayer.createSet(playerAniData);
        for (AnimationPlayer ani : playerAni) {
            ani.slowDown();
        }
    }

    @Override
    protected BlockTile getBlockTile(int x, int y, int width, int height, IsBlockType bType) {
        switch (bType) {
            case PLAYER:
                return new MoveableSprite( playerAni, PLAYER, x, y, tileSize, getMaxPoint() );
            case ENEMY:
                // für jeden Gegner einen eigenen AnimationPlayer erstellen
                AnimationPlayer enemyAni = new AnimationPlayer(enemyAniData);
                DeadOrAliveTile doaTile = new DeadOrAliveTile(x, y, tileSize, ENEMY, enemyAni);
                doaTile.setDeadData(CORPSE_ENEMY, () -> imgMap.get( CORPSE_ENEMY ));
                return doaTile;
            default:
                return new BlockTile(x, y, tileSize, bType, () -> imgMap.get( bType ));
        }
    }

}
