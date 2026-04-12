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

import game.DialogOutputListener;
import game.Message;
import game.item.IsItem;
import game.item.Item;
import game.item.ItemAction;
import game.item.Usable;
import java.util.List;

public class Ammo extends Item implements Usable {

    private final int stackSize;
    private final AmmoType aType;
    private final Damage dmg;

    private int stack = 0; // verbleibende Anzahl der Ladungen/Patronen

    public Ammo(String name, AmmoType aType, int stackSize, Damage dmg) {
        this(name, aType, stackSize, 0, dmg);
    }

    public Ammo(String name, AmmoType aType, int stackSize, int stack, Damage dmg) {
        super(0, name);
        this.aType      = aType;
        this.stackSize  = stackSize;
        this.stack      = stack;
        this.dmg        = dmg;
    }

    @Override
    public boolean use(game.Player player) {
        List<AbstractWeapon> weaponList = player.getPlayerAsMinion().getWeaponList();
        for ( AbstractWeapon w : weaponList ) {
            if (w instanceof AmmoWeapon aw) {
                if ( aw.getAmmoType() == getType() ) {
                    aw.addAmmo(this);
                    return true;
        }}}
        return false;
    }

    @Override
    public void itemActionPerformed(IsItem item, ItemAction action, DialogOutputListener dialogListener) {
        super.itemActionPerformed(item, action, dialogListener);
        switch (action) {
            case ItemAction.USE  -> dialogListener.show( new Message( toString(), this ));
        }
    }

    public int getStack() {
        return stack;
    }

    public void setStack(int value) {
        this.stack = value;
    }

    public void charge(int amount) {
        this.stack = Math.min(stack+amount, stackSize);
    }

    public int getStackSize() {
        return stackSize;
    }

    public AmmoType getType() {
        return aType;
    }

    public boolean tryConsume() {
        if (stack > 0) {
            --stack;
            return true;
        } else {
            return false;
        }
    }

    public Damage getDamage() {
        return dmg;
    }

    @Override
    public String toString() {
        return getName() + " (" + aType + "), " + stack + "/" + stackSize + ", " + dmg.toString() + "\n" + getDescription();
    }

}
