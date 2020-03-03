package cn.tedu.shoot;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Boss extends FlyingObject {

	private static BufferedImage[] images;
	public static int X_MAXSPEED;
	public static int Y_MAXSPEED;

	private int life = 500;

	// 静态代码块
	static {
		images = new BufferedImage[1];
		images[0] = loadImage("Boss.png");
		X_MAXSPEED = 3;
		Y_MAXSPEED = 5;
	}

	// 成员变量
	private int xSpeed;
	private int ySpeed;

	public Boss() {
		super(60, 60);
		xSpeed = 1;
		ySpeed = 2;
	}

	public void step() {
		if (isLife()) {
			if (y <= 50) {
				ySpeed = Math.abs(ySpeed);
				y += ySpeed;
			} else if (x >= ShootGame.SVREEN_WIDTH - this.width - this.width - 50 || x <= 0
					|| y >= ShootGame.SVREEN_HEIGHT - (ShootGame.SVREEN_HEIGHT >> 1) || y <= 0) {
				Random random = new Random();
				ySpeed = Math.abs(random.nextInt(Y_MAXSPEED)) + 1;
				xSpeed = Math.abs(random.nextInt(X_MAXSPEED)) + 1;
				if (x >= ShootGame.SVREEN_WIDTH - this.width - this.width - 50) {
					xSpeed = -xSpeed;
				}
				if (y >= ShootGame.SVREEN_HEIGHT - (ShootGame.SVREEN_HEIGHT >> 1)) {
					ySpeed = -ySpeed;
				}
				x += xSpeed;
				y += ySpeed;
			} else {
				x += xSpeed;
				y += ySpeed;
			}
		}
	}

	public Bullet[] shoot() {

		Bullet[] bullets = new Bullet[1];
		bullets[0] = new Bullet(this.x + 68, this.y + (this.height << 1), false, true);
		bullets[0].setSpeed(5);
		return bullets;
	}

	/** 减少生命 */
	public void subtractLife() {
		life--;
	}

	/** 获取Boss的生命之值 */
	public int getLife() {
		return life;
	}

	int index = 1;

	@Override
	public BufferedImage getImage() {
		if (isLife()) {
			return images[0];
		} else if (isDead()) {

		}
		return null;

	}

	@Override
	public boolean outOfBounds() {
		return false;
	}

	public void updateSpeed(boolean f) {
		if (f)
			X_MAXSPEED += 2;
		else
			Y_MAXSPEED += 3;
	}

	public void setLife(int i) {
		this.life = i;
	}
}
