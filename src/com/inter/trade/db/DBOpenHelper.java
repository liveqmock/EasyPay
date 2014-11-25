package com.inter.trade.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static DBOpenHelper m_dbOpenHelper;
	//数据库版本号
	private static final int db_version = 1;
//	private int old_version = 0;

	private DBOpenHelper(Context context) {
		super(context, "kvpioneer.db", null, db_version);
	}

	public synchronized static DBOpenHelper getInstance(Context context) {
		if (m_dbOpenHelper == null) {
			m_dbOpenHelper = new DBOpenHelper(context);
		}
		return m_dbOpenHelper;
	}
	String sql = "CREATE TABLE IF NOT EXISTS TITLE ("
			+ "ID INTEGER PRIMARY KEY," + "NAME text not null"
			+");";
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
