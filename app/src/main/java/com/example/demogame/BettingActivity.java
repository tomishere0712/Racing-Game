package com.example.demogame;

import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;

import com.example.demogame.models.Bet;
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
    private Button btnStartRace, btnLogout;
    private List<Car> cars;
    private Map<Integer, Double> bets; // carId -> bet amount
    private Map<Integer, EditText> betInputs; // carId -> EditText
    private Map<Integer, CheckBox> carCheckboxes; // carId -> CheckBox

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betting);

        initializeViews();
        initializeCars();
        setupCarBettingUI();
        updateBalance();

        // Play betting sound
        SoundManager.getInstance().playSound(this, R.raw.betting_sound, true);

        btnStartRace.setOnClickListener(v -> startRace());
        btnLogout.setOnClickListener(v -> logout());
    }

    private void initializeViews() {
        tvBalance = findViewById(R.id.tvBalance);
        tvTotalBet = findViewById(R.id.tvTotalBet);
        tvUsername = findViewById(R.id.tvUsername);
        carBettingContainer = findViewById(R.id.carBettingContainer);
        btnStartRace = findViewById(R.id.btnStartRace);
        btnLogout = findViewById(R.id.btnLogout);

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
            View carBetView = inflater.inflate(R.layout.item_car_bet, carBettingContainer, false);

            ImageView ivCar = carBetView.findViewById(R.id.ivCar);
            TextView tvCarName = carBetView.findViewById(R.id.tvCarName);
            CheckBox cbSelectCar = carBetView.findViewById(R.id.cbSelectCar);
            EditText etBetAmount = carBetView.findViewById(R.id.etBetAmount);

            ivCar.setImageResource(car.getDrawableResId());
            tvCarName.setText(car.getName());

            carCheckboxes.put(car.getId(), cbSelectCar);
            betInputs.put(car.getId(), etBetAmount);

            // Initially disable bet input
            etBetAmount.setEnabled(false);

            cbSelectCar.setOnCheckedChangeListener((buttonView, isChecked) -> {
                etBetAmount.setEnabled(isChecked);
                if (!isChecked) {
                    etBetAmount.setText("");
                    bets.remove(car.getId());
                }
                updateTotalBet();
            });

            etBetAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        if (!s.toString().isEmpty()) {
                            double amount = Double.parseDouble(s.toString());
                            bets.put(car.getId(), amount);
                        } else {
                            bets.remove(car.getId());
                        }
                    } catch (NumberFormatException e) {
                        bets.remove(car.getId());
                    }
                    updateTotalBet();
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            carBettingContainer.addView(carBetView);
        }
    }

    private void updateBalance() {
        if (UserManager.getInstance().getCurrentUser() != null) {
            tvUsername.setText("Player: " + UserManager.getInstance().getCurrentUser().getUsername());
            tvBalance.setText(String.format("Balance: $%.2f", UserManager.getInstance().getCurrentUser().getBalance()));
        }
    }

    private void updateTotalBet() {
        double total = 0;
        for (double amount : bets.values()) {
            total += amount;
        }
        tvTotalBet.setText(String.format("Total Bet: $%.2f", total));
    }

    private void startRace() {
        if (bets.isEmpty()) {
            Toast.makeText(this, "Please place at least one bet!", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalBet = 0;
        for (double amount : bets.values()) {
            totalBet += amount;
        }

        if (totalBet <= 0) {
            Toast.makeText(this, "Please enter valid bet amounts!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (totalBet > UserManager.getInstance().getCurrentUser().getBalance()) {
            Toast.makeText(this, "Insufficient balance! Total bet exceeds your balance.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check minimum bet
        for (double amount : bets.values()) {
            if (amount < 10) {
                Toast.makeText(this, "Minimum bet per car is $10", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Deduct bet amount
        UserManager.getInstance().getCurrentUser().deductBalance(totalBet);
        UserManager.getInstance().saveUserBalance(this);

        // Start race activity
        Intent intent = new Intent(BettingActivity.this, RacingActivity.class);
        intent.putExtra("bets", new HashMap<>(bets));
        SoundManager.getInstance().stopSound();
        startActivity(intent);
    }

    private void logout() {
        UserManager.getInstance().logout();
        SoundManager.getInstance().stopSound();
        Intent intent = new Intent(BettingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBalance();
        // Clear previous bets
        bets.clear();
        for (CheckBox cb : carCheckboxes.values()) {
            cb.setChecked(false);
        }
        for (EditText et : betInputs.values()) {
            et.setText("");
            et.setEnabled(false);
        }
        updateTotalBet();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SoundManager.getInstance().stopSound();
    }
}
