package com.supermario;

import java.awt.image.BufferedImage;

/**
 *     障碍物类
 */

public class Obstacle implements Runnable {
    // 用于表示坐标
    private int x;
    private int y;
    // 用于记录障碍物类型
    private int type;
    // 用于显示图像
    private BufferedImage show = null;
    // 定义当前的场景对象
    private com.supermario.BackGround bg = null;
    // 定义一个线程对象
    private Thread thread = new Thread(this);

    public Obstacle(int x, int y, int type, com.supermario.BackGround bg) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.bg = bg;
        show = StaticValue.obstacle.get(type);
        if (type == 8) {
            thread.start();
        }
    }

    @Override
    public void run() {
        while(true) {
            // 旗子落地
            if (this.bg.isReach()) {
                if (this.y < 374) {
                    this.y += 5;
                }else {
                    this.bg.setBase(true);
                }
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

    public int getType() {
        return type;
    }

    public BufferedImage getShow() {
        return show;
    }


}
