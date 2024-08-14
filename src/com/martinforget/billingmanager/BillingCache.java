package com.martinforget.billingmanager;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class BillingCache extends BillingBase {
    private HashMap<String, PurchaseInfo> data = new HashMap();
    private String cacheKey;
    private String version;

    BillingCache(Context context, String key) {
        super(context);
        this.cacheKey = key;
        this.load();
    }

    private String getPreferencesCacheKey() {
        return this.getPreferencesBaseKey() + this.cacheKey;
    }

    private String getPreferencesVersionKey() {
        return this.getPreferencesCacheKey() + ".version";
    }

    private void load() {
        String[] entries = this.loadString(this.getPreferencesCacheKey(), "").split(Pattern.quote("#####"));
        String[] var2 = entries;
        int var3 = entries.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String entry = var2[var4];
            if (!TextUtils.isEmpty(entry)) {
                String[] parts = entry.split(Pattern.quote(">>>>>"));
                if (parts.length > 2) {
                    this.data.put(parts[0], new PurchaseInfo(parts[1], parts[2]));
                } else if (parts.length > 1) {
                    this.data.put(parts[0], new PurchaseInfo(parts[1], (String)null));
                }
            }
        }

        this.version = this.getCurrentVersion();
    }

    private void flush() {
        ArrayList<String> output = new ArrayList();
        Iterator var2 = this.data.keySet().iterator();

        while(var2.hasNext()) {
            String productId = (String)var2.next();
            PurchaseInfo info = (PurchaseInfo)this.data.get(productId);
            output.add(productId + ">>>>>" + info.responseData + ">>>>>" + info.signature);
        }

        this.saveString(this.getPreferencesCacheKey(), TextUtils.join("#####", output));
        this.version = Long.toString((new Date()).getTime());
        this.saveString(this.getPreferencesVersionKey(), this.version);
    }

    boolean includesProduct(String productId) {
        this.reloadDataIfNeeded();
        return this.data.containsKey(productId);
    }

    PurchaseInfo getDetails(String productId) {
        this.reloadDataIfNeeded();
        return this.data.containsKey(productId) ? (PurchaseInfo)this.data.get(productId) : null;
    }

    void put(String productId, String details, String signature) {
        this.reloadDataIfNeeded();
        if (!this.data.containsKey(productId)) {
            this.data.put(productId, new PurchaseInfo(details, signature));
            this.flush();
        }

    }

    void clear() {
        this.reloadDataIfNeeded();
        this.data.clear();
        this.flush();
    }

    private String getCurrentVersion() {
        return this.loadString(this.getPreferencesVersionKey(), "0");
    }

    private void reloadDataIfNeeded() {
        if (!this.version.equalsIgnoreCase(this.getCurrentVersion())) {
            this.data.clear();
            this.load();
        }

    }

    List<String> getContents() {
        return new ArrayList(this.data.keySet());
    }

    public String toString() {
        return TextUtils.join(", ", this.data.keySet());
    }
}
