package com.martinforget.cardiaccoherencelite;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.martinforget.Data;

/**
 * Created by forgetm on 2017-02-14.
 */

public class SoundActivity extends Activity {

    private TableRow BreatheInSoundRow;
    private TableRow BreatheOutSoundRow;

    private Data data;
    private ProfileManager profileMgr;
    private int breatheInSoundId = 1;
    private int breatheOutSoundId = 1;

    private TextView breatheInTxt;
    private TextView breatheOutTxt;
    private TextView volumeTxt;
    private boolean isBreatheInChanged = false;
    private ImageView btnBack;

    private long volumeVal = 50;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_activity);

        Bundle bundle = getIntent().getExtras();
        breatheInSoundId = bundle.getInt("breatheInSound");
        breatheOutSoundId = bundle.getInt("breatheOutSound");
        volumeVal = bundle.getLong("volume");



        breatheInTxt = (TextView) findViewById(R.id.BreatheInSoundTxt);
        breatheOutTxt = (TextView) findViewById(R.id.BreatheOutSoundTxt);
        btnBack = (ImageView) findViewById(R.id.btnBack2) ;

        setSoundTxt();
        data = Data.getInstance(this.getBaseContext());

        // Store temp data
        data.StoreDataLong("breatheInSound", (long) breatheInSoundId);
        data.StoreDataLong("breatheOutSound", (long)breatheOutSoundId);
        data.StoreDataLong("volume", Long.valueOf(volumeVal));

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setTitle(getResources().getString(R.string.soundtitle));

        // Sound
        BreatheInSoundRow = (TableRow) findViewById(R.id.BreatheInSoundRow);

        BreatheInSoundRow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isBreatheInChanged = true;
                openSoundSelectionDialog(breatheInSoundId);

            }

        });


        // Sound
        BreatheOutSoundRow = (TableRow) findViewById(R.id.BreatheOutSoundRow);

        BreatheOutSoundRow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isBreatheInChanged = false;
                openSoundSelectionDialog(breatheOutSoundId);

            }

        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SeekBar volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        volumeBar.setMax(100);
        volumeBar.setProgress((int) volumeVal);

        volumeTxt = (TextView) findViewById(R.id.volumeTxt);
        volumeTxt.setText(String.valueOf((int) volumeVal / 10));

        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                volumeVal = progress;
                if (volumeVal > 100)
                    volumeVal = 100;
                volumeTxt.setText(String.valueOf((int) volumeVal / 10));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("Relaxation", "Volume :"+ volumeVal);
                int maxVolume = 100;

                float log1=(float)(1-(Math.log(maxVolume-volumeVal))/Math.log(maxVolume));
                startTestSound(log1);
                data.StoreDataLong("volume", Long.valueOf(volumeVal));

            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return false;

    }

    private void openSoundSelectionDialog(int soundId) {
        final SoundSelectionDialog dialog = new SoundSelectionDialog(this,
                soundId);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialogint) {
                if (!dialog.isCancelled)
                {
                if (isBreatheInChanged)
                    breatheInSoundId = dialog.soundId;
                else
                    breatheOutSoundId = dialog.soundId;

                setSoundTxt();
                    Log.d("Writing Sound:", "BreatheIn="+breatheInSoundId + " BreatheOut="+breatheOutSoundId);
                    data.StoreDataLong("breatheInSound", (long) breatheInSoundId);
                    data.StoreDataLong("breatheOutSound", (long)breatheOutSoundId);
            }

            }
        });

        dialog.show();
    }

    private void setSoundTxt()
    {
        String tempString = "sound" + breatheInSoundId;
        Log.d("Relaxation", "String :"+ tempString);
        breatheInTxt.setText(getResources().getString(getResourceId(tempString, "string", getPackageName())));
        tempString = "sound" + breatheOutSoundId;
        Log.d("Relaxation", "String :"+ tempString);
        breatheOutTxt.setText(getResources().getString(getResourceId(tempString, "string", getPackageName())));
    }


    private int getResourceId(String pVariableName, String pResourcename, String pPackageName)
    {
        try {
            return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void startTestSound(float volume)
    {
        MediaPlayer mp;

        mp = MediaPlayer.create(getApplicationContext(), R.raw.sound1);

        mp.setVolume(volume, volume);
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
