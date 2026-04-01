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

import java.util.LinkedList;

public class TextBox extends InteractiveObject implements HasDialog, HasID {

    protected final LinkedList<Message> msgList;

    private final int id;

    public TextBox(int id, String name) {
        super(name);
        this.id = id;
        this.msgList = new LinkedList<>();
    }

    public TextBox(int id, String name, String text) {
        this(id, name);
        msgList.add( new Message( text, this ));
    }

    @Override
    public int getId() {
        return id;
    }

    public void addMessage(Message msg) {
        msgList.add(msg);
    }

    @Override
    public LinkedList<Message> getDialog() {
        return getDialogCopy();
    }

    public LinkedList<Message> getDialogCopy() {
        return (LinkedList<Message>) msgList.clone();
/*
        LinkedList<Message> newMsgList = new LinkedList<>();
        newMsgList.addAll(this.msgList);
        return newMsgList;
 */
    }

}
