package com.martinforget.billingmanager;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class Security {

    Security() {
    }

    public static boolean verifyPurchase(String productId, String base64PublicKey, String signedData, String signature) {
        if (!TextUtils.isEmpty(signedData) && !TextUtils.isEmpty(base64PublicKey) && !TextUtils.isEmpty(signature)) {
            PublicKey key = generatePublicKey(base64PublicKey);
            return verify(key, signedData, signature);
        } else if (!productId.equals("android.test.purchased") && !productId.equals("android.test.canceled") && !productId.equals("android.test.refunded") && !productId.equals("android.test.item_unavailable")) {
            Log.e("IABUtil/Security", "Purchase verification failed: missing data.");
            return false;
        } else {
            return true;
        }
    }

    public static PublicKey generatePublicKey(String encodedPublicKey) {
        try {
            byte[] decodedKey = Base64.decode(encodedPublicKey, 0);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
        } catch (NoSuchAlgorithmException var3) {
            throw new RuntimeException(var3);
        } catch (InvalidKeySpecException var4) {
            Log.e("IABUtil/Security", "Invalid key specification.");
            throw new IllegalArgumentException(var4);
        } catch (IllegalArgumentException var5) {
            Log.e("IABUtil/Security", "Base64 decoding failed.");
            throw var5;
        }
    }

    public static boolean verify(PublicKey publicKey, String signedData, String signature) {
        try {
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(publicKey);
            sig.update(signedData.getBytes());
            if (!sig.verify(Base64.decode(signature, 0))) {
                Log.e("IABUtil/Security", "Signature verification failed.");
                return false;
            }

            return true;
        } catch (NoSuchAlgorithmException var5) {
            Log.e("IABUtil/Security", "NoSuchAlgorithmException.");
        } catch (InvalidKeyException var6) {
            Log.e("IABUtil/Security", "Invalid key specification.");
        } catch (SignatureException var7) {
            Log.e("IABUtil/Security", "Signature exception.");
        } catch (IllegalArgumentException var8) {
            Log.e("IABUtil/Security", "Base64 decoding failed.");
        }

        return false;
    }
}
