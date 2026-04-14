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

public enum QuestString {

    PROLOG      ("quest_prolog"),
    TEXT        ("quest_text"),
    OBJ_FOUND   ("questobj_found"),
    COMPLETE    ("quest_complete"),
    EPILOG      ("quest_epilog");

    private final String name;

    QuestString(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
