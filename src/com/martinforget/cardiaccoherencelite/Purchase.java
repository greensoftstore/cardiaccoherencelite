package com.martinforget.cardiaccoherencelite;

import android.content.Context;
import android.util.Log;
import com.martinforget.Data;
public class Purchase {

	public static String SUBS_PREMIUM_VERSION = "sku.premium.subscription";
	public static String PREMIUM_VERSION = "sku.premium.version";
	public static String EXPERT_ITEM = "sku.expert.item";
	public static String NOTIFICATION_ITEM = "sku.notification.item";
	public static String MONITOR_ITEM = "sku.monitor.item";
	public static String PROFILE_ITEM = "sku.profile.item";
	public static String PURCHASE_NAME = "xW35Dcdfd4223";

	private static String AppKey3 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzh8NvogbaB7EhgfvZWotYoy+GdURXrMGxkxRsU5U";
	private static String AppKey1 = "vBuaHI3gEXDvhH8JoTBfNEwQL78MHyZDgtBqt+aH9hfALnse96FbyiVvTAj7fQo74JHB3MsbLPb8t/RfDheJfwY5v0MNPCHM0jvZD+TUfKQmtRtwG9lo8MuGYT";
	private static String AppKey2 = "+Rh9OiCwQeJkOZy89UOOTGK5d2BpUb5/89MUs6JVHQ+t4zBvOUSTT79dtidMm7lmFFgBWiwJzC6g6ijr4j2MW1AbDbP6PxBiASi0Vl8eiwz8CeVgc/SwpbwkpogJlaJwHzobNMIlPz1PcHol9AEVl+2VYe6DKN1LwjwIJrKVxvxO8bWRRHnQIDAQAB";
	private static String AppKey;

	private Context context;

	public Data data;

	Purchase(Context context) {
		this.context = context;
		data = Data.getInstance(this.context);

		Log.d("data", "data=" + data);

		AppKey = AppKey3 + AppKey1 + AppKey2;
	}

	public String getAppKey() {
		return AppKey;
	}

	public void setPurchasedPremiumStatus(boolean status) {

		if (status)
			data.StoreDataString("az1tgdgg32k63",
					"ddjk39fkr93l45j89".toString());
		else
			data.StoreDataString("az1tgdgg32k63",
					"ljsdfakl245r2w98sd9k".toString());
	}

	public boolean isPremiumPurchased() {
		boolean returnValue = false;
		// Log.d("Comparing", data.ReadDataString("az1tgdgg32k63") + " to "
		// +"ddjk39fkr93l45j89");
		if (data.ReadDataString("az1tgdgg32k63") != null) {
			if (data.ReadDataString("az1tgdgg32k63")
					.equals("ddjk39fkr93l45j89")) {
				returnValue = true;
			}
		}
		return returnValue;
	}

	public void setPurchasedExpertStatus(boolean status) {
		if (status)
			data.StoreDataString("sdfdgdfgh345gd32234k21342",
					"sfdk3klfd09sln43450jsed0".toString());
		else
			data.StoreDataString("sdfdgdfgh345gd32234k21342",
					"ljsdfakl245r2w98sd9k".toString());
	}

	public boolean isExpertPurchased() {
		boolean returnValue = false;
		// Log.d("Comparing", data.ReadDataString("az1tgdgg32k63") + " to "
		// +"ddjk39fkr93l45j89");
		if (data.ReadDataString("sdfdgdfgh345gd32234k21342") != null) {
			if (data.ReadDataString("sdfdgdfgh345gd32234k21342")
					.equals("sfdk3klfd09sln43450jsed0")) {
				returnValue = true;
			}
		}
		return returnValue;
	}

	public void setPurchasedNotificationStatus(boolean status) {
		if (status)
			data.StoreDataString("ll589fgdiod034ererot034jowe",
					"6kfgd9kj3sfdsklewerthdfkjert".toString());
		else
			data.StoreDataString("ll589fgdiod034ererot034jowe",
					"ljsdfakl245r2w98sd9k".toString());
	}

	public boolean isNotificationPurchased() {
		boolean returnValue = false;
		// Log.d("Comparing", data.ReadDataString("az1tgdgg32k63") + " to "
		// +"ddjk39fkr93l45j89");
		if (data.ReadDataString("ll589fgdiod034ererot034jowe") != null) {
			if (data.ReadDataString("ll589fgdiod034ererot034jowe")
					.equals("6kfgd9kj3sfdsklewerthdfkjert")) {
				returnValue = true;

			}
		}
		return returnValue;
	}

	public void setPurchasedMonitorStatus(boolean status) {
		if (status)
			data.StoreDataString("kjuhw34esauiui3s8suseujhjhseddfgvmsz",
					"fgdkjkj09fgh09ijoweruihfdnwqknhtyrvcbzxleiower".toString());

		else
			data.StoreDataString("kjuhw34esauiui3s8suseujhjhseddfgvmsz",
					"ljsdfakl245r2w98sd9k".toString());

	}

	public boolean isMonitorPurchased() {
		boolean returnValue = false;
		// Log.d("Comparing", data.ReadDataString("az1tgdgg32k63") + " to "
		// +"ddjk39fkr93l45j89");
		if (data.ReadDataString("kjuhw34esauiui3s8suseujhjhseddfgvmsz") != null) {
			if (data.ReadDataString("kjuhw34esauiui3s8suseujhjhseddfgvmsz")
					.equals("fgdkjkj09fgh09ijoweruihfdnwqknhtyrvcbzxleiower")) {
				returnValue = true;
			}
		}
		return returnValue;
	}


	public void setPurchasedProfileStatus(boolean status) {
		if (status)
			data.StoreDataString("xx43jhsdvbeo4sad0439d090sdj",
					"pfdgklmlmfds897sdgw23jsadkj2324khkjnb234kg".toString());

		else
			data.StoreDataString("xx43jhsdvbeo4sad0439d090sdj",
					"vlkfdkljeriofdknfgdgioh32jerk892".toString());

	}

	public boolean isProfilePurchased() {
		boolean returnValue = false;
		if (data.ReadDataString("xx43jhsdvbeo4sad0439d090sdj") != null) {
			if (data.ReadDataString("xx43jhsdvbeo4sad0439d090sdj")
					.equals("pfdgklmlmfds897sdgw23jsadkj2324khkjnb234kg")) {
				returnValue = true;
			}
		}
		return returnValue;
	}
}
