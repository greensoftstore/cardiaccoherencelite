package com.martinforget.billingmanager;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class PurchaseInfo implements Parcelable {
    public final String responseData;
    public final String signature;
    /** @deprecated */
    @Deprecated
    public final String developerPayload;
    public final PurchaseData purchaseData;
    public static final Parcelable.Creator<PurchaseInfo> CREATOR = new Parcelable.Creator<PurchaseInfo>() {
        public PurchaseInfo createFromParcel(Parcel source) {
            return new PurchaseInfo(source);
        }

        public PurchaseInfo[] newArray(int size) {
            return new PurchaseInfo[size];
        }
    };

    public PurchaseInfo(String responseData, String signature) {
        this.responseData = responseData;
        this.signature = signature;
        this.developerPayload = "";
        this.purchaseData = this.parseResponseDataImpl();
    }

    public PurchaseInfo(String responseData, String signature, String developerPayload) {
        this.responseData = responseData;
        this.signature = signature;
        this.developerPayload = developerPayload;
        this.purchaseData = this.parseResponseDataImpl();
    }

    PurchaseData parseResponseDataImpl() {
        try {
            JSONObject json = new JSONObject(this.responseData);
            PurchaseData data = new PurchaseData();
            data.orderId = json.optString("orderId");
            data.packageName = json.optString("packageName");
            data.productId = json.optString("productId");
            long purchaseTimeMillis = json.optLong("purchaseTime", 0L);
            data.purchaseTime = purchaseTimeMillis != 0L ? new Date(purchaseTimeMillis) : null;
            data.purchaseState = PurchaseState.values()[json.optInt("purchaseState", 1)];
            data.developerPayload = this.developerPayload;
            data.purchaseToken = json.getString("purchaseToken");
            data.autoRenewing = json.optBoolean("autoRenewing");
            return data;
        } catch (JSONException var5) {
            Log.e("iabv3.purchaseInfo", "Failed to parse response data", var5);
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.responseData);
        dest.writeString(this.developerPayload);
        dest.writeString(this.signature);
    }

    protected PurchaseInfo(Parcel in) {
        this.responseData = in.readString();
        this.developerPayload = in.readString();
        this.signature = in.readString();
        this.purchaseData = this.parseResponseDataImpl();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && o instanceof PurchaseInfo) {
            PurchaseInfo other = (PurchaseInfo)o;
            return this.responseData.equals(other.responseData) && this.signature.equals(other.signature) && this.developerPayload.equals(other.developerPayload) && this.purchaseData.purchaseToken.equals(other.purchaseData.purchaseToken) && this.purchaseData.purchaseTime.equals(other.purchaseData.purchaseTime);
        } else {
            return false;
        }
    }
}
