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

import game.quest.HasQuest;
import game.quest.Quest;

public class Npc extends TextBox implements HasQuest {

    private Quest quest;

    public Npc(int id, String name) {
        super(id, name);
    }

    protected Npc(Npc npc) {
        super(npc);
        this.quest = npc.getQuest();
    }

    @Override
    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    @Override
    public Quest getQuest() {
        return quest;
    }

    @Override
    public boolean hasQuest() {
        return quest != null;
    }

    // shallow copy
    @Override
    public Npc clone() throws CloneNotSupportedException  {
        return new Npc(this);
    }

}
