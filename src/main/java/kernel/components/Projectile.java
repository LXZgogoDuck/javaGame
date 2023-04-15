package kernel.components;

import java.awt.geom.Rectangle2D;

import kernel.main.Game;

import static kernel.utilz.Constants.Projectiles.*;

public class Projectile {
	private Rectangle2D.Float hitbox;
	private int dir;
	private boolean active = true;

	public Projectile(int x, int y, int dir) {
		int xOff = (int) (-3 * Game.SCALE);
		int yOff = (int) (5 * Game.SCALE);
		if (dir == 1)
			xOff = (int) (29 * Game.SCALE);
		hitbox = new Rectangle2D.Float(x + xOff, y + yOff, CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT);
		this.dir = dir;
	}

	public void updatePos() {
		hitbox.x += dir * SPEED;
	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

}
