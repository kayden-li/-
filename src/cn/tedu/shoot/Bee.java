package cn.tedu.shoot;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 英雄机
 * 
 * @author 刘浩
 *
 */
public class Bee extends FlyingObject implements Award {

	private static BufferedImage[] images;

	// 静态代码块
	static {
		images = new BufferedImage[2];
		for (int i = 0; i < images.length; i++) {
			images[i] = loadImage("bee" + i + ".png");
		}
	}

	// 成员变量
	private int xSpeed;
	private int ySpeed;
	private int awardType;// 0:火力 1:生命

	private int isRotate = 0;
	private boolean isLeft = true;
	private boolean isFirstR = true;

	public Bee() {
		super(images[0].getWidth(), images[0].getHeight());
		xSpeed = 1;
		ySpeed = 2;
		Random random = new Random();
		awardType = random.nextInt(2);// 0-1
	}

	public void step() {
		// y += ySpeed;
		if (x >= ShootGame.SVREEN_WIDTH - this.width || x <= 0) {
			xSpeed = -xSpeed;
		}
		x += xSpeed;
		if (isLife()) {
			y += ySpeed;
		}

		else if (isDead()) {
			y += -5;
			if (!this.isLeft)// 如果是图片向右抛出的
				x += 3;
			else
				x -= 3;
		}
	}

	int index = 1;

	@Override
	public BufferedImage getImage() {
		if (isLife()) {
			return images[0];
		} else if (isDead()) {
			// BufferedImage img = images[index++];
			// if (index == images.length) {
			// state = REMOVE;
			// }
			// return img;

			if (this.isFirstR) {// 判断是否为第一次请求旋转
				this.isFirstR = false;
				Random random = new Random();
				this.isLeft = random.nextBoolean();// 给定一个随机方向
			}
			// state = REMOVE;
			BufferedImage imag = images[0];
			double rotationRequired = Math.toRadians(isRotate);// 转换为角度
			double locationX = imag.getWidth() / 2;
			double locationY = imag.getHeight() / 2;// 图片的旋转中心
			AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);// 设置旋转矩阵
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);// 两个变换类，不知道为什么要这样做
			BufferedImage newImage = new BufferedImage(imag.getWidth(), imag.getHeight(), imag.getType());// 以原来格式设置新图片
			op.filter(imag, newImage);// 为新图片以旋转形式填充内容

			isRotate += 5;// 旋转速度
			return newImage;// 返回旋转后的图片
		}
		return null;

	}

	@Override
	public boolean outOfBounds() {
		return this.y >= ShootGame.SVREEN_HEIGHT;
	}

	@Override
	public int getAwardType() {
		return awardType;
	}

}
