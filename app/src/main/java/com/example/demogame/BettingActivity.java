package com.example.demogame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.text.InputType;


import androidx.appcompat.app.AppCompatActivity;

import com.example.demogame.models.Car;
import com.example.demogame.utils.SoundManager;
import com.example.demogame.utils.UserManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BettingActivity extends AppCompatActivity {

    private TextView tvBalance, tvTotalBet, tvUsername;
    private LinearLayout carBettingContainer;
    private Button btnStartRace, btnLogout, btnAddMoney;

    private List<Car> cars;
    private Map<Integer, Double> bets;
    private Map<Integer, EditText> betInputs;
    private Map<Integer, CheckBox> carCheckboxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betting);

        initializeViews();
        initializeCars();
        setupCarBettingUI();
        updateBalance();

        //SoundManager.getInstance().playSound(this, R.raw.betting_sound, true);

        btnStartRace.setOnClickListener(v -> startRace());
        btnLogout.setOnClickListener(v -> logout());
        btnAddMoney.setOnClickListener(v -> addMoney());
    }

    private void initializeViews() {
        tvBalance = findViewById(R.id.tvBalance);
        tvTotalBet = findViewById(R.id.tvTotalBet);
        tvUsername = findViewById(R.id.tvUsername);
        carBettingContainer = findViewById(R.id.carBettingContainer);

        btnStartRace = findViewById(R.id.btnStartRace);
        btnLogout = findViewById(R.id.btnLogout);
        btnAddMoney = findViewById(R.id.btnAddMoney);

        bets = new HashMap<>();
        betInputs = new HashMap<>();
        carCheckboxes = new HashMap<>();
    }

    private void initializeCars() {
        cars = new ArrayList<>();
        cars.add(new Car(1, "Red Racer", R.drawable.car_red));
        cars.add(new Car(2, "Blue Lightning", R.drawable.car_blue));
        cars.add(new Car(3, "Green Machine", R.drawable.car_green));
        cars.add(new Car(4, "Yellow Thunder", R.drawable.car_yellow));
        cars.add(new Car(5, "Purple Storm", R.drawable.car_purple));
    }

    private void setupCarBettingUI() {
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Car car : cars) {
            View view = inflater.inflate(R.layout.item_car_bet, carBettingContainer, false);

            ImageView ivCar = view.findViewById(R.id.ivCar);
            TextView tvCarName = view.findViewById(R.id.tvCarName);
            CheckBox cbSelectCar = view.findViewById(R.id.cbSelectCar);
            EditText etBetAmount = view.findViewById(R.id.etBetAmount);

            ivCar.setImageResource(car.getDrawableResId());
            tvCarName.setText(car.getName());

            etBetAmount.setEnabled(false);

            cbSelectCar.setOnCheckedChangeListener((b, checked) -> {
                etBetAmount.setEnabled(checked);
                if (!checked) {
                    etBetAmount.setText("");
                    bets.remove(car.getId());
                }
                updateTotalBet();
            });

            etBetAmount.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void afterTextChanged(Editable s) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        if (!s.toString().isEmpty()) {
                            bets.put(car.getId(), Double.parseDouble(s.toString()));
                            playBetSound();
                        } else {
                            bets.remove(car.getId());
                        }
                    } catch (Exception e) {
                        bets.remove(car.getId());
                    }
                    updateTotalBet();
                }
            });

            carBettingContainer.addView(view);
        }
    }

    private void updateBalance() {
        if (UserManager.getInstance().getCurrentUser() != null) {
            tvUsername.setText("Player: " +
                    UserManager.getInstance().getCurrentUser().getUsername());
            tvBalance.setText(String.format(
                    "Balance: $%.2f",
                    UserManager.getInstance().getCurrentUser().getBalance()));
        }
    }

    private void updateTotalBet() {
        double total = 0;
        for (double v : bets.values()) total += v;
        tvTotalBet.setText(String.format("Total Bet: $%.2f", total));
    }

    // 🔥 ADD MONEY
    private void addMoney() {
        if (UserManager.getInstance().getCurrentUser() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Money");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Enter amount");

        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String value = input.getText().toString().trim();

            if (value.isEmpty()) {
                Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(value);
            } catch (Exception e) {
                Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
                return;
            }

            if (amount <= 0) {
                Toast.makeText(this, "Amount must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }

            UserManager.getInstance().getCurrentUser().addBalance(amount);
            UserManager.getInstance().saveUserBalance(this);
            updateBalance();
            playBetSound();

            Toast.makeText(this, "+$" + amount + " added!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void startRace() {
        if (bets.isEmpty()) {
            Toast.makeText(this, "Place at least one bet!", Toast.LENGTH_SHORT).show();
            return;
        }

        double total = 0;
        for (double v : bets.values()) total += v;

        if (total > UserManager.getInstance().getCurrentUser().getBalance()) {
            Toast.makeText(this, "Insufficient balance!", Toast.LENGTH_SHORT).show();
            return;
        }

        UserManager.getInstance().getCurrentUser().deductBalance(total);
        UserManager.getInstance().saveUserBalance(this);

        Intent i = new Intent(this, RacingActivity.class);
        i.putExtra("bets", new HashMap<>(bets));
        SoundManager.getInstance().stopBgm();
        startActivity(i);
    }

    private void logout() {
        UserManager.getInstance().logout();
        SoundManager.getInstance().stopBgm();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        SoundManager.getInstance().stopSound();
//    }

    private void playBetSound() {
        MediaPlayer mp = MediaPlayer.create(this, R.raw.bettingsound);
        if (mp != null) {
            mp.setOnCompletionListener(MediaPlayer::release);
            mp.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SoundManager.getInstance().playBgm(
                this,
                R.raw.themesound2,
                true
        );
    }

}
