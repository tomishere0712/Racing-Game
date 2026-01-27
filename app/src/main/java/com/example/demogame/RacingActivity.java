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
        btnReset.setOnClickListener(v -> resetRace());
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

        btnReset.setEnabled(false);
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

    private void startRace() {
        if (raceStarted) {
            Toast.makeText(this, "Race already in progress!", Toast.LENGTH_SHORT).show();
            return;
        }

        raceStarted = true;
        raceFinished = false;
        finishCount = 0;
        btnStart.setEnabled(false);
        btnReset.setEnabled(false);
        tvRaceStatus.setText("Race in Progress...");

        // Play racing sound
        //SoundManager.getInstance().playSound(this, R.raw.racing_sound, true);

        SoundManager.getInstance().stopBgm(); //stop nhạc theme khi vô đua xe
        SoundManager.getInstance().playBgm(this, R.raw.racingsound, true);

        Random random = new Random();

        for (int i = 0; i < cars.size(); i++) {
            final Car car = cars.get(i);
            final ImageView carView = carViews.get(i);
            final int carIndex = i;

            // Random speed: 60-100 (lower value = faster)
            int baseDuration = 3000 + random.nextInt(4000); // 3-7 seconds

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
                        btnReset.setEnabled(true);
                        SoundManager.getInstance().stopBgm();

                        // Wait a moment then show results
                        new Handler().postDelayed(() -> showResults(), 1500);
                    }
                }
            });

            animators.add(animator);
            animator.start();
        }
    }

    private void resetRace() {
        // Stop all animations
        for (ValueAnimator animator : animators) {
            if (animator != null && animator.isRunning()) {
                animator.cancel();
            }
        }
        animators.clear();

        // Reset car positions
        for (int i = 0; i < carViews.size(); i++) {
            carViews.get(i).setX(0);
            cars.get(i).setPosition(0);
            cars.get(i).setFinishPosition(0);
        }

        raceStarted = false;
        raceFinished = false;
        finishCount = 0;
        btnStart.setEnabled(true);
        btnReset.setEnabled(false);
        tvRaceStatus.setText("Ready to Race!");

        SoundManager.getInstance().stopBgm();
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
        for (ValueAnimator animator : animators) {
            if (animator != null && animator.isRunning()) {
                animator.cancel();
            }
        }
        SoundManager.getInstance().stopBgm();
    }
}
