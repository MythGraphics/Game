/*
 *
 */

package graphic.MissileLauncher;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.DirectionalImage;

public class DefaultMissileLauncher extends MissileLauncher {

    public DefaultMissileLauncher(DirectionalImage missileImage) {
        super(missileImage);
    }

    @Override
    public boolean hasMissile() {
        return missileImage != null;
    }

}
