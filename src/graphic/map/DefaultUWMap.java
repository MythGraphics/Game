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
import graphic.DirectionalImage;
import graphic.HasImage;
import static graphic.io.BinaryIO.*;
import static graphic.io.ImageUtility.scale;
import static graphic.io.TilesetUtility.*;
import static graphic.map.DefaultBlockType.*;
import static graphic.map.GameMap.DEFAULT_TILE_SIZE;
import graphic.tile.BlockTile;
import graphic.tile.MoveableTile;
import graphic.tile.TileBuilder;
import java.awt.Color;
import java.awt.Point;
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
        imgMap.put( WALL5,  new TileBuilder.Tile( loadStretchedImage( SPRITE+"land/Stone1.png" )));

        imgMap.put( BUBBLE, new TileBuilder.Tile( scale(
            getSpriteSet(
                loadImage(TILESET+"uw/bubble.png"), new Point(0, 0), 0, 0, 225, 1
            )[0], DEFAULT_TILE_SIZE
        )));

        AnimationData[] playerAniData = AnimationData.buildDirectionalImageSet(
            getSpriteSetVertical(
                loadImage(TILESET+"player/lpc_female_blond/idle2.png"), 0, DEFAULT_TILE_SIZE, 4
            ), "ULDR"
        );
        playerAniSet = new DirectionalImage( AnimationPlayer.createSet( playerAniData ));

        AnimationData missileAniData = new AnimationData(
            scaleImageSet(
                getSpriteSet(
                    loadImage(TILESET+"BlizzardEntertainment/succubus_missile_fly.png"), 97, -1
                ), DEFAULT_TILE_SIZE
            )
        );
        playerProjectile = DirectionalImage.createSingleAnimation(missileAniData);
    }

    @Override
    protected BlockTile getBlockTile(int x, int y, IsBlockType bType) {
        switch (bType) {
            case PLAYER:
                MoveableTile playerTile = new MoveableTile(x, y, PLAYER, tileSize, getMaxPoint(), playerAniSet);
                playerTile.setMissileImage(playerProjectile);
                return playerTile;
            default:
                return new BlockTile( x, y, tileSize, bType, imgMap.get( bType ));
        }
    }

}
