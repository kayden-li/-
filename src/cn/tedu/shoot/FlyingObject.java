package cn.tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * 父类 敌人：小敌机+大敌机+小蜜蜂 英雄机+子弹+天空
 * 
 * @author 刘浩
 *
 */
public abstract class FlyingObject {

	// 存活的状态
	public static final int LIFE = 0;
	// 死了
	public static final int DEAD = 1;
	// 刪除
	public static final int REMOVE = 2;
	// 默认的状态
	public int state = LIFE;

	// 成员变量
	int width;// 图片宽
	int height;// 高
	int x;// x坐标
	int y;// y坐标

	/** 给敌人设置一个构造方法 */
	public FlyingObject(int width, int height) {

		this.width = width;
		this.height = height;
		Random rand = new Random();
		x = rand.nextInt(400 - this.width);
		y = -this.height;
	}

	/** 给英雄机和子弹/.天空提供 */
	public FlyingObject(int width, int height, int x, int y) {

		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}

	/** 移动的父类方法 */
	public void step() {

	}

	// 读取照片
	public static BufferedImage loadImage(String fileName) {

		try {
			// 同包之内读取图片
			BufferedImage images = ImageIO.read(FlyingObject.class.getResource(fileName));
			return images;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	// 获取图片的方法
	public abstract BufferedImage getImage();

	/** 判断状态 */
	public boolean isLife() {
		return state == LIFE;
	}

	public boolean isDead() {
		return state == DEAD;
	}

	public boolean isRemove() {
		return state == REMOVE;
	}

	/** 画对象的方法 */
	public void paintObject(Graphics g) {
		g.drawImage(this.getImage(), this.x, this.y, null);
	}

	/** 检测越界的方法 */
	public abstract boolean outOfBounds();

	/** 是否碰撞算法-------this:敌人 other:子弹或者英雄机 */
	public boolean hit(FlyingObject other) {
		int x = other.x;// 子弹x坐标
		int y = other.y;// 子弹y坐标
		int x1 = this.x - other.width;
		int x2 = this.x + this.width;
		int y1 = this.y - other.height;
		int y2 = this.y + this.height;

		return x >= x1 && x <= x2 && y >= y1 && y <= y2 && other.isLife() && this.isLife();
	}

	/***/
	public void goDead() {
		state = DEAD;
	}

}
