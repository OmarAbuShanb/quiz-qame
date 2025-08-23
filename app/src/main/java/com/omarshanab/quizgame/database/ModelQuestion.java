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

    @ColumnInfo(name = "answer_1")
    private String answer1;

    @ColumnInfo(name = "answer_2")
    private String answer2;

    @ColumnInfo(name = "answer_3")
    private String answer3;

    @ColumnInfo(name = "answer_4")
    private String answer4;

    @ColumnInfo(name = "true_answer")
    private String trueAnswer;

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

    public ModelQuestion(int questionId, String title, String answer1, String answer2, String answer3, String answer4, String trueAnswer, int points, int duration, int patternId, String hint, int levelNo) {
        this.questionId = questionId;
        this.title = title;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.trueAnswer = trueAnswer;
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

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public String getTrueAnswer() {
        return trueAnswer;
    }

    public void setTrueAnswer(String trueAnswer) {
        this.trueAnswer = trueAnswer;
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
