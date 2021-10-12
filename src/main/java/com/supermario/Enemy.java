package com.supermario;

import java.awt.image.BufferedImage;

/**
 * 敌人类
 */
public class Enemy implements Runnable {

    // 存储当前坐标
    private int x, y;
    // 存储敌人类型
    private int type;
    // 判断敌人的运动方向
    private boolean face_to = true;
    // 显示敌人的当前图像
    private BufferedImage show;
    // 定义一个背景对象
    private com.supermario.BackGround bg;
    // 食人花的极限范围
    private int max_up;
    private int max_down;
    // 定义一个线程对象 用于实现食人花和蘑菇的运动
    private Thread thread = new Thread(this);
    // 定义当前图片的状态
    private int image_type = 0;


    // 蘑菇敌人的构造函数
    public Enemy(int x, int y, int type, boolean face_to, com.supermario.BackGround bg) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.face_to = face_to;
        this.bg = bg;
        show = com.supermario.StaticValue.mogu.get(0);
        thread.start();
    }

    // 食人花的构造函数
    public Enemy(int x, int y, int type, boolean face_to, com.supermario.BackGround bg, int max_up, int max_down) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.face_to = face_to;
        this.bg = bg;
        this.max_up = max_up;
        this.max_down = max_down;
        show = com.supermario.StaticValue.flower.get(0);
        thread.start();
    }

    // 蘑菇敌人死亡方法
    public void death() {
        show = com.supermario.StaticValue.mogu.get(2);
        this.bg.getEnemyList().remove(this);
    }


    @Override
    public void run() {
        while (true) {
            // 判断是否为蘑菇敌人
            if (type == 1) {
                if (face_to) {
                    this.x -= 2;
                } else {
                    this.x += 2;
                }
                image_type = image_type == 1 ? 0 : 1;
                show = com.supermario.StaticValue.mogu.get(image_type);
            }

            // 定义两个布尔变量 表示能不能向左或右移动
            boolean canLeft = true;
            boolean canRight = true;

            for (int i = 0; i < bg.getObstacleList().size(); i++) {
                com.supermario.Obstacle ob1 = bg.getObstacleList().get(i);

                // 判断是否可以向左走
                if (ob1.getX() == this.x + 36 && (ob1.getY() + 65 > this.y && ob1.getY() - 35 < this.y)) {
                    canRight = false;
                }
                // 判断是否可以向右走
                if (ob1.getX() == this.x - 36 && (ob1.getY() + 65 > this.y && ob1.getY() - 35 < this.y)) {
                    canLeft = false;
                }
            }

            // 向左走 并且 遇到了障碍物 或者 走到了屏幕的最左侧
            if (face_to && !canLeft || this.x == 0) {
                // 改变移动方向
                face_to = false;
            }
            // 向右走 并且 无法向右走 或者 走到了屏幕的最右侧
            else if ((!face_to) && (!canRight) || this.x == 764) {
                face_to = true;
            }

            // 判断是否为食人花敌人
            if (type == 2) {
                if (face_to) {
                    this.y -= 2;
                } else {
                    this.y += 2;
                }

                image_type = image_type == 1 ? 0 : 1;

                // 食人花是否达到极限位置
                if (face_to && (this.y == max_up)) {
                    face_to = false;
                }
                if ((!face_to) && (this.y == max_down)) {
                    face_to = true;
                }

                show = com.supermario.StaticValue.flower.get(image_type);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getShow() {
        return show;
    }

    public int getType() {
        return type;
    }
}
