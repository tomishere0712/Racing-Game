package com.example.demogame;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demogame.models.Car;
import com.example.demogame.utils.SoundManager;
import com.example.demogame.utils.UserManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RacingActivity extends AppCompatActivity {

    private List<Car> cars;
    private List<ImageView> carViews;
    private List<ValueAnimator> animators;
    private Button btnStart, btnReset;
    private TextView tvRaceStatus;
    private Map<Integer, Double> bets;
    private boolean raceStarted = false;
    private boolean raceFinished = false;
    private int finishCount = 0;
    private float raceDistance;
    private Random random = new Random(System.currentTimeMillis());
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_racing);

        // Get bets from intent
        bets = (HashMap<Integer, Double>) getIntent().getSerializableExtra("bets");

        initializeViews();
        initializeCars();
        setupRaceTrack();

        btnStart.setOnClickListener(v -> startRace());
        btnReset.setOnClickListener(v -> resetAndRaceAgain());
    }

    private void initializeViews() {
        btnStart = findViewById(R.id.btnStart);
        btnReset = findViewById(R.id.btnReset);
        tvRaceStatus = findViewById(R.id.tvRaceStatus);

        carViews = new ArrayList<>();
        carViews.add(findViewById(R.id.ivCar1));
        carViews.add(findViewById(R.id.ivCar2));
        carViews.add(findViewById(R.id.ivCar3));
        carViews.add(findViewById(R.id.ivCar4));
        carViews.add(findViewById(R.id.ivCar5));

        animators = new ArrayList<>();

        // Reset button always enabled
        btnReset.setEnabled(true);
    }

    private void initializeCars() {
        cars = new ArrayList<>();
        cars.add(new Car(1, "Red Racer", R.drawable.car_red_animated));
        cars.add(new Car(2, "Blue Lightning", R.drawable.car_blue_animated));
        cars.add(new Car(3, "Green Machine", R.drawable.car_green_animated));
        cars.add(new Car(4, "Yellow Thunder", R.drawable.car_yellow_animated));
        cars.add(new Car(5, "Purple Storm", R.drawable.car_purple_animated));
    }

    private void setupRaceTrack() {
        for (int i = 0; i < carViews.size(); i++) {
            ImageView carView = carViews.get(i);
            Car car = cars.get(i);
            carView.setImageResource(car.getDrawableResId());
            carView.setX(0);

            Drawable d = carView.getDrawable();
            if (d instanceof Animatable) {
                ((Animatable) d).start();
            }
        }

        // Calculate race distance (screen width - car width)
        new Handler().postDelayed(() -> {
            View trackView = findViewById(R.id.trackContainer);
            raceDistance = trackView.getWidth() - carViews.get(0).getWidth() - 100;
        }, 100);
    }

    /**
     * Tính toán tỉ lệ thắng như nhà cái:
     * - Bet ít (< 20% balance): Tỉ lệ thắng cao 70-85%
     * - Bet vừa (20-50% balance): Tỉ lệ thắng trung bình 50-65%
     * - Bet cao (50-80% balance): Tỉ lệ thắng thấp hơn 35-50%
     * - Bet rất cao (> 80% balance): Tỉ lệ thắng thấp 25-40% nhưng vẫn có cơ hội
     */
    private int calculateWinnerCarId() {
        if (bets == null || bets.isEmpty()) {
            // Không có bet, random hoàn toàn
            return random.nextInt(5) + 1;
        }

        double userBalance = UserManager.getInstance().getCurrentUser().getBalance();
        double totalBet = 0;
        int highestBetCarId = -1;
        double highestBetAmount = 0;

        // Tìm xe được bet nhiều nhất
        for (Map.Entry<Integer, Double> entry : bets.entrySet()) {
            totalBet += entry.getValue();
            if (entry.getValue() > highestBetAmount) {
                highestBetAmount = entry.getValue();
                highestBetCarId = entry.getKey();
            }
        }

        // Tính tỉ lệ bet so với balance
        double betRatio = totalBet / (userBalance + totalBet); // Tính trên balance trước khi bet

        // Tính tỉ lệ thắng dựa trên mức bet
        double winChance;
        if (betRatio < 0.2) {
            // Bet ít: tỉ lệ thắng cao 70-85%
            winChance = 0.70 + random.nextDouble() * 0.15;
        } else if (betRatio < 0.5) {
            // Bet vừa: tỉ lệ thắng trung bình 50-65%
            winChance = 0.50 + random.nextDouble() * 0.15;
        } else if (betRatio < 0.8) {
            // Bet cao: tỉ lệ thắng thấp hơn 35-50%
            winChance = 0.35 + random.nextDouble() * 0.15;
        } else {
            // Bet rất cao: tỉ lệ thắng thấp 25-40% nhưng vẫn có cơ hội
            winChance = 0.25 + random.nextDouble() * 0.15;
        }

        // Random xem có thắng không
        if (random.nextDouble() < winChance && highestBetCarId != -1) {
            // Thắng - xe được bet nhiều nhất sẽ thắng
            return highestBetCarId;
        } else {
            // Thua - chọn random xe khác (không phải xe được bet nhiều nhất)
            List<Integer> otherCars = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                if (i != highestBetCarId) {
                    otherCars.add(i);
                }
            }
            return otherCars.get(random.nextInt(otherCars.size()));
        }
    }

    private void startRace() {
        if (raceStarted) {
            Toast.makeText(this, "Race already in progress!", Toast.LENGTH_SHORT).show();
            return;
        }

        raceStarted = true;
        raceFinished = false;
        finishCount = 0;
        btnStart.setEnabled(false);
        tvRaceStatus.setText("Race in Progress...");

        // Play racing sound
        SoundManager.getInstance().stopBgm();
        SoundManager.getInstance().playBgm(this, R.raw.racingsound, true);

        // Reseed random for each race
        random = new Random(System.currentTimeMillis());

        // Xác định xe thắng dựa trên tỉ lệ nhà cái
        int winnerCarId = calculateWinnerCarId();

        for (int i = 0; i < cars.size(); i++) {
            final Car car = cars.get(i);
            final ImageView carView = carViews.get(i);

            // Tính thời gian chạy: xe thắng nhanh nhất, các xe khác chậm hơn random
            int baseDuration;
            if (car.getId() == winnerCarId) {
                // Xe thắng: 3-4 giây
                baseDuration = 3000 + random.nextInt(1000);
            } else {
                // Xe khác: 4-7 giây (chậm hơn)
                baseDuration = 4000 + random.nextInt(3000);
            }

            ValueAnimator animator = ValueAnimator.ofFloat(0, raceDistance);
            animator.setDuration(baseDuration);
            animator.setInterpolator(new LinearInterpolator());

            animator.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();
                carView.setX(value);
                car.setPosition(value);
            });

            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    finishCount++;
                    car.setFinishPosition(finishCount);

                    if (finishCount == 1) {
                        tvRaceStatus.setText(car.getName() + " wins!");
                    }

                    if (finishCount == cars.size()) {
                        // All cars finished
                        raceFinished = true;
                        SoundManager.getInstance().stopBgm();

                        // Wait a moment then show results
                        handler.postDelayed(() -> showResults(), 1500);
                    }
                }
            });

            animators.add(animator);
            animator.start();
        }
    }

    private void resetAndRaceAgain() {
        // Remove any pending callbacks
        handler.removeCallbacksAndMessages(null);

        // Stop all animations immediately
        for (ValueAnimator animator : animators) {
            if (animator != null) {
                animator.removeAllListeners();
                animator.removeAllUpdateListeners();
                animator.cancel();
            }
        }
        animators.clear();

        // Reset car positions and animations
        for (int i = 0; i < carViews.size(); i++) {
            ImageView carView = carViews.get(i);
            carView.setX(0);
            cars.get(i).setPosition(0);
            cars.get(i).setFinishPosition(0);

            // Restart car animation
            Drawable d = carView.getDrawable();
            if (d instanceof Animatable) {
                ((Animatable) d).stop();
                ((Animatable) d).start();
            }
        }

        raceStarted = false;
        raceFinished = false;
        finishCount = 0;
        btnStart.setEnabled(false);
        tvRaceStatus.setText("Restarting Race...");

        SoundManager.getInstance().stopBgm();

        // Reseed random để có kết quả mới hoàn toàn
        random = new Random(System.currentTimeMillis());

        // Tự động bắt đầu đua lại sau 800ms
        handler.postDelayed(() -> startRace(), 800);
    }

    private void showResults() {
        // Sort cars by finish position
        Collections.sort(cars, Comparator.comparingInt(Car::getFinishPosition));

        Intent intent = new Intent(RacingActivity.this, ResultActivity.class);

        // Pass race results
        ArrayList<String> carNames = new ArrayList<>();
        ArrayList<Integer> carDrawables = new ArrayList<>();
        ArrayList<Integer> finishPositions = new ArrayList<>();

        for (Car car : cars) {
            carNames.add(car.getName());
            carDrawables.add(car.getDrawableResId());
            finishPositions.add(car.getFinishPosition());
        }

        intent.putStringArrayListExtra("carNames", carNames);
        intent.putIntegerArrayListExtra("carDrawables", carDrawables);
        intent.putIntegerArrayListExtra("finishPositions", finishPositions);
        intent.putExtra("bets", new HashMap<>(bets));

        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        for (ValueAnimator animator : animators) {
            if (animator != null) {
                animator.removeAllListeners();
                animator.removeAllUpdateListeners();
                animator.cancel();
            }
        }
        SoundManager.getInstance().stopBgm();
    }
}
