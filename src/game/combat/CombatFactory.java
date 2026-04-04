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

import game.Resource;
import static game.combat.AmmoType.PROJECTILE;
import static game.combat.ArmorType.*;
import static game.combat.CombatantType.*;
import static game.combat.DamageType.*;
import static game.combat.WeaponType.*;
import static graphic.io.BinaryIO.BINARYIO;
import static graphic.io.ImageUtility.scale;
import java.util.ArrayList;
import java.util.Optional;

public class CombatFactory {

    public enum DefaultMinion {

        GIFTZERG        ( getDefaultGiftZerg() ),
        FEUERZERG       ( getDefaultFeuerZerg() ),
        KRIEGERZERG     ( getDefaultKriegerZerg() ),

        HASE            ( getDefaultBattlePet_Hase() ),
        KATZE           ( getDefaultBattlePet_Katze() ),
        RATTE           ( getDefaultBattlePet_Ratte() );

        private final Combatant combatant;

        DefaultMinion(Combatant combatant) {
            this.combatant = combatant;
        }

        public Combatant getMinion() {
            return combatant;
        }

        public static Optional<DefaultMinion> getByName(String s) {
            if ( s == null || s.isBlank() ) {
                return Optional.empty();
            }
            try {
                return Optional.of( DefaultMinion.valueOf( s.toUpperCase().trim() ));
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        }

    }

    private String name;
    private CombatantType cType;
    private Weapon weapon1, weapon2;

    private final ArrayList<Armor> armorList;

    public CombatFactory() {
        armorList = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(CombatantType cType) {
        this.cType = cType;
    }

    public void addArmor(Armor armor) {
        armorList.add(armor);
    }

    public void setWeapon(Weapon weapon) {
        if ( weapon1 != null ) {
            weapon1 = weapon;
            return;
        }
        if ( weapon2 != null ) {
            weapon2 = weapon;
        }
    }

    public Combatant build() throws NullPointerException {
        if ( name  == null ) { throw new NullPointerException( name.getClass()  + " is null."); }
        if ( cType == null ) { throw new NullPointerException( cType.getClass() + " is null."); }
        Combatant c = new Combatant(name, cType);
        armorList.forEach( armor -> c.addArmor( armor ));
        c.addWeapon(weapon1);
        c.addWeapon(weapon2);
        return c;
    }

    public static Player getDefaultApotheker(Resource health) {
        Player c = new Player( "Der Apotheker", APOTHEKER, health, 0 );
        c.addArmor( new Armor( "Antikörper", ANTIKÖRPER, 50 ));
        c.addArmor( new Armor( "Kittel", RÜSTUNG, 10 ));
        c.addWeapon( new Weapon( -1, "Pistill", KNÜPPEL, PHYSISCH, 10 ));
        return c;
    }

    public static Player getDefaultKrieger(Resource health) {
        Player c = new Player( "Poppeye", KRIEGER, health, 0 );
        c.addArmor( new Armor( "Dicke Muckies", RÜSTUNG, 50 ));
        c.addArmor( new Armor( "Matrosenhemd", RÜSTUNG, 10 ));
        c.addWeapon( new Weapon( -1, "Pfeife", KNÜPPEL, PHYSISCH, 10 ));
        return c;
    }

    public static Player getDefaultMagier(Resource health) {
        Player c = new Player( "Gandalf", MAGIER, health, 0 );
        c.addArmor( new Armor( "Asbestumhang", ASBESTBESCHICHTUNG, 50 ));
        c.addArmor( new Armor( "Robe", RÜSTUNG, 10 ));
        c.addWeapon( new Weapon( -1, "Elderstab", STAB, PHYSISCH, 10 ));
        return c;
    }

    public static Enemy getDefaultFeuerZerg() {
        Enemy c = new Enemy( "Höllenzerg", MAGIER, 1 );
        c.addArmor( new Armor( "Asbestschuppen", ASBESTBESCHICHTUNG, 50 ));
        c.addWeapon( new Weapon( -1, "rasiermesserscharfe Klauen", DOLCH, PHYSISCH, 25 ));
        c.addWeapon( new Weapon( -1, "langer, spitzer Stachel", SCHWERT, PHYSISCH, 10 ));
        c.setImg( scale( BINARYIO.loadImage( "sprites/minion/zerg.png" ), 100 ));
        return c;
    }

    public static Enemy getDefaultGiftZerg() {
        Enemy c = new Enemy( "Schleimtriefender Zerg", APOTHEKER, 1 );
        c.addArmor( new Armor( "Antikörper", ANTIKÖRPER, 50 ));
        c.addArmor( new Armor( "Chitinschuppen", RÜSTUNG, 20 ));
        c.addWeapon( new Weapon( -1, "rasiermesserscharfe Klauen", DOLCH, PHYSISCH, 25 ));
        c.addWeapon( new Weapon( -1, "langer, spitzer Stachel", SCHWERT, PHYSISCH, 10 ));
        c.setImg( scale( BINARYIO.loadImage( "sprites/minion/zerg.png" ), 100 ));
        return c;
    }

    public static Enemy getDefaultKriegerZerg() {
        Enemy c = new Enemy( "Mucki-Zerg", KRIEGER, 1 );
        c.addArmor( new Armor( "Chitinpanzerplatten", RÜSTUNG, 75 ));
        c.addWeapon( new Weapon( -1, "rasiermesserscharfe Klauen", DOLCH, PHYSISCH, 5 ));
        c.addWeapon( new Weapon( -1, "langer, spitzer Stachel", SCHWERT, PHYSISCH, 10 ));
        c.setImg( scale( BINARYIO.loadImage( "sprites/minion/zerg.png" ), 100 ));
        return c;
    }

    public static Enemy getDefaultBattlePet_Ratte() {
        Enemy c = new Enemy( "Ranzige Ratte", APOTHEKER, 1 );
        c.addArmor( new Armor( "verfilztes Fell", RÜSTUNG, 50 ));
        c.addWeapon( new Weapon( -1, "rasiermesserscharfe Krallen", KRIEGSCLEVE, PHYSISCH, 5 ));
        c.addWeapon( new Weapon( -1, "giftiger Biss", DOLCH, GIFT, 15 ));
        c.setImg( scale( BINARYIO.loadImage( "sprites/minion/rat.png" ), 100 ));
        return c;
    }

    public static Enemy getDefaultBattlePet_Katze() {
        Enemy c = new Enemy( "Mucki-Mietze", KRIEGER, 1 );
        c.addArmor( new Armor( "dickes Fell", RÜSTUNG, 50 ));
        c.addWeapon( new Weapon( -1, "rasiermesserscharfe Krallen", KRIEGSCLEVE, PHYSISCH, 5 ));
        c.addWeapon( new Weapon( -1, "kräftiger Biss", DOLCH, PHYSISCH, 5 ));
        c.setImg( scale( BINARYIO.loadImage( "sprites/minion/cat.png" ), 100 ));
        return c;
    }

    public static Enemy getDefaultBattlePet_Hase() {
        Enemy c = new Enemy( "Heißer Hoppler", MAGIER, 1 );
        c.addArmor( new Armor( "Asbestfell", ASBESTBESCHICHTUNG, 50 ));
        c.addWeapon( new Weapon( -1, "brennende Krallen", KRIEGSCLEVE, FEUER, 5 ));
        c.addWeapon( new Weapon( -1, "brennender Biss", DOLCH, FEUER, 5 ));
        c.setImg( scale( BINARYIO.loadImage( "sprites/minion/rabbit3.png" ), 100 ));
        return c;
    }

    public static Player getDefaultSpaceMarine(Resource health) {
        Player c = new Player( "SpaceMarine Johannis Kraut", SPACE_MARINE, health, 0 );
        c.addArmor( new Armor( "Space Suit", RÜSTUNG, 90 ));
        AmmoWeapon weapon = new AmmoWeapon( -1, "BFG Sturmgewehr", GEWEHR, PROJECTILE );
        weapon.addAmmo( new Ammo( "Plasmaladung", PROJECTILE, 100, 10, new Damage( NUKLEAR, 50 )));
        c.addWeapon(weapon);
        return c;
    }

}
