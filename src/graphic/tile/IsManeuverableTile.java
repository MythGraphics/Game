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

import graphic.HasDirection;
import graphic.Maneuverable;

// IsPlayer
public interface IsManeuverableTile extends IsMoveableTile, Maneuverable, HasDirection {}
