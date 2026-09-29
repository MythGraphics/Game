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

import java.awt.image.BufferedImage;

public class DeadOrAliveImageSet implements HasImage {

    public enum State {
        ALIVE,
        DYING,
        DEAD
    }

    private HasImage aliveImage;
    private HasImage deadImage;
    private AnimationPlayer deathAnimation;
    private State currentState = State.ALIVE;
    private boolean animationFinished = false;

    public DeadOrAliveImageSet(HasImage aliveImage) {
        this(aliveImage, null, null);
    }

    public DeadOrAliveImageSet(HasImage aliveImage, HasImage deadImage) {
        this(aliveImage, deadImage, null);
    }

    public DeadOrAliveImageSet(HasImage aliveImage, HasImage deadImage, AnimationPlayer deathAnimation) {
        if (aliveImage == null) {
            throw new IllegalArgumentException("aliveImage darf nicht null sein.");
        }
        this.aliveImage     = aliveImage;
        this.deadImage      = deadImage != null ? deadImage : () -> null;
        this.deathAnimation = deathAnimation;
    }

    public void setAliveImage(HasImage aliveImage) {
        this.aliveImage = aliveImage;
    }

    public void setDeadImage(HasImage deadImage) {
        this.deadImage = deadImage;
    }

    public void setDeathAnimation(AnimationPlayer deathAnimation) {
        this.deathAnimation = deathAnimation;
        animationFinished   = false;
    }

    public void setAlive() {
        this.currentState = State.ALIVE;
        this.animationFinished = false;
        if (deathAnimation != null) {
            deathAnimation.reset();
        }
    }

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
