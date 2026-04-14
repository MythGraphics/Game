/*
 *
 */

package game;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import game.combat.Combatant;
import javax.swing.JFrame;

public class PeacefulGameRoutine extends GameRoutine {

    public PeacefulGameRoutine(Player player, String audioTrackListFileString, JFrame frame) {
        super(player, null, audioTrackListFileString, frame);
    }

    @Override
    void loot(Combatant enemy) {}

}
