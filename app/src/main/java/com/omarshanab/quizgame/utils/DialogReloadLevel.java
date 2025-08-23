package com.omarshanab.quizgame.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.omarshanab.quizgame.R;
import com.omarshanab.quizgame.databinding.LayoutReloadLevelDialogBinding;
import com.omarshanab.quizgame.interfaces.DialogListener;

public class DialogReloadLevel extends Dialog {
    public Activity activity;
    DialogListener dialogListener;

    public DialogReloadLevel(Activity activity, DialogListener dialogListener) {
        super(activity);
        this.activity = activity;
        this.dialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutReloadLevelDialogBinding binding = LayoutReloadLevelDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.DialogScaleAnimation;
        binding.buOk.setOnClickListener(view -> dismiss());
    }

    @Override
    protected void onStop() {
        super.onStop();
        dialogListener.onDialogListener();
    }
}