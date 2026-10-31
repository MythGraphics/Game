/*
 *
 */

package graphic.component;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.block.Block;
import util.vector.Vector2f;

public class VectorMovementComponent implements Moveable {

    protected float px      = 0;
    protected float py      = 0;
    protected float vx      = 0;
    protected float vy      = 0;
    protected float vmax    = 0;
    protected boolean heavy = false; // "schwere" Blöcke übertragen Impuls, "leichte" nicht

    public VectorMovementComponent(float vmax, boolean heavy) {
        this.vmax   = vmax;
        this.heavy  = heavy;
    }

    public float getPx() {
        return px;
    }

    public float getPy() {
        return py;
    }

    public void setPx(float px) {
        this.px = px;
    }

    public void setPy(float py) {
        this.py = py;
    }

    public float getVx() {
        return vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public float getVmax() {
        return vmax;
    }

    public float getMoveAngle() {
        return Vector2f.getAngle(vx, vy);
    }

    public boolean isHeavy() {
        return heavy;
    }

    public void setHeavy(boolean heavy) {
        this.heavy = heavy;
    }

    public void accelerate(float dvx, float dvy) {
//      this.vx = Math.clamp(vx + dvx, -vmax, vmax);
//      this.vy = Math.clamp(vy + dvy, -vmax, vmax);
        this.vx += dvx;
        this.vy += dvy;
        float speedSquare = vx*vx + vy*vy;
        if (speedSquare > vmax*vmax) {
            float speed = (float) Math.sqrt(speedSquare);
            this.vx = (vx / speed) * vmax;
            this.vy = (vy / speed) * vmax;
        }
    }

    /**
     * Beschleunigt in Winkel-Richtung (radiant, 0.0f: RIGHT).
     * @param dv        delta-velocity
     * @param rotAngle  angle
     */
    public void accelerateAngle(float dv, float rotAngle) {
        this.vx = Math.clamp((float) (vx + Math.cos(rotAngle) * dv), -vmax, vmax);
        this.vy = Math.clamp((float) (vy + Math.sin(rotAngle) * dv), -vmax, vmax);
    }

    public float getSpeed() {
        return (float) Math.sqrt(vx*vx + vy*vy);
    }

    @Override
    public void move(Block owner) {
        px += vx;
        py += vy;
        owner.setPos( Math.round( px ), Math.round( py ));
    }

}
