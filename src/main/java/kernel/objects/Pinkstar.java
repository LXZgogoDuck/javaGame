package kernel.objects;

import kernel.state.Playing;
import kernel.state.cheatPlay;

import static kernel.utilz.Constants.Directions.LEFT;
import static kernel.utilz.Constants.Directions.RIGHT;
import static kernel.utilz.Constants.EnemyConstants.*;
import static kernel.utilz.HelpMethods.CanMoveHere;
import static kernel.utilz.HelpMethods.IsFloor;

public class Pinkstar extends Enemy {
	private boolean preRoll = true;
	private int tickSinceLastDmgToPlayer;
	private int tickAfterRollInIdle;
	private int rollDurationTick, rollDuration = 300;

	public Pinkstar(float x, float y) {
		super(x, y, PINKSTAR_WIDTH, PINKSTAR_HEIGHT, PINKSTAR);
		initHitbox(17, 21);
	}

	public void update(int[][] lvlData, Playing playing) {
		updateMotion(lvlData, playing);
		updateAnimationTick();
	}
	public void update(int[][] lvlData, cheatPlay cheatPlay) {
		updateMotion(lvlData, cheatPlay);
		updateAnimationTick();
	}
	private void updateMotion(int[][] lvlData, cheatPlay cheatPlay) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);
		if (inAir)
			inAirChecks(lvlData, cheatPlay);
		else {
			switch (state) {
				case IDLE:
					preRoll = true;
					if (tickAfterRollInIdle >= 120) {
						if (IsFloor(hitbox, lvlData))
							newState(RUNNING);
						else
							inAir = true;
						tickAfterRollInIdle = 0;
						tickSinceLastDmgToPlayer = 60;
					} else
						tickAfterRollInIdle++;
					break;
				case RUNNING:
					if (canSeePlayer(lvlData, cheatPlay.getPlayer())) {
						newState(ATTACK);
						setWalkDir(cheatPlay.getPlayer());
					}
					move(lvlData, cheatPlay);
					break;
				case ATTACK:
					if (preRoll) {
						if (aniIndex >= 3)
							preRoll = false;
					} else {
						move(lvlData, cheatPlay);
						checkDmgToPlayer(cheatPlay.getPlayer());
						checkRollOver(cheatPlay);
					}
					break;
				case HIT:
					if (aniIndex <= GetSpriteAmount(enemyType, state) - 2)
						pushBack(pushBackDir, lvlData, 2f);
					updatePushBackDrawOffset();
					tickAfterRollInIdle = 120;

					break;
			}
		}
	}

	private void updateMotion(int[][] lvlData, Playing playing) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);
		if (inAir)
			inAirChecks(lvlData, playing);
		else {
			switch (state) {
				case IDLE:
					preRoll = true;
					if (tickAfterRollInIdle >= 120) {
						if (IsFloor(hitbox, lvlData))
							newState(RUNNING);
						else
							inAir = true;
						tickAfterRollInIdle = 0;
						tickSinceLastDmgToPlayer = 60;
					} else
						tickAfterRollInIdle++;
					break;
				case RUNNING:
					if (canSeePlayer(lvlData, playing.getPlayer())) {
						newState(ATTACK);
						setWalkDir(playing.getPlayer());
					}
					move(lvlData, playing);
					break;
				case ATTACK:
					if (preRoll) {
						if (aniIndex >= 3)
							preRoll = false;
					} else {
						move(lvlData, playing);
						checkDmgToPlayer(playing.getPlayer());
						checkRollOver(playing);
					}
					break;
				case HIT:
					if (aniIndex <= GetSpriteAmount(enemyType, state) - 2)
						pushBack(pushBackDir, lvlData, 2f);
					updatePushBackDrawOffset();
					tickAfterRollInIdle = 120;

					break;
			}
		}
	}

	private void checkDmgToPlayer(Player player) {
		if (hitbox.intersects(player.getHitbox()))
			if (tickSinceLastDmgToPlayer >= 60) {
				tickSinceLastDmgToPlayer = 0;
				player.changeHealth(-GetEnemyDmg(enemyType), this);
			} else
				tickSinceLastDmgToPlayer++;
	}

	private void setWalkDir(Player player) {
		if (player.getHitbox().x > hitbox.x)
			walkDir = RIGHT;
		else
			walkDir = LEFT;

	}

	protected void move(int[][] lvlData, Playing playing) {
		float xSpeed = 0;

		if (walkDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (state == ATTACK)
			xSpeed *= 2;

		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			if (IsFloor(hitbox, xSpeed, lvlData)) {
				hitbox.x += xSpeed;
				return;
			}

		if (state == ATTACK) {
			rollOver(playing);  rollDurationTick = 0;
		}
		changeWalkDir();
	}
	protected void move(int[][] lvlData, cheatPlay cheatPlay) {
		float xSpeed = 0;

		if (walkDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (state == ATTACK)
			xSpeed *= 2;

		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			if (IsFloor(hitbox, xSpeed, lvlData)) {
				hitbox.x += xSpeed;
				return;
			}

		if (state == ATTACK) {
			rollOver(cheatPlay);  rollDurationTick = 0;
		}
		changeWalkDir();
	}


	private void checkRollOver(Playing playing) {
		rollDurationTick++;
		if (rollDurationTick >= rollDuration) {
			rollOver(playing);
			rollDurationTick = 0;
		}
	}
	private void checkRollOver(cheatPlay cheatPlay) {
		rollDurationTick++;
		if (rollDurationTick >= rollDuration) {
			rollOver(cheatPlay);
			rollDurationTick = 0;
		}
	}


	private void rollOver(Playing playing) {
		newState(IDLE);
	}
	private void rollOver(cheatPlay cheatPlay) {
		newState(IDLE);
	}


}
