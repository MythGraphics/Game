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

    /**
     * Overwrite to implement any action, if the player doesn't survided.
     * @param player Player
     * @param collider Sprite (player) rendered on the map
     */
    public void playerDead(Combatant player, DeadOrAlive collider) {
        collider.dead();
    }

    /**
     * Overwrite to implement any action, if and only if the player survided and the enemy doesn't.
     * It will not be called, if both died!
     * @param enemy Enemy
     * @param target Sprite (enemy) rendered on the map
     */
    public void enemyDead(Combatant enemy, DeadOrAlive target) {
        target.dead();
    }

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
                Combatant playerMinion = getPlayer().getPlayerAsMinion();
                Combatant enemyMinion  = getEnemy().getMinion();
                if ( enemyMinion == null || playerMinion == null ) {
                    return;
                }
                if ( !enemyMinion.isAlive() ) {
                    enemyMinion.resurrect();
                    enemyMinion.setLevel(( byte ) ( playerMinion.getLevel()+1 ));
                }
                CombatFrame cFrame = new CombatFrame(frame, playerMinion, enemyMinion);
                cFrame.setVisible(true);
                if ( !playerMinion.isAlive() ) {
                    playerDead( playerMinion, (DeadOrAlive) e.collider );
                } else if ( !enemyMinion.isAlive() ) {
                    enemyDead( enemyMinion, (DeadOrAlive) e.target );
                    ((DeadOrAlive) e.target).dead();
                }
            }
        }
    }

}
