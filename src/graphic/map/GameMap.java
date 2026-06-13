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

import graphic.Direction;
import graphic.MoveableSprite;
import graphic.Sprite;
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
import java.util.Set;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public abstract class GameMap extends JPanel implements ActionListener {

    public final static int DEFAULT_TILE_SIZE = 32;
    public final static int FPS = 15;

    public final int visibleWidth, visibleHeight, boardWidth, boardHeight;
    public final Dimension board, visibleBoard;

    public static boolean prerenderMap = true;

    protected final int tileSize, rowCount, columnCount;
    protected final String[] tileMap;
    protected final Collection<Block> blocks = new ArrayList<>();

    protected Block space;
    protected MoveableSprite player;

    private final Collection<CollisionActionListener> collisionListeners = new ArrayList<>();
    private final Point lastPlayerPos = new Point();

    private boolean active  = false;
    private boolean ready   = false;
    private Timer renderLoop;

    public GameMap(String[] tileMap, int tileSize, Dimension visibleSize) {
        this.tileMap        = tileMap;
        this.tileSize       = tileSize;
        visibleBoard        = visibleSize;
        visibleHeight       = visibleSize.height;
        visibleWidth        = visibleSize.width;
        rowCount            = tileMap.length;
        columnCount         = tileMap[0].length();
        boardWidth          = columnCount * tileSize;
        boardHeight         = rowCount * tileSize;
        board               = new Dimension(boardWidth, boardHeight);
    }

    public GameMap(String[] tileMap, Dimension visibleSize) {
        this(tileMap, DEFAULT_TILE_SIZE, visibleSize);
    }

    public GameMap(String[] tileMap) {
        // setzt visibleSize auf mapSize, aber max. 800x600
        this(
            tileMap,
            DEFAULT_TILE_SIZE,
            new Dimension(
                Math.min( tileMap[0].length()*DEFAULT_TILE_SIZE, 800 ),
                Math.min( tileMap.length*DEFAULT_TILE_SIZE,      600 )
            )
        );
    }

    protected abstract void loadSprites();
    public abstract Color getAmbientColor();

    Block getBlock(BlockType bType, int x, int y, int tileSize) {
        return switch (bType) {
            case EXIT   -> new Sprite( null, x, y, tileSize, EXIT );
            default     -> new Sprite( null, x, y, tileSize, bType );
        };
    }

    public void init() {
        loadSprites();
        initMap();
        if (prerenderMap) {
            renderMapImage(ENEMY, NPC, PLAYER);
        }
        renderLoop = new Timer(1000/FPS, this);
        ready = true;
    }

    public void renderMapImage(BlockType ... bType) {
        BufferedImage prerenderedMap = new BufferedImage(boardWidth, boardHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = prerenderedMap.createGraphics();
        drawMap(g2d, 0, 0, PLAYER, NPC, ENEMY);
        g2d.dispose();
        try {
            File file = new File("currentMap.png");
            ImageIO.write(prerenderedMap, "png", file);
            System.out.println( "Map gespeichert: " + file.getAbsolutePath() );
        } catch (IOException e) {
            System.err.println( e.getMessage() );
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return visibleBoard;
    }

    public Point getMaxPoint() {
        return new Point(boardWidth, boardHeight);
    }

    /**
     * Berechnet den X-Offset für den Scroll-Buffer, um den Spieler zu zentrieren.
     * Stellt sicher, dass der Viewport innerhalb der Kartengrenzen bleibt.
     */
    private int getOffsetX() {
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
                "TileMapChar '" + tileMapChar + "' auf " + x + ", " + y + " ungültig (TileType ist null) -> " +
                "wird ignoriert"
            );
            return;
        }
        switch (bType) {
            case SPACE, SPACEHOLDER -> {} // space wird in loadSpaceTile() initialisiert, spaceholder ignorieren
            case PLAYER -> {
                player = (MoveableSprite) getBlock( PLAYER, x, y, tileSize );
                lastPlayerPos.x = x;
                lastPlayerPos.y = y;
            }
            default -> blocks.add( getBlock( bType, x, y, tileSize ));
        }
    }

    void loadSpaceTile() {
        space = getBlock( SPACE, 0, 0, tileSize );
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
                loadTileMapChar(tileMapChar, x, y, tileSize);
            }
        }
        loadSpaceTile();
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
     * Feuert Event bei Kollision Player -> Panel-Grenze
     */
    void firePanelEvent(Block collider) {
        System.out.println(
            "collision of block type "  + collider.getType() + " with panel boundary" +
            " at "                      + collider.x + "," + collider.y +
            " (column "                 + ( collider.x / collider.getWidth() + 1 ) +  // +1, um keine Indizes auszugeben
            ", row "                    + ( collider.y / collider.getHeight() + 1 ) + // +1, um keine Indizes auszugeben
            ")"
        );
        collisionListeners.forEach( actionListener -> actionListener.collisionPerformed(
            new CollisionEvent(
                this,
                CollisionType.BOUNDARY,
                new Block(collider.x, collider.y, 0, BOUNDARY) {
                    @Override
                    public Image getImage() { return null; }
                },
                collider
            )
        ));
    }

    /**
     * Feuert Event bei Kollision Player -> Block
     * @param block Kollisionsblock
     */
    void fireBlockEvent(Block target, Block collider) {
        System.out.println(
            "collision with block type "    + target.getType() +
            " at "                          + target.x + "," + target.y +
            " (column "                     + ( target.x / target.getWidth() + 1 ) +  // +1, um keine Indizes auszugeben
            ", row "                        + ( target.y / target.getHeight() + 1 ) + // +1, um keine Indizes auszugeben
            ") as collision type "          + target.getType().getCollisionType()
        );
        collisionListeners.forEach( actionListener -> actionListener.collisionPerformed(
            new CollisionEvent( this, target.getType().getCollisionType(), target, collider )
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
        detectBlockCollision(blocks);
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
        detectPanelCollision(); // Kollision mit Panelgrenze
        detectBlockCollision(blocks); // Kollision mit Block
    }

    final void detectPanelCollision() {
        if (player.x < 0) {
            firePanelEvent(player);
            player.x = 0;
        }
        else if (player.x >= boardWidth) {
            firePanelEvent(player);
            player.x = boardWidth-tileSize;
        }
        else if (player.y < 0) {
            firePanelEvent(player);
            player.y = 0;
        }
        else if (player.y >= boardHeight) {
            firePanelEvent(player);
            player.y = boardHeight-tileSize;
        }
    }

    final void detectBlockCollision(Collection<Block> blocks) {
        blocks.forEach( block -> {
            if ( collision( block, player )) {
                fireBlockEvent(block, player);
                if ( !block.type.passable ) {
                    resetPlayerPosition();
                }
            }
        });
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

    public void removeBlock(Block block) {
        if (block instanceof DeadOrAliveBlock doaBlock) {
            // doaBlock.setAliveImage(null);
            doaBlock.dead();
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

    final void drawMap(Graphics2D g2d, int offsetX, int offsetY, BlockType... bTypeExclude) {
        Set<BlockType> excludedTypes = Set.of(bTypeExclude);
        Collection<Block> currentBlocks = blocks.stream()
                                                .filter( block -> !excludedTypes.contains( block.getType() ))
                                                .collect( Collectors.toList() );
        if ( space != null && !excludedTypes.contains( SPACE )) {
            drawSpace(g2d, offsetX, offsetY, space);
        }
        if ( !currentBlocks.isEmpty() ) {
            drawBlocks(g2d, offsetX, offsetY, currentBlocks);
        }
        if ( !excludedTypes.contains( PLAYER )) {
            drawPlayer(g2d, offsetX, offsetY, player);
        }
    }

    void draw(Graphics2D g2d) {
        int offsetX = getOffsetX();
        int offsetY = getOffsetY();
        drawSpace(g2d, offsetX, offsetY, space);
        drawBlocks(g2d, offsetX, offsetY, blocks);
        drawPlayer(g2d, offsetX, offsetY, player);
    }

    private void drawSpace(Graphics2D g2d, int offsetX, int offsetY, Block space) {
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
    }

    private void drawBlocks(Graphics2D g2d, int offsetX, int offsetY, Collection<Block> blocks) {
        blocks.forEach( block -> {
            g2d.drawImage( block.getImage(), block.x-offsetX, block.y-offsetY, block.width, block.height, null );
        });
    }

    private void drawPlayer(Graphics2D g2d, int offsetX, int offsetY, Block player) {
        g2d.drawImage( player.getImage(), player.x-offsetX, player.y-offsetY, player.width, player.height, null );
    }

    public int getEnemyCount() {
        int value = 0;
        for (Block block : blocks) {
            if (block instanceof DeadOrAliveBlock doaBlock) {
                if ( doaBlock.getType() == ENEMY && !doaBlock.isDead() ) {
                    ++value;
                }
            }
        }
        return value;
    }

}
