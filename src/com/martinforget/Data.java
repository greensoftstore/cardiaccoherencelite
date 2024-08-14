package com.martinforget;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.util.Log;

public class Data {
    // Singleton
    private static Data _instance;

    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String SERIAL_NAME = "MySerialFile";
    public static final String CONFIG_DIR = "/Configs";
    public String profilePrefix = "profile";

    private SharedPreferences settings;
    private String datafilename;
    private boolean useProfiles = false;
    private String currentProfileName;

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    private boolean debugMode = false;

    private Data(Context context) {
        settings = context.getSharedPreferences(PREFS_NAME, 0);
    }

    private Data(Context context, boolean useProfiles) {

        this.useProfiles = useProfiles;
        settings = context.getSharedPreferences(PREFS_NAME, 0);
    }

    public static Data getInstance(Context context)
    {
        if (_instance == null)
        {
            _instance = new Data(context);
        }
        return _instance;
    }

    public static Data getInstance(Context context, boolean useProfiles)
    {
        if (_instance == null)
        {
            _instance = new Data(context, useProfiles);
        }
        return _instance;
    }

    public void StoreDataString(String name, String text, int id) {

            name = getNameWithProfile(name,id);

        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, text);
        editor.commit();
    }

    public void StoreDataString(String name, String text) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, text);
        editor.commit();
    }

    public String ReadDataString(String name) {

        String text = settings.getString(name, null);
        return text;
    }

    public String ReadDataString(String name, int id) {

            name = getNameWithProfile(name,id);

        String text = settings.getString(name, null);
        return text;
    }

    public void StoreDataLong(String name, Long value) {

        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(name, value);
        editor.commit();
    }

    public void StoreDataLong(String name, Long value, int id) {

            name = getNameWithProfile(name,id);

        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(name, value);
        editor.commit();
    }
    public long ReadDataLong(String name) {

        long value = settings.getLong(name, 0);
        return value;
    }

    public long ReadDataLong(String name, int id) {

            name = getNameWithProfile(name,id);

        long value = settings.getLong(name, 0);
        return value;
    }
    public void ClearAllData() {
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }

    public static void WiteSerializable(Context context, Object object, int layernumber) {

        ObjectOutputStream objectOut = null;
        try {

            FileOutputStream fileOut = context.openFileOutput(SERIAL_NAME, Activity.MODE_PRIVATE);
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(object);
            fileOut.getFD().sync();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objectOut != null) {
                try {
                    objectOut.close();
                } catch (IOException e) {
                    // do nowt
                }
            }
        }
    }

    public static Object ReadSerializable(Context context, int layernumber) {

        ObjectInputStream objectIn = null;
        Object object = null;
        try {

            FileInputStream fileIn = context.getApplicationContext().openFileInput(SERIAL_NAME);
            objectIn = new ObjectInputStream(fileIn);
            object = objectIn.readObject();

        } catch (FileNotFoundException e) {
            // Do nothing
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (objectIn != null) {
                try {
                    objectIn.close();
                } catch (IOException e) {
                    // do nowt
                }
            }
        }

        return object;
    }

    public boolean saveSharedPreferencesToFile(Context context, String filename) {

        File myPath = new File(Environment.getExternalStorageDirectory().toString() + CONFIG_DIR);
        if (!myPath.isDirectory()) {
            Log.d("Data", "Create directory :" + myPath.getPath());
            myPath.mkdir();
        }

        File dst = new File(myPath, filename);

        boolean res = false;
        ObjectOutputStream output = null;
        try {
            output = new ObjectOutputStream(new FileOutputStream(dst));
         /*   SharedPreferences pref =
                                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);*/
            output.writeObject(settings.getAll());

            res = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return res;
    }

    @SuppressWarnings({"unchecked"})
    public boolean loadSharedPreferencesFromFile(Context context, String filename) {
        File myPath = new File(Environment.getExternalStorageDirectory().toString() + CONFIG_DIR);
        File src = new File(myPath, filename);

        boolean res = false;
        ObjectInputStream input = null;
        Log.d("Data", "Loading");
        try {
            Log.d("Data", "trying");
            input = new ObjectInputStream(new FileInputStream(src));
            Editor prefEdit = settings.edit(); //context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
            prefEdit.clear();
            Map<String, ?> entries = (Map<String, ?>) input.readObject();
            for (Entry<String, ?> entry : entries.entrySet()) {
                Object v = entry.getValue();
                String key = entry.getKey();

                if (v instanceof Boolean)
                    prefEdit.putBoolean(key, ((Boolean) v).booleanValue());
                else if (v instanceof Float)
                    prefEdit.putFloat(key, ((Float) v).floatValue());
                else if (v instanceof Integer)
                    prefEdit.putInt(key, ((Integer) v).intValue());
                else if (v instanceof Long)
                    prefEdit.putLong(key, ((Long) v).longValue());
                else if (v instanceof String)
                    prefEdit.putString(key, ((String) v));
            }
            prefEdit.commit();
            res = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return res;
    }

    public void SaveDataFile(String saveddataname) {

        // BEGIN EXAMPLE
        File myPath = new File(Environment.getExternalStorageDirectory().toString());
        File myFile = new File(myPath, "SportLineup.sav");

        try {
            FileWriter fw = new FileWriter(myFile);
            PrintWriter pw = new PrintWriter(fw);

            Map<String, ?> prefsMap = settings.getAll();

            for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
                pw.println(entry.getKey() + ": " + entry.getValue().toString());
            }

            pw.close();
            fw.close();
        } catch (Exception e) {
            // what a terrible failure...
            Log.wtf(getClass().getName(), e.toString());
        }
    }


    public String getDatafilename() {
        return datafilename;
    }

    public void setDatafilename(String datafilename) {
        this.datafilename = datafilename;
    }

    public String getCurrentProfileName() {
        return currentProfileName;
    }

    public void setCurrentProfileName(String currentProfileName) {
        this.currentProfileName = currentProfileName;
    }

    public boolean isUseProfiles() {
        return useProfiles;
    }

    public boolean isProfilesUsed () {
        //TODO

        return true;
    }

    private String getNameWithProfile(String name, int id)
    {
        name = profilePrefix + String.valueOf(id)+ name;
        return name;
    }


}