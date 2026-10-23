/*
 *
 */

package game.routine;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import game.GameFrame;
import game.Player;
import game.resource.Resource;
import static game.resource.Resource.ResourceType.MANA;
import static graphic.io.BinaryIO.SPRITE;
import static graphic.io.BinaryIO.loadImage;
import graphic.map.DefaultSpaceMap;

public class SpaceGameRoutine extends MartialGameRoutine {

    public SpaceGameRoutine(GameFrame gameFrame, DefaultSpaceMap map) {
        super(gameFrame, map);
    }

    @Override
    public void initMap() {
        ((DefaultSpaceMap) map).init( getPlayer() );
    }

    @Override
    protected Player buildPlayer() {
        Player p = super.buildPlayer();
        Resource mana = new Resource("Energie", MANA, 1000, 1000);
        mana.addResourceChangeListener(gameFrame);
        p.addResource(mana);
        p.setImage( loadImage( SPRITE + "player/man1.png" ));
        return p;
    }

}
