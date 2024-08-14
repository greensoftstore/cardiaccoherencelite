package com.martinforget;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class SessionHistoryHelper {

	private static SessionHistory sessionHistory = new SessionHistory();

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd/MM/yy HH:mm");
	private static long startTime;
	private static long endTime;


	public static void setDuration(long duration) {

		sessionHistory.setDuration(duration);

	}

	public static void setEndTime(Date currentDate) {
		currentDate = new Date();
		endTime = currentDate.getTime();
		sessionHistory.setEndTime(dateFormat.format(endTime));

	}

	public static void addParameterSession(int sessiontime, int cyclenumber,
			int ratiovalue) {

		setStartTime(new Date());
		sessionHistory.setDuration(sessiontime * 60 * 1000);
		sessionHistory.setCycles(cyclenumber);
		sessionHistory.setRatio(ratiovalue);



	}

	public static void addSession() {

		setEndTime(new Date());

		sessionHistory.setDuration((endTime - startTime) / 1000);

		Log.e("SessionEnd", sessionHistory.toString());

	}

	public static void setStartTime(Date currentDate) {

		currentDate = new Date();
		startTime = currentDate.getTime();
		sessionHistory.setStartTime(dateFormat.format(startTime));

	}

	public static void eraseSession() {
		sessionHistory = null;

	}

	public static String getStartTime() {

		return sessionHistory.getStartTime();

	}

	public static String getEndTime() {

		return sessionHistory.getEndTime();

	}

	public static long getDuration() {

		return sessionHistory.getDuration();

	}

	public static int getCycle() {

		return sessionHistory.getCycles();

	}

	public static int getRatio() {

		return sessionHistory.getRatio();

	}

	public  static int getHeartBegin() {

		return sessionHistory.getHeartBegin();

	}

	public  static int getHeartEnd() {

		return sessionHistory.getHeartBegin();

	}


}
