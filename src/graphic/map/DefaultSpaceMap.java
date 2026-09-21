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
import graphic.io.TilesetUtility;
import static graphic.io.TilesetUtility.getSpriteSet;
import static graphic.io.TilesetUtility.scaleImageSet;
import static graphic.map.DefaultBlockType.*;
import static graphic.map.GameMap.DEFAULT_TILE_SIZE;
import graphic.tile.BlockTile;
import graphic.tile.MoveableTile;
import graphic.tile.TileBuilder;
import graphic.tile.TilesetBuilder;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class DefaultSpaceMap extends GameMap {

    public final static Color AMBIENT_COLOR = new Color(0, 0, 0);

    private final Map<IsBlockType, HasImage> imgMap = new HashMap<>();

    private DirectionalImage shipSet, playerProjectile;

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
        imgMap.put( WALL5, new TileBuilder.Tile( loadStretchedImage( SPRITE+"space/asteroidSmall.png" )));

        AnimationData portalAniData = new AnimationData(
            getSpriteSet(
                loadImage(TILESET+"space/portal.png"), 80, -1
            )
        );
        AnimationPlayer portalAni = new AnimationPlayer(portalAniData);
        portalAni.slowDown();
        imgMap.put(PORTAL, portalAni);

        AnimationData missileAniData = new AnimationData(
            scaleImageSet(
                getSpriteSet(
                    loadImage(TILESET+"BlizzardEntertainment/succubus_missile_fly.png"), 97, -1
                ), DEFAULT_TILE_SIZE
            )
        );
        playerProjectile = DirectionalImage.createSingleAnimation(missileAniData);

        TilesetBuilder builder = new TilesetBuilder( loadImage( TILESET+"space/ships.png" ), 36, 36 );
        builder.setDirection(Direction.DOWN);
        BufferedImage[] ship1 = builder.getTileSet(4);
        ship1 = TilesetUtility.scaleImageSet(ship1, tileSize);
        shipSet = new DirectionalImage( DirectionalImage.create( ship1 ));
    }

    @Override
    protected BlockTile getBlockTile(int x, int y, IsBlockType bType) {
        switch (bType) {
            case PLAYER:
                MoveableTile playerTile = new MoveableTile(x, y, PLAYER, tileSize, getMaxPoint(), shipSet);
                playerTile.setMissileImage(playerProjectile);
                return playerTile;
            default:
                return new BlockTile( x, y, tileSize, bType, imgMap.get( bType ));
        }
    }

}
