package com.example.admin.emojiclick;

import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

import java.util.Random;

public class RandomEmoji {
    Random ranNum = new Random();

    public int randomEmoji() {
        switch (ranNum.nextInt(23)) {
            case 0: {
                return R.drawable.emoji1;
            }
            case 1: {
                return R.drawable.emoji2;
            }
            case 2: {
                return R.drawable.emoji3;
            }
            case 3: {
                return R.drawable.emoji4;
            }
            case 4: {
                return R.drawable.emoji5;
            }
            case 5: {
                return R.drawable.emoji7;
            }
            case 6: {
                return R.drawable.emoji8;
            }
            case 7: {
                return R.drawable.emoji9;
            }
            case 8: {
                return R.drawable.emoji10;
            }
            case 9: {
                return R.drawable.emoji11;
            }
            case 10: {
                return R.drawable.emoji12;
            }
            case 11: {
                return R.drawable.emoji13;
            }
            case 12: {
                return R.drawable.emoji14;
            }
            case 13: {
                return R.drawable.emoji15;
            }
            case 14: {
                return R.drawable.emoji16;
            }
            case 15: {
                return R.drawable.emoji17;
            }
            case 16: {
                return R.drawable.emoji18;
            }
            case 17: {
                return R.drawable.emoji19;
            }
            case 18: {
                return R.drawable.emoji20;
            }
            case 19: {
                return R.drawable.emoji21;
            }
            case 20: {
                return R.drawable.emoji22;
            }
            case 21: {
                return R.drawable.emoji23;
            }
            case 22: {
                return R.drawable.emoji24;
            }
            case 23: {
                return R.drawable.emoji25;
            }
        }
        return R.drawable.emoji25;
    }

}

