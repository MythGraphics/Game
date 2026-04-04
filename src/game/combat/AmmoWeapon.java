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

public class AmmoWeapon extends AbstractWeapon {

    final AmmoType aType;

    private Ammo ammo;
    private final Random rand;

    public AmmoWeapon(int id, String name, WeaponType wType, AmmoType aType) {
        super(id, name, wType);
        this.aType = aType;
        this.rand = new Random();
    }

    public void addAmmo(Ammo ammo) {
        if ( ammo == null || !ammo.getType().equals( aType )) {
            return;
        }
        this.ammo = ammo;
        getActionListeners().forEach( listener -> listener.changeStatsPerformed( this ));
    }

    @Override
    Map<DamageType, Damage> getDamageList() {
        return new HashMap<>( Map.of( ammo.getDamage().getType(), ammo.getDamage() ));
    }

    @Override
    public Damage getDamage(DamageType dType) {
        if ( ammo == null || !ammo.getDamage().getType().equals( dType )) {
            return new Damage(dType, 0);
        } else {
            return ammo.getDamage();
        }
    }

    @Override
    public Damage makeDamage(DamageType dType) {
        Damage dmg = new Damage(dType, 0);
        if ( ammo == null || !ammo.getDamage().getType().equals( dType )) {
            return dmg;
        }
        if ( !ammo.tryConsume() ) {
            return dmg;
        }
        getActionListeners().forEach( listener -> listener.changeStatsPerformed( this ));
        if ( rand.nextInt(100) < wType.getCrit() ) {  // krit. Trefferchance berücksichtigen
            dmg.setDamage( ammo.getDamage().getDamage() * 2 ); // krit. Treffer
            getActionListeners().forEach( listener -> listener.kritDamagePerformed( this, dmg ));
        } else {
            dmg.setDamage( ammo.getDamage().getDamage() );
            getActionListeners().forEach( listener -> listener.normalDamagePerformed( this, dmg ));
        }
        return dmg;
    }

    public Ammo getAmmo() {
        return ammo;
    }

    public AmmoType getAmmoType() {
        return aType;
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + ammo.toString();
    }
}
