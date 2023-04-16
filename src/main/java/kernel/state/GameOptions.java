package kernel.state;

import kernel.gameUI.AudioOptions;
import kernel.gameUI.PauseButton;
import kernel.gameUI.UrmButton;
import kernel.main.Game;
import kernel.main.cheatingGame;
import kernel.utilz.LoadSave;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import static kernel.utilz.Constants.UI.URMButtons.URM_SIZE;
public class GameOptions extends State implements Statemethods {
    private AudioOptions audioOptions;
    private BufferedImage backgroundImg, optionsBackgroundImg;
    private int bgX, bgY, bgW, bgH;
    private UrmButton menuB;

    public GameOptions(Game game) {
        super(game);
        loadBTN();
        loadIMG();
        audioOptions = game.getAudioOptions();
    }
    public GameOptions (cheatingGame cheatingGame){
        super(cheatingGame);
        loadBTN();
        loadIMG();
        audioOptions = cheatingGame.getAudioOptions();
    }
    public void loadBTN(){
        int menuX = (int) (387 * Game.SCALE);
        int menuY = (int) (325 * Game.SCALE); //size
        menuB = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
    }
    public void loadIMG(){
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        optionsBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_MENU);
        bgW = (int) (optionsBackgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (optionsBackgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (33 * Game.SCALE);
    }

    @Override
    public void update() {
        menuB.update();
        audioOptions.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(optionsBackgroundImg, bgX, bgY, bgW, bgH, null);

        menuB.draw(g);
        audioOptions.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        } else
            audioOptions.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed())
                Gamestate.state = Gamestate.MENU;
        } else
            audioOptions.mouseReleased(e);
        menuB.reBooleans();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);

        if (isIn(e, menuB))
            menuB.setMouseOver(true);
        else
            audioOptions.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            Gamestate.state = Gamestate.MENU;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

}
