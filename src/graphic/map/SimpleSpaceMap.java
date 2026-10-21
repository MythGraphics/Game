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

import game.resource.Resource;
import static game.resource.Resource.ResourceType.HEALTH;
import static graphic.Direction.UP;
import graphic.*;
import static graphic.block.DefaultBlockType.*;
import graphic.block.IsBlockType;
import static graphic.io.BinaryIO.*;
import graphic.io.TilesetUtility;
import static graphic.io.TilesetUtility.getSpriteSet;
import static graphic.io.TilesetUtility.scaleImageSet;
import static graphic.map.GameMap.DEFAULT_TILE_SIZE;
import static graphic.map.GameMap.loadStretchedImage;
import graphic.missileLauncher.DefaultMissileLauncher;
import graphic.tile.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class SimpleSpaceMap extends SimpleMap {

    private final Map<IsBlockType, HasImage> imgMap = new HashMap<>();

    private Resource playerHealth;

    public SimpleSpaceMap(char[][] tileMap) {
        super(tileMap);
    }

    public void init(Resource playerHealth) {
        this.playerHealth = playerHealth;
        super.init();
    }

    @Override
    public void init() {}

    @Override
    public Color getAmbientColor() {
        return Color.BLACK;
    }

    @Override
    protected void loadSprites() {
        imgMap.put( WALL5,  new TileBuilder.Tile( loadStretchedImage( SPRITE+"space/asteroid1.png" )));
        imgMap.put( CORPSE, new TileBuilder.Tile( loadStretchedImage( SPRITE+"space/wreck.png" )));

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
        HasImage playerProjectile = new AnimationPlayer(missileAniData);
        imgMap.put(MISSILE, playerProjectile);

        TilesetBuilder builder = new TilesetBuilder( loadImage( TILESET+"space/ships.png" ), 36, 36 );
        builder.setDirection(Direction.DOWN);
        BufferedImage[][] ships = new BufferedImage[8][];
        for (int i = 0; i < ships.length; ++i) {
            ships[i] = TilesetUtility.scaleImageSet(builder.getTileSet(4), tileSize);
        }
        imgMap.put( ENEMY, () -> ships[0][2] );

        DirectionalImage playerTile = new DirectionalImage( UP, DirectionalImage.create( ships[7] ));
        imgMap.put(PLAYER, playerTile);
    }

    @Override
    protected IsBlockTile getBlockTile(int x, int y, IsBlockType bType) {
        switch (bType) {
            case PLAYER:
                DeadOrAliveTileDecorator<ManeuverableTile> player = new DeadOrAliveTileDecorator<>(
                    new ManeuverableTile(x, y, PLAYER, GameMap.DEFAULT_TILE_SIZE, new Point(
                        Integer.MAX_VALUE, Integer.MAX_VALUE
                    ), (DirectionalImage) imgMap.get( PLAYER ))
                );
                player.setMissileLauncher( new DefaultMissileLauncher( player, imgMap.get( MISSILE )));
                player.setOverlayResource(playerHealth);
                return player;
            case ENEMY:
                DeadOrAliveTileDecorator<BlockTile> enemyTile = new DeadOrAliveTileDecorator<>(
                    new BlockTile(x, y, tileSize, ENEMY, imgMap.get( bType ))
                );
                enemyTile.setOverlayResource( new Resource( "HP", HEALTH, 25, 25 ));
                enemyTile.setDeadTile( CORPSE, imgMap.get( CORPSE ));
                return enemyTile;
            default:
                return new BlockTile( x, y, tileSize, bType, imgMap.get( bType ));
        }
    }

}
