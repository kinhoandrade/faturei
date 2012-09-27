package com.mobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter 
{ 
    public static final String KEY_ROWID = "_id";
    public static final String KEY_VALOR = "valor";
    public static final String KEY_DATA = "data";
    public static final String KEY_CARTAO = "cartao";
    public static final String KEY_DESCRICAO = "descricao";
    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "faturei";
    private static final String DATABASE_TABLE = "compras";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
        "create table if not exists compras (_id integer primary key autoincrement, "
        + "valor text not null, data text not null, descricao text not null, " 
        + "cartao text not null);";
        
    private final Context context;  
    
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.onCreate(getWritableDatabase());
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {        	
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                  + " to "
                  + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS compras");
            onCreate(db);
        }
    }    
    
    //---opens the database---
    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }
    
    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
	public long insertCompra(String valor, String data, String cartao, String descricao){
    	long retorno = 0;
    	try{
	        ContentValues initialValues = new ContentValues();
	        initialValues.put(KEY_VALOR, valor);
	        initialValues.put(KEY_DATA, data);
	        initialValues.put(KEY_CARTAO, cartao);
	        initialValues.put(KEY_DESCRICAO, descricao);
	        retorno = db.insert(DATABASE_TABLE, null, initialValues);
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	this.close();
        } 
        return retorno;
    }
    
    //---deletes a particular title---
    public boolean deleteValor(long rowId) 
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public int deleteAll(){
    	return db.delete(DATABASE_TABLE, "_id > 0", null);
    }

    public Cursor getAllCompras() 
    {
        return db.query(DATABASE_TABLE, new String[] {
        		KEY_ROWID, 
        		KEY_VALOR,
        		KEY_DATA,
                KEY_CARTAO,
                KEY_DESCRICAO}, 
                null, 
                null, 
                null, 
                null, 
                null);
    }
    
    public Cursor getAllCartoes() 
    {
        return db.query(DATABASE_TABLE, new String[] {
                KEY_CARTAO}, 
                null, 
                null, 
                null, 
                null, 
                null);
    }
    
    public Cursor getTitle(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                		KEY_ROWID, 
                		KEY_VALOR,
                		KEY_DATA,
                        KEY_CARTAO,
                        KEY_DESCRICAO
                		}, 
                		KEY_ROWID + "=" + rowId, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    //---updates a title---
    public boolean updateTitle(long rowId, String valor, String data, String cartao, String descricao) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_VALOR, valor);
        args.put(KEY_DATA, data);
        args.put(KEY_CARTAO, cartao);
        args.put(KEY_DESCRICAO, descricao);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}