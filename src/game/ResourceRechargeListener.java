/*
 *
 */

package game;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

@FunctionalInterface
public interface ResourceRechargeListener {

    void resourceRechargePerformed(Resource r, int charge, int overCharge);

}
