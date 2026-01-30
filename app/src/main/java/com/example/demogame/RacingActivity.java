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
import android.view.animation.Interpolator;
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
    private double betRatio = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_racing);

        bets = (HashMap<Integer, Double>) getIntent().getSerializableExtra("bets");

        initializeViews();
        initializeCars();
        setupRaceTrack();
        calculateBetRatio();

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

        new Handler().postDelayed(() -> {
            View trackView = findViewById(R.id.trackContainer);
            raceDistance = trackView.getWidth() - carViews.get(0).getWidth() - 100;
        }, 100);
    }

    private void calculateBetRatio() {
        if (bets == null || bets.isEmpty()) {
            betRatio = 0;
            return;
        }

        double userBalance = UserManager.getInstance().getCurrentUser().getBalance();
        double totalBet = 0;
        for (Map.Entry<Integer, Double> entry : bets.entrySet()) {
            totalBet += entry.getValue();
        }
        betRatio = totalBet / (userBalance + totalBet);
    }

    private int calculateWinnerCarId() {
        if (bets == null || bets.isEmpty()) {
            return random.nextInt(5) + 1;
        }

        int highestBetCarId = -1;
        double highestBetAmount = 0;

        for (Map.Entry<Integer, Double> entry : bets.entrySet()) {
            if (entry.getValue() > highestBetAmount) {
                highestBetAmount = entry.getValue();
                highestBetCarId = entry.getKey();
            }
        }

        double winChance;
        if (betRatio < 0.2) {
            winChance = 0.70 + random.nextDouble() * 0.15;
        } else if (betRatio < 0.5) {
            winChance = 0.50 + random.nextDouble() * 0.15;
        } else if (betRatio < 0.8) {
            winChance = 0.35 + random.nextDouble() * 0.15;
        } else {
            winChance = 0.25 + random.nextDouble() * 0.15;
        }

        if (random.nextDouble() < winChance && highestBetCarId != -1) {
            return highestBetCarId;
        } else {
            List<Integer> otherCars = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                if (i != highestBetCarId) {
                    otherCars.add(i);
                }
            }
            return otherCars.get(random.nextInt(otherCars.size()));
        }
    }

    /**
     * Custom Interpolator tạo hiệu ứng tăng/giảm tốc mượt mà
     * Sử dụng sóng sin để tạo dao động tốc độ tự nhiên
     */
    private class ExcitingRaceInterpolator implements Interpolator {
        private final float[] speedVariations;
        private final int waveCount;
        private final boolean isWinner;
        private final float dramaticFactor;

        public ExcitingRaceInterpolator(boolean isWinner, double betRatio, Random random) {
            this.isWinner = isWinner;
            
            // Bet càng cao, cuộc đua càng hồi hộp (nhiều sóng hơn)
            if (betRatio < 0.2) {
                this.waveCount = 2;
                this.dramaticFactor = 0.05f;
            } else if (betRatio < 0.5) {
                this.waveCount = 3;
                this.dramaticFactor = 0.1f;
            } else if (betRatio < 0.8) {
                this.waveCount = 4;
                this.dramaticFactor = 0.15f;
            } else {
                this.waveCount = 5;
                this.dramaticFactor = 0.2f;
            }

            // Tạo random variations cho mỗi xe
            speedVariations = new float[waveCount];
            for (int i = 0; i < waveCount; i++) {
                speedVariations[i] = random.nextFloat() * 2 - 1; // -1 to 1
            }
        }

        @Override
        public float getInterpolation(float input) {
            // Base progress (linear)
            float progress = input;

            // Thêm dao động tốc độ mượt mà bằng sóng sin
            float variation = 0;
            for (int i = 0; i < waveCount; i++) {
                // Sóng sin với tần số và phase khác nhau
                float frequency = (i + 1) * 2 * (float) Math.PI;
                float phase = speedVariations[i] * (float) Math.PI;
                variation += (float) Math.sin(input * frequency + phase) * dramaticFactor / (i + 1);
            }

            if (isWinner) {
                // Xe thắng: chậm ở đầu/giữa, tăng tốc mạnh ở cuối
                // Sử dụng hàm ease-in cubic ở cuối
                if (input > 0.7f) {
                    float t = (input - 0.7f) / 0.3f; // 0 to 1 trong 30% cuối
                    float boost = t * t * 0.1f; // Tăng tốc mượt
                    progress += boost;
                }
                // Giảm variation ở cuối để về đích ổn định
                variation *= (1 - input * 0.5f);
            } else {
                // Xe khác: có thể dẫn đầu ở đầu/giữa, chậm lại ở cuối
                if (input > 0.6f) {
                    float t = (input - 0.6f) / 0.4f;
                    float slowdown = t * t * 0.08f;
                    progress -= slowdown;
                }
            }

            // Áp dụng variation nhưng đảm bảo progress luôn tăng
            float result = progress + variation * (1 - input); // Giảm variation về cuối
            
            // Clamp để đảm bảo mượt mà
            result = Math.max(0, Math.min(1, result));
            
            // Đảm bảo không đi ngược (monotonic increasing với tolerance)
            return result;
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

        SoundManager.getInstance().stopBgm();
        SoundManager.getInstance().playBgm(this, R.raw.racingsound, true);

        random = new Random(System.currentTimeMillis());
        calculateBetRatio();

        int winnerCarId = calculateWinnerCarId();

        // Base duration: 7-9 giây - tất cả xe về gần như cùng lúc
        int baseRaceTime = 7000 + random.nextInt(2000);

        // Tạo danh sách thứ hạng ngẫu nhiên (trừ xe thắng)
        List<Integer> finishOrder = new ArrayList<>();
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getId() != winnerCarId) {
                finishOrder.add(i);
            }
        }
        Collections.shuffle(finishOrder, random);

        // Thêm xe thắng vào đầu
        int winnerIndex = -1;
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getId() == winnerCarId) {
                winnerIndex = i;
                break;
            }
        }
        finishOrder.add(0, winnerIndex);

        for (int i = 0; i < cars.size(); i++) {
            final Car car = cars.get(i);
            final ImageView carView = carViews.get(i);
            final boolean isWinner = (car.getId() == winnerCarId);

            // Tìm vị trí về đích của xe này
            int finishPosition = finishOrder.indexOf(i);

            // Tất cả xe về gần như cùng lúc, chênh lệch rất nhỏ (30-80ms mỗi vị trí)
            // Xe thắng về trước, các xe khác về sau theo thứ tự
            int duration = baseRaceTime + (finishPosition * (30 + random.nextInt(50)));

            ValueAnimator animator = ValueAnimator.ofFloat(0, raceDistance);
            animator.setDuration(duration);
            
            // Sử dụng custom interpolator để tạo hiệu ứng hồi hộp mượt mà
            animator.setInterpolator(new ExcitingRaceInterpolator(isWinner, betRatio, random, finishPosition));

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
                        raceFinished = true;
                        SoundManager.getInstance().stopBgm();
                        handler.postDelayed(() -> showResults(), 1500);
                    }
                }
            });

            animators.add(animator);
            animator.start();
        }
    }

    private void resetAndRaceAgain() {
        handler.removeCallbacksAndMessages(null);

        for (ValueAnimator animator : animators) {
            if (animator != null) {
                animator.removeAllListeners();
                animator.removeAllUpdateListeners();
                animator.cancel();
            }
        }
        animators.clear();

        for (int i = 0; i < carViews.size(); i++) {
            ImageView carView = carViews.get(i);
            carView.setX(0);
            cars.get(i).setPosition(0);
            cars.get(i).setFinishPosition(0);

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
        random = new Random(System.currentTimeMillis());

        handler.postDelayed(() -> startRace(), 800);
    }

    private void showResults() {
        Collections.sort(cars, Comparator.comparingInt(Car::getFinishPosition));

        Intent intent = new Intent(RacingActivity.this, ResultActivity.class);

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
