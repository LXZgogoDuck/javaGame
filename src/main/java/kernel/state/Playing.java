package kernel.state;

import kernel.components.ObjectManager;
import kernel.effects.Rain;
import kernel.gameUI.GameCompletedOverlay;
import kernel.gameUI.GameOverOverlay;
import kernel.gameUI.LevelCompletedOverlay;
import kernel.gameUI.PauseOverlay;
import kernel.levels.LevelManager;
import kernel.main.Game;
import kernel.objects.EnemyManager;
import kernel.objects.Player;
import kernel.utilz.LoadSave;
import kernel.main.cheatingGame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Float.POSITIVE_INFINITY;
import static kernel.utilz.Constants.Environment.*;

public class Playing extends State implements Statemethods{
    private Player player;
//    private Player player2;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private GameCompletedOverlay gameCompletedOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private Rain rain;
    private boolean drawRain;
    private boolean paused = false;
    private int xLvlOffset;
    private int leftBorder = (int) (0.25 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.75 * Game.GAME_WIDTH);
    private int maxLvlOffsetX;
    private BufferedImage backgroundImg, bigCloud, smallCloud, shipImgs[];
    private int[] smallCloudsPos;
    private Random rnd = new Random();
    private boolean lvlCompleted;
    private boolean gameCompleted;
    private boolean gameOver;
    private boolean playerDying;
    private boolean drawShip = true;
    private int shipAni, shipTick, shipDir = 1;
    private float shipHeightDelta, shipHeightChange = 0.05f * Game.SCALE;

    public Playing(Game game) {
        super(game);
       //initialize classes
        //manager loading
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);

        //load the player
        player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this,125);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

        //load all the overlays for settings
        pauseOverlay = new PauseOverlay(this,game);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
        gameCompletedOverlay = new GameCompletedOverlay(this);

        // load background pictures
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
        bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudsPos = new int[8];
        for (int i = 0; i < smallCloudsPos.length; i++)
            smallCloudsPos[i] = (int) (90 * Game.SCALE) + rnd.nextInt((int) (100 * Game.SCALE));

        shipImgs = new BufferedImage[4];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SHIP);
        for (int i = 0; i < shipImgs.length; i++)
            shipImgs[i] = temp.getSubimage(i * 78, 0, 78, 72);

        rain = new Rain();  //bg rain loading

        calcLvlOffset();
        loadStartLevel();
        if (rnd.nextFloat() >= 0.6f)
            drawRain = true;
    }

    public void loadNextLevel() {
        levelManager.setLevelIndex(levelManager.getLevelIndex() + 1);
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        resetAll();
        drawShip = false;
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
    }

    @Override
    public void update() {
        if (paused)
            pauseOverlay.update();
        else if (lvlCompleted)
            levelCompletedOverlay.update();
        else if (gameCompleted)
            gameCompletedOverlay.update();
        else if (gameOver)
            gameOverOverlay.update();
        else if (playerDying)
            player.update();
        else {
            if (drawRain)
                rain.update(xLvlOffset);
            levelManager.update();
            objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData());
            checkCloseToBorder();
            if (drawShip)
                updateShipAni();
        }
    }

    private void updateShipAni() {
        shipTick++;
        if (shipTick >= 35) {
            shipTick = 0;
            shipAni++;
            if (shipAni >= 4)
                shipAni = 0;
        }
        shipHeightDelta += shipHeightChange * shipDir;
        shipHeightDelta = Math.max(Math.min(10 * Game.SCALE, shipHeightDelta), 0);

        if (shipHeightDelta == 0)
            shipDir = 1;
        else if (shipHeightDelta == 10 * Game.SCALE)
            shipDir = -1;

    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder)
            xLvlOffset += diff - rightBorder;
        else if (diff < leftBorder)
            xLvlOffset += diff - leftBorder;
        xLvlOffset = Math.max(Math.min(xLvlOffset, maxLvlOffsetX), 0);
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        drawClouds(graphics);
        if (drawRain)  rain.draw(graphics, xLvlOffset);
        if (drawShip)  graphics.drawImage(shipImgs[shipAni], (int) (100 * Game.SCALE) - xLvlOffset, (int) ((288 * Game.SCALE) + shipHeightDelta), (int) (78 * Game.SCALE), (int) (72 * Game.SCALE), null);
        levelManager.draw(graphics, xLvlOffset);
        objectManager.draw(graphics, xLvlOffset);
        enemyManager.draw(graphics, xLvlOffset);
        player.render(graphics, xLvlOffset);  // arrayIndex out of bounds
        //in different situations of the game,set the corresponding overlays
        //esc_if game is paused, set "pauseOverlay"
        if (paused) {
            graphics.setColor(new Color(0, 0, 0, 150));
            graphics.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(graphics);
        } else if (gameOver)
            gameOverOverlay.draw(graphics);
        else if (gameCompleted)
            gameCompletedOverlay.draw(graphics);
        else if (lvlCompleted)
            levelCompletedOverlay.draw(graphics);
    }

    private void drawClouds(Graphics g) {
        for (int i = 0; i < 4; i++)
            g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int) (xLvlOffset * 0.3), (int) (204 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);

        for (int i = 0; i < smallCloudsPos.length; i++)
            g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int) (xLvlOffset * 0.7), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
    }

    public void resetGameCompleted() {
        gameCompleted = false;
    }
    //reset all the "check booleans" to its orginal state_prepare for the next game
    public void resetAll() {
        gameOver = false; paused = false;
        lvlCompleted = false;  playerDying = false;
        drawRain = false;
        if (rnd.nextFloat() >= 0.6f)   drawRain = true; //possibility to have rain
        player.resetAll();
        objectManager.resetAllObjects();
        enemyManager.resetAllEnemies();
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    public void checkObjectHit(Rectangle2D.Float attackBox) {
        objectManager.checkObjectHit(attackBox);
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    public void checkPotionTouched(Rectangle2D.Float hitbox) {
        objectManager.checkObjectTouched(hitbox);
    }

    public void checkSpikesTouched(Player p) {
        objectManager.checkSpikesTouched(p);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver) {
            if (e.getButton() == MouseEvent.BUTTON1)
                player.setAttacking(true);
            else if (e.getButton() == MouseEvent.BUTTON3)
                player.powerAttack();
        }
    }
//either clicking mouse or press space key can attack enemy!
    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver && !gameCompleted && !lvlCompleted)
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    player.setJump(true);break;
                case KeyEvent.VK_RIGHT:
                    player.setRight(true); break;
                case KeyEvent.VK_LEFT:
                    player.setLeft(true); break;
                case KeyEvent.VK_A:
                    player.setLeft(true);  break;
                case KeyEvent.VK_D:
                    player.setRight(true);  break;
                case KeyEvent.VK_W:
                    player.setJump(true);  break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                case KeyEvent.VK_SPACE:
                    player.setAttacking(true); break;
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver && !gameCompleted && !lvlCompleted)
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(false); break;
                case KeyEvent.VK_D:
                    player.setRight(false); break;
                case KeyEvent.VK_W:
                    player.setJump(false); break;
                case KeyEvent.VK_ESCAPE:
                    player.setAttacking(false); break;
                case KeyEvent.VK_UP:
                    player.setJump(false);break;
                case KeyEvent.VK_RIGHT:
                    player.setRight(false); break;
                case KeyEvent.VK_LEFT:
                    player.setLeft(false); break;

            }
    }

    public void mouseDragged(MouseEvent e) {
        if (!gameOver && !gameCompleted && !lvlCompleted)
            if (paused)
                pauseOverlay.mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (gameOver)
            gameOverOverlay.mousePressed(e);
        else if (paused)
            pauseOverlay.mousePressed(e);
        else if (lvlCompleted)
            levelCompletedOverlay.mousePressed(e);
        else if (gameCompleted)
            gameCompletedOverlay.mousePressed(e);

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (gameOver)
            gameOverOverlay.mouseReleased(e);
        else if (paused)
            pauseOverlay.mouseReleased(e);
        else if (lvlCompleted)
            levelCompletedOverlay.mouseReleased(e);
        else if (gameCompleted)
            gameCompletedOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameOver)
            gameOverOverlay.mouseMoved(e);
        else if (paused)
            pauseOverlay.mouseMoved(e);
        else if (lvlCompleted)
            levelCompletedOverlay.mouseMoved(e);
        else if (gameCompleted)
            gameCompletedOverlay.mouseMoved(e);
    }

    public void setLevelCompleted(boolean levelCompleted) {
        if(game!=null) {
            game.getAudioPlayer().lvlCompleted();
        }
        else if(game == null) cheatingGame.getAudioPlayer().lvlCompleted();
        if (levelManager.getLevelIndex() + 1 >= levelManager.getAmountOfLevels()) {
            //when all the levels are finished, the total game is completed!
            gameCompleted = true;
            levelManager.setLevelIndex(0);
            levelManager.loadNextLevel();
            resetAll();
            return;
        }
        this.lvlCompleted = levelCompleted;
    }

    public void setMaxLvlOffset(int lvlOffset) {
        this.maxLvlOffsetX = lvlOffset;
    }

    public void unpauseGame() {
        paused = false;
    }

    public Player getPlayer() {
        return player;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;
    }
}
