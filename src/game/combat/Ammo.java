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

import graphic.texter.Message;
import game.item.ItemEvent.ItemActionType;
import static game.item.ItemEvent.ItemActionType.USE;
import game.item.UsableItem;
import java.util.LinkedList;
import java.util.List;

public class Ammo extends UsableItem {

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
    public LinkedList<Message> getDialog(ItemActionType actionType) {
        switch (actionType) {
            case USE -> {
                LinkedList<Message> list = new LinkedList<>();
                list.add( new Message( toString(), this ));
                return list;
            }
        }
        return super.getDialog(actionType);
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
