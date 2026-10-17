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

import graphic.Maneuverable;
import graphic.map.IsMoveableBlock;

public interface IsMoveableTile extends Maneuverable, IsMoveableBlock, IsBlockTile {}
