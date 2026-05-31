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

    private game.combat.Enemy minion;

    public Enemy(int id, String name, game.combat.Enemy minion) {
        super(id, name);
        this.minion = minion;
    }

    protected Enemy(Enemy enemy) {
        super(enemy);
        this.minion = enemy.getMinion();
    }

    public game.combat.Enemy getMinion() {
        return minion;
    }

    public void setMinion(game.combat.Enemy minion) {
        this.minion = minion;
    }

    // shallow copy
    @Override
    public Enemy clone() throws CloneNotSupportedException {
        return new Enemy(this);
    }

}
