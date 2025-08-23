package com.omarshanab.quizgame.utils;

import android.app.Activity;
import android.widget.Toast;

import com.omarshanab.quizgame.R;

import java.util.regex.Pattern;

public class UtilValidation {

    public static boolean validateEmailAddress(String emailAddressInput, Activity activity) {
        String emailAddress = emailAddressInput.trim();
        String expression = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        boolean isMatches = pattern.matcher(emailAddress).matches();
        if (!isMatches) {
            activity.runOnUiThread(() ->
                    Toast.makeText(activity.getApplicationContext(),
                            activity.getString(R.string.write_the_email_correctly), Toast.LENGTH_SHORT).show());
        }

        return isMatches;
    }

    public static boolean validatePassword(String passwordInput, boolean isLogin, Activity activity) {
        String password = passwordInput.trim();
        boolean result = true;
        String massage = null;
        if (!password.matches(".*[0-9].*")) {
            System.out.println(password + "password");
            massage = "Password should contain at least 1 digit";
            result = false;
        } else if (!password.matches(".*[a-z].*")) {
            massage = "Password should contain at least 1 lower case letter";
            result = false;
        } else if (!password.matches(".*[A-Z].*")) {
            massage = "Password should contain at least 1 upper case letter";
            result = false;
        }
//        else if (!password.matches(".*[a-zA-Z].*")) {
//            massage = "Password should contain a letter";
//            result = false;
//        }
        else if (password.matches(".*\\s.*")) {
            massage = "Password should not contain white spaces";
            result = false;
        } else if (!password.matches(".*[@#$%^&+=].*")) {
            massage = "Password should contain at least 1 special character";
            result = false;
        } else if (!password.matches(".{8,24}")) {
            massage = "Password should contain 8 - 24 characters";
            result = false;
        }

        if (isLogin && !result) {
            massage = activity.getString(R.string.write_the_password_correctly);
        }

        if (!result) {
            String finalMassage = massage;
            activity.runOnUiThread(() ->
                    Toast.makeText(activity.getApplicationContext(), finalMassage, Toast.LENGTH_SHORT).show());
        }
        return result;
    }

    public static boolean validateFullName(String fullNameInput, Activity activity) {
        String fullName = fullNameInput.trim();
        String expression = "^[a-zA-Z]{4,}(?: [a-zA-Z]+){0,2}$";
        //  ^   start of string
        //  [a-zA-Z]{4,}    4 or more ASCII letters
        //  (?: [a-zA-Z]+){0,2}     0 to 2 occurrences of a space followed with one or more ASCII letters
        //  $    end of string
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        boolean isMatches = pattern.matcher(fullName).matches();
        if (!isMatches)
            activity.runOnUiThread(() ->
                    Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.write_the_full_name_correctly), Toast.LENGTH_SHORT).show());

        return isMatches;
    }
}
