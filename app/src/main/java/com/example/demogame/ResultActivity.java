package com.example.demogame;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demogame.utils.SoundManager;
import com.example.demogame.utils.UserManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private ImageView ivWinner, ivSecond, ivThird;
    private TextView tvWinnerName, tvSecondName, tvThirdName;
    private TextView tvCongratulations, tvWinnings, tvNewBalance;
    private LinearLayout betResultsContainer;
    private Button btnBackToBetting, btnPlayAgain;
    private Map<Integer, Double> bets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initializeViews();
        displayResults();
        calculateWinnings();

        // Play victory sound
        //SoundManager.getInstance().playSound(this, R.raw.victory_sound, false);

        btnBackToBetting.setOnClickListener(v -> backToBetting());
        btnPlayAgain.setOnClickListener(v -> playAgain());
    }

    private void initializeViews() {
        ivWinner = findViewById(R.id.ivWinner);
        ivSecond = findViewById(R.id.ivSecond);
        ivThird = findViewById(R.id.ivThird);
        tvWinnerName = findViewById(R.id.tvWinnerName);
        tvSecondName = findViewById(R.id.tvSecondName);
        tvThirdName = findViewById(R.id.tvThirdName);
        tvCongratulations = findViewById(R.id.tvCongratulations);
        tvWinnings = findViewById(R.id.tvWinnings);
        tvNewBalance = findViewById(R.id.tvNewBalance);
        betResultsContainer = findViewById(R.id.betResultsContainer);
        btnBackToBetting = findViewById(R.id.btnBackToBetting);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
    }

    private void displayResults() {
        ArrayList<String> carNames = getIntent().getStringArrayListExtra("carNames");
        ArrayList<Integer> carDrawables = getIntent().getIntegerArrayListExtra("carDrawables");
        ArrayList<Integer> finishPositions = getIntent().getIntegerArrayListExtra("finishPositions");
        bets = (HashMap<Integer, Double>) getIntent().getSerializableExtra("bets");

        if (carNames != null && carDrawables != null && carNames.size() >= 3) {
            // Display podium (1st, 2nd, 3rd)
            ivWinner.setImageResource(carDrawables.get(0));
            tvWinnerName.setText("🏆 1st: " + carNames.get(0));

            ivSecond.setImageResource(carDrawables.get(1));
            tvSecondName.setText("🥈 2nd: " + carNames.get(1));

            ivThird.setImageResource(carDrawables.get(2));
            tvThirdName.setText("🥉 3rd: " + carNames.get(2));

            // Display remaining positions (4th, 5th)
            for (int i = 3; i < carNames.size(); i++) {
                TextView tv = new TextView(this);
                tv.setText((i + 1) + "th: " + carNames.get(i));
                tv.setTextSize(16);
                tv.setTextColor(Color.BLACK);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(16, 8, 16, 8);
                betResultsContainer.addView(tv);
            }
        }
    }

    private void calculateWinnings() {
        double totalWinnings = 0;
        boolean wonAnyBet = false;

        ArrayList<String> carNames = getIntent().getStringArrayListExtra("carNames");

        if (bets != null && !bets.isEmpty()) {
            // Check if user bet on the winner (car ID 1 = index 0 in results)
            // The first car in carNames is the winner
            String winnerName = carNames.get(0);

            // Find which car ID won
            int winnerCarId = getCarIdByName(winnerName);

            if (bets.containsKey(winnerCarId)) {
                // User bet on the winner! Pay 3x
                double betAmount = bets.get(winnerCarId);
                totalWinnings = betAmount * 3;
                wonAnyBet = true;

                TextView tvBetResult = new TextView(this);
                tvBetResult.setText("✅ " + winnerName + " - Bet: $" + String.format("%.2f", betAmount) +
                                   " - Won: $" + String.format("%.2f", totalWinnings));
                tvBetResult.setTextSize(16);
                tvBetResult.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                tvBetResult.setPadding(16, 8, 16, 8);
                betResultsContainer.addView(tvBetResult);
            }

            // Show losing bets
            for (Map.Entry<Integer, Double> entry : bets.entrySet()) {
                int carId = entry.getKey();
                if (carId != winnerCarId) {
                    String carName = getCarNameById(carId);
                    TextView tvBetResult = new TextView(this);
                    tvBetResult.setText("❌ " + carName + " - Lost: $" + String.format("%.2f", entry.getValue()));
                    tvBetResult.setTextSize(16);
                    tvBetResult.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    tvBetResult.setPadding(16, 8, 16, 8);
                    betResultsContainer.addView(tvBetResult);
                }
            }
        }

        if (wonAnyBet) {
            tvCongratulations.setText("🎉 Congratulations! You Won! 🎉");
            tvWinnings.setText("Total Winnings: $" + String.format("%.2f", totalWinnings));
            UserManager.getInstance().getCurrentUser().addBalance(totalWinnings);

            SoundManager.getInstance().playSfx(this, R.raw.winsound);
        } else {
            tvCongratulations.setText("Better Luck Next Time!");
            tvWinnings.setText("Total Winnings: $0.00");

            SoundManager.getInstance().playSfx(this, R.raw.losesound);
        }

        UserManager.getInstance().saveUserBalance(this);
        tvNewBalance.setText("New Balance: $" +
            String.format("%.2f", UserManager.getInstance().getCurrentUser().getBalance()));
    }

    private int getCarIdByName(String name) {
        if (name.contains("Red")) return 1;
        if (name.contains("Blue")) return 2;
        if (name.contains("Green")) return 3;
        if (name.contains("Yellow")) return 4;
        if (name.contains("Purple")) return 5;
        return 1;
    }

    private String getCarNameById(int id) {
        switch (id) {
            case 1: return "Red Racer";
            case 2: return "Blue Lightning";
            case 3: return "Green Machine";
            case 4: return "Yellow Thunder";
            case 5: return "Purple Storm";
            default: return "Unknown";
        }
    }

    private void backToBetting() {
        SoundManager.getInstance().stopSfx();
        Intent intent = new Intent(ResultActivity.this, BettingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void playAgain() {
        backToBetting();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SoundManager.getInstance().stopSfx();
    }
}
