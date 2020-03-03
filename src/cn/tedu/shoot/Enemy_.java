package cn.tedu.shoot;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 小敌机
 * 
 * @author 刘浩
 *
 */
public class Enemy_ extends FlyingObject implements Enemy {

	private static BufferedImage[] images;
	private int type = 0;

	// 静态代码块
	static {
		images = new BufferedImage[16];
		for (int i = 1; i <= images.length; i++) {
			images[i - 1] = loadImage("enemy" + i + ".png");
		}
	}

	// 成员变量
	int speed;// 速度

	// 构造方法
	public Enemy_() {
		super(150, 100);
		speed = 4;
		type = new Random().nextInt(images.length);
		this.width = images[type].getWidth();
		this.height = images[type].getHeight();
	}

	// 走起来
	public void step() {
		y += speed;
	}

	int index = 1;

	// 获取图片
	public BufferedImage getImage() {
		if (isLife()) {
			return images[type];
		} else if (isDead()) {
			if (index == images.length) {
				state = REMOVE;
			}
			return null;
		}
		return null;
	}

	// 小敌机越界的判断
	public boolean outOfBounds() {
		return this.y >= ShootGame.SVREEN_HEIGHT;
	}

	// 得分
	public int getScore() {
		return 1;
	}

	public Bullet[] shoot() {

		Bullet[] bullets = new Bullet[1];
		bullets[0] = new Bullet(this.x + 18, this.y + 40, false, false);
		bullets[0].setSpeed(this.speed + 2);
		return bullets;
	}

}
