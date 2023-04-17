package kernel.gameUI;

import kernel.main.Game;
import kernel.state.Gamestate;
import kernel.state.Playing;
import kernel.utilz.LoadSave;
import kernel.state.cheatPlay;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
//游戏结束后退出页面
public class GameCompletedOverlay {
	private Playing playing;
	private cheatPlay cheatPlay;
	private BufferedImage img;
	private MenuButton quit;//when game is completed
	private int imgX, imgY, imgW, imgH;//size for images
	public GameCompletedOverlay(Playing playing) {
		this.playing = playing;
		//load gameOver images
		img = LoadSave.GetSpriteAtlas(LoadSave.GAME_COMPLETED);
		imgW = (int) (img.getWidth() * Game.SCALE);
		imgH = (int) (img.getHeight() * Game.SCALE);
		imgX = Game.GAME_WIDTH / 2 - imgW / 2;
		imgY = (int) (100 * Game.SCALE);
		//set buttons
		quit = new MenuButton(Game.GAME_WIDTH / 2, (int) (270 * Game.SCALE), 2, Gamestate.MENU);
	}
	public GameCompletedOverlay(cheatPlay cheatPlay) {
		this.cheatPlay = cheatPlay;

		img = LoadSave.GetSpriteAtlas(LoadSave.GAME_COMPLETED);
		imgW = (int) (img.getWidth() * Game.SCALE);
		imgH = (int) (img.getHeight() * Game.SCALE);
		imgX = Game.GAME_WIDTH / 2 - imgW / 2;
		imgY = (int) (100 * Game.SCALE);

		quit = new MenuButton(Game.GAME_WIDTH / 2, (int) (270 * Game.SCALE), 2, Gamestate.MENU);
	}

	public void draw(Graphics graphics) {
		graphics.setColor(new Color(0, 0, 0, 200));
		graphics.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		graphics.drawImage(img, imgX, imgY, imgW, imgH, null);
		quit.draw(graphics);
	}

	public void update() {
		quit.update();
	}
	//check whether the mouse is inside the button's scope
	private boolean ifInside(MenuButton btn, MouseEvent mouseEvent) {
		return btn.getBounds().contains(mouseEvent.getX(), mouseEvent.getY());
	}

	public void mouseMoved(MouseEvent e) {
		quit.setMouseOver(false);
		if (ifInside(quit, e))   quit.setMouseOver(true);
	}

	public void mouseReleased(MouseEvent e) {
		if (ifInside(quit, e)) {
			if (quit.isMousePressed()) {
				if(playing!=null) {
					playing.resetAll();
					playing.resetGameCompleted();
					playing.setGamestate(Gamestate.MENU);
				}
				else {
					cheatPlay.resetAll();
					cheatPlay.resetGameCompleted();
					cheatPlay.setGamestate(Gamestate.MENU);
				}
			}
		}
		quit.resetBools();
	}

	public void mousePressed(MouseEvent e) {
		if (ifInside(quit, e))  quit.setMousePressed(true);
	}
}


