package com.android.base.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseManager {

	private Context mContext;
	
	private String DBName;
	
	private int DBVersion;

	private static SQLiteDatabase dbRead;
	
	private static SQLiteDatabase dbWrite;

	public DataBaseManager(Context context,String DBName,int DBVersion) {
		this.mContext = context;
		this.DBName = DBName;
		this.DBVersion = DBVersion;
	}
	
	//使用多SQLiteDatabase实例模式操纵数据库
	public SQLiteDatabase getReadableDB() {
		return DatabaseHelper.getInstance(mContext,DBName,DBVersion).getWritableDatabase();
	}
	
	public SQLiteDatabase getWritableDB() {
		return DatabaseHelper.getInstance(mContext,DBName,DBVersion).getWritableDatabase();
	}

	//使用单SQLiteDatabase实例模式获取数据库操作权限
	public static SQLiteDatabase getWritableDatabase(Context context,String dbname,int dbversion) {
		if(null==dbWrite){
			dbWrite =DatabaseHelper.getInstance(context,dbname,dbversion).getWritableDatabase();
		}
		return dbWrite;
	}

	public static SQLiteDatabase getReadableDatabase(Context context,String dbname,int dbversion) {
		if(null==dbRead){
			dbRead =DatabaseHelper.getInstance(context,dbname,dbversion).getReadableDatabase();
		}
		return dbRead;
	}

}

