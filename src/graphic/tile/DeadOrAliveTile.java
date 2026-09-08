/*
 *
 */

package graphic.tile;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.HasImage;
import graphic.map.BlockTile;
import graphic.map.DefaultBlockType;
import static graphic.map.DefaultBlockType.CORPSE;
import graphic.map.IsBlockType;

public class DeadOrAliveTile extends BlockTile {

    private boolean dead = false;

    private IsBlockType aliveType, deadType;
    private HasImage aliveImage, deadImage;

    public DeadOrAliveTile(int x, int y, int tileSize, IsBlockType bType, HasImage image) {
        super(x, y, tileSize, tileSize, bType, image);
        init(bType, image);
    }

    public DeadOrAliveTile(int x, int y, int width, int height, IsBlockType bType, HasImage image) {
        super(x, y, width, height, bType, image);
        init(bType, image);
    }

    private void init(IsBlockType bType, HasImage image) {
        setAliveData(bType, image);
        IsBlockType defaultDeadType = (bType instanceof DefaultBlockType dbt)
                ? DefaultBlockType.getDeadTile(dbt)
                : CORPSE;
        setDeadData(defaultDeadType, null);
    }

    public void setAliveData(IsBlockType bType, HasImage image) {
        this.aliveType  = bType;
        this.aliveImage = image;
    }

    public void setDeadData(IsBlockType bType, HasImage image) {
        this.deadType  = bType;
        this.deadImage = image;
    }

    public boolean isDead() {
        return dead;
    }

    public void swap() {
        if (dead) {
            alive();
        } else {
            dead();
        }
    }

    public void dead() {
        dead = true;
        change(deadType, deadImage);
    }

    public void alive() {
        dead = false;
        change(aliveType, aliveImage);
    }

}
