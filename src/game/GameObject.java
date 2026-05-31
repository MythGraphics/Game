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

    AMMO            ("Ammo"),           // game.combat.Ammo
    ENEMY           ("Enemy"),          // game.Enemy
    NPC             ("Npc"),            // game.Npc
    QUEST           ("Quest"),          // game.quest.AbstractQuest
    QUEST_OBJECTIVE ("QuestObjective"), // game.item.Item
    TEXT            ("Text"),           // game.TextBox
    ITEM            ("Item"),           // game.item.Item
    MINION          ("Minion"),         // game.combat.Combatant
    PORTAL          ("Portal"),         // java.awt.Point
    WEAPON          ("Weapon");         // game.combat.AbstractWeapon

    private final String str;

    GameObject(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

}
