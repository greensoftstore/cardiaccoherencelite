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

public class MusicActivity extends Activity {

    private TableRow MusicRow;


    private Data data;
    private int musicSelectedId = 1;

    private TextView musicSelectedTxt;
    private TextView volumeTxt;
    private boolean isMusicChanged = false;
    private ImageView btnBack;

    private long volumeVal = 50;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_activity);

        Bundle bundle = getIntent().getExtras();
        musicSelectedId = bundle.getInt("musicSelected");
        volumeVal = bundle.getLong("volume");


        // music 0 doesn't exist
        if (musicSelectedId == 0)
            musicSelectedId = 1;

        musicSelectedTxt = (TextView) findViewById(R.id.MusicSelectedTxt);
        btnBack = (ImageView) findViewById(R.id.btnBack2) ;

        setMusicTxt();
        data = Data.getInstance(this.getBaseContext());


        // Store temp data
        data.StoreDataLong("musicSelected", (long) musicSelectedId);
        data.StoreDataLong("volume", Long.valueOf(volumeVal));

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setTitle(getResources().getString(R.string.musictitle));

        // Sound
        MusicRow = (TableRow) findViewById(R.id.MusicRow);

        MusicRow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isMusicChanged = true;
                openMusicSelectionDialog(musicSelectedId);

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
                data.StoreDataLong("volumeMusic", Long.valueOf(volumeVal));

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

    private void openMusicSelectionDialog(int soundId) {
        final MusicSelectionDialog dialog = new MusicSelectionDialog(this,
                soundId);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialogint) {
                if (!dialog.isCancelled)
                {
                if (isMusicChanged)
                    musicSelectedId = dialog.soundId;

                    setMusicTxt();
                    Log.d("Writing Sound:", "musicSelected="+musicSelectedId );
                    data.StoreDataLong("musicSelected", (long) musicSelectedId);
            }

            }
        });

        dialog.show();
    }

    private void setMusicTxt()
    {
        String tempString = "music" + musicSelectedId;
        Log.d("Relaxation", "String :"+ tempString);
        musicSelectedTxt.setText(getResources().getString(getResourceId(tempString, "string", getPackageName())));
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
