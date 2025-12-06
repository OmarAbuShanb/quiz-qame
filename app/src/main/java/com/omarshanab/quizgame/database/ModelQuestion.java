package com.omarshanab.quizgame.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(
        tableName = "Question",
        foreignKeys = {
                @ForeignKey(
                        entity = ModelLevel.class,
                        parentColumns = {"level_no"},
                        childColumns = {"level_no"},
                        onUpdate = ForeignKey.CASCADE,
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {@Index("level_no")}
)
public class ModelQuestion implements Serializable {
    @PrimaryKey()
    @ColumnInfo(name = "question_id")
    private int questionId;

    @ColumnInfo()
    private String title;

    @ColumnInfo(name = "answers")
    private String answers;

    @ColumnInfo(name = "true_answers")
    private String trueAnswers;

    @ColumnInfo()
    private int points;

    @ColumnInfo()
    private int duration;

    @ColumnInfo(name = "pattern_id")
    private int patternId;

    @ColumnInfo()
    private String hint;

    @ColumnInfo(name = "level_no")
    private int levelNo;

    public ModelQuestion(int questionId, String title, String answers, String trueAnswers, int points, int duration, int patternId, String hint, int levelNo) {
        this.questionId = questionId;
        this.title = title;
        this.answers = answers;
        this.trueAnswers = trueAnswers;
        this.points = points;
        this.duration = duration;
        this.patternId = patternId;
        this.hint = hint;
        this.levelNo = levelNo;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getTrueAnswers() {
        return trueAnswers;
    }

    public void setTrueAnswers(String trueAnswers) {
        this.trueAnswers = trueAnswers;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPatternId() {
        return patternId;
    }

    public void setPatternId(int patternId) {
        this.patternId = patternId;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(int levelNo) {
        this.levelNo = levelNo;
    }
}
