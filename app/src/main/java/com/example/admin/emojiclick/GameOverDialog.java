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
    import android.widget.EditText;
    import android.widget.ImageButton;
    import android.widget.TextView;

        public class GameOverDialog extends DialogFragment implements View.OnTouchListener {
        public Intent intent;
        private MediaPlayer mp;
        private EditText name;
        private int score;
        private int topScore;
        private int visibleFlag;
        private Sound sound;

        public GameOverDialog() {}
        public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            score = getArguments().getInt(SecondActivity.KEY_SCORE_FOR_DIALOG);
            topScore = getActivity().getIntent().getIntExtra(MainActivity.KEY_TOP_SCORE, 0);
            setCancelable(false);
            sound=new Sound(getActivity());
            intent = new Intent(getActivity(), MainActivity.class);
            View localView = paramLayoutInflater.inflate(R.layout.dialog_game_over, null);
            ((TextView)localView.findViewById(R.id.tv_scpre_game_over)).setText("" + score);
            name = ((EditText)localView.findViewById(R.id.et_name));
            name.setVisibility(View.INVISIBLE);
            if (score > topScore)
            {
                intent.putExtra(MainActivity.KEY_SCORE, score);
                name.setVisibility(View.VISIBLE);
                visibleFlag = 1;
            }

            ImageButton ImageButton = (ImageButton)localView.findViewById(R.id.im_back);
            ImageButton.setOnTouchListener(this);
            //  ImageButton.getBackground().clearColorFilter();
            //  ImageButton.invalidate();
            return localView;
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
                {
                    sound.getSoundClick();
                    if (visibleFlag == 1) {
                        intent.putExtra(MainActivity.KEY_NAME, name.getText().toString());
                    }
                 //   intent.setFlags();
                    getActivity().setResult(Activity.RESULT_OK,intent);
                    getActivity().finish();
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
