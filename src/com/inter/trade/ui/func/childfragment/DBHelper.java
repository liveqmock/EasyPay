package com.inter.trade.ui.func.childfragment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{

	public DBHelper(Context context)
	{
		super(context, "MultiDownLoad.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE fileDownloading(_id integer primary key autoincrement,downPath varchar(100),threadId INTEGER,downLength INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{

	}

}