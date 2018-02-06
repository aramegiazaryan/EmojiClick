package com.example.admin.emojiclick;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton buttonEmoji;
    private TextView scoreText;
    private TextView levelText;
    public static final String KEY_SCORE_FOR_DIALOG = "KEY_SCORE_FOR_DIALOG";
    public int heightButton;
    public int heightLayout;
    private RelativeLayout layoutGame;
    private int left;
    private int level = 1;
    public RelativeLayout.LayoutParams lp;
    public Random ranNum;
    private int score;
    private Timer timer;
    private int timerCycle = 1000;
    private boolean timerFlag;
    private int top;
    public int widthButton;
    public int widthLayout;
    public RandomEmoji randomEmoji;
    public Sound sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        FullScreenCall();
        buttonEmoji = (ImageButton) findViewById(R.id.ib_emoji);
        scoreText = (TextView) findViewById(R.id.tv_score);
        levelText = (TextView) findViewById(R.id.tv_level);
        layoutGame = (RelativeLayout) findViewById(R.id.rl_game);
        buttonEmoji.setOnClickListener(this);
        sound = new Sound(this);
        ranNum = new Random();
        timer = new Timer();
        randomEmoji = new RandomEmoji();

        lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ViewTreeObserver observer = layoutGame.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                widthLayout = layoutGame.getWidth();
                heightLayout = layoutGame.getHeight();
                layoutGame.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                randomButton();

            }
        });

    }




    public void emojiAnimation(ImageButton imageButton) {
        RotateAnimation localRotateAnimation = new RotateAnimation(0.0F, 360.0F, 1, 0.5F, 1, 0.5F);
        localRotateAnimation.setDuration(100L);
        localRotateAnimation.setInterpolator(new LinearInterpolator());
        imageButton.startAnimation(localRotateAnimation);
    }


    @Override
    public void onClick(View v) {
        timerFlag = false;
        timer.cancel();
        sound.getSoundEffect();
        score++;
        scoreText.setText("" + score);
        solveLevel();
        emojiAnimation(buttonEmoji);
        randomButton();
        timerStart();
    }

    public void randomButton() {
        widthButton = buttonEmoji.getWidth();
        heightButton = buttonEmoji.getHeight();
        int l = widthLayout - widthButton;
        int t = heightLayout - heightButton;
        left = ranNum.nextInt(l);
        top = ranNum.nextInt(t);
        lp.setMargins(left, top, 1, 1);
        buttonEmoji.setLayoutParams(lp);
        // randomEmoji.randomEmoji(buttonEmoji);
        buttonEmoji.setBackground(getResources().getDrawable(randomEmoji.randomEmoji()));
    }

    public void timerStart() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (timerFlag) {
                            timer.cancel();
                            buttonEmoji.setVisibility(View.INVISIBLE);
                            sound.getSoundGameOver();
                            Bundle bundle = new Bundle();
                            bundle.putInt(KEY_SCORE_FOR_DIALOG, score);
                            GameOverDialog gameOverDialog = new GameOverDialog();
                            gameOverDialog.setArguments(bundle);
                            gameOverDialog.show(getSupportFragmentManager(), null);

                        } else {
                            timerFlag = true;
                        }
                    }
                });

            }
        }, 1, timerCycle);
    }

    public void solveLevel() {

        while (score % 30 != 0) {
            return;
        }
        level++;
        levelText.setText("" + level);
        timerCycle = timerCycle - 100;
    }


    public void FullScreenCall() {
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        timer.cancel();
        sound.getSoundClick();
        finish();
        super.onBackPressed();
    }
}
