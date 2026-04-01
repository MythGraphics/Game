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

import game.Message;
import game.item.Item;
import java.util.ArrayList;

public class Quest extends AbstractQuest {

    private final ArrayList<Message> msgList;
    private final Item reward;

    public Quest(int id, ArrayList<Message> msgList, Item reward) {
        super(id);
        this.msgList = msgList;
        this.reward = reward;
    }

    @Override
    public void accept() {
        super.accept();
        System.out.println("Quest #" + getId() + " angenommen."); // debug
    }

    @Override
    ArrayList<Message> getMessageList() {
        return msgList;
    }

    @Override
    Item getReward() {
        return reward;
    }

}
