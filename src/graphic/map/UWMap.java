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

import game.PlayerActionListener;
import game.UWMapRoutine;
import static graphic.map.BlockType.BUBBLE;
import static graphic.map.BlockType.WATERLINE;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.HashSet;

public abstract class UWMap extends GameMap {

    public final HashSet<Block> bubbles;

    private final UWMapRoutine uw;
    private final Collection<PlayerActionListener> playerActionListeners = new HashSet<>();

    private int uwlevel;

    public UWMap(String[] tileMap, UWMapRoutine uw) {
        super(tileMap);
        this.uw = uw;
        bubbles = new HashSet<>();
    }

    @Override
    // wird vom Timer ausgelöst
    public void actionPerformed(ActionEvent evt) {
        super.actionPerformed(evt);
        if ( super.player.y > uwlevel ) {
            // Spieler ist unter Wasser
            uw.useResource();
        } else {
            // Spieler ist auf oder über Wasser
            uw.rechargeResource();
        }
        firePlayerEvent();
    }

    public void addPlayerActionListener(PlayerActionListener actionListener) {
        playerActionListeners.add(actionListener);
    }

    private void firePlayerEvent() {
        playerActionListeners.forEach( actionListener -> actionListener.playerActionPerformed( uw.getPlayer() ));
    }

    @Override
    void loadTileMapChar(char tileMapChar, int x, int y, int tileSize) {
        super.loadTileMapChar(tileMapChar, x, y, tileSize);
        BlockType tType = BlockType.getTileType(tileMapChar);
        switch (tType) {
            case WATERLINE -> uwlevel = y;
            case BUBBLE -> bubbles.add( getBlock( BUBBLE, x, y, tileSize ));
        }
    }

    /**
     * Hier zusätzliche Kollisionsbehandlung implementieren.
     */
    @Override
    public void checkCollision() {
        super.checkCollision();
        super.handleBlockCollision(bubbles);
    }

    /**
     * Hier zusätzliche Sprites zeichnen lassen.
     * @param g2d Das Graphics-Objekt zum zeichnen
     */
    @Override
    void draw(Graphics2D g2d) {
        super.draw(g2d);
        bubbles.forEach( bubble -> {
            g2d.drawImage( bubble.getImage(), bubble.x, bubble.y, bubble.width, bubble.height, null );
        });
    }

}
