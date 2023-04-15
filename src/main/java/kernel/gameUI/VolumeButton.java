package kernel.gameUI;

import kernel.utilz.LoadSave;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

import static kernel.utilz.Constants.UI.VolumeButtons.*;


public class VolumeButton extends PauseButton {

    private BufferedImage[] imgs;
    private BufferedImage slider;
    private int index = 0;
    private boolean mouseOver, mousePressed;
    private int buttonX, minX, maxX;
    private float floatValue = 0f;

    public VolumeButton(int x, int y, int width, int height) {
        super(x + width / 2, y, VOLUME_WIDTH, height);
        bounds.x -= VOLUME_WIDTH / 2;
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;
        minX = x + VOLUME_WIDTH / 2;
        maxX = x + width - VOLUME_WIDTH / 2;
       //load images for sound buttons
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);

        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);

    }


    public void update() {
        index = 0;
        if (mouseOver) index = 1;
        if (mousePressed) index = 2;
    }

    public void draw(Graphics graphics) {
        graphics.drawImage(slider, x, y, width, height, null);
        graphics.drawImage(imgs[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);
    }

    public void changeX(int x) {
        if (x < minX)  buttonX = minX;
        else if (x > maxX) buttonX = maxX;
        else buttonX = x;
        float ran = maxX - minX; //update float value
        float val = buttonX - minX;
        floatValue = val/ ran;
        bounds.x = buttonX - VOLUME_WIDTH / 2;
    }

    public void resetBooleans() {
        mouseOver = false;
        mousePressed = false;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public void setMousePressed(boolean b) {
    }

    public boolean isMousePressed() { return mousePressed;
    }

    public void setMouseOver(boolean b) {
    }
}
