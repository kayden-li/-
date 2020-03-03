package cn.tedu.shoot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 主函数的类
 * 
 * @author 刘浩
 *
 */
public class ShootGame extends JPanel {

	public static final int SVREEN_WIDTH = 420;
	public static final int SVREEN_HEIGHT = 700;
	// 开始状态
	public static final int START = 0;
	// 运行状态
	public static final int RUNNING = 1;
	// 暂停状态
	public static final int PAUSE = 2;
	// 结束状态
	public static final int GAME_OVER = 3;
	private int state = START;

	private int BossNum = 0;

	private static BufferedImage start;
	private static BufferedImage pause;
	private static BufferedImage gameover;

	private Sky sk = new Sky();
	private Hero hero = new Hero();
	private static Boss boss = new Boss();

	static {
		start = FlyingObject.loadImage("start.png");
		pause = FlyingObject.loadImage("pause.png");
		gameover = FlyingObject.loadImage("gameover.png");
		// boss初始为死亡状态
		boss.state = FlyingObject.DEAD;
	}
	// 1.先画一个生命值到界面上
	// 2.根据不同的状态画出来图片

	private FlyingObject[] flys = {};
	private Bullet[] bullets = {};// 子弹数组
	Bullet[] EnemyBullets = {};

	private int lastBossLife = boss.getLife();

	/** 生成敌人的对象 */
	public FlyingObject nextOne() {
		// 如果是boss状态不生成敌人
		if (boss.isLife())
			return null;
		// 随机数的使用
		Random rand = new Random();
		int type = rand.nextInt(30);
		if (type < 7) {
			return new Bee();// 产生的是小敌机
		} else if (type < 24) {
			return new Enemy_();
		} else if (type < 28) {
			return new BigAirplan();
		} else {
			return new Airplan();
		}
	}

	int enterIndex = 0;// 敌人入场个数

	/** 敌人（小敌机、大敌机、小蜜蜂）入场 */
	public void enterAction() {
		enterIndex++;
		if (enterIndex % 20 == 0) {
			// 不会的可以参考JAVA API 文档
			FlyingObject obj = nextOne();// 获取到产生的敌人
			if (obj != null) {
				flys = Arrays.copyOf(flys, flys.length + 1);// 扩大容量
				flys[flys.length - 1] = obj;// 将产生的obj对象添加到数组扩容后的最后一位
			}
		}
		// 到达关底，出现boos
		if (enterIndex >= 2000 && boss.isDead()) {
			BossNum++;
			enterIndex = 0;
			boss.x = 50;
			boss.y = -boss.height;
			boss.state = FlyingObject.LIFE;
			if (lastBossLife >= 100) {
				Random random = new Random();
				boss.updateSpeed(random.nextBoolean());
				boss.setLife(200);
			} else {
				boss.setLife(lastBossLife + lastBossLife);
			}
			lastBossLife = boss.getLife();
		}
	}

	int shootIndex1 = 0;
	int shootIndex2 = 0;
	int shootBossStep = 100;

	/** 子弹对象入场 */
	public void shootAction() {
		shootIndex1++;
		shootIndex2++;
		if (shootIndex1 >= 30) {
			shootIndex1 = 0;
			Bullet[] bs = hero.shoot();
			bullets = Arrays.copyOf(bullets, bullets.length + bs.length);// 扩容
			/*
			 * 数组的追加 src:源数组， srcPos：源数组中的起始位置 dest:目标数组， destPos:目标数组的起始位置
			 * length:要准备复制的数组元素的长度
			 */
			System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length);
		}

		if (boss.isLife())
			shootBossStep = 25;
		else
			shootBossStep = 120;
		// 敌人子弹
		if (shootIndex2 >= shootBossStep) {
			shootIndex2 = 0;

			// boss发射子弹
			if (boss.isLife()) {
				Bullet[] bulletEnemy = boss.shoot();
				EnemyBullets = Arrays.copyOf(EnemyBullets, EnemyBullets.length + bulletEnemy.length);
				System.arraycopy(bulletEnemy, 0, EnemyBullets, EnemyBullets.length - bulletEnemy.length,
						bulletEnemy.length);
			}
			for (int i = 0; i < flys.length; i++) {
				FlyingObject flyingObject = flys[i];
				if (flyingObject.isLife()) {
					if (flyingObject instanceof BigAirplan) {
						Bullet[] bulletEnemy = ((BigAirplan) flyingObject).shoot();
						EnemyBullets = Arrays.copyOf(EnemyBullets, EnemyBullets.length + bulletEnemy.length);
						System.arraycopy(bulletEnemy, 0, EnemyBullets, EnemyBullets.length - bulletEnemy.length,
								bulletEnemy.length);
					} else if (flyingObject instanceof Airplan) {
						Bullet[] bulletEnemy = ((Airplan) flyingObject).shoot();
						EnemyBullets = Arrays.copyOf(EnemyBullets, EnemyBullets.length + bulletEnemy.length);
						System.arraycopy(bulletEnemy, 0, EnemyBullets, EnemyBullets.length - bulletEnemy.length,
								bulletEnemy.length);
					} else if (flyingObject instanceof Enemy_) {
						Bullet[] bulletEnemy = ((Enemy_) flyingObject).shoot();
						EnemyBullets = Arrays.copyOf(EnemyBullets, EnemyBullets.length + bulletEnemy.length);
						System.arraycopy(bulletEnemy, 0, EnemyBullets, EnemyBullets.length - bulletEnemy.length,
								bulletEnemy.length);
					}
				}
			}
		}
	}

	/** 删除越界的飞行物 */
	public void outOfBoundsAction() {
		int index = 0;

		// 创建一个数组，用来存放不越界的飞行物
		FlyingObject[] flyLives = new FlyingObject[flys.length];
		// 1.遍历出所有的敌人
		for (int i = 0; i < flys.length; i++) {
			// 2.获取每一个敌人对象
			FlyingObject f = flys[i];
			// 3.判断每一个敌人对象是否越界
			if (!f.outOfBounds()) {
				// 如果不越界，将不越界的对象保存到提前设置好的数组中
				flyLives[index] = f;
				index++;
			}
		}
		// 4.将不越界的敌人数组复制到flys中，长度改变即可。
		flys = Arrays.copyOf(flyLives, index);
		index = 0;// 清零
		Bullet[] bulletLive = new Bullet[bullets.length];
		for (int i = 0; i < bullets.length; i++) {
			Bullet bs = bullets[i];
			if (!bs.outOfBounds() && !bs.isRemove()) {
				bulletLive[index] = bs;
				index++;
			}
		}

		index = 0;
		Bullet[] bulletLitesEnemy = new Bullet[EnemyBullets.length];
		for (int i = 0; i < EnemyBullets.length; i++) {
			Bullet bs = EnemyBullets[i];
			if (!bs.outOfBounds() && !bs.isRemove()) {
				bulletLitesEnemy[index++] = bs;

			}
		}
		EnemyBullets = Arrays.copyOf(bulletLitesEnemy, index);
	}

	int score = 0;

	/** 子弹与敌人发生碰撞 */
	public void bulletBangAction() {

		// 遍历子弹
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			// 攻击boss
			if (boss.hit(b)) {
				boss.subtractLife();
				b.goDead();
			}
			// 遍历敌人子弹
			for (int j = 0; j < EnemyBullets.length; ++j) {
				Bullet bullet = EnemyBullets[j];
				if (b.hit(bullet)) {
					b.goDead();
					bullet.goDead();
				}
			}
			// 遍历敌人
			for (int j = 0; j < flys.length; j++) {
				FlyingObject f = flys[j];
				// 判断是否碰撞
				if (f.hit(b)) {
					// System.out.println("发生碰撞啦！");
					// 子弹消失，敌人Over
					f.goDead();
					b.goDead();
					// 判断被撞上的对象类型
					if (f instanceof Enemy) { // 类型的判断//如果是Enemy,玩家得分
						Enemy e = (Enemy) f;
						score += e.getScore();
						// System.out.println("得分：" + score);
					}
					if (f instanceof Award) { // 如果是Award,英雄机得奖励
						Award a = (Award) f;
						int type = a.getAwardType();
						switch (type) {
						case Award.DOUBLE_FIRE:
							hero.addDoubleFire();
							break;
						case Award.LIFE:
							hero.addLife();
							break;
						}
					}
				}
			}
		}
	}

	/** 英雄机与敌人碰撞 */
	public void heroBandAction() {
		// 遍历敌人
		for (int i = 0; i < flys.length; i++) {
			FlyingObject f = flys[i];// 获取每一个敌人
			if (f.isLife() && hero.isLife() && f.hit(hero)) {
				f.goDead();// 敌人over
				hero.subtractLife();// 减少生命
				hero.clearDoubleFire();// 清火力
			}
		}

		for (int i = 0; i < EnemyBullets.length; i++) {
			Bullet bt = EnemyBullets[i];// 获取到每一个子弹
			if (bt.hit(hero)) {
				hero.subtractLife();
				bt.goDead();
			}
		}
		if (boss.isLife()) {
			if (boss.hit(hero)) {
				hero.subtractLife();
				boss.subtractLife();
			}
		}
	}

	/** 检测游戏是否结束 */
	public void checkGameOverAction() {
		if (boss.getLife() <= 0 && boss.isLife()) {
			boss.goDead();
			enterIndex = 0;
			score += lastBossLife * BossNum;
		}
		if (hero.getLife() <= 0) {
			state = GAME_OVER;
		}
	}

	// bgm
	@SuppressWarnings("deprecation")
	public static void playBGM() {

		File file = new File("src/cn/tedu/shoot/test.wav");
		Player player = null;
		try {
			player = Manager.createPlayer(file.toURL());
		} catch (NoPlayerException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		player.start();

	}

	/** 画各种对象 */
	@Override
	public void paint(Graphics g) {// g：画笔
		super.paint(g);
		sk.paintObject(g);// 画天空
		for (int i = 0; i < flys.length; i++) {// 画敌人
			flys[i].paintObject(g);
		}
		hero.paintObject(g);// 画英雄
		for (int i = 0; i < bullets.length; i++) {// 画子弹
			bullets[i].paintObject(g);
		}
		if (boss.isLife() && boss.getLife() > 0) {
			boss.paintObject(g);
			// 血条框
			g.draw3DRect(SVREEN_WIDTH >> 1, 10, 180, 20, false);
			// 血条
			if (boss.getLife() / 100 != 0) {
				// 下一管血
				g.setColor(Color.BLUE);
				g.fill3DRect(SVREEN_WIDTH >> 1, 10, 180, 20, false);
				// 当前血管
				g.setColor(Color.RED);
				g.fill3DRect(SVREEN_WIDTH >> 1, 10, (int) ((boss.getLife() - 100) * 1.8), 20, false);
			} else {
				g.setColor(Color.RED);
				g.fill3DRect(SVREEN_WIDTH >> 1, 10, (int) (boss.getLife() * 1.8), 20, false);
			}
			g.setColor(Color.BLACK);
		}
		for (int i = 0; i < EnemyBullets.length; ++i) {
			EnemyBullets[i].paintObject(g);
		}
		g.drawString("得分:" + score, 10, 20);
		g.drawString("生命值:" + hero.getLife(), 10, 40);

		switch (state) {
		case START:
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE:
			g.drawImage(pause, 0, 0, null);
			break;
		case GAME_OVER:
			g.drawImage(gameover, 0, 0, null);
		}
	}

	/** 飞行物的移动 */
	public void stepAction() {
		sk.step();
		for (int i = 0; i < flys.length; i++) {
			flys[i].step();
		}
		for (int i = 0; i < bullets.length; i++) {
			bullets[i].step();
		}
		for (int i = 0; i < EnemyBullets.length; ++i) {

			EnemyBullets[i].step();
		}
		if (boss.isLife()) {
			boss.step();
		}
	}

	/** 启动程序 */
	public void action() {

		// 创建鼠标监听事件
		MouseAdapter ma = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				// 获取鼠标的x，y
				int x = e.getX();
				int y = e.getY();
				hero.moveTo(x, y);
			}

			public void mouseClicked(MouseEvent e) {
				// 由运行修改为暂停状态
				switch (state) {
				case START:
					state = RUNNING;
					break;
				case GAME_OVER:
					score = 0;
					sk = new Sky();
					flys = new FlyingObject[0];
					bullets = new Bullet[0];
					EnemyBullets = new Bullet[0];
					hero = new Hero();
					state = START;
					break;
				}
			}

			public void mouseEntered(MouseEvent e) {
				// 根据开始状态和结束状态进行处理
				if (state == PAUSE) {
					state = RUNNING;
				}
			}

			public void mouseExited(MouseEvent e) {
				// 由暂停修改为运行状态
				if (state == RUNNING) {
					state = PAUSE;
				}
			}
		};

		this.addMouseListener(ma);// 处理鼠标的操作事件
		this.addMouseMotionListener(ma);// 处理鼠标的滑动事件
		Timer timer = new Timer();
		int inter = 10;
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (state == RUNNING) {
					enterAction();// 敌人入场
					shootAction();// 子弹入场
					stepAction();//
					outOfBoundsAction();// 删除越界的飞行物
					bulletBangAction();// 碰撞检测
					heroBandAction();// 英雄机与敌人碰撞
					checkGameOverAction();
				}
				repaint();// 刷新(系统调用paint方法)
			}
		}, inter, inter);
	}

	/** 程序的入口 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("设计小游戏");
		ShootGame sg = new ShootGame();
		playBGM();
		frame.add(sg);
		frame.setSize(SVREEN_WIDTH, SVREEN_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		// frame.setResizable(false);
		sg.action();
	}
}
