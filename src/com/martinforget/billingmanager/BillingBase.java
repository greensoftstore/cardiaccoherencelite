package com.martinforget.billingmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BillingBase {
    private Context context;

    BillingBase(Context context) {
        this.context = context;
    }

    Context getContext() {
        return this.context;
    }

    String getPreferencesBaseKey() {
        return this.getContext().getPackageName() + "_preferences";
    }

    private SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this.getContext());
    }

    boolean saveString(String key, String value) {
        SharedPreferences sp = this.getPreferences();
        if (sp != null) {
            SharedPreferences.Editor spe = sp.edit();
            spe.putString(key, value);
            spe.commit();
            return true;
        } else {
            return false;
        }
    }

    String loadString(String key, String defValue) {
        SharedPreferences sp = this.getPreferences();
        return sp != null ? sp.getString(key, defValue) : defValue;
    }

    boolean saveBoolean(String key, Boolean value) {
        SharedPreferences sp = this.getPreferences();
        if (sp != null) {
            SharedPreferences.Editor spe = sp.edit();
            spe.putBoolean(key, value);
            spe.commit();
            return true;
        } else {
            return false;
        }
    }

    boolean loadBoolean(String key, boolean defValue) {
        SharedPreferences sp = this.getPreferences();
        return sp != null ? sp.getBoolean(key, defValue) : defValue;
    }
}
