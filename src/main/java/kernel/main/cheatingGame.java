package kernel.main;

import kernel.audio.AudioPlayer;
import kernel.gameUI.AudioOptions;
import kernel.state.*;
import kernel.state.Menu;

import java.awt.*;
//the implementation and comment are similar to Game class
public class cheatingGame implements Runnable{
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private cheatPlay cheatPlay;
    private Menu menu;
    private GameOptions gameOptions;
    private AudioOptions audioOptions;
    private AudioPlayer audioPlayer;
    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 1.5f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
    private final boolean SHOW_FPS_UPS = true;

    public cheatingGame() {
        audioOptions = new AudioOptions(this);
        audioPlayer = new AudioPlayer();
        menu = new Menu(this);
        cheatPlay = new cheatPlay(this);
        gameOptions = new GameOptions(this);
        gamePanel = new GamePanel(this);
        new GameWindow(gamePanel);
        gamePanel.requestFocusInWindow();
        startGameLoop();
    }
    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        switch (Gamestate.state) {
            case MENU -> menu.update();
            case PLAYING -> cheatPlay.update();
            case OPTIONS -> gameOptions.update();
            case QUIT -> System.exit(0);
        }
    }

    @SuppressWarnings("incomplete-switch")
    public void render(Graphics g) {
        switch (Gamestate.state) {
            case MENU -> menu.draw(g);
            case PLAYING -> cheatPlay.draw(g);
            case OPTIONS -> gameOptions.draw(g);
        }
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;
        long previousTime = System.nanoTime();
        int frames = 0;   int updates = 0;
        long lastCheck = System.currentTimeMillis();
        double deltaU = 0;   double deltaF = 0;
        while (true) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;
            if (deltaU >= 1) {
                update();  updates++;
                deltaU--;
            }
            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;   deltaF--;
            }
            if (SHOW_FPS_UPS)
                if (System.currentTimeMillis() - lastCheck >= 1000) {
                    lastCheck = System.currentTimeMillis();
                    System.out.println("FPS: " + frames + " | UPS: " + updates);
                    frames = 0;   updates = 0;
                }
        }
    }
    public void windowFocusLost() {
        if (Gamestate.state == Gamestate.PLAYING)
            cheatPlay.getPlayer().resetDirBooleans();
    }

    public Menu getMenu() {
        return menu;
    }
    public cheatPlay getCheatPlay(){
        return cheatPlay;
    }

    public GameOptions getGameOptions() {
        return gameOptions;
    }
    public AudioOptions getAudioOptions() {
        return audioOptions;
    }
    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }
}