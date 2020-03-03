package cn.tedu.shoot;

import java.awt.image.BufferedImage;

/**
 * 英雄机
 * 
 * @author 刘浩
 *
 */
public class Hero extends FlyingObject {

    private static BufferedImage[] images;
    // 静态代码块
    static {
        images = new BufferedImage[2];
        images[0] = loadImage("hero0.png");
        images[1] = loadImage("hero1.png");
    }

    // 成员变量
    private int life;// 生命值
    private int doubleFire;// 火力

    // 构造方法
    public Hero() {
        super(100, 200, 200, 400);
        this.life = 30;
        this.doubleFire = 0;// 默认是单倍火力
    }

    int index = 0;

    @Override
    // 实现父类中的获取图片方法
    public BufferedImage getImage() {

        if (isLife()) {
            if (index++ <= 30) {
                return images[0];
            } else if (index++ <= 60) {
                return images[1];
            } else
                index = 0;
        }
        /*
         * 0ms: index = 0; 10ms: images[0]; index = 1; 20ms: images[1]; index = 2; 30ms:
         * images[0]; index = 3; ....
         * 
         */
        return null;
    }

    /** 英雄机发射 子弹 */
    public Bullet[] shoot() {
        // 英雄机的宽分为4等分
        int xStep = this.width / 4;
        int yStep = 20;
        // 分情况讨论
        if (doubleFire > 0) { // 双倍火力
            Bullet[] bs = new Bullet[2];
            bs[0] = new Bullet(this.x - 2 * xStep, this.y - yStep, true, false);
            bs[1] = new Bullet(this.x + 4 * xStep, this.y - yStep, true, false);
            doubleFire -= 2;
            return bs;
        } else { // 单倍火力
            Bullet[] bs = new Bullet[1];
            bs[0] = new Bullet(this.x + 2 * xStep, this.y - yStep, true, false);
            return bs;
        }
    }

    /** 英雄机随鼠标移动 */
    public void moveTo(int x, int y) {
        this.x = x - this.width / 2;
        this.y = y - this.height / 2;

    }

    @Override
    public boolean outOfBounds() {
        return false;
    }

    /** 加生命 */
    public void addLife() {
        life++;
    }

    /** 加火力 */
    public void addDoubleFire() {
        doubleFire += 20;
    }

    /** 减少生命 */
    public void subtractLife() {
        life--;
    }

    /** 清空火力 */
    public void clearDoubleFire() {
        doubleFire = 0;
    }

    /** 获取英雄机的生命之值 */
    public int getLife() {
        return life;
    }

}
