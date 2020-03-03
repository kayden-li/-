package cn.tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * 
 * @author 刘浩
 *
 */
public class Bullet extends FlyingObject {
    private static BufferedImage image;
    private static BufferedImage imageEnemy;
    private static BufferedImage imageBoss;
    // 静态代码块
    static {
        image = loadImage("bullet.png");
        imageEnemy = loadImage("apple.png");
        imageBoss = loadImage("BossBullet.png");
    };

    // 成员变量
    private int speed;// 速度
    private boolean type = true;
    private boolean isBoss = false;

    // 构造方法
    public Bullet(int x, int y, boolean b, boolean c) {
        super(10, 20, x, y);
        this.type = b;
        this.isBoss = c;
        this.speed = -3;
        if (c) {
            this.width = imageBoss.getWidth();
            this.height = imageBoss.getHeight();
        } else {
            if (type) {
                this.width = image.getWidth();
                this.height = image.getHeight();
            } else {
                this.width = imageEnemy.getWidth();
                this.height = imageEnemy.getHeight();
            }
        }
    }

    // 走起来
    public void step() {
        // y -= speed;
        if (isLife()) {
            y += speed;
        }
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public BufferedImage getImage() {
        if (isLife())
            if (isBoss) {
                return imageBoss;
            } else {
                if (type)
                    return image;
                else {
                    return imageEnemy;
                }
            }
        else
            return null;
    }

    /** 画对象的方法 */
    public void paintObject(Graphics g) {
        g.drawImage(this.getImage(), this.x, this.y, null);
    }

    @Override
    public boolean outOfBounds() {
        return this.y <= -this.height || this.y >= ShootGame.SVREEN_HEIGHT;
    }

}
