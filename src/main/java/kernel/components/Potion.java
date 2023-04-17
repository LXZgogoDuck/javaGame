package kernel.components;
import kernel.main.Game;
public class Potion extends GameObject {
    private float a;
    private int max, dirction = 1;

    public Potion(int x, int y, int objType) {
        super(x, y, objType);
        doAnimation = true;
        initHitbox(7, 14);
        xDrawOffset = (int) (3 * Game.SCALE);
        yDrawOffset = (int) (2 * Game.SCALE);
        max = (int) (10 * Game.SCALE);
    }

    public void update() {
        updateAnimationTick();
        a += (0.075f * Game.SCALE * dirction);
        if (a >= max)   dirction = -1;
        else if (a < 0)   dirction = 1;
        hitbox.y = y + a;
    }

}
