/*
 *
 */

package graphic.missileLauncher;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.HasImage;
import graphic.block.IsBlock;

public class DefaultMissileLauncher extends MissileLauncher {

    public DefaultMissileLauncher(IsBlock owner, HasImage missileImage) {
        super(owner, missileImage);
    }

    @Override
    public boolean hasMissile() {
        return missileImage != null;
    }

}
