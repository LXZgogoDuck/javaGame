package kernel.components;

import java.awt.geom.Rectangle2D;

import kernel.main.Game;

import static kernel.utilz.Constants.Projectiles.*;

public class Projectile {
	private Rectangle2D.Float hitbox;
	private int dir;
	private boolean active = true;

	public Projectile(int x, int y, int dir) {
		int xSet = (int) (-3 * Game.SCALE);
		int yOff = (int) (5 * Game.SCALE);
		if (dir == 1)
			xSet = (int) (29 * Game.SCALE);
		hitbox = new Rectangle2D.Float(x + xSet, y + yOff, ball_width, ball_height);
		this.dir = dir;
	}

	public void updatePos() {
		hitbox.x += dir * speed;
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
