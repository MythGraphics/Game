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
import graphic.HasImage;
import static graphic.io.BinaryIO.*;
import static graphic.io.TilesetUtility.*;
import static graphic.map.DefaultBlockType.*;
import graphic.tile.MoveableTile;
import graphic.tile.TileBuilder;
import graphic.tile.TileBuilder.Tile;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class DefaultLandMap extends GameMap {

    public final static Color AMBIENT_COLOR = new Color(124, 188, 62);

    private final Map<IsBlockType, HasImage> imgMap = new HashMap<>();
    private final TileBuilder terrainBuilder = new TileBuilder();

    private AnimationData[] playerAniData;
    private AnimationData npcAniData, portalAniData;
    private AnimationPlayer npcAni;

    public DefaultLandMap(char[][] tileMap) {
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
        npcAniData = new AnimationData( getSpriteSetHorizontal(
            loadImage(TILESET+"npc/lpc_male_blackbeard/idle2.png"), 0, DEFAULT_TILE_SIZE, 2
        ));
        portalAniData = new AnimationData( scaleImageSet(
            getSpriteSetHorizontal(
                loadImage(TILESET+"portal.png"), 0, DEFAULT_TILE_SIZE, 4
            ), DEFAULT_TILE_SIZE
        ));
        terrainBuilder.add( loadScaledImage( SPRITE+"land/Bush1.png" ));
        terrainBuilder.add( loadScaledImage( SPRITE+"land/Bush2.png" ));
        terrainBuilder.add( loadScaledImage( SPRITE+"land/Bush3.png" ));
        terrainBuilder.add( loadScaledImage( SPRITE+"land/Bush4.png" ));
        terrainBuilder.add( loadScaledImage( SPRITE+"land/Bush5.png" ));
        terrainBuilder.add( loadScaledImage( SPRITE+"land/Bush5_berries.png" ));
        terrainBuilder.add( loadScaledImage( SPRITE+"land/Mushroom1.png" ));
        terrainBuilder.add( loadScaledImage( SPRITE+"land/Mushroom2.png" ));
        npcAni = new AnimationPlayer(npcAniData);
        npcAni.slowDown();
        imgMap.put( SPACE,          new Tile( loadScaledImage(       SPRITE+"land/Gras1.png" )));
        imgMap.put( WALL5,          new Tile( loadStretchedImage(    SPRITE+"land/Stone1.png" )));
        imgMap.put( TEXTSIGN,       new Tile( loadScaledImage(       SPRITE+"land/Sign1.png" )));
        imgMap.put( ENVIRONMENT0,   new Tile( loadStretchedImage(    SPRITE+"land/Straw1.png" )));
        imgMap.put( WALL1,          new Tile( loadScaledImage(       SPRITE+"land/Water.png" )));
        imgMap.put( WALL3,          new Tile( loadScaledImage(       SPRITE+"land/Water2Land.png" )));
        imgMap.put( ENVIRONMENT1,   new Tile( loadImage(             SPRITE+"land/House.png" )));
        imgMap.put( ENVIRONMENT2,   new Tile( scaleImage( loadImage( SPRITE+"land/Tree1.png" ), 5*DEFAULT_TILE_SIZE )));
    }

    @Override
    protected BlockTile getBlockTile(int x, int y, int width, int height, IsBlockType bType) {
        switch (bType) {
            case PLAYER:
                AnimationPlayer[] playerAni = AnimationPlayer.createSet(playerAniData);
                return new MoveableTile(
                    x, y, tileSize, PLAYER, getMaxPoint(), playerAni
                );
            case NPC:
                return new BlockTile(x, y, tileSize, NPC, npcAni);
            case PORTAL:
                // für jedes Portal einen eigenen AnimationPlayer erstellen
                AnimationPlayer portalAni = new AnimationPlayer(portalAniData);
                portalAni.slowDown();
                return new BlockTile(x, y, tileSize, PORTAL, portalAni);
            case SPACE:
            case TEXTSIGN:
            case WALL1:
            case WALL3:
            case WALL5:
            case ENVIRONMENT0:
            case ENVIRONMENT1:
            case ENVIRONMENT2:
                return new BlockTile( x, y, tileSize, bType, imgMap.get( bType ));
            case TERRAIN:
                return new BlockTile( x, y, tileSize, bType, terrainBuilder.getRandom() );
            default:
                return new BlockTile( x, y, tileSize, bType, null );
        }
    }

}
