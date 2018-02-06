package com.example.admin.emojiclick;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_QUANTITY = "KEY_QUANTITY";
    public static final String KEY_SAVE_NAME = "Save_Name";
    public static final String KEY_SAVE_NAME1 = "Save_Name1";
    public static final String KEY_SAVE_SCORE = "Save_Score";
    public static final String KEY_SAVE_SCORE1 = "Save_Score1";
    public static final String KEY_SCORE = "KEY_SCORE";
    public static final String KEY_TOP_SCORE = "Save_Top";
    public static final String KEY_SOUND = "Save_Sound";
    private ImageButton buttonStart;
    private ImageButton buttonTop;
    private ImageButton buttonSound;
    private ImageButton effectEmoji1;
    private ImageButton effectEmoji2;
    private ImageButton effectEmoji3;
    public SharedPreferences sPref;
    private Timer timer;
    private RandomEmoji randomEmoji;
    private RelativeLayout layoutEffect;
    private RelativeLayout.LayoutParams lp;
    public int heightLayout;
    public int widthLayout;
    private Random ranNum;
    private boolean soundFlag;
    private Sound sound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkFirstRun();
        FullScreenCall();
        buttonStart = (ImageButton) findViewById(R.id.btn_start);
        buttonTop = (ImageButton) findViewById(R.id.btn_top);
        buttonSound = (ImageButton) findViewById(R.id.btn_sound);
        effectEmoji1 = (ImageButton) findViewById(R.id.im_emoji_effect1);
        effectEmoji2 = (ImageButton) findViewById(R.id.im_emoji_effect2);
        effectEmoji3 = (ImageButton) findViewById(R.id.im_emoji_effect3);
        layoutEffect = (RelativeLayout) findViewById(R.id.rl_effect);
        timer = new Timer();
        randomEmoji = new RandomEmoji();
        ranNum = new Random();
        sound = new Sound(this);
        getSoundIcon(getSaveDataInt(KEY_SOUND));
        buttonStart.setOnTouchListener(this);
        buttonTop.setOnTouchListener(this);
        buttonSound.setOnTouchListener(this);

        ViewTreeObserver observer = layoutEffect.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                widthLayout = layoutEffect.getWidth();
                heightLayout = layoutEffect.getHeight();
                layoutEffect.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                timerStart(randomCycle(), effectEmoji1);
                timerStart(randomCycle(), effectEmoji2);
                timerStart(randomCycle(), effectEmoji3);
            }
        });
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                ImageButton view = (ImageButton) v;
                view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                v.invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:
                switch (v.getId()) {
                    case R.id.btn_start: {
                        sound.getSoundClick();
                        timer.cancel();
                        Intent intent = new Intent(this, SecondActivity.class);
                        intent.putExtra(KEY_TOP_SCORE, lestTop());
                        startActivityForResult(intent, 15);
                        break;
                    }

                    case R.id.btn_top: {
                        sound.getSoundClick();
                        TopDialog topDialog = new TopDialog();
                        Bundle bundle = new Bundle();
                        sendDataTop(bundle);
                        topDialog.setArguments(bundle);
                        topDialog.show(getSupportFragmentManager(), null);
                        break;
                    }

                    case R.id.btn_sound: {

                        if (soundFlag) {
                            sound.getSoundClick();
                            getSoundIcon(0);
                        } else {
                            getSoundIcon(1);
                        }
                        break;
                    }

                }
            case MotionEvent.ACTION_CANCEL: {
                ImageButton view = (ImageButton) v;
                view.getBackground().clearColorFilter();
                view.invalidate();
                break;
            }
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        buttonStart.getBackground().clearColorFilter();
        buttonStart.invalidate();
        if (resultCode == RESULT_OK) {
            if (data != null) {
                int score = data.getIntExtra(KEY_SCORE, 0);
                String name = data.getStringExtra(KEY_NAME);
                updateTop(score, name);
            }
        }
    }

    public void checkFirstRun() {
        while (!getSharedPreferences("PREFERENCE", 0).getBoolean("isFirstRun", true)) {
            return;
        }

        for (int i = 1; i <= 10; i++) {
            setSaveData(KEY_SAVE_SCORE + i, 0);
            setSaveData(KEY_SAVE_NAME + i, "");
        }
        setSaveData(KEY_SOUND, 1);
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();
    }


    public int getSaveDataInt(String paramString) {

        sPref = getSharedPreferences(paramString, 0);
        return sPref.getInt(paramString, 0);
    }


    public String getSaveDataString(String paramString) {
        sPref = getSharedPreferences(paramString, MODE_PRIVATE);
        return sPref.getString(paramString, "");
    }


    public void setSaveData(String name, String data) {
        sPref = getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(name, data);
        editor.commit();
    }

    public void setSaveData(String name, int data) {
        sPref = getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putInt(name, data);
        editor.commit();
    }

    public void updateTop(int score, String name) {
        int i;
        for (i = 1; i < 11; i++) {

            if (score > getSaveDataInt(KEY_SAVE_SCORE + i)) {
                setSaveData(KEY_SAVE_SCORE + 11, score);
                setSaveData(KEY_SAVE_NAME + 11, name);
                for (int j = i; j <= 11; j++) {
                    int k = getSaveDataInt(KEY_SAVE_SCORE + j);
                    String str = getSaveDataString(KEY_SAVE_NAME + j);
                    setSaveData(KEY_SAVE_SCORE + j, getSaveDataInt(KEY_SAVE_SCORE + 11));
                    setSaveData(KEY_SAVE_NAME + j, getSaveDataString(KEY_SAVE_NAME + 11));
                    setSaveData(KEY_SAVE_SCORE + 11, k);
                    setSaveData(KEY_SAVE_NAME + 11, str);
                }
                break;
            }
        }
    }

    public void sendDataTop(Bundle bundle) {

        for (int i = 1; i <= 10; i++) {

            if (getSaveDataInt(KEY_SAVE_SCORE + i) == 0) {
                if (i == 1) {
                    bundle.putInt(KEY_QUANTITY, 0);
                    break;
                } else {
                    bundle.putInt(KEY_QUANTITY, i - 1);
                    break;
                }

            }
            bundle.putInt(KEY_QUANTITY, i);
            bundle.putInt(KEY_SAVE_SCORE1 + i, getSaveDataInt(KEY_SAVE_SCORE + i));
            bundle.putString(KEY_SAVE_NAME1 + i, getSaveDataString(KEY_SAVE_NAME + i));
        }

    }

    public int lestTop() {
        int i = 1;
        while (getSaveDataInt(KEY_SAVE_SCORE + i) != 0 && i <= 10) {
            i++;
        }
        return getSaveDataInt(KEY_SAVE_SCORE + i);
    }

    public void timerStart(final int timerCycle, final ImageButton effectEmoji) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        randomButton(timerCycle, effectEmoji);
                        ;

                    }
                });

            }
        }, 1, timerCycle);
    }

    public void randomButton(int timeCycle, ImageButton effectEmoji) {
        lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        int widthButton = effectEmoji.getWidth();
        int heightButton = effectEmoji.getHeight();
        int l = widthLayout - widthButton;
        int t = heightLayout - heightButton;
        int left = ranNum.nextInt(l);
        int top = ranNum.nextInt(t);
        lp.setMargins(left, top, 1, 1);
        effectEmoji.setLayoutParams(lp);
        effectEmoji.setBackground(getResources().getDrawable(randomEmoji.randomEmoji()));
        startAnimation(timeCycle, effectEmoji);
    }

    public int randomCycle() {
        int cycle = 0;
        while (cycle < 1000) {
            cycle = ranNum.nextInt(2000);
        }
        return cycle;
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

    public void getSoundIcon(int flag) {
        setSaveData(KEY_SOUND, flag);
        if (getSaveDataInt(KEY_SOUND) == 0) {
            soundFlag = false;
            buttonSound.setBackground(getResources().getDrawable(R.drawable.mute));
            sound.setFlagMute(true);
        } else {
            soundFlag = true;
            buttonSound.setBackground(getResources().getDrawable(R.drawable.sound));
            sound.setFlagMute(false);
        }
    }

   /* @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }*/


    public void startAnimation(int timeCycle, ImageView ivDH) {

        Animation scaleAnim = new ScaleAnimation(0, 1, 0, 1);
        scaleAnim.setDuration(timeCycle);
        scaleAnim.setRepeatCount(0);
        scaleAnim.setInterpolator(new AccelerateInterpolator());
        scaleAnim.setRepeatMode(Animation.REVERSE);

      /*  Animation rotateAnim = new RotateAnimation(0, 360, Animation.ABSOLUTE, Animation.ABSOLUTE, Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF);
        rotateAnim.setDuration(1000);
        rotateAnim.setRepeatCount(2);
        rotateAnim.setInterpolator(new AccelerateInterpolator());
        rotateAnim.setRepeatMode(Animation.REVERSE);*/

        RotateAnimation localRotateAnimation = new RotateAnimation(0.0F, 360.0F, 1, 0.5F, 1, 0.5F);
        localRotateAnimation.setDuration(timeCycle - 100);
        localRotateAnimation.setInterpolator(new LinearInterpolator());


        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(localRotateAnimation);
        animationSet.addAnimation(scaleAnim);
        //  animationSet.addAnimation(rotateAnim);
        ivDH.startAnimation(animationSet);
    }


    @Override
    protected void onDestroy() {
        System.exit(0);
        super.onDestroy();
    }
}

