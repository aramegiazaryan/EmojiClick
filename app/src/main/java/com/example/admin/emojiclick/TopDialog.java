package com.example.admin.emojiclick;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class TopDialog extends DialogFragment implements View.OnTouchListener {

    public CustomAdapter customAdapter;
    public Intent intent;
    private List<Contact> items;
    private MediaPlayer mp;
    private Sound sound;

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
    {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View localView = paramLayoutInflater.inflate(R.layout.dialog_top, null);
        setCancelable(false);
        sound =new Sound(getActivity());
        ListView listView = (ListView)localView.findViewById(R.id.lv_top);
        ImageButton backButton = (ImageButton)localView.findViewById(R.id.ib_top_back);
        backButton.setOnTouchListener(this);
        backButton.getBackground().clearColorFilter();
        backButton.invalidate();
        items = new ArrayList();
        int i = getArguments().getInt(MainActivity.KEY_QUANTITY);
        for (int j = 1; j <= i; j++)
        {
            Contact contact = new Contact(j, getArguments().getString(MainActivity.KEY_SAVE_NAME1 + j), getArguments().getInt(MainActivity.KEY_SAVE_SCORE1+ j));
            items.add(contact);
        }
        customAdapter = new CustomAdapter(getActivity(),items);
        listView.setAdapter(customAdapter);
        return localView;
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                ImageButton view = (ImageButton) v;
                view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                v.invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                sound.getSoundClick();
                dismiss();
                break;
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
}
