package kernel.gameUI;

import kernel.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static kernel.utilz.Constants.UI.URMButtons.URM_DEFAULT_SIZE;
import static kernel.utilz.Constants.UI.URMButtons.URM_SIZE;

public class UrmButton {
    private BufferedImage[] imgs;
    private int rowIndex, index;
    private boolean mouseOver, mousePressed;
    private int x, y, w, h;
    private Rectangle bounds;

    public UrmButton(int x, int y, int width, int height, int rowIndex) {
        this.x = x;   this.y = y;
        this.w = width;  this.h = height;
        this.rowIndex = rowIndex;
        createBounds();
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.urm);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * URM_DEFAULT_SIZE, rowIndex * URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE);

    }
    private void createBounds() {
        bounds = new Rectangle(x, y, w, h);
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getWidth() {
        return w;
    }

    public void setWidth(int width) {
        this.w = width;
    }
    public int getHeight() {
        return h;
    }

    public void setHeight(int height) {
        this.h = height;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void update() {
        index = 0;
        if (mouseOver)  index = 1;
        if (mousePressed)  index = 2;
    }

    public void draw(Graphics g) {
        g.drawImage(imgs[index], x, y, URM_SIZE, URM_SIZE, null);
    }

    public void reBooleans() {
        mouseOver = false;   mousePressed = false;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

}

