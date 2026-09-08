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

import graphic.tile.TileBuilder;
import graphic.AnimationData;
import graphic.AnimationPlayer;
import graphic.HasImage;
import graphic.tile.MoveableTile;
import static graphic.io.BinaryIO.*;
import static graphic.io.ImageUtility.scale;
import static graphic.io.TilesetUtility.getSpriteSet;
import static graphic.io.TilesetUtility.getSpriteSetVertical;
import static graphic.map.DefaultBlockType.*;
import static graphic.map.GameMap.DEFAULT_TILE_SIZE;
import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class DefaultUWMap extends UWMap {

    public final static Color AMBIENT_COLOR = new Color(12, 80, 200);

    private final Map<IsBlockType, HasImage> imgMap = new HashMap<>();

    private AnimationData[] playerAniData;

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
        playerAniData = AnimationData.buildDirectionalImageSet(
            getSpriteSetVertical(
                loadImage(TILESET+"player/lpc_female_blond/idle2.png"), 0, DEFAULT_TILE_SIZE, 4
            ), "ULDR"
        );
        imgMap.put( WALL5,  new TileBuilder.Tile( loadStretchedImage( SPRITE+"land/Stone1.png" )));
        imgMap.put( BUBBLE, new TileBuilder.Tile( scale(
            getSpriteSet(
                loadImage(TILESET+"uw/bubble.png"), new Point(0, 0), 0, 0, 225, 1
            )[0], DEFAULT_TILE_SIZE
        )));
    }

    @Override
    protected BlockTile getBlockTile(int x, int y, int width, int height, IsBlockType bType) {
        switch (bType) {
            case PLAYER:
                AnimationPlayer[] playerAni = AnimationPlayer.createSet(playerAniData);
                return new MoveableTile(
                    playerAni, PLAYER, x, y, tileSize, getMaxPoint()
                );
            case WALL5:
            case BUBBLE:
                return new BlockTile(x, y, tileSize, bType, imgMap.get( bType ));
            default:
                return new BlockTile(x, y, tileSize, bType, null);
        }
    }

}
