package com.omarshanab.quizgame.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "User", indices = {@Index(value = {"email"}, unique = true)})
@TypeConverters({ConverterDate.class})
public class ModelUser {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "full_name")
    private String fullName;

    @ColumnInfo
    private String email;

    @ColumnInfo
    private String password;

    @ColumnInfo(name = "barth_date")
    private Date barthDate;

    // 1 > male || 2 > female
    @ColumnInfo
    private int gender;

    @ColumnInfo
    private String country;

    @ColumnInfo(defaultValue = "0")
    private boolean rememberMy;

    public ModelUser(String fullName, String email, String password, Date barthDate, int gender, String country, boolean rememberMy) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.barthDate = barthDate;
        this.gender = gender;
        this.country = country;
        this.rememberMy = rememberMy;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMy() {
        return rememberMy;
    }

    public void setRememberMy(boolean rememberMy) {
        this.rememberMy = rememberMy;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBarthDate() {
        return barthDate;
    }

    public void setBarthDate(Date barthDate) {
        this.barthDate = barthDate;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
