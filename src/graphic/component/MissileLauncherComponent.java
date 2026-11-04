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

import graphic.missileLauncher.HasMissileLauncher;
import graphic.missileLauncher.MissileLauncher;

public record MissileLauncherComponent(MissileLauncher getMissileLauncher) implements HasMissileLauncher {}
