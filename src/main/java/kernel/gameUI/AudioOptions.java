package kernel.gameUI;


import java.awt.Graphics;
import java.awt.event.MouseEvent;

import kernel.main.Game;
import kernel.main.cheatingGame;

import static kernel.utilz.Constants.UI.PauseButtons.SOUND_SIZE;
import static kernel.utilz.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static kernel.utilz.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

//checked!
public class AudioOptions {

	private VolumeButton volumeButton;
	private SoundButton musicButton, sfxButton;
	private cheatingGame cheatingGame;
	private Game game;

	public AudioOptions(Game game) {
		this.game = game;
		createSoundButtons();
		createVolumeButton();
	}
	public AudioOptions(cheatingGame cheatingGame){
		this.cheatingGame = cheatingGame;
		createSoundButtons();
		createVolumeButton();
	}

	private void createVolumeButton() {
		int volX = (int) (309 * Game.SCALE);
		int volY = (int) (278 * Game.SCALE);
		volumeButton = new VolumeButton(volX, volY, SLIDER_WIDTH, VOLUME_HEIGHT);
	}

	private void createSoundButtons() {
		int sndX = (int) (450 * Game.SCALE);
		int musicY = (int) (140 * Game.SCALE);
		int sfxY = (int) (186 * Game.SCALE);
		musicButton = new SoundButton(sndX, musicY, SOUND_SIZE, SOUND_SIZE);
		sfxButton = new SoundButton(sndX, sfxY, SOUND_SIZE, SOUND_SIZE);
	}

	public void update() {
		musicButton.update();
		sfxButton.update();
		volumeButton.update();
	}

	public void draw(Graphics graphics) {
		// Sound buttons
		musicButton.draw(graphics);
		sfxButton.draw(graphics);
		// Volume button
		volumeButton.draw(graphics);
	}

	public void mouseDragged(MouseEvent e) {
		if (volumeButton.isMousePressed()) {
			float valueBefore = volumeButton.getFloatValue();
			volumeButton.changeX(e.getX());
			float valueAfter = volumeButton.getFloatValue();
			if (valueBefore != valueAfter) {
				if(game!= null)  game.getAudioPlayer().setVolume(valueAfter);
				else cheatingGame.getAudioPlayer().setVolume(valueAfter);
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		if (ifInside(e, musicButton))
			musicButton.setMousePressed(true);
		else if (ifInside(e, sfxButton))
			sfxButton.setMousePressed(true);
		else if (ifInside(e, volumeButton))
			volumeButton.setMousePressed(true);
	}

	public void mouseReleased(MouseEvent e) {
		if (ifInside(e, musicButton)) {
			if (musicButton.isMousePressed()) {
				musicButton.setMuted(!musicButton.isMuted());
				if(game!= null) game.getAudioPlayer().Mute();
				if(game==null) cheatingGame.getAudioPlayer().Mute();
			}

		} else if (ifInside(e, sfxButton)) {
			if (sfxButton.isMousePressed()) {
				sfxButton.setMuted(!sfxButton.isMuted());
				if(game!=null) game.getAudioPlayer().togEffectMute();
				if(game==null) cheatingGame.getAudioPlayer().togEffectMute();
			}
		}

		musicButton.resetBools();
		sfxButton.resetBools();
		volumeButton.resetBooleans();
	}

	public void mouseMoved(MouseEvent e) {
		musicButton.setMouseOver(false);
		sfxButton.setMouseOver(false);
		volumeButton.setMouseOver(false);

		if (ifInside(e, musicButton))
			musicButton.setMouseOver(true);
		else if (ifInside(e, sfxButton))
			sfxButton.setMouseOver(true);
		else if (ifInside(e, volumeButton))
			volumeButton.setMouseOver(true);
	}

	private boolean ifInside(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

}
