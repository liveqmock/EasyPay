/*
 * @Title:  DBHelper.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月1日 下午4:04:08
 * @version:  V1.0
 */
package com.inter.trade.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.inter.trade.db.DaoMaster.DevOpenHelper;
import com.inter.trade.ui.PayApp;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * 数据库帮助工具
 * 
 * @author ChenGuangChi
 * @data: 2014年7月1日 下午4:04:08
 * @version: V1.0
 */
public class DBHelper {
	private static Context mContext;
    private static DBHelper instance;
    private static RecordDao recordDao;
    private static SQLiteDatabase db;
    private static SupportBankDao bankDao;
	private DBHelper() {
	}

	public static DBHelper getInstance(Context context) {
		if (instance == null) {
			instance = new DBHelper();
		}
		DevOpenHelper helper = PayApp.getHelper();
		if(helper==null){
			helper= new DaoMaster.DevOpenHelper(context, "notes-db", null);
		}
		db = helper.getWritableDatabase();
		DaoMaster master=new DaoMaster(db);
		DaoSession daoSession =master.newSession();
		recordDao = daoSession.getRecordDao();
		bankDao=daoSession.getSupportBankDao();
		return instance;
	}
	
	/**
	 * 添加一条数据或者更新数据
	 * TODO<请描述这个方法是干什么的>
	 * @param key
	 * @throw
	 * @return void
	 */
	public void insert(int key){
		// 数据库对象
		
		QueryBuilder<Record> builder = recordDao.queryBuilder();
		builder.where(RecordDao.Properties.Key.eq(key));
		long count = builder.count();
		
		System.out.println("数据条数："+count);
		if(count<=0){//记录为空的时候
			Record record=new Record(null, 1, key);
			recordDao.insert(record);
		}else{//有记录的时候
			List<Record> list = builder.list();
			Record record =list.get(0);
			record.setNumber(record.getNumber()+1);
			recordDao.update(record);
		}
	}
	
	/**
	 * 获取数据的条数 
	 * @return
	 * @throw
	 * @return long
	 */
	public long getCount(){
		return recordDao.count();
	}
	
	/**
	 * 查询前九条数据 
	 * @return
	 * @throw
	 * @return ArrayList<Record>
	 */
	public ArrayList<Record> queryAll(){
		ArrayList<Record> result=null;
		QueryBuilder<Record> builder = recordDao.queryBuilder();
		builder.orderDesc(RecordDao.Properties.Number);
		List<Record> list = builder.list();
		if(list!=null){
			result=new ArrayList<Record>();
			for(int i=0;i<list.size();i++){
				if(i==9){
					break;
				}
				result.add(list.get(i));
			}
		}
		return result;
	}
	
	/**
	 * 关闭数据库
	 * @throw
	 * @return void
	 */
	public void closeDB(){
		if(db!=null&& db.isOpen()){
			db.close();
		}
	}
	
	/**
	 * 批量添加数据(易宝支持的银行) 
	 * @param list
	 * @throw
	 * @return void
	 */
	public void insertBanks(ArrayList<SupportBank> list){
		if(queryBanks()==null|| queryBanks().size()==0){
			bankDao.insertInTx(list);
		}
	}
	
	/**
	 * 查询易宝所有支持的银行 
	 * @return
	 * @throw
	 * @return List<SupportBank>
	 */
	public List<SupportBank> queryBanks(){
		QueryBuilder<SupportBank> qb = bankDao.queryBuilder();
		return qb.list();
	}
	
}
