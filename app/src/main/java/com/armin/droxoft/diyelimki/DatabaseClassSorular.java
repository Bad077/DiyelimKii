package com.armin.droxoft.diyelimki;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseClassSorular {

    private static final String DATABASENAME= "Sxflques.db";
    private static final String TABLENAME = "SoruTablosu";
    private static final int DATABASEVERSION = 1;

    private static final String ROWID = "_id";
    private static final String SORUID = "soruid";
    private static final String WHATIF = "whatif";
    private static final String RESULT = "result";
    private static final String KATEGORI = "kategori";
    private static final String YES = "yes";
    private static final String NO = "no";
    private static final String USERID = "userid";

    Context context;
    private static SQLiteDatabase sqLiteDatabase;


    public DatabaseClassSorular(Context context){
        this.context = context;
    }

    public DatabaseClassSorular open(){
        DbHelper dbHelper = new DbHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        sqLiteDatabase.close();
    }

    public long olustur(String soruid, String whatif , String result , String kategori , String yes, String no , String userid){

        boolean oncedenvar = false;
        List<String> varolanlar = databasedenidcek();
        for(int i = 0 ; i <varolanlar.size() ; i++){
            if(soruid.equals(varolanlar.get(i))){
                oncedenvar = true;
            }
        }
        if(!oncedenvar) {
            ContentValues cV = new ContentValues();
            cV.put(SORUID, soruid);
            cV.put(WHATIF, whatif);
            cV.put(RESULT, result);
            cV.put(KATEGORI, kategori);
            cV.put(YES, yes);
            cV.put(NO, no);
            cV.put(USERID, userid);
            return sqLiteDatabase.insert(TABLENAME, null, cV);
        }
        return 15;
    }

    public List<String> databasedenidcek(){
        String[] kolonlar = new String[]{ROWID, SORUID, WHATIF, RESULT, KATEGORI, YES, NO, USERID};
        Cursor c = sqLiteDatabase.query(TABLENAME, kolonlar, null, null, null, null, null);
        List<String> kayitliidler = new ArrayList<>();
        int karsiidindexi = c.getColumnIndex(SORUID);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            kayitliidler.add(c.getString(karsiidindexi));
        }
        c.close();
        return kayitliidler;
    }

    public String soruidcek(int a) {
        String[] kolonlar = new String[]{ROWID, SORUID, WHATIF, RESULT, KATEGORI, YES, NO, USERID};
        Cursor c = sqLiteDatabase.query(TABLENAME, kolonlar, ROWID + "='" + a + "'", null, null, null, null);
        List<String> kayitlisoruidler = new ArrayList<>();
        int soruidindexi = c.getColumnIndex(SORUID);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            kayitlisoruidler.add(c.getString(soruidindexi));
        }
        c.close();
        return kayitlisoruidler.get(0);
    }

    public String whatifcek(int a) {
        String[] kolonlar = new String[]{ROWID, SORUID, WHATIF, RESULT, KATEGORI, YES, NO, USERID};
        Cursor c = sqLiteDatabase.query(TABLENAME, kolonlar, ROWID + "='" + a + "'", null, null, null, null);
        List<String> kayitliwhatifler = new ArrayList<>();
        int whatifindexi = c.getColumnIndex(WHATIF);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            kayitliwhatifler.add(c.getString(whatifindexi));
        }
        c.close();
        return kayitliwhatifler.get(0);
    }

    public String resultcek(int a) {
        String[] kolonlar = new String[]{ROWID, SORUID, WHATIF, RESULT, KATEGORI, YES, NO, USERID};
        Cursor c = sqLiteDatabase.query(TABLENAME, kolonlar, ROWID + "='" + a + "'", null, null, null, null);
        List<String> kayitliresultlar = new ArrayList<>();
        int resultindexi = c.getColumnIndex(RESULT);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            kayitliresultlar.add(c.getString(resultindexi));
        }
        c.close();
        return kayitliresultlar.get(0);
    }

    public String yescek(int a) {
        String[] kolonlar = new String[]{ROWID, SORUID, WHATIF, RESULT, KATEGORI, YES, NO, USERID};
        Cursor c = sqLiteDatabase.query(TABLENAME, kolonlar, ROWID + "='" + a + "'", null, null, null, null);
        List<String> kayitliyesler = new ArrayList<>();
        int yesindexi = c.getColumnIndex(YES);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            kayitliyesler.add(c.getString(yesindexi));
        }
        c.close();
        return kayitliyesler.get(0);
    }

    public String nocek(int a) {
        String[] kolonlar = new String[]{ROWID, SORUID, WHATIF, RESULT, KATEGORI, YES, NO, USERID};
        Cursor c = sqLiteDatabase.query(TABLENAME, kolonlar, ROWID + "='" + a + "'", null, null, null, null);
        List<String> kayitlinolar = new ArrayList<>();
        int noindexi = c.getColumnIndex(NO);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            kayitlinolar.add(c.getString(noindexi));
        }
        c.close();
        return kayitlinolar.get(0);
    }


    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASENAME, null, DATABASEVERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLENAME + "(" + ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SORUID +" TEXT NOT NULL, "+ WHATIF + " TEXT NOT NULL, " + RESULT + " TEXT NOT NULL, "
                    + KATEGORI + " TEXT NOT NULL, " + YES + " TEXT NOT NULL, " + NO + " TEXT NOT NULL, "+USERID+ " TEXT NOT NULL);");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
            onCreate(db);
        }
    }
}
