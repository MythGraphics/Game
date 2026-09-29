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

    AMMO            ("Ammo",            game.combat.Ammo.class          ),
    ENEMY           ("Enemy",           game.Enemy.class                ),
    NPC             ("Npc",             game.Npc.class                  ),
    QUEST           ("Quest",           game.quest.AbstractQuest.class  ),
    QUEST_OBJECTIVE ("QuestObjective",  game.item.Item.class            ),
    TEXT            ("Text",            game.TextBox.class              ),
    ITEM            ("Item",            game.item.Item.class            ),
    MINION          ("Minion",          game.combat.Combatant.class     ),
    PORTAL          ("Portal",          java.awt.Point.class            ),
    WEAPON          ("Weapon",          game.combat.AbstractWeapon.class);

    private final String filePrefix;
    private final Class clazz;

    GameObject(String filePrefix, Class clazz) {
        this.filePrefix = filePrefix;
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }

    @Override
    public String toString() {
        return filePrefix;
    }

}
