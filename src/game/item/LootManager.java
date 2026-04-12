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
import game.item.Item;
import game.item.ItemBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootManager {

    private final List<Item> lootPool;
    private final Random rand;
    private final Item defaultLoot;

    public LootManager() {
        this(null, null);
    }

    public LootManager(List<Item> lootPool, Item defaultLoot) {
        if ( lootPool != null ) {
            this.lootPool = lootPool;
        } else {
            this.lootPool = new ArrayList<>();
        }
        this.defaultLoot = defaultLoot;
        this.rand = new Random();
    }

    public Item getLootFromPool() {
        if ( lootPool.isEmpty() ) {
            return defaultLoot;
        }
        return lootPool.remove( rand.nextInt( lootPool.size() ));
    }

    public Item getCredits(Combatant enemy) {
        int value = rand.nextInt( 100 * enemy.getLevel() );
        return ItemBuilder.createCoinPouch( "des " + enemy.getName(), value );
    }

    public AbstractWeapon getWeapon(Combatant enemy) {
        int id = rand.nextInt();
        DamageType dType = enemy.getType().getDamageType();
        WeaponType wType = WeaponType.values()[ rand.nextInt( WeaponType.values().length )];
        AbstractWeapon w;
        if ( wType.needAmmo() ) {
            w = new AmmoWeapon( id, getName(wType), wType, wType.getAmmoType() );
        } else {
            int baseDmg = rand.nextInt( 10 * enemy.getLevel() );
            w = new Weapon( id, getName(wType), wType, dType, baseDmg );
        }
        w.setDescription( "Loot von " + enemy.getName() );
        return w;
    }

    public Ammo getAmmo(Combatant enemy, AmmoType aType) {
        if (aType == NONE) {
            return null;
        }
        int stackSize = 5 + rand.nextInt( 2*enemy.getLevel() );
        int baseDmg = rand.nextInt( 10 * enemy.getLevel() );
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
        ammo.setStack( rand.nextInt( stackSize ));
        ammo.setDescription( "Loot von " + enemy.getName() );
        return ammo;
    }

    private String getName(WeaponType wType) {
        return wType.getNamePool()[ rand.nextInt( wType.getNamePool().length )];
    }

}
