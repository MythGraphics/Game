/*
 *
 */

package graphic.map;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

public class BlockType implements IsBlockType {

    public final char mapChar;

    private InteractionType iType;
    private boolean passable;

    public BlockType(char mapChar, InteractionType iType, boolean passable) {
        this.mapChar    = mapChar;
        this.iType      = iType;
        this.passable   = passable;
    }

    @Override
    public char getMapChar() {
        return mapChar;
    }

    @Override
    public InteractionType getInteractionType() {
        return iType;
    }

    @Override
    public boolean isPassable() {
        return passable;
    }

    public void setInteractionType(InteractionType iType) {
        this.iType = iType;
    }

    public void setPassable(boolean passable) {
        this.passable = passable;
    }

}
