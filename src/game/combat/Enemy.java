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

public class Enemy extends Combatant {

    public Enemy(String name, CombatantType cType, int level) {
        super(name, cType);
        super.setLevel( (byte) level );
    }

    @Override
    public void buffDamage(Damage dmg) {
        dmg.buffDamage( super.getLevel() );
    }

}
