package kernel.gameUI;

import kernel.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static kernel.utilz.Constants.UI.PauseButtons.SOUND_SIZE_DEFAULT;

public class SoundButton extends PauseButton {

    private BufferedImage[][] soundImgs;
    private boolean mouseOver, mousePressed;
    private boolean muted;
    private int rIndex, cIndex;

    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SOUND_BUTTONS);
        soundImgs = new BufferedImage[2][3]; //load images for sound btns
        for (int j = 0; j < soundImgs.length; j++)
            for (int i = 0; i < soundImgs[j].length; i++)
                soundImgs[j][i] = temp.getSubimage(i * SOUND_SIZE_DEFAULT, j * SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT);
    }

    public void update() {
        if (muted)   rIndex = 1;
        else   rIndex = 0;
        cIndex = 0;
        if (mouseOver)  cIndex = 1;
        if (mousePressed)  cIndex = 2;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public void draw(Graphics g) {
        g.drawImage(soundImgs[rIndex][cIndex], x, y, width, height, null);
    }


    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMuted() {return muted;}

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

}
