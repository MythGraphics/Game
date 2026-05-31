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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Weapon extends AbstractWeapon {

    final DamageType dType; // Basis-Schadenstyp der Waffe
    final int baseDmg;      // Basis-Schaden der Waffe

    private final Random rand;

    private Map<DamageType, Damage> dmgList;

    public Weapon(int id, String name, WeaponType wType, DamageType dType, int baseDmg) {
        super(id, name, wType);
        this.dType = dType;
        this.baseDmg = baseDmg;
        rand = new Random();
        resetDamage(); // init dmgList
    }

    public final void resetDamage() {
        dmgList = new HashMap<>( Map.of( dType, new Damage( dType, baseDmg )));
    }

    @Override
    Map<DamageType, Damage> getDamageList() {
        return dmgList;
    }

    /**
     * Fügt der Waffe eine Schadensart hinzu.
     * @param damage Die zusätzliche Schadensart.
     */
    public void addDamage(Damage damage) {
        if ( dmgList.containsKey( damage.getType() )) {
            int value = dmgList.get( damage.getType() ).getBaseDamage(); // ursprünglichen Basis-Schaden holen
            value += damage.getBaseDamage(); // neuen Basis-Schaden addieren
            dmgList.put( damage.getType(), new Damage( damage, value )); // neues Damage-Objekt erzeugen und speichern
        } else {
            dmgList.put( damage.getType(), damage );
        }
    }

    /**
     * Gibt den Basis-Schaden dieser Waffe zurück.
     * @return Der nominale Schaden.
     */
    public int getBaseDamage() {
        return baseDmg;
    }

    @Override
    public Damage makeDamage(DamageType dType) {
        Damage dmg = dmgList.get(dType);
        if ( dmg == null || dmg.getDamage() == 0 ) {
            return new Damage(dType, 0);
        }
        if ( rand.nextInt(100) < wType.getCrit() ) {  // krit. Trefferchance berücksichtigen
            dmg.setDamage( dmg.getDamage() * 2 ); // krit. Treffer
            getActionListeners().forEach( listener -> listener.kritDamagePerformed( this, dmg ));
        } else {
            getActionListeners().forEach( listener -> listener.normalDamagePerformed( this, dmg ));
        }
        return dmg;
    }

    @Override
    public Damage getDamage(DamageType dType) {
        return dmgList.get(dType);
    }

    /**
     * Gibt den Basis-Schadenstyp der Waffe zurück.
     * @return Basis-Schadenstyp der Waffe
     */
    public DamageType getDamageType() {
        return dType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder( super.toString() );
        sb.append("\n");
        dmgList.values().forEach( damage ->
            sb.append(damage.dType).append(": ").append( damage.getDamage() ).append( ",\n" )
        );
        return sb.toString();
    }

}
