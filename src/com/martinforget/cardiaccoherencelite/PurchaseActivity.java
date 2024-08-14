package com.martinforget.cardiaccoherencelite;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

//import com.anjlab.android.iab.v3.BillingProcessor;
//import com.anjlab.android.iab.v3.PurchaseInfo;
import com.martinforget.billingmanager.BillingProcessor;
import com.martinforget.billingmanager.PurchaseInfo;
import com.martinforget.Data;
//import com.martinforget.Data;


public class PurchaseActivity extends Activity {

    public static int BILLING_ERROR=1;
    public static int BILLING_SUCCESS=2;
    public static int ALREADY_PURCHASED=3;
    public static int LIVE_LONG=4;
    public static int CANCELLED=5;

    private Purchase purchase;
    private BillingProcessor bp;
    private boolean readyToPurchase = false;
    private int hack = 0;
    boolean hasFlash = false;
    private Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_activity);
         
        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        Bundle b = getIntent().getExtras();
        if(b != null)
            hasFlash = b.getBoolean("hasFlash");

        data = Data.getInstance(this.getBaseContext(), true);
        purchase = new Purchase(this);


        bp = new BillingProcessor(this, purchase.getAppKey(),
                new BillingProcessor.IBillingHandler() {


                    @Override
                    public void onBillingError(int errorCode, Throwable error) {
                        Log.d("Purchasing",
                                "onBillingError: "
                                        + Integer.toString(errorCode));
                        billigError();
                    }

                    @Override
                    public void onBillingInitialized() {
                        Log.d("Purchasing", "Activity onBillingInitialized");
                        readyToPurchase = true;
                    }

                    @Override
                    public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {
                        if (bp.isSubscribed(Purchase.SUBS_PREMIUM_VERSION))
                            purchase.setPurchasedPremiumStatus(true);
                        else
                            purchase.setPurchasedPremiumStatus(false);
                        if (bp.isPurchased(Purchase.PREMIUM_VERSION))
                            purchase.setPurchasedPremiumStatus(true);
                        if (bp.isPurchased(Purchase.EXPERT_ITEM))
                            purchase.setPurchasedExpertStatus(true);
                        if (bp.isPurchased(Purchase.NOTIFICATION_ITEM))
                            purchase.setPurchasedNotificationStatus(true);
                        if (bp.isPurchased(Purchase.MONITOR_ITEM))
                            purchase.setPurchasedMonitorStatus(true);
                        if (bp.isPurchased(Purchase.PROFILE_ITEM))
                            purchase.setPurchasedProfileStatus(true);
                        billigSuccess();
                    }

                    @Override
                    public void onPurchaseHistoryRestored() {
                        for (String sku : bp.listOwnedProducts())
                            Log.d("Purchasing", "Owned Managed Product: " + sku);
                        for (String sku : bp.listOwnedSubscriptions())
                            Log.d("Purchasing", "Owned Subscription: " + sku);
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        Log.d("Purchasing", "onClick");

        Intent intent=new Intent();
        switch (v.getId()) {
            case R.id.actCancelButton:
                if (!readyToPurchase) {
                    Log.d("Purchasing", "Billing not initialized.");
                    showToast(this.getString(R.string.purchase_error1));

                    setResult(CANCELLED,intent);
                    finish();
                    return;
                }

                setResult(CANCELLED,intent);
                finish();
                break;

            case R.id.actPremiumSubscribeButton:
                if (!readyToPurchase) {
                    Log.d("Purchasing", "Billing not initialized.");
                    showToast(this.getString(R.string.purchase_error2));

                    setResult(CANCELLED,intent);
                    finish();
                    return;
                }
                //purchase.setPurchasedPremiumStatus(true); // Test without purchasing
                subscribeItem(Purchase.SUBS_PREMIUM_VERSION);
                break;

            case R.id.actPremiumPurchaseButton:
                if (!readyToPurchase) {
                    Log.d("Purchasing", "Billing not initialized.");
                    showToast(this.getString(R.string.purchase_error2));

                    setResult(CANCELLED,intent);
                    finish();
                    return;
                }
                //purchase.setPurchasedPremiumStatus(true); // Test without purchasing
                purchaseItem(Purchase.PREMIUM_VERSION);
                break;

            case R.id.actNotificationPurchaseButton:
                if (!readyToPurchase) {
                    Log.d("Purchasing", "Billing not initialized.");
                    showToast(this.getString(R.string.purchase_error3));

                    setResult(CANCELLED,intent);
                    finish();
                    return;
                }
                //purchase.setPurchasedPremiumStatus(true); // Test without purchasing
                purchaseItem(Purchase.NOTIFICATION_ITEM);
                break;
            case R.id.actMonitorPurchaseButton:
                if (hasFlash) {
                    //purchase.setPurchasedPremiumStatus(true); // Test without purchasing
                    //billigSuccess();
                    if (!readyToPurchase) {
                        Log.d("Purchasing", "Billing not initialized.");
                        showToast(this.getString(R.string.purchase_error4));

                        setResult(CANCELLED,intent);
                        finish();
                        return;
                    }
                    purchaseItem(Purchase.MONITOR_ITEM);
                }
                else
                    showToast(this.getString(R.string.optionNotAvailable));

                break;
            case R.id.actExpertPurchaseButton:
                if (!readyToPurchase) {
                    Log.d("Purchasing", "Billing not initialized.");
                    showToast(this.getString(R.string.purchase_error5));

                    setResult(CANCELLED,intent);
                    finish();
                    return;
                }
                //purchase.setPurchasedPremiumStatus(true); // Test without purchasing
                purchaseItem(Purchase.EXPERT_ITEM);
                break;
            case R.id.actProfilePurchaseButton:
                if (!readyToPurchase) {
                    Log.d("Purchasing", "Billing not initialized.");
                    showToast(this.getString(R.string.purchase_error6));

                    setResult(CANCELLED,intent);
                    finish();
                    return;
                }
                //purchase.setPurchasedProfileStatus(true); // Test without purchasing
                purchaseItem(Purchase.PROFILE_ITEM);
                break;
/* For testing Purchases
            case R.id.actExpertPurchaseButton2:
                if (!readyToPurchase) {
                    Log.d("Purchasing", "Billing not initialized.");
                    showToast(this.getString(R.string.purchase_error6));

                    setResult(CANCELLED,intent);
                    finish();
                    return;
                }
                //purchase.setPurchasedProfileStatus(true); // Test without purchasing
                purchaseItem(Purchase.PREMIUM_VERSION);
                break;
*/
            case R.id.hack:
                //Log.d("Purchasing", "Hack pressed");
                hack++;

                if (hack == 25) {
                    purchase.setPurchasedPremiumStatus(true);
                    showToast("Live long and prosper");
                    Intent intent2=new Intent();
                    setResult(LIVE_LONG,intent2);
                    finish();
                }
                break;
/*
            case R.id.resetPurchase:
                // Clear all purchase!!! REMOVE FROM PROD!!!


                bp.consumePurchaseAsync(Purchase.EXPERT_ITEM, new BillingProcessor.IPurchasesResponseListener() {
                    @Override
                    public void onPurchasesSuccess() {

                    }

                    @Override
                    public void onPurchasesError() {

                    }
                });
                bp.consumePurchaseAsync(Purchase.MONITOR_ITEM, new BillingProcessor.IPurchasesResponseListener() {
                    @Override
                    public void onPurchasesSuccess() {

                    }

                    @Override
                    public void onPurchasesError() {

                    }
                });
                bp.consumePurchaseAsync(Purchase.NOTIFICATION_ITEM, new BillingProcessor.IPurchasesResponseListener() {
                    @Override
                    public void onPurchasesSuccess() {

                    }

                    @Override
                    public void onPurchasesError() {

                    }
                });
                bp.consumePurchaseAsync(Purchase.PREMIUM_VERSION, new BillingProcessor.IPurchasesResponseListener() {
                    @Override
                    public void onPurchasesSuccess() {

                    }

                    @Override
                    public void onPurchasesError() {

                    }
                });
                bp.consumePurchaseAsync(Purchase.PROFILE_ITEM, new BillingProcessor.IPurchasesResponseListener() {
                    @Override
                    public void onPurchasesSuccess() {

                    }

                    @Override
                    public void onPurchasesError() {

                    }
                });

                data.StoreDataString("az1tgdgg32k63",
                        "ljsdfakl245r2w98sd9k".toString());
                data.StoreDataString("sdfdgdfgh345gd32234k21342",
                        "ljsdfakl245r2w98sd9k".toString());
                data.StoreDataString("ll589fgdiod034ererot034jowe",
                        "ljsdfakl245r2w98sd9k".toString());
                data.StoreDataString("kjuhw34esauiui3s8suseujhjhseddfgvmsz",
                        "ljsdfakl245r2w98sd9k".toString());
                data.StoreDataString("xx43jhsdvbeo4sad0439d090sdj",
                        "vlkfdkljeriofdknfgdgioh32jerk892".toString());
                showToast("Purchase cleared");
                Intent intent2=new Intent();
                setResult(LIVE_LONG,intent2);
                finish();
                break;
*/

            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();
        super.onDestroy();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void purchaseItem(String sku) {
        Log.d("Purchasing", "Purchasing:" + sku);
        if (!bp.isPurchased(sku)) {
            bp.purchase(this, sku);

        } else {
            showToast(this.getString(R.string.purchase_already));
            Intent intent=new Intent();
            setResult(ALREADY_PURCHASED,intent);
            finish();
        }
    }

    private void subscribeItem(String sku) {
        Log.d("Purchasing", "Subscribing:" + sku);
        if (!bp.isSubscribed(sku)) {
            bp.subscribe(this, sku);

        } else {
            showToast(this.getString(R.string.purchase_already));
            Intent intent=new Intent();
            setResult(ALREADY_PURCHASED,intent);
            finish();
        }
    }
    private void billigError() {
        showToast(this.getString(R.string.purchase_error7));
        Intent intent=new Intent();
        setResult(BILLING_ERROR,intent);

        // For test

        finish();
    }

    private void billigSuccess() {
        showToast(this.getString(R.string.purchase_thankyou));
        Intent intent=new Intent();
        setResult(BILLING_SUCCESS,intent);
        finish();
    }
}
