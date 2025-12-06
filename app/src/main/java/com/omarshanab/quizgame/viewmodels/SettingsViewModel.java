package com.omarshanab.quizgame.viewmodels;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.omarshanab.quizgame.activites.ActivitySettings;
import com.omarshanab.quizgame.database.DatabaseRepository;
import com.omarshanab.quizgame.utils.Utils;

public class SettingsViewModel extends AndroidViewModel {
    private final DatabaseRepository repository;
    private final SharedPreferences sharedPreferences;

    private final MutableLiveData<Integer> userPoints = new MutableLiveData<>();
    private final MutableLiveData<Integer> userLevel = new MutableLiveData<>();
    private final MutableLiveData<Boolean> soundAllowed = new MutableLiveData<>();
    private final MutableLiveData<Boolean> notificationAllowed = new MutableLiveData<>();
    private final MutableLiveData<Boolean> historyDeleted = new MutableLiveData<>();


    public SettingsViewModel(@NonNull Application application) {
        super(application);
        repository = DatabaseRepository.getInstance(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);

        soundAllowed.setValue(sharedPreferences.getBoolean(ActivitySettings.AllowSOUND, true));
        notificationAllowed.setValue(sharedPreferences.getBoolean(ActivitySettings.AllowNOTIFICATION, true));
        loadUserStats();
    }

    public void loadUserStats() {
        repository.getSumPoints(Utils.userId, userPoints::postValue);
        repository.getLastUserLevel(Utils.userId, userLevel::postValue);
    }

    public LiveData<Integer> getUserPoints() {
        return userPoints;
    }

    public LiveData<Integer> getUserLevel() {
        return userLevel;
    }

    public LiveData<Boolean> isSoundAllowed() {
        return soundAllowed;
    }

    public LiveData<Boolean> isNotificationAllowed() {
        return notificationAllowed;
    }
    public LiveData<Boolean> isHistoryDeleted() {
        return historyDeleted;
    }

    public void setAllowSound(boolean allowed) {
        soundAllowed.setValue(allowed);
        Utils.allowSound = allowed;
        sharedPreferences.edit().putBoolean(ActivitySettings.AllowSOUND, allowed).apply();
        if (allowed) {
            Utils.playOrResumeOrPauseLoopSound(getApplication());
        } else {
            if (Utils.loopMP != null && Utils.loopMP.isPlaying()) {
                Utils.loopMP.pause();
            }
        }
    }

    public void setAllowNotification(boolean allowed) {
        notificationAllowed.setValue(allowed);
        Utils.allowNotification = allowed;
        sharedPreferences.edit().putBoolean(ActivitySettings.AllowNOTIFICATION, allowed).apply();
        if (allowed) {
            Utils.setAlarmNotification(getApplication());
        } else {
            Utils.chancelAlarmNotification(getApplication());
        }
    }

    public void deleteAllHistory() {
        repository.deleteAllHistoryUser(Utils.userId, () -> {
            historyDeleted.postValue(true);
            loadUserStats();
        });
    }
}