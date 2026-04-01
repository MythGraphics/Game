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

public class Ammo {

    private final String name;
    private final int stackSize;
    private final AmmoType aType;
    private final Damage dmg;

    private int stack = 0; // verbleibende Anzahl der Ladungen/Patronen

    public Ammo(String name, AmmoType aType, int stackSize, Damage dmg) {
        this(name, aType, stackSize, 0, dmg);
    }

    public Ammo(String name, AmmoType aType, int stackSize, int stack, Damage dmg) {
        this.name = name;
        this.aType = aType;
        this.stackSize = stackSize;
        this.stack = stack;
        this.dmg = dmg;
    }

    public String getName() {
        return name;
    }

    public int getStack() {
        return stack;
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
        return name + " (" + aType + "), " + stack + "/" + stackSize + ", " + dmg.toString();
    }

}
