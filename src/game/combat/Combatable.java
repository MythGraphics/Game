/*
 *
 */

package game.combat;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import game.HasName;
import game.Resource;
import java.util.List;

public interface Combatable extends HasName {

    byte getLevel();
    Resource getHealth();
    List<Damage> makeDamage();
    void takeDamage(List<Damage> dmgList);
    boolean isAlive();

}
