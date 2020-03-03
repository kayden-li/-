package cn.tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * 天空
 * 
 * @author 刘浩
 *
 */
public class Sky extends FlyingObject {

	private static BufferedImage image;

	// 静态代码块
	static {
		image = loadImage("background.png");
	}
	// 成员变量
	private int speed;
	private int y1;// 第二张图片的y的坐标

	public Sky() {
		super(ShootGame.SVREEN_WIDTH, ShootGame.SVREEN_HEIGHT, 0, 0);
		speed = 3;
		y1 = -ShootGame.SVREEN_HEIGHT;

	}

	@Override
	public void step() {
		y += speed;
		y1 += speed;
		if (y >= ShootGame.SVREEN_HEIGHT) {
			y = -ShootGame.SVREEN_HEIGHT;
		}
		if (y1 >= ShootGame.SVREEN_HEIGHT) {
			y1 = -ShootGame.SVREEN_HEIGHT;
		}
	}

	@Override
	public BufferedImage getImage() {

		return image;
	}

	/** 重写父类中的paintObject()方法 */
	public void paintObject(Graphics g) {
		g.drawImage(this.getImage(), this.x, this.y, null);
		// TODO...(标记自己没写的地方)
		g.drawImage(getImage(), x, y1, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.tedu.shoot.FlyingObject#outOfBounts()
	 */
	@Override
	public boolean outOfBounds() {
		return false;
	}

}
