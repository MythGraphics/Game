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
import java.util.Collections;
import javax.swing.DefaultListModel;

public class MinionManager {

    private final DefaultListModel<Combatant> listModel = new DefaultListModel<>();

    public MinionManager() {}

    public DefaultListModel<Combatant> getListModel() {
        return listModel;
    }

    public void remove(Combatant minion) {
        listModel.removeElement(minion);
    }

    public void add(Combatant minion) {
        listModel.addElement(minion);
    }

    public Combatant get(int index) {
        return listModel.get(index);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for ( Combatant minion : Collections.list( listModel.elements() )) {
            sb.append( minion.getUIString() );
            sb.append( ", " );
        }
        return sb.toString();
    }

}
