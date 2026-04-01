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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class AbstractWeapon implements Blockable {

    final String name;
    final WeaponType wType;

    private final ArrayList<WeaponActionListener> actionListeners;
    private final Random rand;
    private String description = "";

    public AbstractWeapon(String name, WeaponType wType) {
        this.name = name;
        this.wType = wType;
        this.actionListeners = new ArrayList<>();
        rand = new Random();
    }

    abstract Map<DamageType, Damage> getDamageList();

    /**
     * Gibt den auszuteilenden Schaden der angegebenen Schadensart als Damage-Objekt zurück.
     * Berücksichtigt kritische Treffer.
     * Wird für Armor-Logik im Combatant-Objekt benötigt.
     * @param dType DamageType
     * @return Auszuteilender Schaden
     */
    abstract Damage makeDamage(DamageType dType);

    /**
     * Gibt den auszuteilenden Nominal-Schaden (ohne kritische Treffer) der angegebenen Schadensart
     * als Damage-Objekt zurück.
     * @param dType DamageType
     * @return Schaden als Damage-Objekt
     */
    abstract Damage getDamage(DamageType dType);

    public List<WeaponActionListener> getActionListeners() {
        return actionListeners;
    }

    public void addWeaponActionListener(WeaponActionListener actionListener) {
        this.actionListeners.add(actionListener);
    }

    public void removeAllActionListener() {
        this.actionListeners.clear();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public WeaponType getWeaponType() {
        return wType;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Gibt den erlittenen Schaden unter Berücksichtigung der Blockchance der Waffe zurück.
     * Wird für Armor-Berechnung benötigt.
     * @param damage anzurichtender Schaden als Damage-Objekt
     * @return Erlittene Schaden oder 0, wenn geblockt
     */
    @Override
    public int takeDamage(Damage damage) {
        int dmg = damage.getDamage();
        if ( rand.nextInt(100)+1 <= wType.getBlock() ) {
            dmg = 0; // Angriff geblockt
        }
        return dmg;
    }

    @Override
    public String toString() {
        return name + " (" + wType + ")\n" + getDescription();
    }

}
