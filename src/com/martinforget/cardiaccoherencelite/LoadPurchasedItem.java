package com.martinforget.cardiaccoherencelite;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
 
//import com.android.billingclient.api.BillingClient;
//import com.android.billingclient.api.BillingResult;
//import com.android.billingclient.api.PurchasesUpdatedListener;

//import com.anjlab.android.iab.v3.BillingProcessor;
//import com.anjlab.android.iab.v3.PurchaseInfo;
//import com.anjlab.android.iab.v3.SkuDetails;

import com.martinforget.billingmanager.BillingProcessor;
import com.martinforget.billingmanager.PurchaseInfo;
import com.martinforget.billingmanager.SkuDetails;
import com.martinforget.Data;

import java.util.List;

/**
 * Created by forgetm on 2017-06-07.
 */

public class LoadPurchasedItem {

    private Context context;
    private BillingProcessor bp;
    public Purchase purchase;
    private Data data;


    public LoadPurchasedItem(Context context) {
        this.context = context;

        Log.d("Purchasing", "Initializing Billing");
        data = Data.getInstance(this.context, true);
        purchase = new Purchase(this.context);

        bp = BillingProcessor.newBillingProcessor(this.context, purchase.getAppKey(),
                new BillingProcessor.IBillingHandler() {

                    @Override
                    public void onBillingError(int errorCode, Throwable error) {
                        Log.d("Purchasing", "Error!");
                    }

                    @Override
                    public void onBillingInitialized() {
                        bp.loadOwnedPurchasesFromGoogleAsync(new BillingProcessor.IPurchasesResponseListener() {
                            @Override
                            public void onPurchasesSuccess() {
                                /*for (String sku : bp.listOwnedProducts())
                                    Log.d("Purchasing", "Owned Managed Product: " + sku);
                                for (String sku : bp.listOwnedSubscriptions())
                                    Log.d("Purchasing", "Owned Subscription: " + sku);*/
                                for (String sku : bp.listOwnedProducts()) {
                                    Log.d("Purchasing", "Owned Managed Product: " + sku);
                                    if (data.isDebugMode() == true)
                                        Toast.makeText(context,
                                                "Product=" + sku,
                                                Toast.LENGTH_LONG).show();
                                }
                                for (String sku : bp.listOwnedSubscriptions()) {
                                    Log.d("Purchasing", "Owned Subscription: " + sku);
                                    if (data.isDebugMode() == true)
                                        Toast.makeText(context,
                                                "Subscription=" + sku,
                                                Toast.LENGTH_LONG).show();
                                }
                                // Handle if premium version (for legacy customer)
                                if (bp.isPurchased(Purchase.PREMIUM_VERSION))
                                    purchase.setPurchasedPremiumStatus(true);
                                // Handle premium subscription
                                else
                                {
                                    if (bp.isSubscribed(Purchase.SUBS_PREMIUM_VERSION))
                                        purchase.setPurchasedPremiumStatus(true);
                                    else
                                        purchase.setPurchasedPremiumStatus(false);
                                }

                                PurchaseInfo pInfo = bp.getSubscriptionPurchaseInfo(Purchase.SUBS_PREMIUM_VERSION);
                                bp.getSubscriptionListingDetailsAsync(purchase.getAppKey(),
                                        new BillingProcessor.ISkuDetailsResponseListener() {
                                            @Override
                                            public void onSkuDetailsResponse(@Nullable List<SkuDetails> products) {

                                            }

                                            @Override
                                            public void onSkuDetailsError(String error) {

                                            }
                                        });
                                if (bp.isPurchased(Purchase.PREMIUM_VERSION))
                                    purchase.setPurchasedProfileStatus(true);
                                if (bp.isPurchased(Purchase.EXPERT_ITEM))
                                    purchase.setPurchasedExpertStatus(true);
                                if (bp.isPurchased(Purchase.NOTIFICATION_ITEM))
                                    purchase.setPurchasedNotificationStatus(true);
                                if (bp.isPurchased(Purchase.MONITOR_ITEM))
                                    purchase.setPurchasedMonitorStatus(true);
                                if (bp.isPurchased(Purchase.PROFILE_ITEM))
                                    purchase.setPurchasedProfileStatus(true);

                                bpCleanup();
                            }

                            @Override
                            public void onPurchasesError() {

                            }
                        });
                    }

                    @Override
                    public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {
                        if (bp.isSubscribed(Purchase.SUBS_PREMIUM_VERSION))
                            purchase.setPurchasedPremiumStatus(true);
                        else
                            purchase.setPurchasedPremiumStatus(false);

                        if (bp.isPurchased(Purchase.EXPERT_ITEM))
                            purchase.setPurchasedExpertStatus(true);
                        if (bp.isPurchased(Purchase.NOTIFICATION_ITEM))
                            purchase.setPurchasedNotificationStatus(true);
                        if (bp.isPurchased(Purchase.MONITOR_ITEM))
                            purchase.setPurchasedMonitorStatus(true);
                        if (bp.isPurchased(Purchase.PROFILE_ITEM))
                            purchase.setPurchasedProfileStatus(true);
                    }

                    @Override
                    public void onPurchaseHistoryRestored() {
                        Log.d("Purchasing", "onPurchaseHistoryRestored");
                        for (String sku : bp.listOwnedProducts()) {
                            Log.d("Purchasing", "Owned Managed Product: " + sku);
                            if (data.isDebugMode() == true)
                                Toast.makeText(context,
                                        "Subscription=" + sku,
                                        Toast.LENGTH_LONG).show();
                        }
                        for (String sku : bp.listOwnedSubscriptions()) {
                            Log.d("Purchasing", "Owned Subscription: " + sku);
                            if (data.isDebugMode() == true)
                                Toast.makeText(context,
                                        "Product=" + sku,
                                        Toast.LENGTH_LONG).show();
                        }
                    }
                });
        bp.initialize();

    }


    public void bpCleanup() {
        if (bp != null) {
            bp.release();

        }

    }


}

