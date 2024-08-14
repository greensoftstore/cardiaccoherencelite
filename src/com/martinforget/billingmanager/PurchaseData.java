package com.martinforget.billingmanager;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class PurchaseData implements Parcelable {
    public String orderId;
    public String packageName;
    public String productId;
    public Date purchaseTime;
    public PurchaseState purchaseState;
    /** @deprecated */
    @Deprecated
    public String developerPayload;
    public String purchaseToken;
    public boolean autoRenewing;
    public static final Parcelable.Creator<PurchaseData> CREATOR = new Parcelable.Creator<PurchaseData>() {
        public PurchaseData createFromParcel(Parcel source) {
            return new PurchaseData(source);
        }

        public PurchaseData[] newArray(int size) {
            return new PurchaseData[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderId);
        dest.writeString(this.packageName);
        dest.writeString(this.productId);
        dest.writeLong(this.purchaseTime != null ? this.purchaseTime.getTime() : -1L);
        dest.writeInt(this.purchaseState == null ? -1 : this.purchaseState.ordinal());
        dest.writeString(this.developerPayload);
        dest.writeString(this.purchaseToken);
        dest.writeByte((byte)(this.autoRenewing ? 1 : 0));
    }

    public PurchaseData() {
    }

    protected PurchaseData(Parcel in) {
        this.orderId = in.readString();
        this.packageName = in.readString();
        this.productId = in.readString();
        long tmpPurchaseTime = in.readLong();
        this.purchaseTime = tmpPurchaseTime == -1L ? null : new Date(tmpPurchaseTime);
        int tmpPurchaseState = in.readInt();
        this.purchaseState = tmpPurchaseState == -1 ? null : PurchaseState.values()[tmpPurchaseState];
        this.developerPayload = in.readString();
        this.purchaseToken = in.readString();
        this.autoRenewing = in.readByte() != 0;
    }
}
