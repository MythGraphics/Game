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

import game.InteractiveObject;
import game.Message;
import game.item.IsItem;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class SelectableQuest extends AbstractQuest implements KeyListener {

    private final ArrayList<Quest> questList;

    private int selectedIndex = -1;

    public SelectableQuest(int id, ArrayList<Quest> questList) {
        super(id);
        this.questList = questList;
    }

    public static SelectableQuest newInstance( int id,
                                               String prolog, String[] questText, String[] epilog, String[] complete,
                                               InteractiveObject source, IsItem[] reward ) {
        boolean equalLength = QuestFactory.checkArraysEqualLengths(questText, epilog, complete, reward);
        if ( !equalLength ) {
            throw new ArrayIndexOutOfBoundsException("Arrays not equal in length.");
        }
        ArrayList<Quest> questList = new ArrayList<>();
        for (int i = 0; i < questText.length; ++i) {
            questList.add( new Quest( id,
                Quest.newMessageList( prolog, questText[i], epilog[i], complete[i], source ),
                reward[i]
            ));
        }
        return new SelectableQuest(id, questList);
    }

    @Override
    public void keyReleased(KeyEvent evt) {}

    @Override
    public void keyPressed(KeyEvent evt) {}

    @Override
    public void keyTyped(KeyEvent evt) {
        selectedIndex = evt.getKeyChar()-1;
    }

    public boolean select(KeyEvent evt) {
        keyTyped(evt);
        return isSelected();
    }

    public boolean isSelected() {
        if ( selectedIndex < 0 ) {
            return false;
        }
        return selectedIndex < questList.size();
    }

    public Quest getQuest() {
        if ( !isSelected() ) {
            return null;
        }
        return questList.get(selectedIndex);
    }

    @Override
    public List<Message> getMessageList() {
        return questList.get(selectedIndex).getMessageList();
    }

    @Override
    public IsItem getReward() {
        return questList.get(selectedIndex).getReward();
    }

}
