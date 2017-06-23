package com.blueocean.stare_app;

import java.io.Serializable;

/**
 * Created by BlueOcean_hua
 * Date 2017/6/10
 * Nicename 蓝色海洋
 * Desc 分享犹如大海，互联你我他
 */

public class PersonBo implements Serializable{

    private String name;
    private int score;
    private String editScore = "0";
    private int headId = 0;
    private boolean isClick = false;

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getEditScore() {
        return editScore;
    }

    public void setEditScore(String editScore) {
        this.editScore = editScore;
    }

    public int getHeadId() {
        return headId;
    }

    public void setHeadId(int headId) {
        this.headId = headId;
    }
}
