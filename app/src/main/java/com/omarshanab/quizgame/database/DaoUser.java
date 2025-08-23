package com.omarshanab.quizgame.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface DaoUser {

    @Insert
    void insertUser(ModelUser... modelUser);

    @Query("UPDATE User SET email = :email , full_name = :fullName , country = :country , barth_date = :barthDate , gender = :gender WHERE user_id = :userId")
    void updateUser(int userId, String email, String fullName, String country, Long barthDate, int gender);

    @Query("SELECT * FROM User where email = :email AND password = :password")
    ModelUser hasUser(String email, String password);

    @Query("SELECT * FROM User where rememberMy is 1")
    ModelUser getRememberMyUser();

    @Query("UPDATE User SET rememberMy = :rememberMy WHERE User.email = :email")
    void updateRememberMy(String email, boolean rememberMy);

    @Query("UPDATE User SET password = :newPassword WHERE User.user_id = :userId and password = :oldPassword")
    int updatePassword(int userId, String oldPassword, String newPassword);
}
