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

import game.InteractiveObject;
import game.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Combatant extends InteractiveObject implements Combatable {

    public final static int HP_PER_LEVEL = 100;
    public final static String UNNAMED = "John Smith";
    public final static String RESOURCE_NAME = "Gesundheit";

    final CombatantType cType;

    private final ArrayList<Blockable> armorList;
    private final ArrayList<AbstractWeapon> weaponList;
    private final ArrayList<BattleActionListener> battleActionListeners = new ArrayList<>();
    private final ArrayList<CombatActionListener> combatActionListeners = new ArrayList<>();
    private final Resource health;

    private byte clvl = 1;
    private HashMap<Byte, String> nameMap;

    // Attribute über cType (CombatantType) implementiert

    public Combatant(String name, CombatantType cType) {
        this(name, cType, null);
    }

    public Combatant(String name, CombatantType cType, Resource health) {
        super(name);
        this.cType  = cType;
        if (health == null) {
            this.health = new Resource( RESOURCE_NAME, Resource.ResourceType.HEALTH, HP_PER_LEVEL );
        } else {
            this.health = health;
        }
        armorList   = new ArrayList<>();
        weaponList  = new ArrayList<>(2); // 0-2 Einträge
    }

    @Override
    public Resource getHealth() {
        return health;
    }

    @Override
    public boolean isAlive() {
        return health.getValue() > 0;
    }

    @Override
    public byte getLevel() {
        return clvl;
    }

    public void resurrect() {
        health.setValue(100);
    }

    /**
     * Sets the NameMap. The name String changes by reaching the given level.
     * @param nameMap name map
     */
    public final void setNameMap(HashMap<Byte, String> nameMap) {
        this.nameMap = nameMap;
        refreshName();
    }

    private void refreshName() {
        if ( nameMap == null ) {
            return;
        }
        for (byte i = clvl; i >= 1; --i) {
            if ( nameMap.containsKey( i )) {
                setName( nameMap.get( i ));
                return;
            }
        }
    }

    public void setLevel(byte level) {
        byte oldLevel = this.clvl;
        this.clvl = level;
        if ( level > oldLevel ) {
            levelUp();
            battleActionListeners.forEach( listener -> listener.levelupPerformed( this ));
        }
    }

    private void levelUp() {
        getHealth().setMax( getLevel() * HP_PER_LEVEL );
        getHealth().setValue(100);
        updateDmg();
        refreshName();
    }

    public void addCombatActionListener(CombatActionListener actionListener) {
        this.combatActionListeners.add(actionListener);
        for (AbstractWeapon weapon : weaponList) {
            combatActionListeners.forEach( listener -> weapon.addWeaponActionListener( listener ));
        }
    }

    public void addBattleActionListener(BattleActionListener actionListener) {
        this.battleActionListeners.add(actionListener);
    }

    public void addArmor(Blockable armor) {
        if ( armor == null ) {
            return;
        }
        this.armorList.add(armor);
    }

    public final boolean addWeapon(AbstractWeapon weapon) {
        if ( weapon == null ) {
            return true;
        }
        if ( weaponList.size() <= 2 ) { // kann max. 2 Waffen verwenden
            // fügt den charakterspezifischen Schaden der Waffe hinzu
            if (weapon instanceof Weapon w) {
                w.addDamage( new Damage( cType.getDamageType(), w.getBaseDamage() ));
            }
            weaponList.add(weapon);
            armorList.add(weapon);
            updateDmg();
            return true;
        }
        return false;
    }

    /**
     * Tauscht die Waffe am index 0 oder 1 gegen die Angegebene aus und gibt die ursprüngliche zurück.
     * @param weapon Neue Waffe
     * @param index Inidex des Waffenslots (0 oder 1)
     * @return ursprüngliche Waffe
     */
    public AbstractWeapon changeWeapon(AbstractWeapon weapon, int index) {
        if ( index < 0 || index > 1 ) {
            return null;
        }
        AbstractWeapon old = getWeapon(index);
        if ( old != null ) {
            weaponList.remove(index);
            old.removeAllActionListener();
            armorList.remove(old);
            if (old instanceof Weapon oldWeapon) {
                oldWeapon.resetDamage();
            }
        }
        addWeapon(weapon);
        return old;
    }

    public AbstractWeapon getWeapon(int index) {
        if ( index < weaponList.size() ) {
            return weaponList.get(index);
        }
        return null;
    }

    public Blockable getArmor(int index) {
        if ( index < armorList.size() ) {
            return armorList.get(index);
        }
        return null;
    }

    public List<AbstractWeapon> getWeaponList() {
        return weaponList;
    }

    public List<Blockable> getArmorList() {
        return armorList;
    }

    public int getDamageReduction(DamageType dType) {
        int resi = 0;
        for ( Blockable b : armorList ) {
            if ( b instanceof Armor a ) {
                resi += a.getDamageReduction(dType);
            }
        }
        return resi;
    }

    public double getCrit() {
        double noCrit = 1.0;
        for ( AbstractWeapon w : weaponList ) {
            if ( w != null ) {
                noCrit *= 1.0 - ( w.getWeaponType().getCrit() / 100.0 );
            }
        }
        return (1.0 - noCrit) * 100.0;
    }

    public double getBlock() {
        double noBlock = 1.0;
        for ( AbstractWeapon w : weaponList ) {
            if ( w != null ) {
                noBlock *= 1.0 - ( w.getWeaponType().getBlock() / 100.0 );
            }
        }
        return (1.0 - noBlock) * 100.0;
    }

    public int getDamage(DamageType dType) {
        updateDmg();
        int dmg = 0;
        // über alle Waffen iterieren
        for ( AbstractWeapon w : weaponList ) {
            if ( w == null || w.getDamage(dType) == null ) { continue; }
            dmg += w.getDamage(dType).getDamage();
        }
        return dmg;
    }

    @Override
    public List<Damage> makeDamage() {
        ArrayList<Damage> dmgList = new ArrayList<>();
        int dmg = 0;
//      updateDmg();
        // über alle Waffen iterieren
        for ( AbstractWeapon w : weaponList ) {
            if ( w == null ) { continue; }
            // über alle DamageTypes iterieren
            for ( DamageType dType : DamageType.values() ) {
                final Damage fDamage = w.makeDamage(dType);
                if ( fDamage.getDamage() > 0 ) {
                    dmg += fDamage.getDamage();
                    dmgList.add(fDamage);
                    combatActionListeners.forEach( listener -> listener.makeDamagePerformed( this, fDamage ));
                }
            }
        }
        final int fdmg = dmg;
        combatActionListeners.forEach( listener -> listener.makeTotalDamagePerformed( this, fdmg ));
        return dmgList;
    }

    /**
     * Gibt den erlittenen Gesamtschaden summiert über aller Schadensarten zurück.
     * @param dmgList Schadensliste
     */
    @Override
    public void takeDamage(List<Damage> dmgList) {
        int totalDmg = 0, currentDmg = 0;
        // Block berücksichtigt zwar kein dType, Armor aber sehr wohl
        for ( Damage d : dmgList ) {
            if ( d.getDamage() == 0 ) {
                continue;
            }
            for ( Blockable b : armorList ) {
                currentDmg = b.takeDamage(d);
                if ( currentDmg < d.getDamage() ) {
                    // geblockt: pariert (Waffenblock) oder reduziert (Rüstungsblock)
                    final Damage redDmg = new Damage( d.getType(), d.getDamage()-currentDmg );
                    combatActionListeners.forEach(
                        listener -> listener.deflectDamagePerformed(b, redDmg)
                    );
                    break;
                }
            }
            if ( currentDmg > 0 ) {
                totalDmg += currentDmg;
                final int fdmg = currentDmg;
                combatActionListeners.forEach(
                    listener -> listener.takeDamagePerformed( this, new Damage( d.getType(), fdmg ))
                );
            }
        }

        final int ftd = totalDmg;
        final int overkill = getHealth().force(totalDmg);
        if ( !isAlive() ) {
            battleActionListeners.forEach( listener -> listener.killPerformed( this ));
            combatActionListeners.forEach( listener -> listener.lethalDamagePerformed( this, ftd, overkill ));
        } else {
            // feuert auch bei 0 Schaden
            combatActionListeners.forEach( listener -> listener.damagePerformed( this, ftd ));
        }
    }

    /**
     * DamageBuff, der überschrieben werden kann um Schaden zu verstärken.
     * @param dmg Das zu verstärkende Damage-Objekt
     */
    public void buffDamage(Damage dmg) {}

    private void updateDmg() {
        // über alle Waffen iterieren
        for ( AbstractWeapon w : weaponList ) {
            if ( w == null) { continue; }
            // über alle Damage-Objekte der Waffe iterieren
            for ( Damage dmg : w.getDamageList().values() ) {
                dmg.resetDamage();
                buffDamage(dmg);
            }
        }
    }

    public CombatantType getType() {
        return cType;
    }

    public String getUIString() {
        return getName() + "(" + getLevel() + ")";
    }

    public String getToolTipText() {
        return cType.toString();
    }

    @Override
    public String toString() {
        return
            getName() +
            "(" + cType + "), " +
            "Level " + getLevel() + ";\n" +
            "Waffen: " + weaponList.toString() + ";\n" +
            "Rüstung: " + armorList.toString()
        ;
    }

}
