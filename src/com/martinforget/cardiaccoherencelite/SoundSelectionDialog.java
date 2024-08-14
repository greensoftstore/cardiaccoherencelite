package com.martinforget.cardiaccoherencelite;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;


public class SoundSelectionDialog extends Dialog {


    public int soundId = 0;
    public boolean isCancelled = false;

    private Context context;

    public SoundSelectionDialog(Context context, int soundId) {
        super(context);
        this.soundId = soundId;
        this.context = context;

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.sound_selection_menu);
        this.setCanceledOnTouchOutside(false);

        // Set selected radio button on
        String tempString = "radioButton" + soundId;
        Log.d("Relaxation", "String :" + tempString);
        Resources res = context.getResources();
        int id = res.getIdentifier(tempString, "id", getContext().getPackageName());
        RadioButton radioButton = (RadioButton) findViewById(id);

        radioButton.setChecked(true);

        Button cancelButton;
        cancelButton = (Button) findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isCancelled = true;
                dismiss();

            }

        });

        radioButton = (RadioButton) findViewById(R.id.radioButton1);

        radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isCancelled = false;
                playSound(R.raw.sound1);
                setSelecteSoundId(1);
            }
        });

        radioButton = (RadioButton) findViewById(R.id.radioButton2);

        radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isCancelled = false;
                playSound(R.raw.sound2);
                setSelecteSoundId(2);
            }
        });

        radioButton = (RadioButton) findViewById(R.id.radioButton3);

        radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isCancelled = false;
                playSound(R.raw.sound3);
                setSelecteSoundId(3);
            }
        });

        radioButton = (RadioButton) findViewById(R.id.radioButton4);

        radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isCancelled = false;
                playSound(R.raw.sound4);
                setSelecteSoundId(4);
            }
        });

        radioButton = (RadioButton) findViewById(R.id.radioButton5);

        radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isCancelled = false;
                playSound(R.raw.sound5);
                setSelecteSoundId(5);
            }
        });

        radioButton = (RadioButton) findViewById(R.id.radioButton6);

        radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isCancelled = false;
                playSound(R.raw.sound6);
                setSelecteSoundId(6);
            }
        });

        // Set listener to play sound on click
        TextView textView = (TextView) findViewById(R.id.soundtxt1);
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                playSound(R.raw.sound1);
            }
        });

        textView = (TextView) findViewById(R.id.soundtxt2);
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                playSound(R.raw.sound2);
            }
        });

        textView = (TextView) findViewById(R.id.soundtxt3);
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                playSound(R.raw.sound3);
            }
        });

        textView = (TextView) findViewById(R.id.soundtxt4);
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                playSound(R.raw.sound4);
            }
        });

        textView = (TextView) findViewById(R.id.soundtxt5);
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                playSound(R.raw.sound5);
            }
        });

        textView = (TextView) findViewById(R.id.soundtxt6);
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                playSound(R.raw.sound6);
            }
        });

    }

    private int getResourceId(String pVariableName, String pResourcename, String pPackageName) {
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void setSelecteSoundId(int id) {
        selectRadioButton(id);
        this.soundId = id;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                dismiss();
            }
        }, 500);
    }

    private void selectRadioButton (int id)
    {
        for (int i=1; i<7; i++)
        {
            String tempString = "radioButton" + i;
            Resources res = context.getResources();
            int radioId = res.getIdentifier(tempString, "id", getContext().getPackageName());

            if (i != id)
            {
                RadioButton radioButton = (RadioButton) findViewById(radioId);
                radioButton.setChecked(false);
            }
        }
    }

    private void playSound(int id) {

        MediaPlayer mp;

        mp = MediaPlayer.create(context, id);

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
                mp = null;
            }

        });
        mp.start();
    }

}