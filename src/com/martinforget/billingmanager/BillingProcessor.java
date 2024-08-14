package com.martinforget.billingmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class BillingProcessor extends BillingBase {
    private static final Date DATE_MERCHANT_LIMIT_1;
    private static final Date DATE_MERCHANT_LIMIT_2;
    private long reconnectMilliseconds;
    private BillingClient billingService;
    private String signatureBase64;
    private BillingCache cachedProducts;
    private BillingCache cachedSubscriptions;
    private BillingProcessor.IBillingHandler eventHandler;
    private String developerMerchantId;
    private boolean isSubsUpdateSupported;
    private boolean isHistoryTaskExecuted;
    private Handler handler;

    public static BillingProcessor newBillingProcessor(Context context, String licenseKey, BillingProcessor.IBillingHandler handler) {
        return newBillingProcessor(context, licenseKey, (String)null, handler);
    }

    public static BillingProcessor newBillingProcessor(Context context, String licenseKey, String merchantId, BillingProcessor.IBillingHandler handler) {
        return new BillingProcessor(context, licenseKey, merchantId, handler, false);
    }

    public BillingProcessor(Context context, String licenseKey, BillingProcessor.IBillingHandler handler) {
        this(context, licenseKey, (String)null, handler);
    }

    public BillingProcessor(Context context, String licenseKey, String merchantId, BillingProcessor.IBillingHandler handler) {
        this(context, licenseKey, merchantId, handler, true);
    }

    private BillingProcessor(Context context, String licenseKey, String merchantId, BillingProcessor.IBillingHandler handler, boolean bindImmediately) {
        super(context.getApplicationContext());
        this.reconnectMilliseconds = 1000L;
        this.isHistoryTaskExecuted = false;
        this.handler = new Handler(Looper.getMainLooper());
        this.signatureBase64 = licenseKey;
        this.eventHandler = handler;
        this.cachedProducts = new BillingCache(this.getContext(), ".products.cache.v2_6");
        this.cachedSubscriptions = new BillingCache(this.getContext(), ".subscriptions.cache.v2_6");
        this.developerMerchantId = merchantId;
        this.init(context);
        if (bindImmediately) {
            this.initialize();
        }

    }

    private static Intent getBindServiceIntent() {
        Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        intent.setPackage("com.android.vending");
        return intent;
    }

    private void init(Context context) {
        PurchasesUpdatedListener listener = new PurchasesUpdatedListener() {
            public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
                int responseCode = billingResult.getResponseCode();
                if (responseCode == 0) {
                    if (purchases != null) {
                        Iterator var4 = purchases.iterator();

                        while(var4.hasNext()) {
                            Purchase purchase = (Purchase)var4.next();
                            BillingProcessor.this.handlePurchase(purchase);
                        }
                    }
                } else if (responseCode == 7) {
                    String purchasePayload = BillingProcessor.this.getPurchasePayload();
                    if (TextUtils.isEmpty(purchasePayload)) {
                        BillingProcessor.this.loadOwnedPurchasesFromGoogleAsync((BillingProcessor.IPurchasesResponseListener)null);
                    } else {
                        BillingProcessor.this.handleItemAlreadyOwned(purchasePayload.split(":")[1]);
                        BillingProcessor.this.savePurchasePayload((String)null);
                    }

                    BillingProcessor.this.reportBillingError(responseCode, new Throwable(billingResult.getDebugMessage()));
                } else if (responseCode == 1 || responseCode == 2 || responseCode == 3 || responseCode == 4 || responseCode == 5 || responseCode == 6 || responseCode == 8) {
                    BillingProcessor.this.reportBillingError(responseCode, new Throwable(billingResult.getDebugMessage()));
                }

            }
        };
        this.billingService = BillingClient.newBuilder(context).enablePendingPurchases().setListener(listener).build();
    }

    public void initialize() {
        if (this.billingService != null && !this.billingService.isReady()) {
            this.billingService.startConnection(new BillingClientStateListener() {
                public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                    if (billingResult.getResponseCode() == 0) {
                        BillingProcessor.this.reconnectMilliseconds = 1000L;
                        Log.d("GooglePlayConnection; ", "IsConnected");
                        if (!BillingProcessor.this.isHistoryTaskExecuted) {
                            (BillingProcessor.this.new HistoryInitializationTask()).execute(new Void[0]);
                        }
                    } else {
                        BillingProcessor.this.retryBillingClientConnection();
                        BillingProcessor.this.reportBillingError(billingResult.getResponseCode(), new Throwable(billingResult.getDebugMessage()));
                    }

                }

                public void onBillingServiceDisconnected() {
                    Log.d("ServiceDisconnected; ", "BillingServiceDisconnected, trying new Connection");
                    if (!BillingProcessor.this.isConnected()) {
                        BillingProcessor.this.retryBillingClientConnection();
                    }

                }
            });
        }

    }

    private void retryBillingClientConnection() {
        this.handler.postDelayed(new Runnable() {
            public void run() {
                BillingProcessor.this.initialize();
            }
        }, this.reconnectMilliseconds);
        this.reconnectMilliseconds = Math.min(this.reconnectMilliseconds * 2L, 900000L);
    }

    public boolean isConnected() {
        return this.isInitialized() && this.billingService.isReady();
    }

    public void release() {
        if (this.isConnected()) {
            Log.d("iabv3", "BillingClient can only be used once -- closing connection");
            this.billingService.endConnection();
        }

    }

    public boolean isInitialized() {
        return this.billingService != null;
    }

    public boolean isPurchased(String productId) {
        return this.cachedProducts.includesProduct(productId);
    }

    public boolean isSubscribed(String productId) {
        return this.cachedSubscriptions.includesProduct(productId);
    }

    public List<String> listOwnedProducts() {
        return this.cachedProducts.getContents();
    }

    public List<String> listOwnedSubscriptions() {
        return this.cachedSubscriptions.getContents();
    }

    private void loadPurchasesByTypeAsync(String type, final BillingCache cacheStorage, final BillingProcessor.IPurchasesResponseListener listener) {
        if (!this.isConnected()) {
            this.reportPurchasesError(listener);
            this.retryBillingClientConnection();
        } else {
            this.billingService.queryPurchasesAsync(type, new PurchasesResponseListener() {
                public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
                    if (billingResult.getResponseCode() == 0) {
                        cacheStorage.clear();
                        Iterator var3 = list.iterator();

                        while(var3.hasNext()) {
                            Purchase purchaseItem = (Purchase)var3.next();
                            String jsonData = purchaseItem.getOriginalJson();
                            if (!TextUtils.isEmpty(jsonData)) {
                                try {
                                    JSONObject purchase = new JSONObject(jsonData);
                                    cacheStorage.put(purchase.getString("productId"), jsonData, purchaseItem.getSignature());
                                } catch (Exception var7) {
                                    BillingProcessor.this.reportBillingError(100, var7);
                                    Log.e("iabv3", "Error in loadPurchasesByType", var7);
                                    BillingProcessor.this.reportPurchasesError(listener);
                                }
                            }
                        }

                        BillingProcessor.this.reportPurchasesSuccess(listener);
                    } else {
                        BillingProcessor.this.reportPurchasesError(listener);
                    }

                }
            });
        }
    }

    public void loadOwnedPurchasesFromGoogleAsync(final BillingProcessor.IPurchasesResponseListener listener) {
        final BillingProcessor.IPurchasesResponseListener successListener = new BillingProcessor.IPurchasesResponseListener() {
            public void onPurchasesSuccess() {
                BillingProcessor.this.reportPurchasesSuccess(listener);
            }

            public void onPurchasesError() {
                BillingProcessor.this.reportPurchasesError(listener);
            }
        };
        final BillingProcessor.IPurchasesResponseListener errorListener = new BillingProcessor.IPurchasesResponseListener() {
            public void onPurchasesSuccess() {
                BillingProcessor.this.reportPurchasesError(listener);
            }

            public void onPurchasesError() {
                BillingProcessor.this.reportPurchasesError(listener);
            }
        };
        this.loadPurchasesByTypeAsync("inapp", this.cachedProducts, new BillingProcessor.IPurchasesResponseListener() {
            public void onPurchasesSuccess() {
                BillingProcessor.this.loadPurchasesByTypeAsync("subs", BillingProcessor.this.cachedSubscriptions, successListener);
            }

            public void onPurchasesError() {
                BillingProcessor.this.loadPurchasesByTypeAsync("subs", BillingProcessor.this.cachedSubscriptions, errorListener);
            }
        });
    }

    public boolean purchase(Activity activity, String productId) {
        return this.purchase(activity, (String)null, productId, "inapp");
    }

    public boolean subscribe(Activity activity, String productId) {
        return this.purchase(activity, (String)null, productId, "subs");
    }

    /** @deprecated */
    @Deprecated
    public boolean isOneTimePurchaseSupported() {
        return true;
    }

    public boolean isSubscriptionUpdateSupported() {
        if (this.isSubsUpdateSupported) {
            return true;
        } else if (!this.isConnected()) {
            return false;
        } else {
            BillingResult result = this.billingService.isFeatureSupported("subscriptionsUpdate");
            this.isSubsUpdateSupported = result.getResponseCode() == 0;
            return this.isSubsUpdateSupported;
        }
    }

    private boolean purchase(final Activity activity, final String oldProductId, String productId, String purchaseType) {
        if (this.isConnected() && !TextUtils.isEmpty(productId) && !TextUtils.isEmpty(purchaseType)) {
            if (TextUtils.isEmpty(productId)) {
                this.reportBillingError(106, (Throwable)null);
                return false;
            } else {
                try {
                    String purchasePayload = purchaseType + ":" + productId;
                    if (!purchaseType.equals("subs")) {
                        purchasePayload = purchasePayload + ":" + UUID.randomUUID().toString();
                    }

                    this.savePurchasePayload(purchasePayload);
                    List<String> skuList = new ArrayList();
                    skuList.add(productId);
                    SkuDetailsParams params = SkuDetailsParams.newBuilder().setSkusList(skuList).setType(purchaseType).build();
                    this.billingService.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                        public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> skuList) {
                            if (skuList != null && !skuList.isEmpty()) {
                                BillingProcessor.this.startPurchaseFlow(activity, (SkuDetails)skuList.get(0), oldProductId);
                            } else {
                                Log.d("onSkuResponse: ", "product id mismatch with Product type");
                                BillingProcessor.this.reportBillingError(101, (Throwable)null);
                            }

                        }
                    });
                    return true;
                } catch (Exception var8) {
                    Log.e("iabv3", "Error in purchase", var8);
                    this.reportBillingError(110, var8);
                    return false;
                }
            }
        } else {
            if (!this.isConnected()) {
                this.retryBillingClientConnection();
            }

            return false;
        }
    }

    private void startPurchaseFlow(final Activity activity, final SkuDetails skuDetails, final String oldProductId) {
        final String productId = skuDetails.getSku();
        this.handler.post(new Runnable() {
            public void run() {
                BillingFlowParams.Builder billingFlowParamsBuilder = BillingFlowParams.newBuilder();
                billingFlowParamsBuilder.setSkuDetails(skuDetails);
                if (!TextUtils.isEmpty(oldProductId)) {
                    PurchaseInfo oldProductDetails = BillingProcessor.this.getSubscriptionPurchaseInfo(oldProductId);
                    if (oldProductDetails != null) {
                        String oldToken = oldProductDetails.purchaseData.purchaseToken;
                        billingFlowParamsBuilder.setSubscriptionUpdateParams(BillingFlowParams.SubscriptionUpdateParams.newBuilder().setOldPurchaseToken(oldToken).build());
                    }
                }

                BillingFlowParams params = billingFlowParamsBuilder.build();
                int responseCode = BillingProcessor.this.billingService.launchBillingFlow(activity, params).getResponseCode();
                if (responseCode == 7) {
                    BillingProcessor.this.handleItemAlreadyOwned(productId);
                }

            }
        });
    }

    private void handleItemAlreadyOwned(final String productId) {
        if (!this.isPurchased(productId) && !this.isSubscribed(productId)) {
            this.loadOwnedPurchasesFromGoogleAsync(new BillingProcessor.IPurchasesResponseListener() {
                public void onPurchasesSuccess() {
                    BillingProcessor.this.handleOwnedPurchaseTransaction(productId);
                }

                public void onPurchasesError() {
                    BillingProcessor.this.handleOwnedPurchaseTransaction(productId);
                }
            });
        } else {
            this.handleOwnedPurchaseTransaction(productId);
        }

    }

    private void handleOwnedPurchaseTransaction(String productId) {
        PurchaseInfo details = this.getPurchaseInfo(productId);
        if (!this.checkMerchant(details)) {
            Log.i("iabv3", "Invalid or tampered merchant id!");
            this.reportBillingError(104, (Throwable)null);
        }

        if (this.eventHandler != null) {
            if (details == null) {
                details = this.getSubscriptionPurchaseInfo(productId);
            }

            this.reportProductPurchased(productId, details);
        }

    }

    private boolean checkMerchant(PurchaseInfo details) {
        if (this.developerMerchantId == null) {
            return true;
        } else if (details.purchaseData.purchaseTime.before(DATE_MERCHANT_LIMIT_1)) {
            return true;
        } else if (details.purchaseData.purchaseTime.after(DATE_MERCHANT_LIMIT_2)) {
            return true;
        } else if (details.purchaseData.orderId != null && details.purchaseData.orderId.trim().length() != 0) {
            int index = details.purchaseData.orderId.indexOf(46);
            if (index <= 0) {
                return false;
            } else {
                String merchantId = details.purchaseData.orderId.substring(0, index);
                return merchantId.compareTo(this.developerMerchantId) == 0;
            }
        } else {
            return false;
        }
    }

    @Nullable
    private PurchaseInfo getPurchaseInfo(String productId, BillingCache cache) {
        PurchaseInfo details = cache.getDetails(productId);
        return details != null && !TextUtils.isEmpty(details.responseData) ? details : null;
    }

    private void getSkuDetailsAsync(String productId, String purchaseType, final BillingProcessor.ISkuDetailsResponseListener listener) {
        ArrayList<String> productIdList = new ArrayList();
        productIdList.add(productId);
        this.getSkuDetailsAsync(productIdList, purchaseType, new BillingProcessor.ISkuDetailsResponseListener() {
            public void onSkuDetailsResponse(@Nullable List<com.martinforget.billingmanager.SkuDetails> products) {
                if (products != null && listener != null) {
                    BillingProcessor.this.reportSkuDetailsResponseCaller(products, listener);
                }

            }

            public void onSkuDetailsError(String string) {
                BillingProcessor.this.reportSkuDetailsErrorCaller(string, listener);
            }
        });
    }

    private void getSkuDetailsAsync(final ArrayList<String> productIdList, String purchaseType, final BillingProcessor.ISkuDetailsResponseListener listener) {
        if (this.billingService != null && this.billingService.isReady()) {
            if (productIdList != null && !productIdList.isEmpty()) {
                try {
                    SkuDetailsParams skuDetailsParams = SkuDetailsParams.newBuilder().setSkusList(productIdList).setType(purchaseType).build();
                    final ArrayList<com.martinforget.billingmanager.SkuDetails> productDetails = new ArrayList();
                    this.billingService.querySkuDetailsAsync(skuDetailsParams, new SkuDetailsResponseListener() {
                        public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> detailsList) {
                            int response = billingResult.getResponseCode();
                            if (response == 0) {
                                if (detailsList != null && detailsList.size() > 0) {
                                    Iterator var4 = detailsList.iterator();

                                    while(var4.hasNext()) {
                                        SkuDetails skuDetails = (SkuDetails)var4.next();

                                        try {
                                            JSONObject object = new JSONObject(skuDetails.getOriginalJson());
                                            productDetails.add(new com.martinforget.billingmanager.SkuDetails(object));
                                        } catch (JSONException var7) {
                                            var7.printStackTrace();
                                        }
                                    }
                                }

                                BillingProcessor.this.reportSkuDetailsResponseCaller(productDetails, listener);
                            } else {
                                BillingProcessor.this.reportBillingError(response, (Throwable)null);
                                String errorMessage = String.format(Locale.US, "Failed to retrieve info for %d products, %d", productIdList.size(), response);
                                Log.e("iabv3", errorMessage);
                                BillingProcessor.this.reportSkuDetailsErrorCaller(errorMessage, listener);
                            }

                        }
                    });
                } catch (Exception var6) {
                    Log.e("iabv3", "Failed to call getSkuDetails", var6);
                    this.reportBillingError(112, var6);
                    this.reportSkuDetailsErrorCaller(var6.getLocalizedMessage(), listener);
                }

            } else {
                this.reportSkuDetailsErrorCaller("Empty products list", listener);
            }
        } else {
            this.reportSkuDetailsErrorCaller("Failed to call getSkuDetails. Service may not be connected", listener);
        }
    }

    @Nullable
    public PurchaseInfo getPurchaseInfo(String productId) {
        return this.getPurchaseInfo(productId, this.cachedProducts);
    }

    @Nullable
    public PurchaseInfo getSubscriptionPurchaseInfo(String productId) {
        return this.getPurchaseInfo(productId, this.cachedSubscriptions);
    }

    public void getSubscriptionListingDetailsAsync(String productId, ISkuDetailsResponseListener listener) {
        this.getSkuDetailsAsync(productId, "subs", listener);
    }

    private String detectPurchaseTypeFromPurchaseResponseData(JSONObject purchase) {
        String purchasePayload = this.getPurchasePayload();
        if (!TextUtils.isEmpty(purchasePayload) && purchasePayload.startsWith("subs")) {
            return "subs";
        } else {
            return purchase != null && purchase.has("autoRenewing") ? "subs" : "inapp";
        }
    }

    private void verifyAndCachePurchase(Purchase purchase) {
        String purchaseData = purchase.getOriginalJson();
        String dataSignature = purchase.getSignature();

        try {
            JSONObject purchaseJsonObject = new JSONObject(purchaseData);
            String productId = purchaseJsonObject.getString("productId");
            if (this.verifyPurchaseSignature(productId, purchaseData, dataSignature)) {
                String purchaseType = this.detectPurchaseTypeFromPurchaseResponseData(purchaseJsonObject);
                BillingCache cache = purchaseType.equals("subs") ? this.cachedSubscriptions : this.cachedProducts;
                cache.put(productId, purchaseData, dataSignature);
                if (this.eventHandler != null) {
                    PurchaseInfo purchaseInfo = new PurchaseInfo(purchaseData, dataSignature, this.getPurchasePayload());
                    this.reportProductPurchased(productId, purchaseInfo);
                }
            } else {
                Log.e("iabv3", "Public key signature doesn't match!");
                this.reportBillingError(102, (Throwable)null);
            }
        } catch (Exception var9) {
            Log.e("iabv3", "Error in verifyAndCachePurchase", var9);
            this.reportBillingError(110, var9);
        }

        this.savePurchasePayload((String)null);
    }

    private boolean verifyPurchaseSignature(String productId, String purchaseData, String dataSignature) {
        try {
            return TextUtils.isEmpty(this.signatureBase64) || Security.verifyPurchase(productId, this.signatureBase64, purchaseData, dataSignature);
        } catch (Exception var5) {
            return false;
        }
    }


    private boolean isPurchaseHistoryRestored() {
        return this.loadBoolean(this.getPreferencesBaseKey() + ".products.restored.v2_6", false);
    }

    private void setPurchaseHistoryRestored() {
        this.saveBoolean(this.getPreferencesBaseKey() + ".products.restored.v2_6", true);
    }

    private void savePurchasePayload(String value) {
        this.saveString(this.getPreferencesBaseKey() + ".purchase.last.v2_6", value);
    }

    private String getPurchasePayload() {
        return this.loadString(this.getPreferencesBaseKey() + ".purchase.last.v2_6", (String)null);
    }

    private void reportBillingError(int errorCode, Throwable error) {
        if (this.eventHandler != null && this.handler != null) {
            this.handler.post(() -> {
                this.eventHandler.onBillingError(errorCode, error);
            });
        }

    }

    private void reportPurchasesSuccess(BillingProcessor.IPurchasesResponseListener listener) {
        if (listener != null && this.handler != null) {
            this.handler.post(() -> {
                listener.onPurchasesSuccess();
            });
        }

    }

    private void reportPurchasesError(BillingProcessor.IPurchasesResponseListener listener) {
        if (listener != null && this.handler != null) {
            this.handler.post(() -> {
                listener.onPurchasesError();
            });
        }

    }

    private void reportSkuDetailsErrorCaller(String error, BillingProcessor.ISkuDetailsResponseListener listener) {
        if (listener != null && this.handler != null) {
            this.handler.post(() -> {
                listener.onSkuDetailsError(error);
            });
        }

    }

    private void reportSkuDetailsResponseCaller(@Nullable List<com.martinforget.billingmanager.SkuDetails> products, BillingProcessor.ISkuDetailsResponseListener listener) {
        if (listener != null && this.handler != null) {
            this.handler.post(() -> {
                listener.onSkuDetailsResponse(products);
            });
        }

    }

    private void reportProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {
        if (this.eventHandler != null && this.handler != null) {
            this.handler.post(() -> {
                this.eventHandler.onProductPurchased(productId, details);
            });
        }

    }

    private void handlePurchase(final Purchase purchase) {
        if (purchase.getPurchaseState() == 1) {
            if (purchase.isAcknowledged()) {
                this.verifyAndCachePurchase(purchase);
            } else {
                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
                this.billingService.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
                    public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                        if (billingResult.getResponseCode() == 0) {
                            BillingProcessor.this.verifyAndCachePurchase(purchase);
                        } else {
                            BillingProcessor.this.reportBillingError(115, (Throwable)null);
                        }

                    }
                });
            }
        }

    }

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2012, 11, 5);
        DATE_MERCHANT_LIMIT_1 = calendar.getTime();
        calendar.set(2015, 6, 21);
        DATE_MERCHANT_LIMIT_2 = calendar.getTime();
    }

    private class HistoryInitializationTask extends AsyncTask<Void, Void, Boolean> {
        private HistoryInitializationTask() {
        }

        protected Boolean doInBackground(Void... nothing) {
            if (!BillingProcessor.this.isPurchaseHistoryRestored()) {
                BillingProcessor.this.loadOwnedPurchasesFromGoogleAsync((BillingProcessor.IPurchasesResponseListener)null);
                return true;
            } else {
                return false;
            }
        }

        protected void onPostExecute(Boolean restored) {
            BillingProcessor.this.isHistoryTaskExecuted = true;
            if (restored) {
                BillingProcessor.this.setPurchaseHistoryRestored();
                if (BillingProcessor.this.eventHandler != null) {
                    BillingProcessor.this.eventHandler.onPurchaseHistoryRestored();
                }
            }

            if (BillingProcessor.this.eventHandler != null) {
                BillingProcessor.this.eventHandler.onBillingInitialized();
            }

        }
    }

    public interface ISkuDetailsResponseListener {
        void onSkuDetailsResponse(@Nullable List<com.martinforget.billingmanager.SkuDetails> var1);

        void onSkuDetailsError(String var1);
    }

    public interface IPurchasesResponseListener {
        void onPurchasesSuccess();

        void onPurchasesError();
    }

    public interface IBillingHandler {
        void onProductPurchased(@NonNull String var1, @Nullable PurchaseInfo var2);

        void onPurchaseHistoryRestored();

        void onBillingError(int var1, @Nullable Throwable var2);

        void onBillingInitialized();
    }
}
