package kernel.main;

import kernel.audio.AudioPlayer;
//import kernel.gameUI.AudioOptions;
import kernel.state.*;
import kernel.state.Menu;

import java.awt.*;

import static kernel.state.Gamestate.*;

public class Game implements Runnable {
// this is common game mode
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int fps = 120;
    private final int ups = 200;
    private Playing playing;
    private Menu menu;
//    private GameOptions gameOptions;
    private AudioPlayer audioPlayer;
//    private AudioOptions audioOptions;
    public final static int tiles_def = 32; //最原始的尺寸

    public final static float SCALE = 1.7f;
    public final static int tiles_width = 26;
    public final static int tiles_height = 14;
    public final static int TILES_SIZE = (int) (tiles_def * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * tiles_width;
    public final static int GAME_HEIGHT = TILES_SIZE * tiles_height;

    private final boolean showFPS_UPS = true;

    public Game() {
        //System.out.println("size: " + GAME_WIDTH + " : " + GAME_HEIGHT);//1664:896
        audioPlayer = new AudioPlayer();
        menu = new Menu(this);
        playing = new Playing(this);
        //mark: gameOptions has to be after all the components, otherwise it will give back "null"
        //set game window and panel
        gamePanel = new GamePanel(this);
        new GameWindow(gamePanel);
        gamePanel.requestFocusInWindow();
        startGameLoop();
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    //页面更新到最新状态
    public void update() {
        switch (Gamestate.gamestate) {
            case MENU -> menu.update();
            case PLAYING -> playing.update();
            case QUIT -> System.exit(0); //if click "exist", then quit the game
        }
    }
    //draw the components
    @SuppressWarnings("incomplete-switch")
    public void render(Graphics g) {//draw the components
        switch (Gamestate.gamestate) {
            case MENU -> menu.draw(g);
            case PLAYING -> playing.draw(g);
        }
    }

    // begin game loop
    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / fps;
        double timePerUpdate = 1000000000.0 / ups;
        long previous = System.nanoTime();
        long lastCheck = System.currentTimeMillis();
        int frames = 0;      int updates = 0;
        double dU = 0;    double dF = 0;

        while (true) {
            long current = System.nanoTime();
            dU += (current - previous) / timePerUpdate;
            dF += (current - previous) / timePerFrame;
            previous = current;

            if (dU >= 1) {
                update();   updates++;
                dU--;
            }
            if (dF >= 1) {
                gamePanel.repaint();
                frames++;   dF--;
            }
            if (showFPS_UPS)
                if (System.currentTimeMillis() - lastCheck >= 1000) {
                    lastCheck = System.currentTimeMillis();
                    System.out.println("FPS: " + frames + " | UPS: " + updates);
                    frames = 0;   updates = 0;
                }
        }
    }

    public void windowFocusLost() {
        if (Gamestate.gamestate == Gamestate.PLAYING)
            playing.getPlayer().resetDirBooleans();
    }

    public Menu getMenu() {
        return menu;
    }
    public Playing getPlaying() {
        return playing;
    }
    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }
}