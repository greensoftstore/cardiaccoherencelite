package com.martinforget;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

import com.martinforget.cardiaccoherencelite.R;


public class SessionHistoryDatabase extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "SessionHistoryDatabase";

	private static final int DATABASE_VERSION = 2;
	public final Context mContext;

	private static final String DATABASE_ALTER_TABLE1 = "ALTER TABLE session_history ADD COLUMN heart_start INTEGER DEFAULT 0" ;

	private static final String DATABASE_ALTER_TABLE2 = "ALTER TABLE session_history ADD COLUMN heart_end INTEGER DEFAULT 0" ;

	public SessionHistoryDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String[] sql = mContext.getString(
				R.string.session_history_database_create).split("\n");
		db.beginTransaction();
		try {
			execMultipleSQL(db, sql);
			db.setTransactionSuccessful();

		} catch (SQLException e) {
			Log.e("error connected", e.toString());

		} finally {
			db.endTransaction();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


		Log.v("Upgrade Version", "old="+oldVersion+" New="+newVersion);

		if (newVersion > oldVersion)
		{
			db.execSQL("DROP TABLE session_history");

			String[] sql = mContext.getString(
					R.string.session_history_database_create).split("\n");
			db.beginTransaction();
			try {
				execMultipleSQL(db, sql);
				db.setTransactionSuccessful();

			} catch (SQLException e) {
				Log.e("error connected", e.toString());

			} finally {
				db.endTransaction();
			}}
		/*
		 * String[] sql=
		 * mContext.getString(R.string.session_history_database_update
		 * ).split("\n"); db.beginTransaction(); try{ execMultipleSQL(db,sql);
		 * db.setTransactionSuccessful();
		 * 
		 * }catch(SQLException e){ Log.e("error with upgrade",e.toString());
		 * 
		 * }finally{ db.endTransaction(); }
		 * 
		 * onCreate(db);
		 */

	}

	private void execMultipleSQL(SQLiteDatabase db, String[] sql) {

		for (String s : sql)
			if (s.trim().length() > 0)
				db.execSQL(s);

	}

	public SessionHistoryCursor getSessionHistoryCursor() {
		String sql = SessionHistoryCursor.QUERY;
		SQLiteDatabase dataBase = getReadableDatabase();
		SessionHistoryCursor cursor = (SessionHistoryCursor) dataBase
				.rawQueryWithFactory(new SessionHistoryCursor.Factory(), sql,
						null, null);
		cursor.moveToFirst();
		return cursor;

	}

	public SessionHistoryCursor getSessionCursor() {

		String sql = SessionHistoryCursor.QUERY;
		SQLiteDatabase dataBase = getReadableDatabase();
		SessionHistoryCursor cursor = (SessionHistoryCursor) dataBase
				.rawQueryWithFactory(new SessionHistoryCursor.Factory(), sql,
						null, null);
		cursor.moveToFirst();
		return cursor;

	}

	public static class SessionHistoryCursor extends SQLiteCursor {
		//
		public SessionHistoryCursor(SQLiteDatabase db,
				SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
			super(db, driver, editTable, query);
			// TODO Auto-generated constructor stub
		}

		public static enum SortBy {
			title, id
		}

		public static final String QUERY = "select * " + "from session_history";

		public static final String QUERYID = "select * "
				+ "from session_history" + "where id=last_id";

		private static class Factory implements SQLiteDatabase.CursorFactory {

			@Override
			public Cursor newCursor(SQLiteDatabase db,
					SQLiteCursorDriver masterQuery, String editTable,
					SQLiteQuery query) {

				return new SessionHistoryCursor(db, masterQuery, editTable,
						query);
			}

		}

		public String getStartTime() {
			Log.e("start_time", "session");
			return getString(getColumnIndexOrThrow("start_time"));
		}

		public long getCycles() {
			return getLong(getColumnIndexOrThrow("number_cycles"));
		}

		public int getRatio() {
			return getInt(getColumnIndexOrThrow("ratio"));
		}

		public int getDuration() {
			return getInt(getColumnIndexOrThrow("duration"));
		}

		public int getHeartBegin() {
			return getInt(getColumnIndexOrThrow("heart_begin"));
		}

		public int getHeartEnd() {
			return getInt(getColumnIndexOrThrow("heart_end"));
		}



	}

	//delete all session history
	public void deleteSession(){
		
		try {
			getWritableDatabase().delete("session_history", null, null);

		} catch (SQLException e) {
			Log.e("Error with new session", String.valueOf(e));

		}
		
		
		
		
		
	}
	
	//add session on history
	public void addSession(String startTime, String endTime, long duration,
			int cyclenumber, int ratiovalue, int heartBegin, int heartEnd) {

		ContentValues map = new ContentValues();
		map.put("start_time", startTime);
		map.put("end_time", endTime);
		map.put("duration", duration);
		map.put("number_cycles", cyclenumber);
		map.put("ratio", ratiovalue);
		map.put("heart_begin", heartBegin);
		map.put("heart_end", heartEnd);

		try {
			getWritableDatabase().insert("session_history", null, map);

		} catch (SQLException e) {
			Log.e("Error with new session", String.valueOf(e));

		}

	}

}
