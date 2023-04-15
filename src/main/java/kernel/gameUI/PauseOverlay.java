package kernel.gameUI;

import kernel.main.cheatingGame;
import kernel.state.Playing;
import kernel.main.Game;
import kernel.state.Gamestate;
import kernel.state.cheatPlay;
import kernel.utilz.LoadSave;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

import static kernel.utilz.Constants.UI.URMButtons.URM_SIZE;


public class PauseOverlay {
    private Playing playing;
    private cheatPlay cheatPlay;
    private BufferedImage backgroundImg;
    private int bX, bY, bW, bH;
    private AudioOptions audioOptions;
    private UrmButton menuBTN, reBTN, unpauseBTN;

    public PauseOverlay(Playing playing,Game game) {
        this.playing = playing;
        setBackgroundImg();
        audioOptions = playing.getGame().getAudioOptions();
        createURM();
    }
    public PauseOverlay(cheatPlay cheatPlay, cheatingGame c){
        this.cheatPlay = cheatPlay;
        setBackgroundImg();
        audioOptions = cheatPlay.getCheatingGame().getAudioOptions();
        createURM();
    }

    public void setBackgroundImg(){
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bW = (int) (backgroundImg.getWidth() * Game.SCALE);
        bH = (int) (backgroundImg.getHeight() * Game.SCALE);
        bX = Game.GAME_WIDTH / 2 - bW / 2;
        bY = (int) (25 * Game.SCALE);
    }
    public void createURM(){
        int menuX = (int) (313 * Game.SCALE);
        int replayX = (int) (387 * Game.SCALE);
        int unpauseX = (int) (462 * Game.SCALE);
        int bY = (int) (325 * Game.SCALE);
        menuBTN = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        reBTN = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
        unpauseBTN = new UrmButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);
    }

    public void update() {
        menuBTN.update(); // update all the buttons to the latest states
        reBTN.update();
        unpauseBTN.update();
        audioOptions.update();
    }

    public void draw(Graphics g) {
        // Background
        g.drawImage(backgroundImg, bX, bY, bW, bH, null);
        // UrmButtons
        audioOptions.draw(g);
        menuBTN.draw(g);  reBTN.draw(g);  unpauseBTN.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e) {
        if (ifInside(e, menuBTN))
            menuBTN.setMousePressed(true);
        else if (ifInside(e, reBTN))
            reBTN.setMousePressed(true);
        else if (ifInside(e, unpauseBTN))
            unpauseBTN.setMousePressed(true);
        else
            audioOptions.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        if (ifInside(e, menuBTN)) {
            if (menuBTN.isMousePressed()) {
                playing.resetAll();
                playing.setGamestate(Gamestate.MENU);
                playing.unpauseGame();
            }
        } else if (ifInside(e, reBTN)) {
            if (reBTN.isMousePressed()) {
                playing.resetAll();
                playing.unpauseGame();
            }
        } else if (ifInside(e, unpauseBTN)) {
            if (unpauseBTN.isMousePressed())
                playing.unpauseGame();
        } else
            audioOptions.mouseReleased(e);

        menuBTN.reBooleans();
        reBTN.reBooleans();
        unpauseBTN.reBooleans();

    }

    public void mouseMoved(MouseEvent e) {
        menuBTN.setMouseOver(false);
        reBTN.setMouseOver(false);
        unpauseBTN.setMouseOver(false);

        if (ifInside(e, reBTN))
            reBTN.setMouseOver(true);
        else if (ifInside(e, menuBTN))
            menuBTN.setMouseOver(true);
        else if (ifInside(e,unpauseBTN))
            unpauseBTN.setMouseOver(true);
        else
            audioOptions.mouseMoved(e);
    }
    private boolean ifInside(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

}



