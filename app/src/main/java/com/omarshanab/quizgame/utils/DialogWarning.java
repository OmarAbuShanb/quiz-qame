package com.omarshanab.quizgame.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.omarshanab.quizgame.R;
import com.omarshanab.quizgame.databinding.LayoutWarningDialogBinding;
import com.omarshanab.quizgame.interfaces.DialogListener;

public class DialogWarning extends Dialog {
    Activity activity;
    String buNegativeText;
    String warningMessage;
    DialogListener dialogListener;

    public DialogWarning(Activity activity, String buNegativeText, String warningMessage, DialogListener dialogListener) {
        super(activity);
        this.activity = activity;
        this.buNegativeText = buNegativeText;
        this.warningMessage = warningMessage;
        this.dialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutWarningDialogBinding binding = LayoutWarningDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.DialogScaleAnimation;
        binding.tvWarningMessage.setText(warningMessage);
        binding.buNegative.setText(buNegativeText);
        binding.buNegative.setOnClickListener(view -> {
            dialogListener.onDialogListener();
            dismiss();
        });
        binding.buPositive.setOnClickListener(view -> dismiss());
    }
}