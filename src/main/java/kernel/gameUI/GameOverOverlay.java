package kernel.gameUI;

import kernel.main.Game;
import kernel.state.Gamestate;
import kernel.state.Playing;
import kernel.state.cheatPlay;
import kernel.utilz.LoadSave;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static kernel.utilz.Constants.UI.URMButtons.URM_SIZE;
//yes

public class GameOverOverlay {
	private cheatPlay cheatPlay;
	private Playing playing;
	private BufferedImage img;
	private int imgX, imgY, imgW, imgH;
	private UrmButton menu, play;

	public GameOverOverlay(Playing playing) {
		this.playing = playing;
		//load gameover images
		img = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN);
		imgX = Game.GAME_WIDTH / 2 - imgW / 2;
		imgY = (int) (100 * Game.SCALE);
		imgW = (int) (img.getWidth() * Game.SCALE);
		imgH = (int) (img.getHeight() * Game.SCALE);
		setButtons();
	}
	public GameOverOverlay(cheatPlay cheatPlay) {
		this.cheatPlay = cheatPlay;
		img = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN);
		imgX = Game.GAME_WIDTH / 2 - imgW / 2;
		imgY = (int) (100 * Game.SCALE);
		imgW = (int) (img.getWidth() * Game.SCALE);
		imgH = (int) (img.getHeight() * Game.SCALE);
		setButtons();
	}

	private void setButtons() {
		int playX = (int) (550 * Game.SCALE);   int menuX = (int) (450 * Game.SCALE);
		int y = (int) (195 * Game.SCALE);
		play = new UrmButton(playX, y, URM_SIZE, URM_SIZE, 0);
		menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
	}

	public void draw(Graphics graphics) {
		graphics.setColor(new Color(0, 0, 0, 200));
		graphics.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		graphics.drawImage(img, imgX, imgY, imgW, imgH, null);
		menu.draw(graphics);   play.draw(graphics);
	}

	public void update() {
		menu.update();   play.update();
	}

	private boolean ifInside(UrmButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	public void mouseMoved(MouseEvent e) {
		play.setMouseOver(false);
		menu.setMouseOver(false);

		if (ifInside(menu, e))
			menu.setMouseOver(true);
		else if (ifInside(play, e))
			play.setMouseOver(true);
	}

	public void mouseReleased(MouseEvent e) {
		if (ifInside(menu, e)) {
			if (menu.isMousePressed()) {
				if(playing != null) {
					playing.resetAll();
					playing.setGamestate(Gamestate.MENU);
				}
				else{
					cheatPlay.resetAll();
					cheatPlay.setGamestate(Gamestate.MENU);
				}
			}
		} else if (ifInside(play, e))
			if (play.isMousePressed()) {
				if(playing!= null) {
					playing.resetAll();
					playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLevelIndex());
				}
				else {
					cheatPlay.resetAll();
					cheatPlay.getCheatingGame().getAudioPlayer().setLevelSong(cheatPlay.getLevelManager().getLevelIndex());
				}
			}

		menu.reBooleans();  play.reBooleans();
	}

	public void mousePressed(MouseEvent e) {
		if (ifInside(menu, e))
			menu.setMousePressed(true);
		else if (ifInside(play, e))
			play.setMousePressed(true);
	}

}
