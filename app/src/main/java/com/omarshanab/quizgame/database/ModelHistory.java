package com.omarshanab.quizgame.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "History",
        foreignKeys = {
                @ForeignKey(
                        entity = ModelUser.class,
                        parentColumns = {"user_id"},
                        childColumns = {"user_id"},
                        onUpdate = ForeignKey.CASCADE,
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = ModelLevel.class,
                        parentColumns = {"level_no"},
                        childColumns = {"level_no"},
                        onUpdate = ForeignKey.CASCADE,
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = ModelQuestion.class,
                        parentColumns = {"question_id"},
                        childColumns = {"question_id"},
                        onUpdate = ForeignKey.CASCADE,
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {@Index("user_id"), @Index("level_no"), @Index("question_id")})
public class ModelHistory {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "level_no")
    private int levelNo;

    @ColumnInfo(name = "question_id")
    private int questionId;

    // 0 > false | 1 true | -1 > skip
    @ColumnInfo()
    private int status;

    @ColumnInfo(name = "question_points")
    private int questionPoints;

    public ModelHistory(int userId, int levelNo, int questionId, int status, int questionPoints) {
        this.userId = userId;
        this.levelNo = levelNo;
        this.questionId = questionId;
        this.status = status;
        this.questionPoints = questionPoints;
    }

    public int getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(int levelNo) {
        this.levelNo = levelNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getQuestionPoints() {
        return questionPoints;
    }

    public void setQuestionPoints(int questionPoints) {
        this.questionPoints = questionPoints;
    }
}
