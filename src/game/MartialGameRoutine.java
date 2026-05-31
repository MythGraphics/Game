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

import game.combat.CombatFrame;
import game.combat.Combatant;
import graphic.CollisionEvent;
import static graphic.CollisionType.ENEMY;
import graphic.DeadOrAlive;
import java.util.ArrayList;
import java.util.List;

public abstract class MartialGameRoutine extends GameRoutine {

    final List<Enemy> enemyList;

    public MartialGameRoutine(List<Enemy> enemyList, GameFrame frame) {
        super(frame);
        if (enemyList == null) {
            this.enemyList = new ArrayList<>();
        } else {
            this.enemyList = enemyList;
        }
    }

    abstract void loot(Combatant enemy);
    abstract void playerMinionDead(Combatant player);

    public void addEnemy(Enemy enemy) {
        enemyList.add(enemy);
    }

    public void removeEnemy(Enemy enemy) {
        enemyList.remove(enemy);
    }

    public Enemy getEnemy() {
        if ( !enemyList.isEmpty() ) {
            return enemyList.remove( rand.nextInt( enemyList.size() ));
        } else {
            return null;
        }
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        super.collisionPerformed(e);
        switch( e.getCollisionType() ) {
            case ENEMY -> {
                Combatant enemyMinion  = getEnemy().getMinion();
                Combatant playerMinion = getPlayer().getPlayerAsMinion();
                if ( enemyMinion == null || playerMinion == null ) {
                    return;
                }
                if ( !enemyMinion.isAlive() ) {
                    enemyMinion.resurrect();
                    enemyMinion.setLevel(( byte ) ( playerMinion.getLevel()+1 ));
                }
                CombatFrame cFrame = new CombatFrame(frame, playerMinion, enemyMinion);
                cFrame.setVisible(true);
                if ( !enemyMinion.isAlive() ) {
                    ((DeadOrAlive) e.block).dead();
                }
                if ( !playerMinion.isAlive() ) {
                    playerMinionDead(playerMinion);
                }
                if ( playerMinion.isAlive() && !enemyMinion.isAlive() ) {
                    loot(enemyMinion);
                }
            }
        }
    }

}
