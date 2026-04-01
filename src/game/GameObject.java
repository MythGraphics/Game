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

    ENEMY           ("Enemy"),
    NPC             ("Npc"),
    QUEST           ("Quest"),
    QUEST_OBJECTIVE ("QuestObjective"),
    TEXT            ("Text"),
    ITEM            ("Item"),
    MINION          ("Minion"), // combat.Combatant
    PORTAL          ("Portal"); // java.awt.Point

    private final String str;

    GameObject(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

}
