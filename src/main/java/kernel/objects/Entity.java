package kernel.objects;

import kernel.main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static kernel.utilz.Constants.Directions.*;
import static kernel.utilz.HelpMethods.CanMoveHere;
//
public abstract class Entity {

    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;
    protected int aniTick, aniIndex;
    protected int state;
    protected float airSpeed;
    protected boolean inAir = false; //check whether jumping or not
    protected float maxBlood;
    protected float currentBlood;
    protected Rectangle2D.Float attackBox;
    protected float walkSpeed;

    protected int pushBackDir;
    protected float pushDrawOffset;
    protected int pushBackOffsetDir = UP;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void updatePushBackDrawOffset() {
        float speed = 0.95f;
        float limit = -30f;

        if (pushBackOffsetDir == UP) {
            pushDrawOffset -= speed;
            if (pushDrawOffset <= limit)
                pushBackOffsetDir = DOWN;
        } else {
            pushDrawOffset += speed;
            if (pushDrawOffset >= 0)
                pushDrawOffset = 0;
        }
    }
    protected void pushBack(int pushBackDir, int[][] lvlData, float speedMulti) {
        float xSpeed = 0;
        if (pushBackDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if (CanMoveHere(hitbox.x + xSpeed * speedMulti, hitbox.y, hitbox.width, hitbox.height, lvlData))
            hitbox.x += xSpeed * speedMulti;
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public int getState() {
        return state;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    protected void newState(int state) {
        this.state = state;
        aniTick = 0;   aniIndex = 0;
    }
}