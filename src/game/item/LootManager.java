/*
 *
 */

package game.item;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import static game.combat.AmmoType.*;
import game.combat.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LootManager {

    public final static int MIN_STACK_SIZE = 5;

    private final static Random rand = new Random();

    final List<Item> lootPool;

    private final Item defaultLoot;

    public LootManager(List<Item> lootPool, Item defaultLoot) {
        if ( lootPool == null ) {
            this.lootPool = new ArrayList<>();
        } else {
            this.lootPool = lootPool;
        }
        this.defaultLoot = defaultLoot;
    }

    public void add2Pool(Item... items) {
        lootPool.addAll( Arrays.asList( items ));
    }

    public void add2Pool(List<Item> items) {
        lootPool.addAll(items);
    }

    /**
     * Returns a random item from the loot pool or defaultLoot, if the pool is empty.
     * @return Loot-Item
     */
    public Item getLoot() {
        if ( lootPool.isEmpty() ) {
            return defaultLoot;
        }
        return lootPool.remove( rand.nextInt( lootPool.size() ));
    }

    public static Item createCredits(Combatant enemy) {
        int value = rand.nextInt( 100 * enemy.getLevel() );
        return ItemBuilder.createCoinPouch( "des " + enemy.getName(), value );
    }

    public static AbstractWeapon createWeapon(Combatant enemy) {
        int id = rand.nextInt();
        DamageType dType = enemy.getType().getDamageType();
        WeaponType wType = WeaponType.values()[ rand.nextInt( WeaponType.values().length )];
        AbstractWeapon weapon;
        if ( wType.needAmmo() ) {
            weapon = new AmmoWeapon( id, getName(wType), wType, wType.getAmmoType() );
        } else {
            int baseDmg = rand.nextInt( 10 * enemy.getLevel() );
            weapon = new Weapon( id, getName(wType), wType, dType, baseDmg );
        }
        weapon.setDescription( "Loot von " + enemy.getName() );
        return weapon;
    }

    public static Ammo createAmmo(Combatant enemy, AmmoType aType) {
        if (aType == NONE) {
            return null;
        }
        int stackSize = MIN_STACK_SIZE + rand.nextInt( 5*enemy.getLevel() );
        int baseDmg = Math.max( 5, rand.nextInt( 20 * enemy.getLevel() ));
        DamageType dType = enemy.getType().getDamageType();
        Damage dmg = new Damage(dType, baseDmg);
        String name = "";
        if (aType == PROJECTILE) {
            name = "Magazin ";
        } else if (aType == SPELL) {
            name = "Verzauberung ";
        }
        name += dType.getSuffix();
        Ammo ammo = new Ammo( name, aType, stackSize, dmg );
        ammo.setStack( Math.max( MIN_STACK_SIZE, rand.nextInt( stackSize )));
        ammo.setDescription( "Loot von " + enemy.getName() );
        return ammo;
    }

    private static String getName(WeaponType wType) {
        return wType.getNamePool()[ rand.nextInt( wType.getNamePool().length )];
    }

}
