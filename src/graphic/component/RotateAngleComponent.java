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

import util.vector.Vector2f;

public class RotateAngleComponent {

    protected float rotAngle = 0; // rotationAngle, radiant, 0.0f: RIGHT

    public RotateAngleComponent(float rotAngle) {
        this.rotAngle = rotAngle;
    }

    public void rotate(float phi) {
        rotAngle += phi;
        rotAngle  = Vector2f.normalizeAngle(rotAngle);
    }

    public float getViewAngle() {
        return rotAngle;
    }

}
