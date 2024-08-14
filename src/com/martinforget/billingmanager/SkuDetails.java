package com.martinforget.billingmanager;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class SkuDetails implements Parcelable {
    public final String productId;
    public final String title;
    public final String description;
    public final boolean isSubscription;
    public final String currency;
    public final Double priceValue;
    public final String subscriptionPeriod;
    public final String subscriptionFreeTrialPeriod;
    public final boolean haveTrialPeriod;
    public final double introductoryPriceValue;
    public final String introductoryPricePeriod;
    public final boolean haveIntroductoryPeriod;
    public final int introductoryPriceCycles;
    public final long priceLong;
    public final String priceText;
    public final long introductoryPriceLong;
    public final String introductoryPriceText;
    public final String responseData;
    public static final Parcelable.Creator<SkuDetails> CREATOR = new Parcelable.Creator<SkuDetails>() {
        public SkuDetails createFromParcel(Parcel source) {
            return new SkuDetails(source);
        }

        public SkuDetails[] newArray(int size) {
            return new SkuDetails[size];
        }
    };

    public SkuDetails(JSONObject source) throws JSONException {
        String responseType = source.optString("type");
        if (responseType == null) {
            responseType = "inapp";
        }

        this.productId = source.optString("productId");
        this.title = source.optString("title");
        this.description = source.optString("description");
        this.isSubscription = responseType.equalsIgnoreCase("subs");
        this.currency = source.optString("price_currency_code");
        this.priceLong = source.optLong("price_amount_micros");
        this.priceValue = (double)this.priceLong / 1000000.0;
        this.priceText = source.optString("price");
        this.subscriptionPeriod = source.optString("subscriptionPeriod");
        this.subscriptionFreeTrialPeriod = source.optString("freeTrialPeriod");
        this.haveTrialPeriod = !TextUtils.isEmpty(this.subscriptionFreeTrialPeriod);
        this.introductoryPriceLong = source.optLong("introductoryPriceAmountMicros");
        this.introductoryPriceValue = (double)this.introductoryPriceLong / 1000000.0;
        this.introductoryPriceText = source.optString("introductoryPrice");
        this.introductoryPricePeriod = source.optString("introductoryPricePeriod");
        this.haveIntroductoryPeriod = !TextUtils.isEmpty(this.introductoryPricePeriod);
        this.introductoryPriceCycles = source.optInt("introductoryPriceCycles");
        this.responseData = source.toString();
    }

    public String toString() {
        return String.format(Locale.US, "%s: %s(%s) %f in %s (%s)", this.productId, this.title, this.description, this.priceValue, this.currency, this.priceText);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            SkuDetails that = (SkuDetails)o;
            if (this.isSubscription != that.isSubscription) {
                return false;
            } else {
                boolean var10000;
                label41: {
                    if (this.productId != null) {
                        if (this.productId.equals(that.productId)) {
                            break label41;
                        }
                    } else if (that.productId == null) {
                        break label41;
                    }

                    var10000 = false;
                    return var10000;
                }

                var10000 = true;
                return var10000;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.productId != null ? this.productId.hashCode() : 0;
        result = 31 * result + (this.isSubscription ? 1 : 0);
        return result;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productId);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeByte((byte)(this.isSubscription ? 1 : 0));
        dest.writeString(this.currency);
        dest.writeDouble(this.priceValue);
        dest.writeLong(this.priceLong);
        dest.writeString(this.priceText);
        dest.writeString(this.subscriptionPeriod);
        dest.writeString(this.subscriptionFreeTrialPeriod);
        dest.writeByte((byte)(this.haveTrialPeriod ? 1 : 0));
        dest.writeDouble(this.introductoryPriceValue);
        dest.writeLong(this.introductoryPriceLong);
        dest.writeString(this.introductoryPriceText);
        dest.writeString(this.introductoryPricePeriod);
        dest.writeByte((byte)(this.haveIntroductoryPeriod ? 1 : 0));
        dest.writeInt(this.introductoryPriceCycles);
        dest.writeString(this.responseData);
    }

    protected SkuDetails(Parcel in) {
        this.productId = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.isSubscription = in.readByte() != 0;
        this.currency = in.readString();
        this.priceValue = in.readDouble();
        this.priceLong = in.readLong();
        this.priceText = in.readString();
        this.subscriptionPeriod = in.readString();
        this.subscriptionFreeTrialPeriod = in.readString();
        this.haveTrialPeriod = in.readByte() != 0;
        this.introductoryPriceValue = in.readDouble();
        this.introductoryPriceLong = in.readLong();
        this.introductoryPriceText = in.readString();
        this.introductoryPricePeriod = in.readString();
        this.haveIntroductoryPeriod = in.readByte() != 0;
        this.introductoryPriceCycles = in.readInt();
        this.responseData = in.readString();
    }
}
