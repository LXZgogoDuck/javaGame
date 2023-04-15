package kernel.objects;


import kernel.state.Playing;
import kernel.state.cheatPlay;

import static kernel.utilz.Constants.Directions.LEFT;
import static kernel.utilz.Constants.EnemyConstants.*;
import static kernel.utilz.HelpMethods.CanMoveHere;
import static kernel.utilz.HelpMethods.IsFloor;

public class Shark extends Enemy {

	public Shark(float x, float y) {
		super(x, y, SHARK_WIDTH, SHARK_HEIGHT, SHARK);
		initHitbox(18, 22);
		initAttackBox(20, 20, 20);
	}

	public void update(int[][] lvlData, Playing playing) {
		updateMotion(lvlData, playing);
		updateAnimationTick();
		updateAttackBoxFlip();
	}
	public void update(int[][] lvlData, cheatPlay cheatPlay) {
		updateMotion(lvlData, cheatPlay);
		updateAnimationTick();
		updateAttackBoxFlip();
	}

	private void updateMotion(int[][] lvlData, Playing playing) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir)
			inAirChecks(lvlData, playing);
		else {
			switch (state) {
				case IDLE:
					if (IsFloor(hitbox, lvlData))
						newState(RUNNING);
					else
						inAir = true;
					break;
				case RUNNING:
					if (canSeePlayer(lvlData, playing.getPlayer())) {
						turnTowardsPlayer(playing.getPlayer());
						if (isPlayerCloseForAttack(playing.getPlayer()))
							newState(ATTACK);
					}

					move(lvlData);
					break;
				case ATTACK:
					if (aniIndex == 0)  attackChecked = false;
					else if (aniIndex == 3) {
						if (!attackChecked)
							checkPlayerHit(attackBox, playing.getPlayer());
						attackMove(lvlData, playing);
					}

					break;
				case HIT:
					if (aniIndex <= GetSpriteAmount(enemyType, state) - 2)
						pushBack(pushBackDir, lvlData, 2f);
					updatePushBackDrawOffset();
					break;
			}
		}
	}
	private void updateMotion(int[][] lvlData, cheatPlay cheatPlay) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir)
			inAirChecks(lvlData, cheatPlay);
		else {
			switch (state) {
				case IDLE:
					if (IsFloor(hitbox, lvlData))
						newState(RUNNING);
					else
						inAir = true;
					break;
				case RUNNING:
					if (canSeePlayer(lvlData, cheatPlay.getPlayer())) {
						turnTowardsPlayer(cheatPlay.getPlayer());
						if (isPlayerCloseForAttack(cheatPlay.getPlayer()))
							newState(ATTACK);
					}

					move(lvlData);
					break;
				case ATTACK:
					if (aniIndex == 0)  attackChecked = false;
					else if (aniIndex == 3) {
						if (!attackChecked)
							checkPlayerHit(attackBox, cheatPlay.getPlayer());
						attackMove(lvlData, cheatPlay);
					}

					break;
				case HIT:
					if (aniIndex <= GetSpriteAmount(enemyType, state) - 2)
						pushBack(pushBackDir, lvlData, 2f);
					updatePushBackDrawOffset();
					break;
			}
		}
	}


	protected void attackMove(int[][] lvlData, Playing playing) {
		float xSpeed = 0;
		if (walkDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;
		if (CanMoveHere(hitbox.x + xSpeed * 3, hitbox.y, hitbox.width, hitbox.height, lvlData))
			if (IsFloor(hitbox, xSpeed * 3, lvlData)) {
				hitbox.x += xSpeed * 3;
				return;
			}
		newState(IDLE);
	}
	protected void attackMove(int[][] lvlData, cheatPlay cheatPlay) {
		float xSpeed = 0;
		if (walkDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;
		if (CanMoveHere(hitbox.x + xSpeed * 3, hitbox.y, hitbox.width, hitbox.height, lvlData))
			if (IsFloor(hitbox, xSpeed * 3, lvlData)) {
				hitbox.x += xSpeed * 3;
				return;
			}
		newState(IDLE);
	}
}
