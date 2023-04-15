package kernel.gameUI;

import kernel.state.Gamestate;
import kernel.state.Playing;
import kernel.main.Game;
import kernel.state.cheatPlay;
import kernel.utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static kernel.utilz.Constants.UI.URMButtons.URM_SIZE;

public class LevelCompletedOverlay {
//yes
    private Playing playing;
    private cheatPlay cheatPlay;
    private UrmButton menu, another;
    private BufferedImage img;
    private int bgX, bgY, bgW, bgH;

    public LevelCompletedOverlay(Playing playing) {
        this.playing = playing;
        //load images
        img = LoadSave.GetSpriteAtlas(LoadSave.COMPLETED_IMG);
        bgW = (int) (img.getWidth() * Game.SCALE);
        bgH = (int) (img.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (75 * Game.SCALE);
        //load buttons
        int menuX = (int) (330 * Game.SCALE);
        int anotherX = (int) (445 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        another = new UrmButton(anotherX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }
    public LevelCompletedOverlay(cheatPlay cheatPlay) {
        this.cheatPlay = cheatPlay;
        //load images
        img = LoadSave.GetSpriteAtlas(LoadSave.COMPLETED_IMG);
        bgW = (int) (img.getWidth() * Game.SCALE);
        bgH = (int) (img.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (75 * Game.SCALE);
        //load buttons
        int menuX = (int) (330 * Game.SCALE);
        int anotherX = (int) (445 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        another = new UrmButton(anotherX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }



    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        g.drawImage(img, bgX, bgY, bgW, bgH, null);
        another.draw(g);
        menu.draw(g);
    }

    public void update() {
        another.update();   menu.update();
    }

    private boolean ifInside(UrmButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent event) {
        another.setMouseOver(false);  menu.setMouseOver(false);
        if (ifInside(menu, event))    menu.setMouseOver(true);
        else if (ifInside(another, event))    another.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (ifInside(menu, e)) {
            if (menu.isMousePressed()) {
                if (playing != null) {
                    playing.resetAll();
                    playing.setGamestate(Gamestate.MENU);
                }
                else{
                    cheatPlay.resetAll();
                    cheatPlay.setGamestate(Gamestate.MENU);
                }
            }
        } else if (ifInside(another, e))
            if (another.isMousePressed()) {
                if(playing!=null) {
                    playing.loadNextLevel();
                    playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLevelIndex());
                }
                else{
                    cheatPlay.loadNextLevel();
                    cheatPlay.getCheatingGame().getAudioPlayer().setLevelSong(cheatPlay.getLevelManager().getLevelIndex());
                }
            }
            menu.reBooleans();   another.reBooleans();//reset the booleans for each state
    }

    public void mousePressed(MouseEvent e) {
        if (ifInside(menu, e))   menu.setMousePressed(true);
        else if (ifInside(another, e))   another.setMousePressed(true);
    }

}
