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
import static graphic.io.BinaryIO.*;
import static graphic.io.ImageUtility.flipImage;
import static graphic.io.ImageUtility.scale;
import static graphic.io.TilesetUtility.getSpriteSet;
import static graphic.io.TilesetUtility.getSpriteSetVertical;
import static graphic.map.DefaultBlockType.*;
import static graphic.map.GameMap.DEFAULT_TILE_SIZE;
import graphic.tile.BlockTile;
import graphic.tile.MoveableTile;
import graphic.tile.TileBuilder;
import graphic.tile.TilesetBuilder;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class DefaultUWMap extends UWMap {

    public final static Color AMBIENT_COLOR = new Color(12, 80, 200);

    private final Map<IsBlockType, HasImage> imgMap = new HashMap<>();

    private DirectionalImage playerAniSet, playerProjectile;

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
        AnimationData[] playerAniData = AnimationData.buildDirectionalImageSet(
            getSpriteSetVertical(
                loadImage(TILESET+"player/lpc_female_blond/idle2.png"), 0, DEFAULT_TILE_SIZE, 4
            ), "ULDR"
        );
        playerAniSet = new DirectionalImage( AnimationPlayer.createSet( playerAniData ));

        imgMap.put( WALL5,  new TileBuilder.Tile( loadStretchedImage( SPRITE+"land/Stone1.png" )));
        imgMap.put( BUBBLE, new TileBuilder.Tile( scale(
            getSpriteSet(
                loadImage(TILESET+"uw/bubble.png"), new Point(0, 0), 0, 0, 225, 1
            )[0], DEFAULT_TILE_SIZE
        )));

        TilesetBuilder builder = new TilesetBuilder( loadImage( TILESET+"dragon/dragon.png" ), 88, 82 );
        builder.setDirection(Direction.DOWN);
        BufferedImage[] up = builder.getTileSet(4);
        builder.setCursorOnStart();
        builder.moveCursorRight();
        builder.moveCursorRight();
        BufferedImage[] right = builder.getTileSet(4);
        builder.setCursorOnStart();
        builder.moveCursorRight();
        builder.moveCursorRight();
        BufferedImage[] down = builder.getTileSet(4);
        BufferedImage[] left = new BufferedImage[4];
        for (int i = 0; i < left.length; ++i) {
            left[i] = flipImage(right[i], true);
        }
        AnimationData[] aniDataSet = AnimationData.buildDirectionalAnimationSet( new BufferedImage[][] {
            up, right, down, left
        });
        AnimationPlayer[] aniSet = AnimationPlayer.createSet(aniDataSet);
        playerProjectile = new DirectionalImage(aniSet);
    }

    @Override
    protected BlockTile getBlockTile(int x, int y, IsBlockType bType) {
        switch (bType) {
            case PLAYER:
                MoveableTile playerTile = new MoveableTile(x, y, PLAYER, tileSize, getMaxPoint(), playerAniSet);
                playerTile.setProjectileImage(playerProjectile);
                return playerTile;
            case WALL5:
            case BUBBLE:
                return new BlockTile( x, y, tileSize, bType, imgMap.get( bType ));
            default:
                return new BlockTile( x, y, tileSize, bType, null );
        }
    }

}
