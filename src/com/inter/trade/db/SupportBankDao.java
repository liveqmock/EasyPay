package com.inter.trade.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SUPPORT_BANK.
*/
public class SupportBankDao extends AbstractDao<SupportBank, Long> {

    public static final String TABLENAME = "SUPPORT_BANK";

    /**
     * Properties of entity SupportBank.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Bankid = new Property(1, String.class, "bankid", false, "BANKID");
        public final static Property Bankno = new Property(2, String.class, "bankno", false, "BANKNO");
        public final static Property Bankname = new Property(3, String.class, "bankname", false, "BANKNAME");
    };


    public SupportBankDao(DaoConfig config) {
        super(config);
    }
    
    public SupportBankDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SUPPORT_BANK' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'BANKID' TEXT," + // 1: bankid
                "'BANKNO' TEXT," + // 2: bankno
                "'BANKNAME' TEXT);"); // 3: bankname
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SUPPORT_BANK'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SupportBank entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String bankid = entity.getBankid();
        if (bankid != null) {
            stmt.bindString(2, bankid);
        }
 
        String bankno = entity.getBankno();
        if (bankno != null) {
            stmt.bindString(3, bankno);
        }
 
        String bankname = entity.getBankname();
        if (bankname != null) {
            stmt.bindString(4, bankname);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SupportBank readEntity(Cursor cursor, int offset) {
        SupportBank entity = new SupportBank( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // bankid
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // bankno
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // bankname
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SupportBank entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBankid(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setBankno(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setBankname(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(SupportBank entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(SupportBank entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
