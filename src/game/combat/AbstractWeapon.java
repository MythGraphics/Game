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
import game.item.ReUsable;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.ImageIcon;

public abstract class AbstractWeapon extends Item implements Blockable, ReUsable {

    final WeaponType wType;

    private final ArrayList<WeaponActionListener> actionListeners;
    private final Random rand;

    public AbstractWeapon(int id, String name, WeaponType wType) {
        super(id, name);
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

    @Override
    public void itemActionPerformed(IsItem item, ItemAction action, DialogOutputListener dialogListener) {
        super.itemActionPerformed(item, action, dialogListener);
        switch (action) {
            case ItemAction.USE -> dialogListener.show( new Message( toString(), this ));
        }
    }

    @Override
    public Image getImg() {
        // ToDo implementieren
        return null;
    }

    @Override
    public ImageIcon getIcon() {
        // ToDo implementieren
        return null;
    }

    @Override
    public boolean use(game.Player player) {
        if ( player.getPlayerAsMinion().addWeapon( this )) {
            return true;
        }
        player.getInventory().add( player.getPlayerAsMinion().changeWeapon( this, 1 ));
        return true;
    }

    @Override
    public ReUsable remove(game.Player player) {
        player.getPlayerAsMinion().getWeaponList().remove(this);
        return this;
    }

    public List<WeaponActionListener> getActionListeners() {
        return actionListeners;
    }

    public void addWeaponActionListener(WeaponActionListener actionListener) {
        this.actionListeners.add(actionListener);
    }

    public void removeAllActionListener() {
        this.actionListeners.clear();
    }

    public WeaponType getWeaponType() {
        return wType;
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
        return super.toString() + " (" + wType + ")\n" + getDescription();
    }

}
