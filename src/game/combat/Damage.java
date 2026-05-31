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

import java.util.EnumMap;

public class Damage implements Cloneable {

    final int baseDmg;
    final DamageType dType;

    private int value = 0;

    public Damage(Damage damage, int value) {
        this( damage.getType(), value );
    }

    public Damage(DamageType dType, int value) {
        this.dType      = dType;
        this.baseDmg    = value;
        this.value      = value;
    }

    public DamageType getType() {
        return dType;
    }

    public int getBaseDamage() {
        return baseDmg;
    }

    public int getDamage() {
        return value;
    }

    void setDamage(int value) {
        this.value = value;
    }

    void resetDamage() {
        this.value = this.baseDmg;
    }

    /**
     * Aktualisiert den Schaden basierend auf des gegebene Attribut.
     * @param a Das zu berücksichtigende Attribut
     */
    public void buffDamage(Attribute a) {
        for ( EnumMap.Entry<DamageType, Integer> buff : a.getBuffs().entrySet() ) {
            if ( dType == buff.getKey() ) {
                value += baseDmg * a.getValue() * buff.getValue() / 100;
            }
        }
    }

    /**
     * Aktualisiert den Schaden dieses Damage-Objektes basierend auf dem gegebenen Level.
     * @param cLevel Der zu berücksichtigende Combatant-Level (cLevel)
     */
    public void buffDamage(byte cLevel) {
        value = baseDmg + baseDmg*cLevel;
    }

    @Override
    public String toString() {
        return String.valueOf( value + " (" + dType + ")" );
    }

    @Override
    public Damage clone() {
        Damage d = new Damage(dType, baseDmg);
        d.setDamage(value);
        return d;
    }

}
