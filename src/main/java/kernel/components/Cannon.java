package kernel.components;

import kernel.main.Game;

public class Cannon extends GameObject {

	private int tY;

	public Cannon(int x, int y, int objType) {
		super(x, y, objType);
		tY = y / Game.TILES_SIZE;
		initHitbox(40, 26);
		hitbox.y += (int) (6 * Game.SCALE);
	}

	public void update() {
		if (doAnimation)
			updateAnimationTick();
	}

	public int getTileY() {
		return tY;
	}

}
