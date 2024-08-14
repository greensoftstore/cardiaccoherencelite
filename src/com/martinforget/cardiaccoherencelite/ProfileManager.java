package com.martinforget.cardiaccoherencelite;

import android.content.Context;
import android.util.Log;

import com.martinforget.Data;

/**
 * Created by forgetm on 2017-07-07.
 */

public class ProfileManager {

    public static final int maxNumberProfiles = 5;

    private int currentProfileId;

    public static final String firstTry = "firstry";
    public static final String heartMonitor = "heartmonitor";
    public static final String heartMonitorFlash = "heartmonitorflash";
    public static final String breatheInSound = "breatheInSound";
    public static final String breatheOutSound = "breatheOutSound";
    public static final String playmusic = "playmusic";
    public static final String musicSelected = "musicSelected";
    public static final String sessionTime = "sessiontime";
    public static final String cycleNumber = "cyclenumber";
    public static final String ratioValue = "ratiovalue";
    public static final String inspTime = "insptime";
    public static final String expTime = "exptime";
    public static final String holdTime = "holdtime";
    public static final String holdOutTime = "holdouttime";
    public static final String notifValue = "notifvalue";
    public static final String expert = "expert";
    public static final String notif = "notif";
    public static final String vibrate = "vibrate";
    public static final String sound = "sound";
    public static final String dayNight = "daynight";
    public static final String volume = "volume";
    public static final String volumeMusic = "volumeMusic";

    // Unique ID
    public static final String usingProfile = "usingprofile";
    public static final String currentProfile = "currentprofile";
    public static final String profile1Name = "profile1name";
    public static final String profile2Name = "profile2name";
    public static final String profile3Name = "profile3name";
    public static final String profile4Name = "profile4name";
    public static final String profile5Name = "profile5name";


    String[] parameters = {firstTry,
            heartMonitor,
            heartMonitorFlash,
            breatheInSound,
            breatheOutSound,
            playmusic,
            musicSelected,
            sessionTime,
            cycleNumber,
            ratioValue,
            inspTime,
            expTime,
            holdTime,
            notifValue,
            expert,
            notif,
            vibrate,
            sound,
            dayNight,
            volume,
            volumeMusic};
    private Data data;

    public ProfileManager(Context context) {
        // initialize data parser
        data = Data.getInstance(context, true);

        if (data.ReadDataLong(usingProfile)==0)
            convertSingleProfile();
        // setCurrentProfile
        setCurrentProfileId((int)data.ReadDataLong(currentProfile));
    }

    private void convertSingleProfile() {
        for (int i = 0; i < maxNumberProfiles; i++) {

            for (String s: parameters) {
                data.StoreDataLong(s, data.ReadDataLong(s, i), i);
                Log.d("ProfileManager", "Converting "+s+" for profile id:"+i);
            }

        }
        data.StoreDataLong(usingProfile,(long)1);
        setCurrentProfileId(1);
    }


    public int getCurrentProfileId() {
        return (int)data.ReadDataLong(currentProfile);
    }

    public void setCurrentProfileId(int currentProfileId) {
        this.currentProfileId = currentProfileId;
        storeCurrentProfileId();
    }

    public void storeCurrentProfileId()
    {
        data.StoreDataLong(currentProfile, (long)currentProfileId);
        data.StoreDataLong(currentProfile, (long)currentProfileId);
    }

    public void setProfileName (int profileId, String name)
    {
        String keyname = "profile" + profileId + "name";
        data.StoreDataString(keyname, name);
    }

    public String getProfileName (int profileId)
    {
        String keyname = "profile" + profileId + "name";
        String returnName = data.ReadDataString(keyname);

        if (returnName == null)
        {
            returnName = "Profile " + profileId;
        }

        return returnName;
    }
}


