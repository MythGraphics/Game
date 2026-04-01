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

import game.quest.Quest;

public class Npc extends TextBox {

    private Quest quest;

    public Npc(int id, String name) {
        super(id, name);
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public Quest getQuest() {
        return quest;
    }

    public boolean hasQuest() {
        return quest != null;
    }

}
