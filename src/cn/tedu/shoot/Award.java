/**
 * 太原工业学院 计算机工程系 软件工程 版权所有 2018-2022
 */
package cn.tedu.shoot;

/**
 *奖励接口：火力/生命
 * @author liuhao
 * @version 1.0
 */
public interface Award {
	
	public int DOUBLE_FIRE = 0;
	public int LIFE = 1;
	
	/**获取奖励的类型*/
	public int getAwardType();

}
