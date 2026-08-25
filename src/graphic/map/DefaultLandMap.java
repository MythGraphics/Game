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
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class DefaultLandMap extends GameMap {

    public final static Color AMBIENT_COLOR = new Color(124, 188, 62);

    private final Map<IsBlockType, BufferedImage> imgMap = new HashMap<>();

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
            scaleImageSet(
                getSpriteSetVertical(
                    loadImage( TILESET+"player/lpc_female_blond/idle2.png" ), 0, DEFAULT_TILE_SIZE, 4
                ), DEFAULT_TILE_SIZE
            )
        );
        npcAniData = new AnimationData(
            scaleImageSet(
                getSpriteSetHorizontal(
                    loadImage( TILESET+"npc/lpc_male_blackbeard/idle2.png" ), 0, DEFAULT_TILE_SIZE, 2
                ), DEFAULT_TILE_SIZE
            )
        );
        portalAniData = new AnimationData(
            scaleImageSet(
                getSpriteSetHorizontal(
                    loadImage( TILESET+"portal.png" ), 0, DEFAULT_TILE_SIZE, 4
                ), DEFAULT_TILE_SIZE
            )
        );
        npcAni = new AnimationPlayer(npcAniData);
        npcAni.slowDown();
        imgMap.put( SPACE,          loadScaledImage(       SPRITE+"land/Gras1.png" ));
        imgMap.put( WALL5,          loadStretchedImage(    SPRITE+"land/Stone1.png" ));
        imgMap.put( TEXTSIGN,       loadScaledImage(       SPRITE+"land/Sign1.png" ));
        imgMap.put( ENVIRONMENT0,   loadStretchedImage(    SPRITE+"land/Straw1.png" ));
        imgMap.put( TERRAIN0,       loadScaledImage(       SPRITE+"land/Bush1.png" ));
        imgMap.put( TERRAIN1,       loadScaledImage(       SPRITE+"land/Bush2.png" ));
        imgMap.put( TERRAIN2,       loadScaledImage(       SPRITE+"land/Bush3.png" ));
        imgMap.put( TERRAIN3,       loadScaledImage(       SPRITE+"land/Bush4.png" ));
        imgMap.put( TERRAIN4,       loadScaledImage(       SPRITE+"land/Bush5.png" ));
        imgMap.put( TERRAIN5,       loadScaledImage(       SPRITE+"land/Bush5_berries.png" ));
        imgMap.put( TERRAIN6,       loadScaledImage(       SPRITE+"land/Mushroom1.png" ));
        imgMap.put( TERRAIN7,       loadScaledImage(       SPRITE+"land/Mushroom2.png" ));
        imgMap.put( WALL1,          loadScaledImage(       SPRITE+"land/Water.png" ));
        imgMap.put( WALL3,          loadScaledImage(       SPRITE+"land/Water2Land.png" ));
        imgMap.put( ENVIRONMENT1,   loadImage(             SPRITE+"land/House.png" ));
        imgMap.put( ENVIRONMENT2,   scaleImage( loadImage( SPRITE+"land/Tree1.png" ), 5 * DEFAULT_TILE_SIZE ));
    }

    @Override
    protected BlockTile getBlockTile(int x, int y, int width, int height, IsBlockType bType) {
        switch (bType) {
            case PLAYER:
                AnimationPlayer[] playerAni = AnimationPlayer.createSet(playerAniData);
                return new MoveableSprite(
                    playerAni, PLAYER, x, y, tileSize, getMaxPoint()
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
            case TERRAIN1:
            case TERRAIN2:
            case TERRAIN3:
            case TERRAIN4:
            case TERRAIN5:
            case TERRAIN6:
            case TERRAIN7:
            case TERRAIN8:
            case WALL1:
            case WALL3:
            case WALL5:
            case ENVIRONMENT0:
                return new BlockTile(x, y, tileSize, bType, () -> imgMap.get( bType ));
            case ENVIRONMENT1:
                return new BlockTile( x, y, ENVIRONMENT1, () -> imgMap.get( ENVIRONMENT1 ));
            case ENVIRONMENT2:
                return new BlockTile( x, y, ENVIRONMENT2, () -> imgMap.get( ENVIRONMENT2 ));
            default:
                return new BlockTile(x, y, tileSize, bType, null);
        }
    }

}
