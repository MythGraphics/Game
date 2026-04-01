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

import graphic.*;
import static graphic.map.BlockType.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public abstract class GameMap extends JPanel implements ActionListener {

    public final static int DEFAULT_TILE_SIZE = 32;
    public final static int FPS = 15;

    public final int visibleWidth, visibleHeight, boardWidth, boardHeight;
    public final Dimension board, visibleBoard;

    protected final int tileSize, rowCount, columnCount;
    protected final String[] tileMap;
    protected final ArrayList<Block> blocks, blocks_prerender;

    protected Block space;
    protected MoveableSprite player;

    private final Collection<CollisionActionListener> collisionListeners;
    private final Point lastPlayerPos = new Point();

    private boolean active  = false;
    private boolean ready   = false;
    private Timer renderLoop;

    public GameMap(String[] tileMap, int tileSize, Dimension visibleSize) {
        this.tileMap    = tileMap;
        this.tileSize   = tileSize;
        visibleBoard    = visibleSize;
        visibleHeight   = visibleSize.height;
        visibleWidth    = visibleSize.width;
        rowCount        = tileMap.length;
        columnCount     = tileMap[0].length();
        boardWidth      = columnCount * tileSize;
        boardHeight     = rowCount * tileSize;
        board           = new Dimension(boardWidth, boardHeight);
        blocks_prerender    = new ArrayList<>(); // ohne Spieler, Enemy, NPC
        blocks              = new ArrayList<>(); // alle anderen Blocks (nackte Map)
        collisionListeners  = new ArrayList<>();
    }

    public GameMap(String[] tileMap, Dimension visibleSize) {
        this(tileMap, DEFAULT_TILE_SIZE, visibleSize);
    }

    public GameMap(String[] tileMap) {
        // setzt visibleSize auf mapSize, aber max. auf 800x600
        this(
            tileMap,
            DEFAULT_TILE_SIZE,
            new Dimension(
                Math.min( tileMap[0].length()*DEFAULT_TILE_SIZE, 800 ),
                Math.min( tileMap.length*DEFAULT_TILE_SIZE,      600 )
            )
        );
    }

    abstract void loadSprites();
    public abstract Color getAmbientColor();

    Block getBlock(BlockType bType, int x, int y, int tileSize) {
        return switch (bType) {
            case EXIT -> new Sprite( null, x, y, tileSize, EXIT );
            default -> new Sprite( null, x, y, tileSize, SPACE );
        }; // fail-save
    }

    public void init() {
        loadSprites();
        initMap();
        prerenderMap();
        renderLoop = new Timer(1000/FPS, this);
        ready = true;
    }

    @Override
    public Dimension getPreferredSize() {
        return visibleBoard;
    }

    private void prerenderMap() {
        BufferedImage prerenderedMap = new BufferedImage(boardWidth, boardHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = prerenderedMap.createGraphics();
        drawMap(g2d, 0, 0); // ganze GameMap rendern
        g2d.dispose();
        try {
            ImageIO.write( prerenderedMap, "png", new File( "currentMap.png" ));
        } catch (IOException e) {
            System.err.println( e.getMessage() );
        }
    }

    public Point getMaxPoint() {
        return new Point(boardWidth, boardHeight);
    }

    /**
     * Berechnet den X-Offset für den Scroll-Buffer, um den Spieler zu zentrieren.
     * Stellt sicher, dass der Viewport innerhalb der Kartengrenzen bleibt.
     */
    private int getOffsetX() {
        // by Google Gemini
        // Ziel-Offset: Spieler zentrieren
        int targetX = player.x - (visibleWidth / 2) + (tileSize / 2);
        // Begrenzungen prüfen:
        // Linker Rand: Offset kann nicht kleiner als 0 sein.
        int offsetX = Math.max(0, targetX);
        // Rechter Rand: Viewport darf nicht über die Gesamtbreite der Karte hinausragen.
        int maxOffsetX = boardWidth - visibleWidth;
        if (maxOffsetX < 0) { maxOffsetX = 0; } // Für den Fall, dass die Karte kleiner als der Viewport ist
        offsetX = Math.min(offsetX, maxOffsetX);
        return offsetX;
    }

    /**
     * Berechnet den Y-Offset für den Scroll-Buffer, um den Spieler zu zentrieren.
     * Stellt sicher, dass der Viewport innerhalb der Kartengrenzen bleibt.
     */
    private int getOffsetY() {
        // by Google Gemini
        // Ziel-Offset: Spieler zentrieren
        int targetY = player.y - (visibleHeight / 2) + (tileSize / 2);
        // Begrenzungen prüfen:
        // Oberer Rand: Offset kann nicht kleiner als 0 sein.
        int offsetY = Math.max(0, targetY);
        // Unterer Rand: Viewport darf nicht über die Gesamthöhe der Karte hinausragen.
        int maxOffsetY = boardHeight - visibleHeight;
        if (maxOffsetY < 0) { maxOffsetY = 0; } // Für den Fall, dass die Karte kleiner als der Viewport ist
        offsetY = Math.min(offsetY, maxOffsetY);
        return offsetY;
    }

    void loadTileMapChar(char tileMapChar, int x, int y, int tileSize) {
        BlockType bType = BlockType.getTileType(tileMapChar);
        if ( bType == null ) {
            System.err.println(
                "TileMapChar '" + tileMapChar + "' auf " + x + ", " + y + " ungültig (TileType ist null)! ->" +
                "auf SPACE gesetzt"
            );
            bType = SPACE;
        }
        switch (bType) {
            case SPACE, SPACEHOLDER -> {} // space wird in initMap() initialisiert, spaceholder ignorieren
            case NPC    -> blocks.add( getBlock( NPC, x, y, tileSize ));
            case ENEMY  -> blocks.add( getBlock( ENEMY, x, y, tileSize ));
            case PLAYER -> {
                player = (MoveableSprite) getBlock( PLAYER, x, y, tileSize );
                lastPlayerPos.x = x;
                lastPlayerPos.y = y;
            }
            default -> blocks_prerender.add( getBlock( bType, x, y, tileSize ));
        }
    }

    private void initMap() {
        int x, y;
        char tileMapChar;
        String row;
        for ( int c = 0; c < columnCount; c++ ) {
            for ( int r = 0; r < rowCount; r++ ) {
                row = tileMap[r];
                try {
                    tileMapChar = row.charAt(c);
                } catch (IndexOutOfBoundsException e) {
                    tileMapChar = ' ';
                }
                x = c*tileSize;
                y = r*tileSize;
                loadTileMapChar( tileMapChar, x, y, tileSize );
            }
        }
        space = getBlock( SPACE, 0, 0, tileSize );
    }

    public void activate() {
        if (!ready) {
            System.err.println("Map cannot activated - not initialized!");
            return;
        }
        if ( !renderLoop.isRunning() ) {
            renderLoop.start();
            active = true;
            System.out.println("Map rendering active.");
        }
    }

    public void deactivate() {
        active = false;
        renderLoop.stop();
        super.repaint();
        System.out.println("Map rendering deactivated.");
    }

    public void dispose() {
        System.out.println("Map is closing ...");
        ready = false;
        renderLoop.stop();
    }

    @Override
    // wird vom Timer ausgelöst
    public void actionPerformed(ActionEvent evt) {
        super.repaint();
    }

    public void addCollisionActionListener(CollisionActionListener actionListener) {
        collisionListeners.add(actionListener);
    }

    /**
     * Feuert Event bei Kollision Player->Block
     * @param block Kollisionsblock
     */
    void fireEvent(Block block) {
        System.out.println(
            "collision with block type "    + block.getType() +
            " at "                          + block.x + "," + block.y +
            " (column "                     + (block.x/block.getWidth()+1) +    // +1, um keine Indizes auszugeben
            ", row "                        + (block.y/block.getHeight()+1) +   // +1, um keine Indizes auszugeben
            ") as collision type "          + block.getType().getCollisionType()
        );
        collisionListeners.forEach( actionListener -> actionListener.collisionPerformed(
            new CollisionEvent( this, block.getType().getCollisionType(), block )
        ));
    }

    public void moveThroughPortal(Point target) {
        Point destination = new Point( target.x+player.x-lastPlayerPos.x, target.y+player.y-lastPlayerPos.y );
        setPlayerPosition(destination);
    }

    public void movePlayer(int deltaCol, int deltaRow) {
        int targetX = player.x + deltaCol*tileSize;
        int targetY = player.y + deltaRow*tileSize;
        setPlayerPosition( new Point( targetX, targetY ));
    }

    /**
     * Setzt die Position des Spielers auf die gegebene Position.
     * @param column Spalte (x)
     * @param row Zeile (y)
     */
    public void setPlayerPosition(int column, int row) {
        // 0 ist ungültig
        if ( column == 0 || row == 0 ) {
            return;
        }

        int targetColumn, targetRow;
        if (column < 0) {
            targetColumn = (column + columnCount) % columnCount;
        } else {
            targetColumn = (column-1 + columnCount) % columnCount;
        }
        if (row < 0) {
            targetRow = (row + rowCount) % rowCount;
        } else {
            targetRow = (row-1 + rowCount) % rowCount;
        }

        // Zuweisung der Pixel-Position
        setPlayerPosition( new Point( targetColumn*tileSize, targetRow*tileSize ));
    }

    public void setPlayerPosition(Point target) {
        setLastPlayerPosition();
        player.x = target.x;
        player.y = target.y;
        checkCollision();
    }

    private void setLastPlayerPosition() {
        lastPlayerPos.x = player.x;
        lastPlayerPos.y = player.y;
    }

    /**
     * Setzt die Position des Spielers auf die Position vor dem Bewegungsbefehl zurück.
     */
    public void resetPlayerPosition() {
        player.x = lastPlayerPos.x;
        player.y = lastPlayerPos.y;
    }

    public void move(KeyEvent evt) {
        if ( !active ) { return; }
        setLastPlayerPosition();
        switch ( evt.getKeyCode() ) {
            case KeyEvent.VK_UP,    KeyEvent.VK_W, KeyEvent.VK_8    -> player.move(Direction.UP);
            case KeyEvent.VK_DOWN,  KeyEvent.VK_S, KeyEvent.VK_2    -> player.move(Direction.DOWN);
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D, KeyEvent.VK_6    -> player.move(Direction.RIGHT);
            case KeyEvent.VK_LEFT,  KeyEvent.VK_A, KeyEvent.VK_4    -> player.move(Direction.LEFT);
        }

        // Kollision mit Panelgrenze
        if ( player.x < 0 )             { player.x = 0; }
        if ( player.x >= boardWidth )   { player.x = boardWidth-tileSize; }
        if ( player.y < 0 )             { player.y = 0; }
        if ( player.y >= boardHeight )  { player.y = boardHeight-tileSize; }

        // Kollision mit Block
        checkCollision();
    }

    void handleBlockCollision(Collection<Block> blocks) {
        blocks.forEach( block -> {
            if ( collision( block, player )) {
                fireEvent(block);
                if ( !block.type.passable ) {
                    resetPlayerPosition();
                }
            }
        });
    }

    void checkCollision() {
        handleBlockCollision(blocks_prerender);
        handleBlockCollision(blocks);
    }

    boolean collision(Block a, Block b) {
        try {
            return  a.x < b.x + b.width     &&
                    a.x + a.width > b.x     &&
                    a.y < b.y + b.height    &&
                    a.y + a.height > b.y
            ;
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void setRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Die folgenden zwei Zeilen sind wichtig, da die Größe des JPanels
        // nun dem sichtbaren Bereich entsprechen soll und nicht der vollen Kartengröße.
        // visibleWidth und visibleHeight müssen im Konstruktor oder als Konstanten gesetzt sein.
        g2d.setClip(0, 0, visibleWidth, visibleHeight);
        if (active) {
            setRenderingHints(g2d);
            draw(g2d);
        }
    }

    void draw(Graphics2D g2d) {
        int offsetX = getOffsetX();
        int offsetY = getOffsetY();
        drawMap(g2d, offsetX, offsetY);
        blocks.forEach( block -> {
            g2d.drawImage( block.getImage(), block.x-offsetX, block.y-offsetY, block.width, block.height, null );
        });
        g2d.drawImage( player.getImage(), player.x-offsetX, player.y-offsetY, player.width, player.height, null );
    }

    // nackte Map ohne Spieler, Enemy und NPC zeichnen
    private void drawMap(Graphics2D g2d, int offsetX, int offsetY) {
        if ( space != null ) {
            for (int r = 0; r < rowCount; r++) {
                for (int c = 0; c < columnCount; c++) {
                    g2d.drawImage(
                        space.getImage(),
                        tileSize*c - offsetX,
                        tileSize*r - offsetY,
                        space.width,
                        space.height,
                        null
                    );
                }
            }
        }
        blocks_prerender.forEach( block -> {
            g2d.drawImage( block.getImage(), block.x-offsetX, block.y-offsetY, block.width, block.height, null );
        });
    }

}
