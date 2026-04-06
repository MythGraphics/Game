/*
 *
 */

package game;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

public enum GameObject {

    AMMO            ("Ammo"),
    ENEMY           ("Enemy"),
    NPC             ("Npc"),
    QUEST           ("Quest"),
    QUEST_OBJECTIVE ("QuestObjective"),
    TEXT            ("Text"),   // game.TextBox
    ITEM            ("Item"),   // game.item.Item
    MINION          ("Minion"), // combat.Combatant
    PORTAL          ("Portal"), // java.awt.Point
    WEAPON          ("Weapon"); // game.combat.AbstractWeapon

    private final String str;

    GameObject(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

}
