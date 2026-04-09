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

import game.ID;
import game.InteractiveObject;
import game.item.IsItem;

public class QuestFactory {

    private QuestFactory() {}

    public static AbstractQuest[] createQuests( String[] prolog, String[] questText, String[] epilog, String[] complete,
                                                InteractiveObject source, IsItem[] reward ) {
        boolean equalLength = checkArraysEqualLengths( prolog, questText, epilog, complete, reward );
        if ( !equalLength ) {
            throw new ArrayIndexOutOfBoundsException("Arrays of constructor not equal in length.");
        }
        Quest[] quests = new Quest[prolog.length];
        for (int i = 0; i < quests.length; ++i) {
            quests[i] = new Quest(
                ID.getNextQuestId(),
                Quest.newMessageList( prolog[i], questText[i], epilog[i], complete[i], source ),
                reward[i]
            );
        }
        return quests;
    }

    public static boolean checkArraysEqualLengths(Object[]... arrays) {
        if (arrays == null || arrays.length == 0) {
            return true; // Leere Eingabe = alle Arrays gleich lang (oder keine Arrays vorhanden)
        }
        int firstArrayLength = arrays[0].length;
        for (int i = 1; i < arrays.length; i++) {
            if (( arrays[i] == null ) || ( arrays[i].length != firstArrayLength )) {
                return false;
            }
        }
        return true;
    }

}
