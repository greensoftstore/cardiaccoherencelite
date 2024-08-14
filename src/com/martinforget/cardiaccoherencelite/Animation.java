package com.martinforget.cardiaccoherencelite;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import androidx.core.app.NotificationCompatSideChannelService;

import com.martinforget.Data;

public class Animation {

    private CallbackListener callbacklistener;
    private ObjectAnimator up;
    private ObjectAnimator down;
    private ObjectAnimator scaleupX;
    private ObjectAnimator scaledownX;
    private ObjectAnimator scaleupY;
    private ObjectAnimator scaledownY;
    private ObjectAnimator scale2upX;
    private ObjectAnimator scale2downX;
    private ObjectAnimator scale2upY;
    private ObjectAnimator scale2downY;
    private ObjectAnimator flipUp;
    private ObjectAnimator flipDown;
    private AnimatorSet scaleUp;
    private AnimatorSet scaleDown;
    private AnimatorSet scale2Up;
    private AnimatorSet scale2Down;
    private static AnimatorSet set;
    private Context context;
    private Vibrator vibrator;
    private Handler handler;
    private Runnable downSound;
    private Runnable upSound;
    private Runnable holdSound;
    private Runnable hold2Sound;
    private boolean vibrate;
    private boolean sound;
    private boolean expertMode;
    private long inspval;
    private long expval;
    private long holdval;
    private long holdoutval;
    private int progress;
    private long volume;
    private long volumeMusic;
    private int breatheInSoundId;
    private int breatheOutSoundId;

    private boolean day;
    private int music;


    private ImageView image;
    private ImageView demoimage;

    private Data data;

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
        createAnimations();
    }

    public void setDemoImage(ImageView demoimage) {
        this.demoimage = demoimage;
    }

    public void setAnimationProgress(int progress) {
        this.progress = progress;
    }

    private int uptime;
    private int downtime;
    private int holdtime;
    private int holdouttime;
    private int ratiovalue;
    private int sessiontime;
    private int count = 0;
    private boolean stopanimation = false;
    private boolean isforced = false;
    private MediaPlayer musicPlayer;
    private boolean playMusic;
    public int getRatiovalue() {
        return ratiovalue;
    }

    public void setRatiovalue(int ratiovalue) {
        this.ratiovalue = ratiovalue;

        if (expertMode) {
            uptime = (int) (100 * inspval);
            downtime = (int) (100 * expval);
            holdtime = (int) (100 * holdval);
            holdouttime = (int) (100 * holdoutval);
        } else {
            downtime = (int) (60000 / cyclenumber) * (100 - ratiovalue) / 100;
            uptime = (int) (60000 / cyclenumber) * ratiovalue / 100;
            holdtime = 0;
            holdoutval = 0;
        }
        createAnimations();
    }

    public void setExpertValues(boolean expertMode, long inspval, long expval, long holdval, long holdoutval) {
        this.expertMode = expertMode;
        this.inspval = inspval;
        this.expval = expval;
        this.holdval = holdval;
        this.holdoutval = holdoutval;
    }

    public int getSessiontime() {
        return sessiontime;
    }

    public void setSessiontime(int sessiontime) {
        this.sessiontime = sessiontime;
        animationcyclenumber = cyclenumber * sessiontime;
        createAnimations();
    }

    public int getCyclenumber() {
        return cyclenumber;
    }

    public void setCyclenumber(int cyclenumber) {
        this.cyclenumber = cyclenumber;

        downtime = (int) (60000 / cyclenumber) * (100 - ratiovalue) / 100;
        uptime = (int) (60000 / cyclenumber) * ratiovalue / 100;
        holdtime = 0;
        animationcyclenumber = cyclenumber * sessiontime;
        createAnimations();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        createAnimations();
    }

    public void setVibration(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    private int cyclenumber;
    private int height;
    private int animationcyclenumber = 0;

    Animation(Context context, ViewGroup root, int height, int sessiontime,
              int cyclenumber, int ratiovalue, ImageView image,
              Vibrator vibrator, boolean vibrate, boolean sound,
              boolean expertMode, long inspval, long expval, long holdval, long holdoutval,
              long volume, long volumeMusic, int breatheInSoundId, int breatheOutSoundId, int music, boolean day, boolean playMusic) {

        this.context = context;
        this.vibrator = vibrator;
        setHeight(height);
        setSessiontime(sessiontime);
        setCyclenumber(cyclenumber);
        setRatiovalue(ratiovalue);
        setImage(image);
        this.vibrate = vibrate;
        this.sound = sound;
        this.expertMode = expertMode;
        this.inspval = inspval;
        this.expval = expval;
        this.holdval = holdval;
        this.holdoutval = holdoutval;
        this.volume = volume;
        this.volumeMusic = volumeMusic;
        this.breatheInSoundId = breatheInSoundId;
        this.breatheOutSoundId = breatheOutSoundId;
        this.music = music;
        this.day = day;
        this.playMusic = playMusic;

        createAnimations();
    }

    public void createAnimations() {
        int top = height - (int) (height * .8);
        int bottom = (int) (height * .6);

        Log.d("CreateAnimations:", "sessiontime=" + sessiontime
                + " cyclenumber =" + cyclenumber + " ratiovalue =" + ratiovalue
                + "animationcyclenumber=" + animationcyclenumber);
        Log.d("CreateAnimations:", "uptime =" + uptime + " downtime ="
                + downtime + " breatheInId=" + breatheInSoundId + " braetheOutId=" + breatheOutSoundId);
        down = ObjectAnimator.ofFloat(image, "Y", top, bottom);
        up = ObjectAnimator.ofFloat(image, "Y", bottom, top);
        scaleupX = ObjectAnimator.ofFloat(image, "scaleX", 3f);
        scaleupY = ObjectAnimator.ofFloat(image, "scaleY", 3f);
        scale2upX = ObjectAnimator.ofFloat(image, "scaleX", 3f);
        scale2upY = ObjectAnimator.ofFloat(image, "scaleY", 3f);
        scaledownX = ObjectAnimator.ofFloat(image, "scaleX", 1f);
        scaledownY = ObjectAnimator.ofFloat(image, "scaleY", 1f);
        scale2downX = ObjectAnimator.ofFloat(image, "scaleX", 1f);
        scale2downY = ObjectAnimator.ofFloat(image, "scaleY", 1f);
        flipUp = ObjectAnimator.ofFloat(image, "rotation", 0.0f, 180.0f);
        flipDown = ObjectAnimator.ofFloat(image, "rotation", 180.0f, 0.0f);
        up.setDuration(uptime);
        down.setDuration(downtime);
        scaleupX.setDuration(holdtime/2);
        scaledownX.setDuration(holdtime/2);
        scaleupY.setDuration(holdtime/2);
        scaledownY.setDuration(holdtime/2);
        scale2upX.setDuration(holdouttime/2);
        scale2downX.setDuration(holdouttime/2);
        scale2upY.setDuration(holdouttime/2);
        scale2downY.setDuration(holdouttime/2);
        flipUp.setDuration(0);
        flipDown.setDuration(0);
        scaleDown = new AnimatorSet();
        scaleDown.play(scaledownX).with(scaledownY);
        scaleUp = new AnimatorSet();
        scaleUp.play(scaleupX).with(scaleupY);
        scale2Down = new AnimatorSet();
        scale2Down.play(scale2downX).with(scale2downY);
        scale2Up = new AnimatorSet();
        scale2Up.play(scale2upX).with(scale2upY);
    }

    private void animate() {

        AnimatorSet newset = new AnimatorSet();
        set = newset;

        if (day) {
            if (holdtime != 0)
                if (holdoutval !=0)
                    set.playSequentially(flipUp, up, flipDown, scaleUp, scaleDown, down, scale2Up, scale2Down);
                else
                    set.playSequentially(flipUp, up, flipDown, scaleUp, scaleDown, down);
            else
                if (holdoutval !=0)
                    set.playSequentially(flipUp, up, flipDown, down, scale2Up, scale2Down);
                else
                    set.playSequentially(flipUp, up, flipDown, down);
        }
        else
        {
            if (holdtime != 0)
                if (holdoutval !=0)
                    set.playSequentially( up, scaleUp, scaleDown, down, scale2Up, scale2Down);
                else
                    set.playSequentially( up, scaleUp, scaleDown, down);
            else
                if (holdoutval !=0)
                    set.playSequentially( up, down, scale2Up, scale2Down);
                else
                    set.playSequentially( up, down);
        }


        set.setInterpolator(new AccelerateDecelerateInterpolator());

        handler = new Handler();

        holdSound =  new Runnable() {
            public void run() {
                if (vibrate) {
                    vibrator.vibrate(50);
                }
                if (sound) {
                    playSound(false);
                }
            }
        };

        hold2Sound =  new Runnable() {
            public void run() {
                if (vibrate) {
                    vibrator.vibrate(50);
                }
                if (sound) {
                    playSound(true);
                }
            }
        };


        upSound = new Runnable() {
            public void run() {
                if (vibrate) {
                    vibrator.vibrate(200);
                }
                if (sound) {
                    playSound(true);
                }
            }
        };

        downSound = new Runnable() {
            public void run() {
                if (vibrate) {
                    vibrator.vibrate(400);
                }
                if (sound) {
                    playSound(false);
                }
            }
        };
        // schedule sound
        handler.postDelayed(upSound, 0);

        if (holdtime !=0)
        {
            handler.postDelayed(holdSound, uptime);
            handler.postDelayed(downSound, uptime + holdtime);
            if (holdouttime != 0) {
                handler.postDelayed(hold2Sound, uptime + holdtime + downtime);
            }
        }
        else
        {
            handler.postDelayed(downSound, uptime );
            if (holdouttime != 0)
                handler.postDelayed(hold2Sound, uptime + downtime);
        }




        // third sound, if hold

        if (holdtime != 0) {
            handler.postDelayed(downSound, uptime + holdtime);

        }



        set.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isforced) {
                    if (continueAnimation() && !stopanimation) {
                        callbacklistener.onPause();
                        count++;
                        callbacklistener.onResume();
                        animate();
                    } else {
                        stopAnimation();
                        stopanimation = false;

                    }
                } else {
                    isforced = false;
                    stopanimation = false;
                }
            }

        });

        set.start();
    }

    public void setCallbackListener(CallbackListener eventListener) {
        callbacklistener = eventListener;
    }

    public void forceStop() {
        Log.d("Animation", "ABORT ANIMATION");
        isforced = true;
        stopanimation = true;
        if (set != null) {
            set.end();
            set.removeAllListeners();

            callbacklistener.onAbort();
            count = 0;
            if (handler != null) {
                handler.removeCallbacks(upSound);
                handler.removeCallbacks(downSound);
                handler.removeCallbacks(holdSound);
                handler.removeCallbacks(hold2Sound);
            }
        }
        stopMusic();
    }

    public void stopAnimation() {
        Log.d("Animation", "ENDING ANIMATION");
        stopanimation = true;

        set.removeAllListeners();
        callbacklistener.onStop();
        count = 0;
        stopMusic();

    }

    public void startAnimation() {
        Log.d("Animation", "STARTING ANIMATION animationcyclenumber="
                + animationcyclenumber);
        animate();
        startMusic();
        callbacklistener.onStart();

    }

    public void setVolume (long volume)
    {
        this.volume = volume;
    }


    public void setVolumeMusic (long volume)
    {
        volumeMusic = volume;
    }

    public void setBreatheInSoundId (int breatheInSoundId)
    {
        this.breatheInSoundId = breatheInSoundId;
    }

    public void setBreatheOutSoundId(int breatheOutSoundId)
    {
        this.breatheOutSoundId = breatheOutSoundId;
    }

    public void setMusic(int music) {
        this.music = music;
    }

    public void setDay(boolean day) {
        this.day = day;
    }

    private boolean continueAnimation() {
        boolean returnvalue = false;

        if (expertMode) {
            // Test if time is reached
            if (progress < 100) {
                returnvalue = true;
                Log.d("Animation", "progress =" + progress);
            }
        } else {
            if (count < animationcyclenumber - 1) {
                returnvalue = true;
            }
        }
        return returnvalue;
    }


    private void playSound(boolean playBreatheIn) {
        data = Data.getInstance(context);

        int maxVolume = 101;
        long volumeVal = volume;

        float log1 = (float) (1 - (Math.log(maxVolume - volumeVal)) / Math.log(maxVolume));

        MediaPlayer mp;

        String tempString = "sound" + breatheInSoundId;
        int id1 = getResourceId(tempString, "raw", context.getPackageName());
        Log.d("SOUND ID", "SoundId1=" + id1 + " breatheInid=" + breatheInSoundId);

        tempString = "sound" + breatheOutSoundId;
        int id2 = getResourceId(tempString, "raw", context.getPackageName());
        Log.d("SOUND ID", "SoundId2=" + id2 + " breatheOutid=" + breatheOutSoundId);

        if (playBreatheIn) {
            mp = MediaPlayer.create(context, id1);
        } else {
            mp = MediaPlayer.create(context, id2);
        }
        mp.setVolume(log1, log1);
        mp.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
                mp = null;
            }

        });
        mp.start();
    }



    private int getResourceId(String pVariableName, String pResourcename, String pPackageName) {
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void startMusic()
    {
        if (music >1 && music <6) {

            int maxVolume = 100;
            long volumeVal = volumeMusic;

            float log1 = (float) (1 - (Math.log(maxVolume - volumeVal)) / Math.log(maxVolume));


            String tempString = "music" + music;
            int id1 = getResourceId(tempString, "raw", context.getPackageName());
            Log.d("MUSIC ID", "MusicId=" + id1 + " music selected=" + music);

            musicPlayer = MediaPlayer.create(context, id1);

            if (!playMusic)
                log1 = 0;
            musicPlayer.setVolume(log1, log1);
            musicPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                }
            });
            musicPlayer.start();
        }
    }

    private void stopMusic()
    {
        if (musicPlayer != null)
        {
            try {

                musicPlayer.stop();
                musicPlayer.reset();
                musicPlayer.release();
                musicPlayer = null;
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void playMusic(boolean play)
    {
        float vol = 0;
        playMusic = play;

        if (play)
        {
            vol = (float) (1 - (Math.log(101 - volumeMusic)) / Math.log(101));
        }

        if (musicPlayer!=null)
            musicPlayer.setVolume(vol, vol);

        Log.d("Music", "set volume to " + volumeMusic);
    }


}
