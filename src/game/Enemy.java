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

public class Enemy extends TextBox {

    private final game.combat.Enemy minion;

    public Enemy(int id, String name, game.combat.Enemy minion) {
        super(id, name);
        this.minion = minion;
    }

    public game.combat.Enemy getMinion() {
        return minion;
    }

}
