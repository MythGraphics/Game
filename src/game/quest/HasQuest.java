/*
 *
 */

package game.quest;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

public interface HasQuest {

    void setQuest(Quest quest);
    Quest getQuest();
    boolean hasQuest();

}
