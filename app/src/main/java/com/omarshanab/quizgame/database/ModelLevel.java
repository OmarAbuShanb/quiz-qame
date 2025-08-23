package com.omarshanab.quizgame.database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Level")
public class ModelLevel {

    @PrimaryKey()
    @ColumnInfo(name = "level_no")
    private int levelNo;

    @ColumnInfo(name = "unlock_points")
    private int unlockPoints;

    public ModelLevel(int levelNo, int unlockPoints) {
        this.levelNo = levelNo;
        this.unlockPoints = unlockPoints;
    }

    public int getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(int levelNo) {
        this.levelNo = levelNo;
    }

    public int getUnlockPoints() {
        return unlockPoints;
    }

    public void setUnlockPoints(int unlockPoints) {
        this.unlockPoints = unlockPoints;
    }
}
