package kernel.state;

import kernel.Main;
import kernel.main.Game;
import kernel.state.Gamestate;
import kernel.gameUI.MenuButton;
import kernel.main.cheatingGame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import kernel.utilz.LoadSave;
// checked already!
public class Menu extends State {
    private MenuButton[] buttons = new MenuButton[2];
    private BufferedImage backgroundImg, background;
    private int menuX, menuY, mWidth, mHeight;

    public Menu(Game game) {
        super(game);
        loadButtons();
        loadBackground();
        background = LoadSave.GetSpriteAtlas(LoadSave.bgimg);
    }
    public Menu(cheatingGame c) {
        super(c);
        loadButtons();
        loadBackground();
        background = LoadSave.GetSpriteAtlas(LoadSave.menu2);
    }
    // load menu background picture and set the position for menu
    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.menu);
        mWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
        mHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - mWidth / 2;
        menuY = (int) (25 * Game.SCALE);
    }
    //load the buttons on the menu including play/options/quit
    private void loadButtons() {
        buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (150 * Game.SCALE), 0, Gamestate.PLAYING);
        buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (220 * Game.SCALE), 2, Gamestate.QUIT);
    }

    public void update() {
        for (MenuButton menuButton : buttons)
            menuButton.update();
    }

    public void draw(Graphics graphics) {
        graphics.drawImage(background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        graphics.drawImage(backgroundImg, menuX, menuY, mWidth, mHeight, null);
    //draw all the buttons on the menu
        for (MenuButton menuButton : buttons)
            menuButton.draw(graphics);
    }
    //the following functions are used to interacted with the users_ get mouse and keyboard inputs
    public void mousePressed(MouseEvent e) {
        for (MenuButton menuButton : buttons) {
            if (isIn(e, menuButton)) {
                menuButton.setMousePressed(true);
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                if (mb.isMousePressed())
                    mb.applyGamestate();
                if (mb.getState() == Gamestate.PLAYING) {
                    if (game != null)
                        game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
                    else cheatingGame.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
                }
                break;
            }
        }
        for (MenuButton mb : buttons)
            mb.resetBools();
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        for (MenuButton menuButton : buttons)
            menuButton.setMouseOver(false);

        for (MenuButton menuButton : buttons)
            if (isIn(mouseEvent, menuButton)) {
                menuButton.setMouseOver(true);
                break;
            }

    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {

    }

}