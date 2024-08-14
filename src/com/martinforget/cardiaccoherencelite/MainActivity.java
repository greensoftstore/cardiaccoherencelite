package com.martinforget.cardiaccoherencelite;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewAnimationUtils;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import com.martinforget.Data;
import com.martinforget.SessionHistoryDatabase;
import com.martinforget.SessionHistoryHelper;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Math.abs;

import io.monedata.Monedata;

public class MainActivity extends Activity implements OnTouchListener {

    private static final int PERMISSION_REQUEST_CODE = 200;
    private ImageView image;
    private Vibrator vibrator;

    private int[] startbuttonpos = new int[2];
    private Activity mActivity;
    private int count = 0;
    private ToggleButton startbutton;
    private SeekBar sessionselector;
    private TextView sessionNb;
    private SeekBar respperminselector;
    private TextView cycleNb;
    private SeekBar ratio;
    private TextView ratioNb;
    private SeekBar insptime;
    private TextView inspNb;
    private SeekBar exptime;
    private TextView expNb;
    private SeekBar holdtime;
    private SeekBar holdouttime;
    private TextView holdNb;
    private TextView holdOutNb;
    private SeekBar notiftime;
    private TextView notifNb;
    private Switch notif;
    private Switch heart;
    private Switch heartFlash;
    private Switch expert;
    private Button testPurchase;
    private TextView inspTimeTxt;
    private TextView expTimeTxt;
    private TextView holdTimeTxt;
    private TextView holdOutTimeTxt;
    private TextView notifTimeTxt;
    private TextView heartTxt;
    private ImageView imgClose;
    private int defratiovalue = 50;
    private int defsessiontime = 3;
    private int defcyclenumber = 6;
    private int ratiovalue;
    private int sessiontime;
    private int cyclenumber;
    private static Data data;
    private SlidingMenu menuS;
    private ImageView backArrow;

    private Chronometer chronometer;
    private int height;
    private int width;
    private int _yDelta;
    private ViewGroup root;
    private boolean animationrunning = false;
    private boolean evalrunning = false;
    private TextView textview;
    private TextView textCycle;
    private long initialy = 0;
    private boolean rising = true;
    private int cycle = 0;
    private long timeWhenStopped = 0;
    private SeekBar progressBar = null;
    private MyTimer timer;
    private int progress = 0;
    private Animation animation = null;
    private ImageView optionsbutton;
    private ImageView vibrationbutton;
    private ImageView soundbutton;
    private ImageView musicbutton;
    private ImageView daynightbutton;
    private ImageView dotsbutton;
    private ImageView parallel_image_day;
    private boolean vibrate = true;
    private boolean playmusic = true;
    private boolean sound = true;
    private boolean expertMode = false;
    private boolean notifMode = false;
    private boolean day = true;
    private boolean heartmonitor = false;
    private boolean heartmonitorflash = true;

    private long inspval = 0;
    private long expval = 0;
    private long holdval = 0;
    private long holdoutval = 0;
    private long notifvalue = 0;
    private int firstry = 0;
    private long volume = 0;
    private long volumeMusic = 0;
    private SessionHistoryDatabase db;
    private RelativeLayout relativeLayout;
    private Menu menu;

    private int breatheInSoundId = 1;
    private int breatheOutSoundId = 2;
    private int music = 1;

    /* Heart Monitor */
    private static final String TAG = "HeartRateMonitor";
    private static final AtomicBoolean processing = new AtomicBoolean(false);

    private static SurfaceView preview = null;
    private static SurfaceHolder previewHolder = null;
    private static Camera camera = null;

    private static ImageView imageHeart = null;
    private static TextView heartBeatText = null;
    private static TextView heartResultText = null;
    private static boolean heartmonitorpermission;
    private static boolean hasFlash = true;
    private View container1;
    private View container2;
    private View container3;
    private int heartStart = 0;
    private int heartEnd = 0;
    private LinearLayout llParameters, llProfile, llHistory, llHelp, llTutorial, llAbout, llShare;

    private boolean isConverging = false;

    private static int averageIndex = 0;
    private static final int averageArraySize = 4;
    private static final int[] averageArray = new int[averageArraySize];

    public enum TYPE {
        GREY, RED
    }

    public static int maxSessionTime = 29; // session time - 1

    private static MainActivity.TYPE currentType = MainActivity.TYPE.GREY;

    public static MainActivity.TYPE getCurrent() {
        return currentType;
    }

    private static int beatsIndex = 0;
    private static final int beatsArraySize = 3;
    private static final int[] beatsArray = new int[beatsArraySize];
    private static double beats = 0;
    private static long startTime = 0;
    private static long startTimeBPM = 0;
    private LoadPurchasedItem loadPurchasedItem;
    private ProfileManager profileMgr;
    private int currentProfileId = 0;

    private ReviewInfo reviewInfo;

    private static ArrayDeque<Integer> heartBeatQue = new ArrayDeque<>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    // Soundplayer
    // private MediaPlayer mediaplayer;

    /**
     * Called when the activity is first created.
     */
    @SuppressLint({"MissingInflatedId", "SuspiciousIndentation"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ImageView imgMenu = findViewById(R.id.imgMenu);
        llAbout = findViewById(R.id.llAbout);
        llHelp = findViewById(R.id.llHelp);
        llHistory = findViewById(R.id.llHistory);
        llParameters = findViewById(R.id.llParameters);
        llProfile = findViewById(R.id.llProfile);
        llShare = findViewById(R.id.llShare);
        llTutorial = findViewById(R.id.llTutorial);
        setMenuClickListener();
        imgMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startCircularReveal();
            }
        });
        mActivity = this;
        // initialize data parser
        data = Data.getInstance(this.getBaseContext(), true);

        profileMgr = new ProfileManager(this.getBaseContext());
        currentProfileId = profileMgr.getCurrentProfileId();
        imgClose = findViewById(R.id.imgClose);
        // Initialize inApp purchase
        loadPurchasedItem = new LoadPurchasedItem(this.getBaseContext());

        // Load initial data
        firstry = (int) data.ReadDataLong(ProfileManager.firstTry);

      RequestUserPermission requestUserPermission = new RequestUserPermission(this);
       if (!requestUserPermission.verifyPositionPermissions()) {
            Log.d("Permissions", "Permission refused");
        } else
        {
            Log.d("Permissions", "Permission granted");
        }

        imgClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRevealCircle();
            }
        });

        //requestUserPermission.verifyStoragePermissions();

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (powerManager.isPowerSaveMode()) {
                Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.energyMode), Toast.LENGTH_LONG).show();
            }
            String testAnimation = Settings.Global.getString(this.getContentResolver(), Settings.Global.ANIMATOR_DURATION_SCALE);
            if (testAnimation != null) {
                Log.d("Animation", "Enable=" + testAnimation);
                if (Float.valueOf(testAnimation) == 0.0) {
                    Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.noanimation), Toast.LENGTH_LONG).show();
                }
            }
        }

        preview = findViewById(R.id.preview);
        imageHeart = findViewById(R.id.image_heart);

        daynightbutton = findViewById(R.id.daynight);
        container1 = findViewById(R.id.container1);
        container2 = findViewById(R.id.container2);
        container3 = findViewById(R.id.container3);
        parallel_image_day = findViewById(R.id.main_fond_img);

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            @SuppressLint("SoonBlockedPrivateApi") Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ignored) {
        }

        // Load vibrator service
        vibrator = (Vibrator) this.getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);

        // initialize data parser
        data = Data.getInstance(this.getBaseContext());

        // Load initial data
        firstry = (int) data.ReadDataLong(ProfileManager.firstTry);

        ratiovalue = (int) data.ReadDataLong(ProfileManager.ratioValue, profileMgr.getCurrentProfileId());
        if (ratiovalue == 0) {
            ratiovalue = defratiovalue;
        }
        sessiontime = (int) data.ReadDataLong(ProfileManager.sessionTime, profileMgr.getCurrentProfileId());
        if (sessiontime == 0) {
            sessiontime = defsessiontime;
        }
        cyclenumber = (int) data.ReadDataLong(ProfileManager.cycleNumber, profileMgr.getCurrentProfileId());
        if (cyclenumber == 0) {
            cyclenumber = defcyclenumber;
        }

        if (data.ReadDataLong(ProfileManager.vibrate, profileMgr.getCurrentProfileId()) == 0) {
            vibrate = true;
        } else
            vibrate = data.ReadDataLong(ProfileManager.vibrate, profileMgr.getCurrentProfileId()) != 1;

        if (data.ReadDataLong(ProfileManager.sound, profileMgr.getCurrentProfileId()) == 0) {
            sound = true;
        } else
            sound = data.ReadDataLong(ProfileManager.sound, profileMgr.getCurrentProfileId()) != 1;

        notifMode = data.ReadDataLong(ProfileManager.notif, profileMgr.getCurrentProfileId()) != 0;

        expertMode = data.ReadDataLong(ProfileManager.expert, profileMgr.getCurrentProfileId()) != 0;

        if (data.ReadDataLong(ProfileManager.heartMonitor, profileMgr.getCurrentProfileId()) == 0)
            heartmonitor = false;
        else
            if (requestUserPermission.verifyCameraPermissions() == true)

                heartmonitor = data.ReadDataLong(ProfileManager.heartMonitor, profileMgr.getCurrentProfileId()) == 1;
            else
                heartmonitor = false;
        if (data.ReadDataLong(ProfileManager.heartMonitorFlash, profileMgr.getCurrentProfileId()) == 0)
            heartmonitorflash = true;
        else
            heartmonitorflash = data.ReadDataLong(ProfileManager.heartMonitorFlash, profileMgr.getCurrentProfileId()) != 1;

        inspval = data.ReadDataLong(ProfileManager.inspTime, profileMgr.getCurrentProfileId());
        if (inspval == 0) {
            inspval = 50;
        }
        expval = data.ReadDataLong(ProfileManager.expTime, profileMgr.getCurrentProfileId());
        if (expval == 0) {
            expval = 50;
        }

        notifvalue = data.ReadDataLong(ProfileManager.notifValue, profileMgr.getCurrentProfileId());
        if (notifvalue == 0) {
            notifvalue = 4;
        }
        if (data.ReadDataLong(ProfileManager.dayNight, profileMgr.getCurrentProfileId()) == 0) {
            day = true;
        } else {
            day = data.ReadDataLong(ProfileManager.dayNight, profileMgr.getCurrentProfileId()) != 1;
        }

        holdval = data.ReadDataLong(ProfileManager.holdTime, profileMgr.getCurrentProfileId());
        holdoutval = data.ReadDataLong(ProfileManager.holdOutTime, profileMgr.getCurrentProfileId());


        // Store app id
        String appId = getString(R.string.appid);
        Log.d("AppId", "AppId:" + (long) Integer.parseInt(appId));
        data.StoreDataLong("appId", (long) Integer.parseInt(appId));


        if (!heartmonitor) imageHeart.setVisibility(View.INVISIBLE);

        breatheInSoundId = (int) data.ReadDataLong(ProfileManager.breatheInSound, profileMgr.getCurrentProfileId());
        if (breatheInSoundId == 0) breatheInSoundId = 1;

        breatheOutSoundId = (int) data.ReadDataLong(ProfileManager.breatheOutSound, profileMgr.getCurrentProfileId());
        if (breatheOutSoundId == 0) breatheOutSoundId = 2;

        music = (int) data.ReadDataLong(ProfileManager.musicSelected, profileMgr.getCurrentProfileId());
        if (music == 0) music = 1;

        if (data.ReadDataLong(ProfileManager.playmusic, profileMgr.getCurrentProfileId()) == 0) {
            playmusic = true;
        } else {
            playmusic = data.ReadDataLong(ProfileManager.playmusic, profileMgr.getCurrentProfileId()) != 1;
        }

        volume = data.ReadDataLong(ProfileManager.volume, profileMgr.getCurrentProfileId());
        if (volume == 0) volume = 50;

        volumeMusic = data.ReadDataLong(ProfileManager.volumeMusic, profileMgr.getCurrentProfileId());
        if (volumeMusic == 0) volumeMusic = 50;

        Log.d("Reading Sound:", "BreatheIn=" + breatheInSoundId + " BreatheOut=" + breatheOutSoundId);
        data.StoreDataLong(ProfileManager.breatheInSound, (long) breatheInSoundId, profileMgr.getCurrentProfileId());
        data.StoreDataLong(ProfileManager.breatheOutSound, (long) breatheOutSoundId, profileMgr.getCurrentProfileId());

        // set background image

        relativeLayout = findViewById(R.id.relativelayout1);
        Resources res = getResources();
        Drawable drawable;
        if (!day) {

            drawable = res.getDrawable(R.drawable.moonland1);
            parallel_image_day.setVisibility(View.GONE);


            relativeLayout.setBackground(drawable);
        }

        // Set top bar color

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(this.getResources().getColor(R.color.colorApp2));
        }

        image = findViewById(R.id.imageprinc);

        createSlidingMenu();

        sessionNb = findViewById(R.id.sessionNb);
        cycleNb = findViewById(R.id.cycleNb);
        ratioNb = findViewById(R.id.ratioNb);
        inspNb = findViewById(R.id.inspNb);
        expNb = findViewById(R.id.expNb);
        holdNb = findViewById(R.id.holdNb);
        holdOutNb = findViewById(R.id.holdOutNb);
        notifNb = findViewById(R.id.notifNb);
        inspTimeTxt = findViewById(R.id.inspTimeTxt);
        expTimeTxt = findViewById(R.id.expTimeTxt);
        holdTimeTxt = findViewById(R.id.holdTimeTxt);
        holdOutTimeTxt = findViewById(R.id.holdOutTimeTxt);
        notifTimeTxt = findViewById(R.id.notifTimeTxt);
        textview = findViewById(R.id.cycletext);
        textCycle = findViewById(R.id.ratioTxt2);
        heartBeatText = findViewById(R.id.heartBeatText);
        heartResultText = findViewById(R.id.heartResultText);
        heartBeatText.setVisibility(View.INVISIBLE);
        heartResultText.setVisibility(View.INVISIBLE);
        updateCycle(0);

        // Reset options if premium subscription has expired
        if (!loadPurchasedItem.purchase.isPremiumPurchased()) {
            if (!loadPurchasedItem.purchase.isExpertPurchased()) {
                expertMode = false;
                data.StoreDataLong(ProfileManager.expert, Long.valueOf(0), profileMgr.getCurrentProfileId());
                //   setExpertMode(false);
            }
            if (!loadPurchasedItem.purchase.isNotificationPurchased()) {
                notifMode = false;
                data.StoreDataLong(ProfileManager.notif, Long.valueOf(0), profileMgr.getCurrentProfileId());
                // setNotifMode(false);
            }
            if (!loadPurchasedItem.purchase.isMonitorPurchased()) {
                heartmonitor = false;
                data.StoreDataLong(ProfileManager.heartMonitor, Long.valueOf(0), profileMgr.getCurrentProfileId());
                //   heart = findViewById(R.id.heartSwitch);
                //   heart.setChecked(heartmonitor);
                //   heartFlash = findViewById(R.id.heartSwitch2);
                //   heartFlash.setChecked(heartmonitor);
            }
        }


        chronometer = findViewById(R.id.chronometer1);

        startbutton = findViewById(R.id.start);
        db = new SessionHistoryDatabase(this);

        startbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Main page:", "start button pressed, stopanimation =" + !startbutton.isChecked());

                if (startbutton.isChecked()) {

                    deleteAllNotifications();
                    heartResultText.setText(0 + "/" + 0);
                    //createCamera();
                    startHeartBeat();
                    chronostart();
                    cycle = 0;
                    updateCycle(cycle);
                    animationrunning = true;
                    animation.startAnimation();

                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


                } else {

                    Log.e("cycle2", String.valueOf(cyclenumber));
                    Log.e("ratio2", String.valueOf(ratiovalue));
                    Log.e("ratio2", String.valueOf(sessiontime));

                    animation.forceStop();
                    animationrunning = false;
                    setimageinplace();

                    stopHeartBeat();

                    SessionHistoryHelper.addSession();

                    db.addSession(SessionHistoryHelper.getStartTime(), SessionHistoryHelper.getEndTime(), SessionHistoryHelper.getDuration(), SessionHistoryHelper.getCycle(), SessionHistoryHelper.getRatio(), heartStart, heartEnd);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    createNotification();
                }
            }
        });

        sessionselector = findViewById(R.id.timeBar);
        ImageView btnBack = findViewById(R.id.btnBack);
        ImageView menu2 = findViewById(R.id.imgMenu2);
        ImageView btnCloseLeft = findViewById(R.id.btnCloseLeft);
        sessionselector.setMax(maxSessionTime);
        sessionNb.setText(String.valueOf(sessiontime));
        sessionselector.setProgress(sessiontime - 1);
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menuS.toggle();
            }
        });

        menu2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startCircularReveal2();
            }
        });

        btnCloseLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRevealCyrcle2();
            }
        });

        LinearLayout llSoundConfig = findViewById(R.id.llSoundConfig);
        LinearLayout llResetParam = findViewById(R.id.llResetParam);
        LinearLayout llMusicConfig = findViewById(R.id.llMusicConfig);

        llSoundConfig.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SoundActivity.class);
                intent.putExtra("breatheInSound", breatheInSoundId);
                intent.putExtra("breatheOutSound", breatheOutSoundId);
                intent.putExtra("volume", volume);
                startActivityForResult(intent, 0);
                hideRevealCyrcle2();
            }
        });


        llMusicConfig.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MusicActivity.class);
                intent.putExtra("musicSelected", music);
                intent.putExtra("volume", volumeMusic);
                startActivityForResult(intent, 1);
                hideRevealCyrcle2();
            }
        });

        llResetParam.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                resetdata();
                hideRevealCyrcle2();
            }
        });

        sessionselector.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sessiontime = progress + 1;
                sessionNb.setText(String.valueOf(sessiontime));

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("Main page:", "sessiontime changed =" + sessiontime);
                data.StoreDataLong(ProfileManager.sessionTime, Long.valueOf(sessiontime), profileMgr.getCurrentProfileId());
                animation.setSessiontime(sessiontime);
            }
        });

        respperminselector = findViewById(R.id.cycleBar);
        respperminselector.setMax(19);
        cycleNb.setText(String.valueOf(cyclenumber));

        respperminselector.setProgress(cyclenumber - 1);
        respperminselector.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cyclenumber = progress + 1;
                cycleNb.setText(String.valueOf(cyclenumber));

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("Main page:", "cyclenumber changed =" + cyclenumber);
                data.StoreDataLong(ProfileManager.cycleNumber, Long.valueOf(cyclenumber), profileMgr.getCurrentProfileId());
                animation.setCyclenumber(cyclenumber);
            }
        });

        ratio = findViewById(R.id.ratiobar);
        ratio.setMax(100);
        ratio.setProgress(ratiovalue);
        ratioNb.setText(ratiovalue + "/" + (100 - ratiovalue));

        ratio.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ratiovalue = progress;
                ratioNb.setText(ratiovalue + "/" + (100 - ratiovalue));

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("Main page:", "ratio changed =" + ratiovalue);
                data.StoreDataLong(ProfileManager.ratioValue, Long.valueOf(ratiovalue), profileMgr.getCurrentProfileId());
                animation.setRatiovalue(ratiovalue);
            }
        });


        insptime = findViewById(R.id.inspbar);
        insptime.setMax(190);
        insptime.setProgress((int) inspval - 10);
        inspNb.setText(String.valueOf((float) inspval / 10));

        insptime.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                inspval = progress + 10;
                inspNb.setText(String.valueOf((float) inspval / 10));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("Main page:", "insptime changed =" + insptime);
                data.StoreDataLong(ProfileManager.inspTime, Long.valueOf(inspval), profileMgr.getCurrentProfileId());
                setExpertMode(expertMode);
                animation.setExpertValues(expertMode, inspval, expval, holdval, holdoutval);

            }
        });


        exptime = findViewById(R.id.expbar);
        exptime.setMax(190);

        exptime.setProgress((int) expval - 10);
        expNb.setText(String.valueOf((float) expval / 10));

        exptime.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                expval = progress + 10;
                expNb.setText(String.valueOf((float) expval / 10));

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("Main page:", "exptime changed =" + expval);
                data.StoreDataLong(ProfileManager.expTime, Long.valueOf(expval), profileMgr.getCurrentProfileId());
                setExpertMode(expertMode);
                animation.setExpertValues(expertMode, inspval, expval, holdval, holdoutval);
            }
        });

        holdtime = findViewById(R.id.holdbar);
        holdtime.setMax(200);

        holdtime.setProgress((int) holdval);
        holdNb.setText(String.valueOf((float) holdval / 10));

        holdtime.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                holdval = progress;
                holdNb.setText(String.valueOf((float) holdval / 10));

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("Main page:", "holdtime changed =" + holdval);
                data.StoreDataLong(ProfileManager.holdTime, Long.valueOf(holdval), profileMgr.getCurrentProfileId());
                setExpertMode(expertMode);
                animation.setExpertValues(expertMode, inspval, expval, holdval, holdoutval);
            }
        });


        holdouttime = findViewById(R.id.holdOutbar);
        holdouttime.setMax(200);

        holdouttime.setProgress((int) holdval);
        holdOutNb.setText(String.valueOf((float) holdoutval / 10));

        holdouttime.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                holdoutval = progress;
                holdOutNb.setText(String.valueOf((float) holdoutval / 10));

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("Main page:", "holdouttime changed =" + holdoutval);
                data.StoreDataLong(ProfileManager.holdOutTime, Long.valueOf(holdoutval), profileMgr.getCurrentProfileId());
                setExpertMode(expertMode);
                animation.setExpertValues(expertMode, inspval, expval, holdval, holdoutval);
            }
        });

        notiftime = findViewById(R.id.notifbar);
        notiftime.setMax(23);
        notiftime.setProgress((int) notifvalue - 1);
        notifNb.setText(String.valueOf(notifvalue));

        notiftime.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                notifvalue = progress + 1;
                notifNb.setText(String.valueOf(notifvalue));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("Main page:", "notifvalue changed =" + notifvalue);
                data.StoreDataLong(ProfileManager.notifValue, Long.valueOf(notifvalue), profileMgr.getCurrentProfileId());
            }
        });


        expert = findViewById(R.id.expertSwitch);
        expert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (data.isDebugMode() == true)
                    Toast.makeText(getBaseContext(), "Premium=" + loadPurchasedItem.purchase.isPremiumPurchased() + " Expert=" + loadPurchasedItem.purchase.isExpertPurchased(), Toast.LENGTH_LONG).show();

                if (loadPurchasedItem.purchase.isPremiumPurchased() || loadPurchasedItem.purchase.isExpertPurchased()) {

                    if (data.isDebugMode() == true)
                        Toast.makeText(getBaseContext(), "Switch=" + ((Switch) buttonView).isChecked(), Toast.LENGTH_LONG).show();
                    // is chkIos checked?
                    if (((Switch) buttonView).isChecked()) {

                        expertMode = true;
                        data.StoreDataLong(ProfileManager.expert, Long.valueOf(1), profileMgr.getCurrentProfileId());

                    } else {
                        expertMode = false;
                        data.StoreDataLong(ProfileManager.expert, Long.valueOf(0), profileMgr.getCurrentProfileId());
                    }
                    if (data.isDebugMode() == true)
                        Toast.makeText(getBaseContext(), "Set ExpertMode=" + expertMode, Toast.LENGTH_LONG).show();
                    // set mode
                    setExpertMode(expertMode);
                } else {
                    expert.setChecked(false);
                    startPurchaseActivity();
                }
            }

        });

        notif = findViewById(R.id.notifSwitch);
        notif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (data.isDebugMode() == true)
                    Toast.makeText(getBaseContext(), "Premium=" + loadPurchasedItem.purchase.isPremiumPurchased() + " Notif=" + loadPurchasedItem.purchase.isNotificationPurchased(), Toast.LENGTH_LONG).show();

                if (loadPurchasedItem.purchase.isPremiumPurchased() || loadPurchasedItem.purchase.isNotificationPurchased()) {

                    // is chkIos checked?
                    if (((Switch) buttonView).isChecked()) {

                        notifMode = true;
                        data.StoreDataLong(ProfileManager.notif, Long.valueOf(1), profileMgr.getCurrentProfileId());

                    } else {
                        notifMode = false;
                        data.StoreDataLong(ProfileManager.notif, Long.valueOf(0), profileMgr.getCurrentProfileId());
                        deleteAllNotifications();
                    }

                    // set mode
                    setNotifMode(notifMode);
                } else {
                    notif.setChecked(false);
                    startPurchaseActivity();
                }
            }

        });

        heart = findViewById(R.id.heartSwitch);
        heart.setChecked(heartmonitor);
        heartFlash = findViewById(R.id.heartSwitch2);
        heartFlash.setChecked(heartmonitorflash);

        if (!hasFlash) {
            heartFlash.setEnabled(false);
            heartTxt = findViewById(R.id.heartTxt2);
            heartTxt.setText(getResources().getString(R.string.heartTextNoFlash));
            heartTxt.setEnabled(false);
            heartTxt = findViewById(R.id.heartTxt);
            heartTxt.setEnabled(false);
        }


        heart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (data.isDebugMode() == true)
                    Toast.makeText(getBaseContext(), "Premium=" + loadPurchasedItem.purchase.isPremiumPurchased() + " Monitor=" + loadPurchasedItem.purchase.isMonitorPurchased(), Toast.LENGTH_LONG).show();

                if (loadPurchasedItem.purchase.isPremiumPurchased() || loadPurchasedItem.purchase.isMonitorPurchased()) {

                    // check for Camera permission
                    if (heartmonitorpermission == false) {
                        if (requestUserPermission.verifyCameraPermissions() == false) {
                            heartmonitor = false;
                            heartmonitorpermission = false;
                            data.StoreDataLong(ProfileManager.heartMonitor, Long.valueOf(2), profileMgr.getCurrentProfileId());
                            heart.setChecked(false);
                            Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.camerapermission), Toast.LENGTH_LONG).show();
                        } else {
                            heartmonitorpermission = true;
                            // if device has no flash
                            if (!hasFlash()) {
                                // force to true as we allow monitor without flash now
                                hasFlash = true;
                                heartmonitor = true;
                                data.StoreDataLong(ProfileManager.heartMonitor, Long.valueOf(2), profileMgr.getCurrentProfileId());
                            }
                        }
                        //heart.setChecked(false);
                    }
                    if (heartmonitorpermission == true) {
                        if (heart.isChecked()) {

                            heartmonitor = true;
                            heartTxt = findViewById(R.id.heartTxt2);
                            heartTxt.setEnabled(true);
                            heartFlash = findViewById(R.id.heartSwitch2);
                            heartFlash.setClickable(true);
                            data.StoreDataLong(ProfileManager.heartMonitor, Long.valueOf(1), profileMgr.getCurrentProfileId());
                            setHeartVisibility(View.VISIBLE);

                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);

                        } else {
                            stopHeartBeat();
                            heartmonitor = false;
                            heartTxt = findViewById(R.id.heartTxt2);
                            heartTxt.setEnabled(false);
                            heartFlash = findViewById(R.id.heartSwitch2);
                            heartFlash.setClickable(false);
                            data.StoreDataLong(ProfileManager.heartMonitor, Long.valueOf(2), profileMgr.getCurrentProfileId());
                            setHeartVisibility(View.INVISIBLE);

                        }
                    }
                } else {
                    heart.setChecked(false);
                    startPurchaseActivity();

                }
            }

        });

        heartFlash = findViewById(R.id.heartSwitch2);

        heartFlash.setChecked(heartmonitorflash);
        heartFlash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (((Switch) buttonView).isChecked()) {
                    heartmonitorflash = false;
                    data.StoreDataLong(ProfileManager.heartMonitorFlash, Long.valueOf(2), profileMgr.getCurrentProfileId());
                } else {
                    heartmonitorflash = true;
                    data.StoreDataLong(ProfileManager.heartMonitorFlash, Long.valueOf(1), profileMgr.getCurrentProfileId());
                }
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });


        // Vibration
        vibrationbutton = findViewById(R.id.vibrate);

        // test if the device has a vibrator
        if (vibrator.hasVibrator()) {
            vibrationbutton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    if (vibrate) {
                        vibrate = false;
                        data.StoreDataLong(ProfileManager.vibrate, Long.valueOf(1), profileMgr.getCurrentProfileId());
                        vibrationbutton.setImageResource(R.drawable.novibrate);

                    } else {
                        vibrate = true;
                        data.StoreDataLong(ProfileManager.vibrate, Long.valueOf(2), profileMgr.getCurrentProfileId());
                        vibrationbutton.setImageResource(R.drawable.vibrate);
                    }
                    animation.setVibration(vibrate);
                }

            });
        } else {
            vibrationbutton.setVisibility(View.GONE);
            container2.setVisibility(View.GONE);
        }
        // Music
        musicbutton = findViewById(R.id.music);

        musicbutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (playmusic) {
                    playmusic = false;
                    data.StoreDataLong(ProfileManager.playmusic, Long.valueOf(1), profileMgr.getCurrentProfileId());
                    animation.playMusic(false);
                    musicbutton.setImageResource(R.drawable.mute_music);

                } else {
                    playmusic = true;
                    data.StoreDataLong(ProfileManager.playmusic, Long.valueOf(2), profileMgr.getCurrentProfileId());
                    animation.playMusic(true);
                    musicbutton.setImageResource(R.drawable.music);

                }
                animation.setMusic(music);
            }

        });
        // Sound
        soundbutton = findViewById(R.id.speaker);

        soundbutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {


                if (sound) {
                    sound = false;
                    data.StoreDataLong(ProfileManager.sound, Long.valueOf(1), profileMgr.getCurrentProfileId());
                    soundbutton.setImageResource(R.drawable.mute);

                } else {
                    sound = true;
                    data.StoreDataLong(ProfileManager.sound, Long.valueOf(2), profileMgr.getCurrentProfileId());
                    soundbutton.setImageResource(R.drawable.speaker);

                }
                animation.setSound(sound);
            }

        });

        // Day-Night


        daynightbutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                relativeLayout = findViewById(R.id.relativelayout1);
                Resources res = getResources();
                Drawable drawable;

                if (day) {
                    day = false;

                    animation.setDay(day);
                    data.StoreDataLong(ProfileManager.dayNight, Long.valueOf(1), profileMgr.getCurrentProfileId());
                    daynightbutton.setImageResource(R.drawable.ic_moon);
                    daynightbutton.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.color2), android.graphics.PorterDuff.Mode.MULTIPLY);
                    container1.setBackground(getResources().getDrawable(R.drawable.cyrcle_shape_blue));
                    textview.setTextColor(Color.WHITE);
                    textCycle.setTextColor(Color.WHITE);
                    chronometer.setTextColor(Color.WHITE);
                    progressBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    progressBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

                    drawable = res.getDrawable(R.drawable.moonland1);
                    parallel_image_day.setVisibility(View.GONE);

                    relativeLayout.setBackground(drawable);
                    setimageinplace();

                } else {
                    day = true;
                    animation.setDay(day);
                    data.StoreDataLong(ProfileManager.dayNight, Long.valueOf(0), profileMgr.getCurrentProfileId());
                    daynightbutton.setImageResource(R.drawable.ic_moon);
                    daynightbutton.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.color2), android.graphics.PorterDuff.Mode.MULTIPLY);
                    container1.setBackground(getResources().getDrawable(R.drawable.cyrcle_shape_blue));
                    textview.setTextColor(Color.BLACK);
                    textCycle.setTextColor(Color.BLACK);
                    chronometer.setTextColor(Color.BLACK);
                    progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorApp), PorterDuff.Mode.MULTIPLY);
                    progressBar.getThumb().setColorFilter(getResources().getColor(R.color.colorApp), PorterDuff.Mode.SRC_IN);


                    drawable = res.getDrawable(R.drawable.mainfond);
                    parallel_image_day.setVisibility(View.VISIBLE);


                    relativeLayout.setBackground(drawable);
                    setimageinplace();
                }

            }

        });

        // For launching purchase activity for testing purposes
     /*   testPurchase = findViewById(R.id.testPurchase);
        testPurchase.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startPurchaseActivity();
            }
        }); */

        // dotsbutton (ONLY IN 4.4)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {


            dotsbutton = findViewById(R.id.dotsimage);
            dotsbutton.setVisibility(ImageView.VISIBLE);
            dotsbutton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    openOptionsMenu();
                }

            });
        }


        startbutton.setChecked(false);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;

        // Set image based on status
        setMusicImage();

        if (firstry != 1) {
            music = 2;
            volumeMusic = 60;
            data.StoreDataLong(ProfileManager.musicSelected, (long) music, profileMgr.getCurrentProfileId());
            data.StoreDataLong(ProfileManager.volumeMusic, volumeMusic, profileMgr.getCurrentProfileId());
            showHint();
        }

        // Create animations
        animation = new Animation(getApplicationContext(), root, height, sessiontime, cyclenumber, ratiovalue, image, vibrator, vibrate, sound, expertMode, inspval, expval, holdval, holdoutval, volume, volumeMusic, breatheInSoundId, breatheOutSoundId, music, day ,playmusic);

        root = findViewById(R.id.relativelayout1);

        setimageinplace();

        animation.setCallbackListener(new CallbackListener() {
            @Override
            public void onStart() {
                Log.d("Main page:", "onStart!");
                chronostart();
                progress = 0;
                cycle = 0;


                progressBar.setProgress(0);
                //progressBar.setVisibility(View.INVISIBLE);
                timer = new MyTimer(sessiontime * 60000, sessiontime * 600);
                timer.start();
                if (heartmonitor) {
                    heartStart = 0;
                    heartEnd = 0;
                    heartResultText.setVisibility(View.INVISIBLE);
                }

                startTimeBPM = System.currentTimeMillis();
            }

            @Override
            public void onStop() {
                Log.d("Main page:", "onStop!");
                chronostop();
                // chronometer.setText("00:00");
                animationrunning = false;
                animation.setDay(day);
                setimageinplace();
                stopHeartBeat();
                startbutton.setChecked(false);
                cycle++;
                updateCycle(cycle);
                if (timer != null) timer.cancel();
                if (heartmonitor) {
                    heartResultText.setVisibility(View.VISIBLE);
                    heartBeatText.setVisibility(View.INVISIBLE);
                    imageHeart.setImageResource(R.drawable.heartlightgrey);
                }
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                createNotification();

                // ask for review
                askForReview();
            }

            @Override
            public void onPause() {
                Log.d("Main page:", "onPause!");
                chronopause();
            }

            @Override
            public void onResume() {
                Log.d("Main page:", "onResume!");
                chronoresume();
                cycle++;
                updateCycle(cycle);

                startTime = System.currentTimeMillis();
                Log.i("Time", "startTime reset!");
            }

            @Override
            public void onAbort() {
                Log.d("Main page:", "onAbort!");
                chronopause();
                // chronometer.setText("00:00");
                animationrunning = false;
                setimageinplace();
                startbutton.setChecked(false);
                if (timer != null) timer.cancel();

                if (heartmonitor) {
                    stopHeartBeat();
                    heartBeatText.setVisibility(View.INVISIBLE);
                    heartResultText.setVisibility(View.VISIBLE);
                }
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }

            @Override
            public void onDemoEnd() {

                startbutton.setClickable(true);
                optionsbutton.setClickable(true);
                menuS.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                root.getBackground().setAlpha(255);
            }
        });

        // Configure progress bar
        progressBar = findViewById(R.id.progressBar);
        progressBar.setEnabled(false);
        progressBar.setMax(100);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF75aaeb, PorterDuff.Mode.SRC_OVER);
        progressBar.getProgressDrawable().setColorFilter(0xFF75aaeb, PorterDuff.Mode.SRC_IN);
        progressBar.setProgress(0);

        // Set image based on status
        setVibrateImage();

        // Set image based on status
        setSoundImage();

        // Set image based on status
        setNightDayImage();

        // Set expertMode
        setExpertMode(expertMode);

        // Set notifMode
        setNotifMode(notifMode);

        // Clear active notifications
        deleteAllNotifications();

        createCamera();
        stopHeartBeat();



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


    }

    private void setMenuClickListener() {
        llParameters.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRevealCircle();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        menuS.toggle();
                    }
                }, 250);

                // Test to display the purchase in debug mode only
                if (data.isDebugMode() == true)
                    loadPurchasedItem = new LoadPurchasedItem(getApplicationContext());

            }
        });

        llProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (data.isDebugMode() == true)
                    Toast.makeText(getBaseContext(), "Premium=" + loadPurchasedItem.purchase.isPremiumPurchased() + " Profile=" + loadPurchasedItem.purchase.isProfilePurchased(), Toast.LENGTH_LONG).show();

                if (loadPurchasedItem.purchase.isPremiumPurchased() || loadPurchasedItem.purchase.isProfilePurchased()) {
                    hideRevealCircle();
                    openProfileSelectionDialog();
                } else {
                    hideRevealCircle();
                    startPurchaseActivity();
                }
            }
        });

        llHistory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRevealCircle();
                startActivity(new Intent(MainActivity.this, SessionHistoryActivity.class));
            }
        });

        final CustomDialog mAlert = new CustomDialog();

        llHelp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRevealCircle();
                mAlert.setTitle(getResources().getString(R.string.whatis));
                mAlert.setMsj(getResources().getString(R.string.help));
                mAlert.setContext(MainActivity.this);
                mAlert.customAlert(mActivity, mActivity);

            }
        });

        llTutorial.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRevealCircle();
                showHint();
            }
        });
        final CustomDialog mDialog = new CustomDialog();

        llAbout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRevealCircle();
                mDialog.setTitle(getResources().getString(R.string.aboutlabel));
                mDialog.setMsj(getResources().getString(R.string.application_custom_name) + "\n" + getResources().getString(R.string.version_number) + "\n" + getResources().getString(R.string.author) + "\n" + "\n" + getResources().getString(R.string.thanks));
                mDialog.setContext(MainActivity.this);
                mDialog.setDebug(data.isDebugMode());
                mDialog.customDialog(mActivity, getApplicationContext());

            }
        });
        llShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRevealCircle();
                shareapp();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // Get view positions;
        getViewPositions();


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (!animationrunning) {
            Log.d("Main page:", "Config Change! " + getResources().getConfiguration().orientation);

            relativeLayout = findViewById(R.id.relativelayout1);
            Resources res = getResources();
            Drawable drawable;

            if (!day) {

                drawable = res.getDrawable(R.drawable.moonland1);
                parallel_image_day.setVisibility(View.GONE);

                relativeLayout.setBackground(drawable);
                setimageinplace();
            }

            getViewPositions();
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            height = metrics.heightPixels;
            width = metrics.widthPixels;
            animation.setHeight(height);
            setimageinplace();
            Log.d("Main page:", "setimageinplace");
        }
        //updateMenuSize();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d("Main page:", "RequestCode" + requestCode);
        if (requestCode == 0) {
            // read temporary stored data
            breatheInSoundId = (int) data.ReadDataLong("breatheInSound");
            breatheOutSoundId = (int) data.ReadDataLong("breatheOutSound");
            volume = (int) data.ReadDataLong("volume");

            animation.setBreatheInSoundId(breatheInSoundId);
            animation.setBreatheOutSoundId(breatheOutSoundId);
            animation.setVolume(volume);

            // Store it to the correct profile
            data.StoreDataLong(ProfileManager.breatheInSound, (long) breatheInSoundId, profileMgr.getCurrentProfileId());
            data.StoreDataLong(ProfileManager.breatheOutSound, (long) breatheOutSoundId, profileMgr.getCurrentProfileId());
            data.StoreDataLong(ProfileManager.musicSelected, (long) music, profileMgr.getCurrentProfileId());
            data.StoreDataLong(ProfileManager.volume, volume, profileMgr.getCurrentProfileId());

            Log.d("Sound:", "BreatheIn=" + breatheInSoundId + " BreatheOut=" + breatheOutSoundId);
        } else if (requestCode == 1) {
            // read temporary stored data
            music = (int) data.ReadDataLong("musicSelected");
            volumeMusic = (int) data.ReadDataLong("volumeMusic");
            animation.setMusic(music);
            animation.setVolumeMusic(volumeMusic);

            // Store it to the correct profile
            data.StoreDataLong(ProfileManager.musicSelected, (long) music, profileMgr.getCurrentProfileId());
            data.StoreDataLong(ProfileManager.volumeMusic, volumeMusic, profileMgr.getCurrentProfileId());

            Log.d("Music:", "musicSelected=" + music);
        } else {

            Log.d("Purchasing", "Purchase activity closed code:" + resultCode);
            if (resultCode != PurchaseActivity.CANCELLED || resultCode != PurchaseActivity.BILLING_ERROR) {
                LoadPurchasedItem loadPurchasedItem = new LoadPurchasedItem(this.getBaseContext());
            }

            //Restart App to load purchasing
            if (resultCode == PurchaseActivity.BILLING_SUCCESS) {
                Intent intent2 = getIntent();

                finish();
                startActivity(intent2);
            }
        }
    }


    public void startCircularReveal() {
        final View view = findViewById(R.id.view);
        final View startView = findViewById(R.id.mainContent);
        final ConstraintLayout background = findViewById(R.id.background);
        final int cx = (int) ((startView.getLeft() - view.getMeasuredWidth() * 0.1) / 2);
        final int cy = (int) ((startView.getTop() - view.getMeasuredHeight() * 0.5) / 2);
        //  view.setBackgroundColor(Color.parseColor("#6FA6FF"));
        int finalRadius = Math.max(cy, view.getHeight() - cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, (float) (view.getMeasuredWidth() * 1.3));

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setDuration(500);
        anim.start();
        background.setVisibility(View.VISIBLE);

        background.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRevealCircle();
            }
        });
    }

    public void startCircularReveal2() {
        final View view = findViewById(R.id.view2);
        final View startView = findViewById(R.id.mainContent2);
        final ConstraintLayout background = findViewById(R.id.background2);
        final int cx = (int) ((startView.getRight()));
        final int cy = (int) ((startView.getTop()));
        //  view.setBackgroundColor(Color.parseColor("#6FA6FF"));
        int finalRadius = Math.max(cy, view.getHeight() - cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, (float) (view.getMeasuredWidth() * 1.3));

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setDuration(500);
        anim.start();
        background.setVisibility(View.VISIBLE);

        background.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRevealCircle();
            }
        });
    }

    private void hideRevealCyrcle2() {
        final View view = findViewById(R.id.view2);
        final View startView = findViewById(R.id.mainContent2);
        final ConstraintLayout background = findViewById(R.id.background2);
        final int cx = (int) ((startView.getRight()));
        final int cy = (int) ((startView.getTop()));
        //  view.setBackgroundColor(Color.parseColor("#6FA6FF"));
        int finalRadius = Math.max(cy, view.getHeight() - cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, (float) (view.getMeasuredWidth() * 1.3), startView.getLeft() / 25);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                view.setVisibility(View.INVISIBLE);
            }
        });
        anim.setDuration(500);
        anim.start();
        background.setVisibility(View.INVISIBLE);

    }


    private void hideRevealCircle() {
        final View view = findViewById(R.id.view);
        final View startView = findViewById(R.id.mainContent);
        final ConstraintLayout background = findViewById(R.id.background);
        final int cx = (int) ((startView.getLeft() - view.getMeasuredWidth() * 0.1) / 2);
        final int cy = (int) ((startView.getTop() - view.getMeasuredHeight() * 0.5) / 2);
        //  view.setBackgroundColor(Color.parseColor("#6FA6FF"));
        int finalRadius = Math.max(cy, view.getHeight() - cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, (float) (view.getMeasuredWidth() * 1.3), startView.getLeft() / 25);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                view.setVisibility(View.INVISIBLE);
            }
        });
        anim.setDuration(500);
        anim.start();
        background.setVisibility(View.INVISIBLE);

    }

    private void updateCycle(int counter) {
        textview.setText("Cycles " + String.valueOf(counter));
    }

    private void createSlidingMenu() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int menuwidth;

        menuwidth = width - (width / 4);


        menuS = new SlidingMenu(this);

        menuS.setMode(SlidingMenu.RIGHT);
        menuS.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        menuS.setBehindOffset(width);
        menuS.setBehindWidth(width);
        menuS.setFadeDegree(0.35f);
        menuS.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menuS.setMenu(R.layout.slideout_main_menu);

        // getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_circleplus));


        //Set values in menu

    }

    private void updateMenuSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int menuwidth = width - (width / 4);
        menuS.setBehindOffset(menuwidth);
        menuS.setBehindWidth(menuwidth);
    }

    private void setimageinplace() {
        image.setImageDrawable(null);

        View v = new ImageView(getBaseContext());
        image = new ImageView(v.getContext());
        if (day) image.setImageDrawable(v.getResources().getDrawable(R.drawable.goutte3));
        else image.setImageDrawable(v.getResources().getDrawable(R.drawable.moon41));

        animation.setImage(image);

        LayoutParams params = new LayoutParams(width / 10, width / 10);
        int bottom = (int) (height * .6);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        // params.leftMargin = (int) (metrics.widthPixels / 2 - 30);
        params.topMargin = bottom;
        image.setLayoutParams(params);
        root.addView(image);
        image.setOnTouchListener(this);
        Log.d("Main page:", "Set image to:" + bottom);
        // createAnimations();

    }

    private void shareapp() {
        // create the send intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        // set the type
        shareIntent.setType("text/plain");

        // add a subject
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.application_custom_name));

        // build the body of the message to be shared
        String shareMessage = getResources().getString(R.string.sharetext);

        // add the message
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

        // start the chooser for sharing
        startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.shareusing)));
    }

    private void resetdata() {
        ratiovalue = defratiovalue;
        ratio.setProgress(ratiovalue);
        data.StoreDataLong(ProfileManager.ratioValue, Long.valueOf(ratiovalue), profileMgr.getCurrentProfileId());
        animation.setRatiovalue(ratiovalue);
        sessiontime = defsessiontime;
        sessionselector.setProgress(sessiontime - 1);
        data.StoreDataLong(ProfileManager.sessionTime, Long.valueOf(sessiontime), profileMgr.getCurrentProfileId());
        animation.setSessiontime(sessiontime);
        cyclenumber = defcyclenumber;
        respperminselector.setProgress(cyclenumber - 1);
        data.StoreDataLong(ProfileManager.cycleNumber, Long.valueOf(cyclenumber), profileMgr.getCurrentProfileId());
        animation.setCyclenumber(cyclenumber);
        inspval = 50;
        insptime.setProgress((int) inspval - 10);
        data.StoreDataLong(ProfileManager.inspTime, Long.valueOf(inspval), profileMgr.getCurrentProfileId());
        expval = 50;
        exptime.setProgress((int) expval - 10);
        data.StoreDataLong(ProfileManager.expTime, Long.valueOf(expval), profileMgr.getCurrentProfileId());
        holdval = 50;
        holdtime.setProgress((int) holdval - 10);
        holdoutval = 50;
        holdouttime.setProgress((int) holdoutval - 10);
        data.StoreDataLong(ProfileManager.holdTime, Long.valueOf(holdval), profileMgr.getCurrentProfileId());
        data.StoreDataLong(ProfileManager.holdOutTime, Long.valueOf(holdoutval), profileMgr.getCurrentProfileId());
        expertMode = false;
        data.StoreDataLong(ProfileManager.expert, Long.valueOf(0), profileMgr.getCurrentProfileId());
        setExpertMode(false);

        // For testing purpose only!!! Remove in prod
        //startPurchaseActivity();
    }

    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            inflater.inflate(R.menu.legacypopupmenu, menu);
        else inflater.inflate(R.menu.popupmenu, menu);

        this.menu = menu;


        return true;
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        Log.d("Relaxation", "On Touch");
        final int Y = (int) event.getRawY();
        long y = view.getTop();

        if (!animationrunning) {

            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                    Log.d("Relaxation", "ACTION_DOWN");
                    LayoutParams lParams = (LayoutParams) view.getLayoutParams();
                    _yDelta = Y - lParams.topMargin;
                    updateCycle(count);
                    progressBar.setProgress(0);
                    break;

                case MotionEvent.ACTION_UP:
                    Log.d("Relaxation", "ACTION_UP");
                    chronometer.stop();
                    evalrunning = false;
                    startbutton.setEnabled(true);
                    break;

                case MotionEvent.ACTION_MOVE:
                    Log.d("Relaxation", "ACTION_MOVE");

                    if (!evalrunning) {
                        evalrunning = true;
                        chronostart();
                        initialy = Y - _yDelta;
                        rising = true;
                        cycle = 0;
                        startbutton.setEnabled(false);
                    }

                    LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                    // slayoutParams.leftMargin = X - _xDelta;
                    layoutParams.topMargin = Y - _yDelta;
                    layoutParams.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -70, getResources().getDisplayMetrics());
                    layoutParams.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -70, getResources().getDisplayMetrics());
                    view.setLayoutParams(layoutParams);
                    y = layoutParams.topMargin;
                    if (rising) {
                        if (initialy < y) {
                            // going down
                            rising = false;
                        }
                    } else {
                        if (initialy > y) {
                            // going up
                            rising = true;
                            cycle++;
                            updateCycle(cycle);

                        }
                    }

                    initialy = y;

                    break;
            }
            root.invalidate();
        }
        return true;
    }

    private void chronostart() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        SessionHistoryHelper.addParameterSession(sessiontime, cyclenumber, ratiovalue);
    }

    private void chronostop() {

        // Log.e("cycle", String.valueOf(cyclenumber));
        // Log.e("ratio", String.valueOf(ratiovalue));
        // Log.e("ratio", String.valueOf(sessiontime));

        SessionHistoryHelper.addSession();
        db = new SessionHistoryDatabase(this);
        db.addSession(SessionHistoryHelper.getStartTime(), SessionHistoryHelper.getEndTime(), SessionHistoryHelper.getDuration(), SessionHistoryHelper.getCycle(), SessionHistoryHelper.getRatio(), heartStart, heartEnd);
        chronometer.stop();
        timeWhenStopped = 0;

        if (!expertMode) chronometer.setText(String.format("%02d:00", sessiontime));
    }

    private void chronopause() {
        timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
        chronometer.stop();
    }

    private void chronoresume() {
        chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        chronometer.start();

    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.martinforget.cardiaccoherencelite/http/host/path"));
        AppIndex.AppIndexApi.start(client, viewAction);
    }


    @Override
    public void onStop() {
        super.onStop();

        if (loadPurchasedItem != null) loadPurchasedItem.bpCleanup();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.martinforget.cardiaccoherencelite/http/host/path"));
        AppIndex.AppIndexApi.end(client, viewAction);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (animationrunning && heartmonitor) destroyCamera();
        Log.d("Main page:", "APP onPause!");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (animationrunning && heartmonitor) {
            createCamera();
            startHeartBeat();
        }
        Log.d("Main page:", "APP onResume!");
    }

    @Override
    public void onBackPressed() {

        if (menuS.isMenuShowing()) {
            menuS.toggle();
        } else {
            super.onBackPressed();  // optional depending on your needs
            if (animation != null) animation.forceStop();

            destroyCamera();
            createNotification();
        }

    }

    public class MyTimer extends CountDownTimer {
        public MyTimer(long startTime, long interval) {

            super(startTime, interval);

        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.v("Tick", "Tick of Progress" + progress + millisUntilFinished + " progress=" + progress);
            progress++;// = progress + (100 / ((float)sessiontime * 60));
            progressBar.setProgress(progress);
            animation.setAnimationProgress(progress);
        }

        @Override
        public void onFinish() {
            // Do what you want
            progress = 100;
            progressBar.setProgress(progress);
            animation.setAnimationProgress(progress);
        }

    }


    private void getViewPositions() {

        startbutton.getLocationOnScreen(startbuttonpos);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int offsetY = displayMetrics.heightPixels - root.getMeasuredHeight();
        Log.d("Relaxation", "offsetY=" + offsetY);
        startbuttonpos[1] = startbuttonpos[1] - 2 * offsetY;

    }

    private void setVibrateImage() {
        if (vibrate) {
            vibrationbutton.setImageResource(R.drawable.vibrate);
        } else {
            vibrationbutton.setImageResource(R.drawable.novibrate);
        }
    }
    private void setMusicImage() {
        if (playmusic) {
            musicbutton.setImageResource(R.drawable.music);
        } else {
            musicbutton.setImageResource(R.drawable.mute_music);
        }
    }
    private void setSoundImage() {
        if (sound) {
            soundbutton.setImageResource(R.drawable.speaker);
        } else {
            soundbutton.setImageResource(R.drawable.mute);
        }
    }

    private void setNightDayImage() {
        if (day) {
            textview.setTextColor(Color.BLACK);
            textCycle.setTextColor(Color.BLACK);
            chronometer.setTextColor(Color.BLACK);
            progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorApp), PorterDuff.Mode.MULTIPLY);
            progressBar.getThumb().setColorFilter(getResources().getColor(R.color.colorApp), PorterDuff.Mode.SRC_IN);

            daynightbutton.setImageResource(R.drawable.ic_moon);
            daynightbutton.setColorFilter(ContextCompat.getColor(this, R.color.color2), android.graphics.PorterDuff.Mode.MULTIPLY);
            container1.setBackground(getResources().getDrawable(R.drawable.cyrcle_shape_blue));
            day = true;
        } else {
            textview.setTextColor(Color.WHITE);
            textCycle.setTextColor(Color.WHITE);
            chronometer.setTextColor(Color.WHITE);
            progressBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            progressBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

            daynightbutton.setImageResource(R.drawable.ic_moon);
            daynightbutton.setColorFilter(ContextCompat.getColor(this, R.color.color2), android.graphics.PorterDuff.Mode.MULTIPLY);
            container1.setBackground(getResources().getDrawable(R.drawable.cyrcle_shape_blue));
            day = false;
        }
    }

    private void setExpertMode(boolean mode) {
        expert.setChecked(expertMode);
        respperminselector.setEnabled(!mode);
        cycleNb.setEnabled(!mode);
        ratio.setEnabled(!mode);
        ratioNb.setEnabled(!mode);
        animation.setExpertValues(expertMode, inspval, expval, holdval, holdoutval);
        animation.setRatiovalue(ratiovalue);
        insptime.setEnabled(mode);
        inspNb.setEnabled(mode);
        inspTimeTxt.setEnabled(mode);
        exptime.setEnabled(mode);
        expNb.setEnabled(mode);
        expTimeTxt.setEnabled(mode);
        holdTimeTxt.setEnabled(mode);
        holdtime.setEnabled(mode);
        holdNb.setEnabled(mode);
        holdouttime.setEnabled(mode);
        holdOutNb.setEnabled(mode);
        holdOutTimeTxt.setEnabled(mode);
    }

    private void setNotifMode(boolean mode) {
        notif.setChecked(mode);
        notifNb.setEnabled(mode);
        notiftime.setEnabled(mode);
    }


    private void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        Log.d("Notification", "Create notification in " + delay / 3600000 + " hours");
    }

    private Notification getNotification(String content) {
        long[] pattern = {0, 600, 0};
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_IMMUTABLE);
        Notification.Builder builder = new Notification.Builder(this);
        return builder.build();


    }

    private void deleteAllNotifications() {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    private void createNotification() {
        if (notifMode) {
            deleteAllNotifications();
            scheduleNotification(getNotification("Cardiac Coherence"), (int) notifvalue * 3600000);
        }
    }

    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] previewData, Camera cam) {

            Log.d("Heart", "Testing camera input");
            if (previewData == null) throw new NullPointerException();
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null) throw new NullPointerException();

            if (!processing.compareAndSet(false, true)) return;

            int width = size.width;
            int height = size.height;

            int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(previewData.clone(), height, width);
            Log.d("Rolling average", "imgAvg=" + imgAvg);
            // Log.i(TAG, "imgAvg="+imgAvg);
            if (imgAvg == 0 || imgAvg == 255) {
                processing.set(false);
                return;
            }

            int averageArrayAvg = 0;
            int averageArrayCnt = 0;
            for (int i = 0; i < averageArray.length; i++) {
                if (averageArray[i] > 0) {
                    averageArrayAvg += averageArray[i];
                    averageArrayCnt++;
                }
            }

            int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt) : 0;
            MainActivity.TYPE newType = currentType;

            Log.d("Rolling average", "imgAvg=" + imgAvg + " rolling=" + rollingAverage);
            if (imgAvg < rollingAverage) {
                newType = MainActivity.TYPE.RED;

                if (newType != currentType) {
                    imageHeart.setImageResource(R.drawable.heart);
                    beats++;
                    Log.i("Rolling average", "BEAT!! beats=" + beats + "time=" + (System.currentTimeMillis() - startTimeBPM) / 1000d);
                }
            } else if (imgAvg > rollingAverage) {
                newType = MainActivity.TYPE.GREY;
                if (animationrunning) {
                    imageHeart.setImageResource(R.drawable.heartgrey);
                }
            }

            if (averageIndex == averageArraySize) averageIndex = 0;
            averageArray[averageIndex] = imgAvg;
            averageIndex++;

            // Transitioned from one state to another to the same
            if (newType != currentType) {
                currentType = newType;
            }

            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTimeBPM) / 1000d;
            if (totalTimeInSecs >= 5) {
                double bps = (beats / totalTimeInSecs);

                int dpm = (int) (bps * 60d);
                Log.i("beats", "beats=" + beats + " totaltime=" + totalTimeInSecs + " dpm=" + dpm);
                if (dpm < 30 || dpm > 180) {
                    startTimeBPM = System.currentTimeMillis();
                    beats = 0;
                    processing.set(false);
                    return;
                }

                // Log.d(TAG,
                // "totalTimeInSecs="+totalTimeInSecs+" beats="+beats);

                if (beatsIndex == beatsArraySize) beatsIndex = 0;
                beatsArray[beatsIndex] = dpm;
                beatsIndex++;

                int beatsArrayAvg = 0;
                int beatsArrayCnt = 0;
                for (int i = 0; i < beatsArray.length; i++) {
                    if (beatsArray[i] > 0) {
                        beatsArrayAvg += beatsArray[i];
                        beatsArrayCnt++;
                    }
                }
                int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
                Log.i("Rolling average", "beatsAvg=" + beatsAvg);
                isConverging = updateHeartBeatValue(beatsAvg, isConverging);

                startTimeBPM = System.currentTimeMillis();
                beats = 0;
            }
            processing.set(false);
        }
    };

    private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            try {
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);
            } catch (Throwable t) {
                Log.e("SurfaceCallback", "Exception in setPreviewDisplay()", t);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (camera != null) {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                Camera.Size size = getSmallestPreviewSize(width, height, parameters);
                if (size != null) {
                    parameters.setPreviewSize(size.width, size.height);
                    //Log.d(TAG, "Using width=" + size.width + " height=" + size.height);
                }
                try {
                    camera.setParameters(parameters);
                } catch (Exception e) {
                    // ignore: tried to stop a non-existent preview
                }


            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Ignore
        }
    };

    private static Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea < resultArea) result = size;
                }
            }
        }

        return result;
    }

    private void createCamera() {
        if (heartmonitor) {
            camera = Camera.open();
            previewHolder = preview.getHolder();
            previewHolder.addCallback(surfaceCallback);
            previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    private void destroyCamera() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }


    private void startHeartBeat() {
        if (heartmonitor) {

            //createCamera();
            if (camera != null) {
                Camera.Parameters parameters = camera.getParameters();
                if (heartmonitorflash) parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                else parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);

                camera.startPreview();
                Log.d("HeartMonitor", "Start Light");
            }
        }
    }


    private void stopHeartBeat() {
        if (heartmonitor) {
            if (camera != null) {

                imageHeart.setImageResource(R.drawable.heartlightgrey);

                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                Log.d("HeartMonitor", "Stop Light");
                heartBeatQue.clear();
                for (int i = 0; i < beatsArraySize; i++)
                    beatsArray[i] = 0;

                // Create next preview callback
                camera.setParameters(parameters);
                camera.setPreviewCallback(null);
                camera.stopPreview();

                try {
                    camera.setPreviewDisplay(previewHolder);
                    camera.setPreviewCallback(previewCallback);
                } catch (Throwable t) {
                    Log.d("HeartMonitor", "ERROR Light");
                    Log.e("SurfaceCallback", "Exception in setPreviewDisplay()", t);
                }

                //destroyCamera();
                //createCamera();

            }
        }
    }

    private boolean updateHeartBeatValue(int value, boolean isConverging) {
        Log.d("Heart", "heartmonitor=" + heartmonitor);
        if (heartmonitor) {
            heartBeatQue.add(value);
            if (heartBeatQue.size() > beatsArraySize) {
                heartBeatQue.removeFirst();
            }

            int previousValue = 0;

            Log.d("Heart", "Reading values");
            int currentValue = 0;
            for (Iterator itr = heartBeatQue.iterator(); itr.hasNext(); ) {
                currentValue = (int) itr.next();
                if (previousValue != 0) {
                    // Test if the value is converging or not
                    int result = abs(currentValue - previousValue);

                    if (result < 30) {
                        isConverging = true;
                        if (heartStart == 0) heartStart = currentValue;
                    } else isConverging = false;
                } else previousValue = currentValue;

                heartEnd = currentValue;
                heartResultText.setText(heartStart + "/" + heartEnd);
                Log.d("Heart", "Value=" + currentValue);
            }
            heartBeatText.setText(String.valueOf(value));
            if (isConverging) {
                heartBeatText.setVisibility(View.VISIBLE);
                heartBeatText.setTextColor(ContextCompat.getColor(this, R.color.DarkGreen));
            } else {
                heartBeatText.setVisibility(View.VISIBLE);
                heartBeatText.setTextColor(Color.RED);
            }
        }
        return isConverging;
    }

    private void setHeartVisibility(int visibility) {
        imageHeart.setVisibility(visibility);
        if (visibility == View.INVISIBLE) {
            heartBeatText.setVisibility(visibility);
            heartResultText.setVisibility(visibility);
        }
    }

    private boolean hasFlash() {
        boolean returnValue = true;
        camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            // Camera in use or doesn't exist
        }

        if (camera == null) {
            returnValue = false;
        } else {

            Camera.Parameters parameters = camera.getParameters();

            if (parameters.getFlashMode() == null) {
                returnValue = false;
            }

            List<String> supportedFlashModes = parameters.getSupportedFlashModes();
            if (supportedFlashModes == null || supportedFlashModes.isEmpty() || supportedFlashModes.size() == 1 && supportedFlashModes.get(0).equals(Camera.Parameters.FLASH_MODE_OFF)) {
                returnValue = false;
            }
        }

        Log.d("Flash", "Flash equipped:" + returnValue);

        destroyCamera();
        return returnValue;
    }

    @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("Permissions","Permisson request code "+ requestCode);

            if (requestCode == RequestUserPermission.REQUEST_CAMERA && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                heart.setClickable(true);
                Log.d("Permissions", "Permisson result CAMERA TRUE");
            }


            if (requestCode == RequestUserPermission.REQUEST_POSITION  && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Linkify the message
                Monedata.initialize(this, "2f696614-f762-4604-af4c-f1649a85e323", true);

                createConsentDialog(this);

            }
            else {
                Monedata.Consent.set(mActivity, false);

            }
    }

    private void showHint() {
        AssetManager assetManager = getAssets();
        final IntroDialog dialog = new IntroDialog(MainActivity.this, assetManager);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);

        dialog.getWindow().setLayout(width, height);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialogint) {

                if (dialog.continueIntro == false) {
                    // false returned
                }

            }
        });
        dialog.show();
        data.StoreDataLong(ProfileManager.firstTry, (long) 1);
    }

    private void openProfileSelectionDialog() {
        final ProfileSelectionDialog dialog = new ProfileSelectionDialog(this, profileMgr.getCurrentProfileId());
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialogint) {
                if (!dialog.isCancelled) {
                    if (currentProfileId != profileMgr.getCurrentProfileId()) {
                        Intent intent = getIntent();

                        if (animation != null) animation.forceStop();

                        destroyCamera();


                        finish();
                        startActivity(intent);
                    }
                }

            }
        });

        dialog.show();
    }

    private void startPurchaseActivity() {
        Intent intent = new Intent(MainActivity.this, PurchaseActivity.class);
        Bundle b = new Bundle();
        b.putBoolean("hasFlash", hasFlash()); //Your id
        intent.putExtras(b);
        startActivityForResult(intent, 1);
    }

    private void askForReview() {
        // Create review manager
        ReviewManager manager = ReviewManagerFactory.create(this);

        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow(MainActivity.this, reviewInfo);
            } else {
                // There was some problem, log or handle the error code.
            }
        });
    }

    private void createConsentDialog (Activity activity)
    {
        final SpannableString s = new SpannableString(getResources().getString(R.string.monedatatext)); // msg should have url to enable clicking
        Linkify.addLinks(s, Linkify.ALL);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setTitle(R.string.monedatatitle);
        TextView textView = new TextView(activity);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(R.string.monedatatext);
        dialogBuilder.setView(textView);

        dialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Monedata.Consent.set(mActivity, false);
                Log.d("Permissions", "Permisson result LOCATION FALSE");
            }
        });

        dialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Monedata.Consent.set(mActivity, true);
                Log.d("Permissions", "Permisson result LOCATION TRUE");
            }
        });


        dialogBuilder.setIcon(R.drawable.monedata);
        dialogBuilder.show();

        // Permissions have been requested (granted or denied)
        Log.d("Permissions", "Permisson result LOCATION TRUE");
    }

    public void changeConsent(View v) {
        createConsentDialog(mActivity);
        Log.d("Permissions", "Change consent called");
    }
}
