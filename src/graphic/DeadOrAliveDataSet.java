/*
 *
 */

package graphic;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import static graphic.map.DefaultBlockType.CORPSE;
import graphic.map.IsBlockType;
import java.awt.image.BufferedImage;

public class DeadOrAliveDataSet implements HasImage, IsDeadOrAlive {

    public enum State {
        ALIVE,
        DYING,
        DEAD
    }

    private HasImage aliveImage;
    private IsBlockType aliveType;
    private HasImage deadImage;
    private IsBlockType deadType;
    private AnimationPlayer deathAnimation;
    private State currentState = State.ALIVE;
    private boolean animationFinished = false;

    public DeadOrAliveDataSet(IsBlockType aliveType, HasImage aliveImage) {
        this(aliveType, aliveImage, CORPSE, null);
    }

    public DeadOrAliveDataSet(IsBlockType aliveType, HasImage aliveImage, IsBlockType deadType, HasImage deadImage) {
        this(aliveType, aliveImage, deadType, deadImage, null);
    }

    public DeadOrAliveDataSet(IsBlockType aliveType, HasImage aliveImage, IsBlockType deadType, HasImage deadImage,
                              AnimationPlayer deathAnimation) {
        if (aliveImage == null || aliveType == null) {
            throw new IllegalArgumentException("aliveType, aliveImage, deadType: darf nicht null sein.");
        }
        this.aliveType      = aliveType;
        this.aliveImage     = aliveImage;
        this.deadType       = deadType  != null ? deadType  : CORPSE;
        this.deadImage      = deadImage != null ? deadImage : () -> null;
        this.deathAnimation = deathAnimation;
    }

    public IsBlockType getBlockType() {
        return switch (currentState) {
            case ALIVE -> aliveType;
            case DEAD  -> deadType;
            case DYING -> deadType;
            default    -> null;
        };
    }

    public void setAliveTile(IsBlockType aliveType, HasImage aliveImage) {
        this.aliveType  = aliveType;
        this.aliveImage = aliveImage;
    }

    public void setDeadTile(IsBlockType deadType, HasImage deadImage) {
        this.deadType  = deadType;
        this.deadImage = deadImage;
    }

    public void setDeathAnimation(AnimationPlayer deathAnimation) {
        this.deathAnimation = deathAnimation;
        animationFinished   = false;
    }

    @Override
    public boolean isAlive() {
        return currentState == State.ALIVE;
    }

    @Override
    public void setAlive() {
        this.currentState = State.ALIVE;
        this.animationFinished = false;
        if (deathAnimation != null) {
            deathAnimation.reset();
        }
    }

    @Override
    public void setDead() {
        if (deathAnimation != null && !animationFinished) {
            this.currentState = State.DYING;
            deathAnimation.reset();
        } else {
            this.currentState = State.DEAD;
        }
    }

    @Override
    public BufferedImage getImage() {
        switch (currentState) {
            case DYING -> {
                if (deathAnimation != null) {
                    BufferedImage img = deathAnimation.getImage();
                    // Prüfen, ob die Todesanimation am Ende angekommen ist
                    AnimationData data = deathAnimation.getData();
                    if ( data != null && deathAnimation.getCurrentIndex() >= data.getFrameCount()-1 ) {
                        animationFinished = true;
                        currentState = State.DEAD; // Nach der Animation endgültig auf tot wechseln
                    }
                    return img;
                }
                currentState = State.DEAD;
                return deadImage.getImage();
            }
            case DEAD -> {
                return deadImage.getImage();
            }
            case ALIVE -> {
                return aliveImage.getImage();
            }
            default -> {
                return null;
            }
        }
    }

    public State getCurrentState() {
        return currentState;
    }

}
