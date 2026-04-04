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

public class ID {

    private static int qID = -1, nID = -1, sID = -1, iID = -1, mID = -1, pID = -1, eID = -1, wID = -1, aID = -1;

    private ID() {}

    public static int getNextQuestID() {
        ++qID;
        return qID;
    }

    public static int getNextNpcID() {
        ++nID;
        return nID;
    }

    public static int getNextSignID() {
        ++sID;
        return sID;
    }

    public static int getNextItemID() {
        ++iID;
        return iID;
    }

    public static int getNextMinionID() {
        ++mID;
        return mID;
    }

    public static int getNextEnemyID() {
        ++eID;
        return eID;
    }

    public static int getNextPortalID() {
        ++pID;
        return pID;
    }

    public static int getWeaponID() {
        ++wID;
        return wID;
    }

    public static int getAmmoID() {
        ++aID;
        return aID;
    }

}
