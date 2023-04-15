package kernel.components;
import kernel.main.Game;
public class Potion extends GameObject {
    private float Offset;
    private int maxOffset, hoverDir = 1;

    public Potion(int x, int y, int objType) {
        super(x, y, objType);
        doAnimation = true;
        initHitbox(7, 14);
        xDrawOffset = (int) (3 * Game.SCALE);
        yDrawOffset = (int) (2 * Game.SCALE);
        maxOffset = (int) (10 * Game.SCALE);
    }

    public void update() {
        updateAnimationTick();
       Offset += (0.075f * Game.SCALE * hoverDir);
        if (Offset >= maxOffset)
            hoverDir = -1;
        else if (Offset < 0)
            hoverDir = 1;
        hitbox.y = y + Offset;
    }

}
