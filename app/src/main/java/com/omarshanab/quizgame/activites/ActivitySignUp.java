package com.omarshanab.quizgame.activites;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.omarshanab.quizgame.R;
import com.omarshanab.quizgame.database.ConverterDate;
import com.omarshanab.quizgame.database.DatabaseRepository;
import com.omarshanab.quizgame.database.ModelUser;
import com.omarshanab.quizgame.databinding.ActivitySignUpBinding;
import com.omarshanab.quizgame.utils.UtilValidation;
import com.omarshanab.quizgame.utils.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ActivitySignUp extends AppCompatActivity {
    ActivitySignUpBinding binding;
    Date date;
    boolean showMenuIcon = false;
    DatabaseRepository repository;
    boolean editMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        repository = DatabaseRepository.getInstance(getApplication());

        if (getIntent().getBooleanExtra("edit_profile", false)) {
            disableViews();
            showMenuIcon = true;
            invalidateOptionsMenu();

            repository.getRememberMyUser(modelUser -> runOnUiThread(() -> {
                binding.teFullName.setText(modelUser.getFullName());
                binding.teEmail.setText(modelUser.getEmail());
                binding.spinnerCountries.setText(modelUser.getCountry());
                if (modelUser.getGender() == 1) {
                    binding.male.setChecked(true);
                } else {
                    binding.female.setChecked(true);
                }
                date = modelUser.getBarthDate();
                binding.tvBirthdate.setText(ConverterDate.toStringSimpleDate(modelUser.getBarthDate()));
            }));
        } else {
            setTitle(getString(R.string.create_new_account));
            binding.buSingUp.setOnClickListener(view -> {
                ModelUser modelUser = getValidateUserData(true);
                if (modelUser != null) {
                    repository.insertUser(modelUser, modelUser1 -> {
                        Utils.initUserData(modelUser1);
                        setResult(100);
                        finish();
                    });
                }
            });
        }


        binding.buBirthdate.setOnClickListener(view -> showDatePickerDialog());

        setSpinnerCountriesAdapter();
    }

    void setSpinnerCountriesAdapter() {
        ArrayList<String> countriesName = new ArrayList<>();
        for (String country : Locale.getISOCountries()) {
            // Palestine
            if (country.equalsIgnoreCase("PS"))
                countriesName.add(0, new Locale("", country).getDisplayCountry(Locale.getDefault()));
                // 💩
            else if (!country.equalsIgnoreCase("ISR"))
                countriesName.add(new Locale("", country).getDisplayCountry(Locale.getDefault()));
        }
        ArrayAdapter<String> adapterSpinnerCountries = new ArrayAdapter<>(ActivitySignUp.this, R.layout.item_country_spinner, countriesName);
        binding.spinnerCountries.setAdapter(adapterSpinnerCountries);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (showMenuIcon) {
            getMenuInflater().inflate(R.menu.edit_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.bu_edit) {
            if (editMode) {
                editMode = false;
                enableViews();
                item.setIcon(R.drawable.ic_check);
            } else {
                editMode = true;
                ModelUser user = getValidateUserData(false);
                if (user != null) {
                    repository.updateUser(Utils.userId, user.getEmail(), user.getFullName(), user.getCountry(),
                            user.getBarthDate(), user.getGender(), () -> {
                                runOnUiThread(() -> {
                                    item.setIcon(R.drawable.ic_edit);
                                    disableViews();
                                });
                            });
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ModelUser getValidateUserData(boolean isSignUp) {
        String fullName = Objects.requireNonNull(binding.teFullName.getText()).toString().trim();
        String email = Objects.requireNonNull(binding.teEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.tePassword.getText()).toString().trim();
        String rePassword = Objects.requireNonNull(binding.teRePassword.getText()).toString().trim();
        String country;
        country = binding.spinnerCountries.getText().toString();

        if (!UtilValidation.validateFullName(fullName, ActivitySignUp.this))
            return null;
        if (!UtilValidation.validateEmailAddress(email, ActivitySignUp.this))
            return null;
        if (isSignUp && !UtilValidation.validatePassword(password, false, ActivitySignUp.this))
            return null;

        String textMassage = "";
        if (isSignUp && !password.equals(rePassword)) {
            textMassage = getString(R.string.enter_the_password_identically);
        } else if (country.equals("")) {
            textMassage = getString(R.string.choose_a_country_name);
        } else if (date == null) {
            textMassage = getString(R.string.enter_date_of_birth);
        }
        if (!textMassage.equals("")) {
            Toast.makeText(ActivitySignUp.this, textMassage, Toast.LENGTH_SHORT).show();
            return null;
        }

        int selectedId = binding.genderRadioGroup.getCheckedRadioButtonId();
        int gender = selectedId == binding.male.getId() ? 1 : 2;

        return new ModelUser(fullName, email, isSignUp ? password : null, date,
                gender, country, true);
    }

    @SuppressLint("ClickableViewAccessibility")
    void disableViews() {
        setTitle(getString(R.string.profile));
        binding.teFullName.setEnabled(false);
        binding.teEmail.setEnabled(false);
        binding.spinnerCountries.setEnabled(false);
        binding.linearLayoutPassword.setVisibility(View.GONE);
        binding.buBirthdate.setOnTouchListener((v, e) -> true);
        binding.male.setClickable(false);
        binding.female.setClickable(false);
        binding.buSingUp.setVisibility(View.GONE);
    }

    @SuppressLint("ClickableViewAccessibility")
    void enableViews() {
        setTitle(getString(R.string.edit_profile));
        binding.teFullName.setEnabled(true);
        binding.teEmail.setEnabled(true);
        binding.spinnerCountries.setEnabled(true);
        binding.buBirthdate.setOnTouchListener((v, e) -> false);
        binding.male.setClickable(true);
        binding.female.setClickable(true);
        setSpinnerCountriesAdapter();
    }

    public void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                (view1, year, monthOfYear, dayOfMonth) -> {
                    date = calendar.getTime();
                    binding.tvBirthdate.setTextColor(getResources().getColor(R.color.black));
                    binding.tvBirthdate.setText(ConverterDate.toStringSimpleDate(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setAccentColor(getResources().getColor(R.color.primary_color));
        dpd.show(getSupportFragmentManager(), "");
    }
}