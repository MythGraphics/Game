/*
 *
 */

package graphic.map;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import java.awt.Dimension;

public record MapRecord(int fps, int tileSize, Dimension visibleSize) {}
