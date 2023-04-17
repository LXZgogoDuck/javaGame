package kernel.gameUI;

import kernel.state.Gamestate;
import kernel.state.Menu;
import kernel.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static kernel.utilz.Constants.UI.Buttons.*;

public class MenuButton {
    private int xPos, yPos, rowIndex, index;
    private int xOffsetCenter = B_WIDTH / 2;
    private Gamestate state;
    private BufferedImage[] imgs;
    private boolean mouseOver, mousePressed;
    private Rectangle boundary;

    public MenuButton(int xPos, int yPos, int rowIndex, Gamestate state) {
        this.xPos = xPos;   this.yPos = yPos;//set positions
        this.rowIndex = rowIndex;    this.state = state;
        //load images
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.btns);
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * 140, rowIndex * 56, 140, 56);
        //initiate Bounds;
        boundary = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
    }

    public void draw(Graphics g) {
        g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);
    }

    public void update() {
        index = 0;
        if (mouseOver)  index = 1;
        if (mousePressed)  index = 2;
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

    public Rectangle getBounds() {
        return boundary;
    }

    public void applyGamestate() {
        Gamestate.gamestate = state;
    }

    public void resetBools() {
        mouseOver = false;   mousePressed = false;
    }
    public Gamestate getState() {
        return state;
    }
}
