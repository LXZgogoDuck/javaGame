package kernel.state;

import kernel.components.ObjectManager;
import kernel.effects.Rain;
import kernel.gameUI.GameCompletedOverlay;
import kernel.gameUI.GameOverOverlay;
import kernel.gameUI.LevelCompletedOverlay;
import kernel.levels.LevelManager;
import kernel.main.Game;
import kernel.main.cheatingGame;
import kernel.objects.EnemyManager;
import kernel.objects.Player;
import kernel.utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static java.lang.Float.POSITIVE_INFINITY;
/*
    ideal cheating mode: the player is driving the ship,and when the ship touch the enemy, the enemy will die immediately_
    need to figure out
     1.how to combine the player and ship picture
     2.when ship hitbox meet enemy hitbox, the enemy's currentBlood == 0
     3.when game is completed, there's special bonus: surprises!

 */
public class cheatPlay extends State{
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private GameOverOverlay gameOverOverlay;
    private GameCompletedOverlay gameCompletedOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private Rain rain;
    private boolean drawRain;
    private boolean paused = false;
    private int xOff;
    private int leftBorder = (int) (0.25 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.75 * Game.GAME_WIDTH);
    private int maxOffX;
    private BufferedImage backgroundImg, bigCloud, smallCloud, shipImgs[];
    private Random rnd = new Random();
    private boolean lvlCompleted;
    private boolean gameCompleted;
    private boolean gameOver;
    private boolean playerDying;

    public cheatPlay(cheatingGame cheatingGame) {
        super(cheatingGame);
        levelManager = new LevelManager(cheatingGame);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);
        float a = POSITIVE_INFINITY;
        //player loading for cheatGame
        player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this, a);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        //overlay loading
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
        gameCompletedOverlay = new GameCompletedOverlay(this);
        // bg loading
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG3);
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
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    private void calcLvlOffset() {
        maxOffX = levelManager.getCurrentLevel().getLvlOffset();
    }

    public void update() {
        if (lvlCompleted)
            levelCompletedOverlay.update();
        else if (gameCompleted)
            gameCompletedOverlay.update();
        else if (gameOver)
            gameOverOverlay.update();
        else if (playerDying)
            player.update();
        else {
            if (drawRain)
                rain.update(xOff);
            levelManager.update();
            objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData());
            checkCloseToBorder();
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xOff;

        if (diff > rightBorder)
            xOff += diff - rightBorder;
        else if (diff < leftBorder)
            xOff += diff - leftBorder;
        xOff = Math.max(Math.min(xOff, maxOffX), 0);
    }
//draw all the components on the cheating game including background/enemies/objects/player.
    public void draw(Graphics graphics) {
        graphics.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        if (drawRain)  rain.draw(graphics, xOff);
        levelManager.draw(graphics, xOff);
        objectManager.draw(graphics, xOff);
        enemyManager.draw(graphics, xOff);
        player.render(graphics, xOff);  // arrayIndex out of bounds

//esc_game paused
        if (gameOver)
            gameOverOverlay.draw(graphics);
        else if (gameCompleted)
            gameCompletedOverlay.draw(graphics);
        else if (lvlCompleted)
            levelCompletedOverlay.draw(graphics);
    }


    public void resetGameCompleted() {
        gameCompleted = false;
    }

    public void resetAll() {
        gameOver = false;
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
        objectManager.checkSpikesTouched_2(p);
    }


    public void mouseClicked(MouseEvent e) {
        if (!gameOver) {
            if (e.getButton() == MouseEvent.BUTTON1)
                player.setAttacking(true);
            else if (e.getButton() == MouseEvent.BUTTON3)
                player.powerAttack();
        }
    }
    //either clicking mouse or press space key can attack enemy!

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
                case KeyEvent.VK_SPACE:
                    player.setAttacking(true); break;
            }
    }


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
    }


    public void mousePressed(MouseEvent e) {
        if (gameOver)
            gameOverOverlay.mousePressed(e);
        else if (lvlCompleted)
            levelCompletedOverlay.mousePressed(e);
        else if (gameCompleted)
            gameCompletedOverlay.mousePressed(e);

    }


    public void mouseReleased(MouseEvent e) {
        if (gameOver)
            gameOverOverlay.mouseReleased(e);
        else if (lvlCompleted)
            levelCompletedOverlay.mouseReleased(e);
        else if (gameCompleted)
            gameCompletedOverlay.mouseReleased(e);
    }


    public void mouseMoved(MouseEvent e) {
        if (gameOver)
            gameOverOverlay.mouseMoved(e);
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
        this.maxOffX = lvlOffset;
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

    public void setPlayerDying(boolean deathState) {
        this.playerDying = deathState;
    }
}
