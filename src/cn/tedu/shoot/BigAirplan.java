package cn.tedu.shoot;

import java.awt.image.BufferedImage;

/**
 * 大敌机
 * 
 * @author 刘浩
 *
 */
public class BigAirplan extends FlyingObject implements Enemy {

    private static BufferedImage[] images;

    // 静态代码块
    static {
        images = new BufferedImage[3];
        for (int i = 0; i < images.length; i++) {
            images[i] = loadImage("bigplane" + i + ".png");
        }
    }

    // 成员变量
    int speed;// 速度

    // 构造方法
    public BigAirplan() {
        super(100, 100);
        speed = 3;
    }

    // 走起来
    public void step() {
        y += speed;
    }

    int index = 1;

    /** 获取图片 */
    @Override
    public BufferedImage getImage() {
        if (isLife()) {
            return images[0];
        } else if (isDead()) {
            BufferedImage img = images[index++];
            if (index == images.length) {
                state = REMOVE;
            }
            return img;

            /*
             * 0ms: index = 1; 10ms: images[1]; index = 2; 返回实际是image[1] 20ms: images[2];
             * index = 3; 30ms 40ms: images[4]; index = 5,(Remove)返回实际是image[4] 50ms: return
             * null;
             * 
             */
        }
        return null;
    }

    public Bullet[] shoot() {
        Bullet[] bullets = new Bullet[1];
        bullets[0] = new Bullet(this.x + 25, this.y + 70, false, false);
        bullets[0].setSpeed(speed + 2);
        return bullets;
    }

    @Override
    public boolean outOfBounds() {
        return this.y >= ShootGame.SVREEN_HEIGHT;
    }

    @Override
    public int getScore() {
        return 3;
    }
}
