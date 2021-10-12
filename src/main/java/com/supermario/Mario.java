package com.supermario;

import java.awt.image.BufferedImage;

/**
 * 马里奥类
 */
public class Mario implements Runnable {
    // 用于表示横纵坐标
    private int x;
    private int y;
    // 用于表示当前状态
    private String status;
    // 当前状态对应的图像
    private BufferedImage show = null;
    // 获取障碍物的信息
    private com.supermario.BackGround backGround = new com.supermario.BackGround();
    // 马里奥的动作
    private Thread thread = null;
    // 马里奥的移动速度
    private int xSpeed;
    // 马里奥的跳跃速度
    private int ySpeed;
    // 定义一个索引
    private int index;
    // 马里奥的跳跃时间
    private int upTime = 0;
    // 用于判断马里奥是否走到了城堡的门口
    private boolean isOK;
    // 用于判断马里奥是否死亡
    private boolean isDeath = false;
    // 得分 消除普通方块加1分，消灭敌人加2分
    private int score;

    public Mario() {
    }

    public Mario(int x, int y) {
        this.x = x;
        this.y = y;
        show = com.supermario.StaticValue.stand_R;
        this.status = "stand -- right";
        thread = new Thread(this);
        thread.start();
    }

    // 马里奥死亡的方法
    public void death() {
        isDeath = true;
    }

    // 马里奥向左移动
    public void leftMove() {
        // 改变速度
        xSpeed = -5;
        // 判断马里奥是否碰到了旗子
        if (backGround.isReach()) {
            xSpeed = 0;
        }
        // 判断马里奥是否处于空中
        if (status.indexOf("jump") != -1) {
            status = "jump -- left";
        } else {
            status = "move -- left";
        }
    }

    // 马里奥向右移动
    public void rightMove() {
        // 改变速度
        xSpeed = 5;
        // 判断马里奥是否碰到了旗子
        if (backGround.isReach()) {
            xSpeed = 0;
        }
        // 判断马里奥是否处于空中
        if (status.indexOf("jump") != -1) {
            status = "jump -- right";
        } else {
            status = "move -- right";
        }
    }

    // 马里奥向左停止
    public void leftStop() {
        // 改变速度
        xSpeed = 0;
        // 判断马里奥是否处于空中
        if (status.indexOf("jump") != -1) {
            status = "jump -- left";
        } else {
            status = "stop -- left";
        }
    }

    // 马里奥向右停止
    public void rightStop() {
        // 改变速度
        xSpeed = 0;
        // 判断马里奥是否处于空中
        if (status.indexOf("jump") != -1) {
            status = "jump -- right";
        } else {
            status = "stop -- right";
        }
    }

    // 马里奥跳跃
    public void jump() {
        if (status.indexOf("jump") == -1) {
            if (status.indexOf("left") != -1) {
                status = "jump -- left";
            } else {
                status = "jump -- right";
            }
            ySpeed = -10;
            upTime = 7;

            // 判断马里奥是否碰到了旗子
            if (backGround.isReach()) {
                ySpeed = 0;
            }
        }
    }

    // 马里奥下落
    public void fall() {
        if (status.indexOf("left") != -1) {
            status = "jump -- left";
        } else {
            status = "jump -- right";
        }
        ySpeed = 10;
    }

    @Override
    public void run() {
        while (true) {

            // 判断是否处于障碍物上
            boolean onObstacle = false;
            // 马里奥是否可以往右走
            boolean canRight = true;
            // 马里奥是否可以往左走
            boolean canLeft = true;

            // 判断马里奥是否到达旗杆位置
            if (backGround.isFlag() && this.x >= 500) {
                this.backGround.setReach(true);

                // 判断旗子是否下落完成
                if (this.backGround.isBase()) {
                    status = "move -- right";
                    if (x < 690) {
                        x += 5;
                    } else {
                        isOK = true;
                    }
                } else {
                    // 马里奥在空中
                    if (y < 395) {
                        xSpeed = 0;
                        this.y += 5;
                        status = "jump -- right";
                    }

                    // 马里奥在地面
                    if (y > 395) {
                        xSpeed = 0;
                        this.y += 395;
                        status = "stop -- right";
                    }
                }

            } else {
                // 遍历当前场景里所有的障碍物
                for (int i = 0; i < backGround.getObstacleList().size(); i++) {
                    com.supermario.Obstacle ob = backGround.getObstacleList().get(i);
                    // 判断马里奥是否位于障碍物上
                    if (ob.getY() == this.y + 25 && (ob.getX() > this.x - 30 && ob.getX() < this.x + 25)) {
                        onObstacle = true;
                    }

                    // 判断跳起来是否顶到砖块
                    if ((ob.getY() >= this.y - 30 && ob.getY() <= this.y - 20) && (ob.getX() > this.x - 30 && ob.getX() < this.x + 25)) {
                        if (ob.getType() == 0) {
                            backGround.getObstacleList().remove(ob);
                            score += 1;
                        }
                        upTime = 0;
                    }

                    // 判断是否可以往右走
                    if (ob.getX() == this.x + 25 && (ob.getY() > this.y - 30 && ob.getY() < this.y + 25)) {
                        canRight = false;
                    }

                    // 判断是否可以往左走
                    if (ob.getX() == this.x - 30 && (ob.getY() > this.y - 30 && ob.getY() < this.y + 25)) {
                        canLeft = false;
                    }

                }

                // 判断马里奥是否碰到敌人死亡或者踩死蘑菇敌人
                for (int i = 0; i < backGround.getEnemyList().size(); i++) {
                    com.supermario.Enemy e = backGround.getEnemyList().get(i);

                    if (e.getY() == this.y + 20 && (e.getX() - 25 <= this.x && e.getX() + 35 >= this.x)) {
                        if (e.getType() == 1) {
                            e.death();
                            score += 2;
                            upTime = 3;
                            ySpeed = -10;
                        } else if (e.getType() == 2) {
                            // 马里奥死亡
                            death();
                        }
                    }

                    // 马里奥碰到敌人会死亡
                    if ((e.getX() + 35 > this.x && e.getX() - 25 < this.x) && (e.getY() + 35 > this.y && e.getY() - 20 < this.y)) {
                        // 马里奥死亡
                        death();
                    }
                }

                // 进行马里奥跳跃操作
                if (onObstacle && upTime == 0) {
                    if (status.indexOf("left") != -1) {
                        if (xSpeed != 0) {
                            status = "move -- left";
                        } else {
                            status = "stop -- left";
                        }
                    } else {
                        if (xSpeed != 0) {
                            status = "move -- right";
                        } else {
                            status = "stop -- right";
                        }
                    }
                } else {
                    // 马里奥处于上升状态
                    if (upTime != 0) {
                        upTime--;
                    } else {
                        fall();
                    }
                    y += ySpeed;
                }
            }


            if ((canLeft && xSpeed < 0) || (canRight && xSpeed > 0)) {
                x += xSpeed;
                // 判断马里奥是否走到了屏幕最左边
                if (x < 0) {
                    x = 0;
                }
            }
            // 判断当前是否是移动状态
            if (status.contains("move")) {
                index = index == 0 ? 1 : 0;
            }
            // 判断是否向左移动
            if ("move -- left".equals(status)) {
                show = com.supermario.StaticValue.run_L.get(index);
            }
            // 判断是否向右移动
            if ("move -- right".equals(status)) {
                show = com.supermario.StaticValue.run_R.get(index);
            }
            // 判断是否向左停止
            if ("stop -- left".equals(status)) {
                show = com.supermario.StaticValue.stand_L;
            }
            // 判断是否向右停止
            if ("stop -- right".equals(status)) {
                show = com.supermario.StaticValue.stand_R;
            }

            // 判断是否向左跳跃
            if ("jump -- left".equals(status)) {
                show = com.supermario.StaticValue.jump_L;
            }
            // 判断是否向右跳跃
            if ("jump -- right".equals(status)) {
                show = com.supermario.StaticValue.jump_R;
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isOK() {
        return isOK;
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

    public void setBackGround(com.supermario.BackGround backGround) {
        this.backGround = backGround;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isDeath() {
        return isDeath;
    }

    public int getScore() {
        return score;
    }
}
