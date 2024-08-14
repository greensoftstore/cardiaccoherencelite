package com.martinforget.billingmanager;

public enum PurchaseState {
    PurchasedSuccessfully,
    Canceled,
    Refunded,
    SubscriptionExpired;

    private PurchaseState() {
    }
}
