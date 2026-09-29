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

public class Armor implements Blockable {

    final String name;
    final ArmorType aType;
    final int dmgRed; // reduziert den absolut erlittenen Schaden um diesen %-Wert

    public Armor(String name, ArmorType aType, int dmgRed) {
        this.name = name;
        this.aType = aType;
        this.dmgRed = dmgRed;
    }

    public ArmorType getArmorType() {
        return aType;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getDamageReduction(DamageType dType) {
        if ( aType.ordinal() == dType.ordinal() ) {
            // Rüstungs- & Schadenstyp passen zusammen
            return dmgRed;
        }
        return 0;
    }

    @Override
    public int takeDamage(Damage dmg) {
        if ( aType.ordinal() != dmg.getType().ordinal() ) {
            // wenn Schadens- und Rüstungstyp nicht zusammen passen, den nominalen Schaden zurückgeben
            return dmg.getDamage();
        }
        return dmg.getDamage() - ( dmg.getDamage() * dmgRed / 100 );
    }

    @Override
    public String toString() {
        return name + " (" + aType + ")";
    }

}
