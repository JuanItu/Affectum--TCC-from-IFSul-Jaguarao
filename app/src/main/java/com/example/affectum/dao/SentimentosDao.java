package com.example.affectum.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.affectum.bean.Sentimentos;

import java.util.ArrayList;
import java.util.List;

public class SentimentosDao extends SQLiteOpenHelper {

    public static final String NOME_BANCO = "banco.db";
    private static final int VERSAO_BANCO = 1;
    Context cX;

    public SentimentosDao(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
        cX = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Sentimentos (_id integer primary key autoincrement, tipo text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Sentimentos (_id integer primary key autoincrement, tipo text)");
    }

    public void createTableSentimentos(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS Sentimentos (_id integer primary key autoincrement, tipo text)");
    }

    public boolean salva(Sentimentos sentimentos) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = null;

        if (sentimentos.getId() == null) {
            sql = "INSERT INTO Sentimentos (tipo) VALUES ('" + sentimentos.getTipo() + "');";
        } else {
            sql = "UPDATE Sentimentos SET tipo = '" + sentimentos.getTipo() + "' WHERE _id = " + sentimentos.getId();
        }

        try {
            db.execSQL(sql);
        } finally {
            db.close();
        }
        return true;
    }

    public Cursor retornaTodos(Sentimentos sentimentos) {

        Cursor cursor = null;
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select * from Sentimentos where _id is not null";

        if (sentimentos != null) {
            if (sentimentos.getId() != null) {
                sql = sql + " and _id = " + sentimentos.getId();
            }

            if(sentimentos.getId() != null) {
                sql = sql + "and tipo = '" + sentimentos.getTipo() + "' ";
            }
        }

        sql = sql + "order by tipo asc";

        cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    @SuppressLint("Range")
    public Sentimentos retornaSentimentos(Sentimentos sentimentos) {

        Sentimentos retornaSentimentos = null;

        Cursor cursor;
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select * from sentimentos where _id is not null ";

        if (sentimentos != null) {
            if (sentimentos.getId() != null) {
                sql = sql + " and _id = " + sentimentos.getId();
            }

            if (sentimentos.getTipo() != null) {

            sql = sql + " and tipo = '" + sentimentos.getTipo() + "' ";
        }
    }
        sql = sql + "order by tipo asc ";

        cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            if (cursor.moveToFirst()){

                retornaSentimentos = new Sentimentos();
                retornaSentimentos.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                retornaSentimentos.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));
            }
        }
        db.close();

        return retornaSentimentos;
    }
    @SuppressLint("Range")
    public Cursor retornaSentimentos(){
        String sql = "select _id, tipo from sentimentos where _id is not null order by tipo";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        db.close();
        return cursor;
    }

    public void exclui(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM sentimentos WHERE _id = '" + id + "'"; //VERIFICAR -- faltando ID?
        try {
            db.execSQL(sql);
        } finally {
            db.close();

        }
    }


   /* @SuppressLint("Range")
    public List<String> retornaSentimetos(){
        List<String> list = new ArrayList<>();

        String sql = "select tipo from sentimentos where _id is not null";

        sql = sql + " order by tipo";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        list.add("");

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex("tipo")));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }*/
  /* @SuppressLint("Range")
   public List<String> retornaSentimentos() {
       List<String> list = new ArrayList<>();

       String sql = "select * from sentimentos where _id is not null";

       SQLiteDatabase db = this.getReadableDatabase();
       Cursor cursor = db.rawQuery(sql, null);

       if (cursor.moveToFirst()) {
           do {
               Sentimentos sentimento = new Sentimentos();
               sentimento.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));
               // Aqui você pode configurar outros atributos de acordo com a sua classe Sentimentos

               //list.add(sentimento);
           } while (cursor.moveToNext());
       }

       cursor.close();
       db.close();

       return list;
   }
   */
   @SuppressLint("Range")
   public List<String> retornaSentimentoList() {
       List<String> classeList = new ArrayList<>();

       String sql = "SELECT tipo FROM Sentimentos ORDER BY tipo";
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor cursor = db.rawQuery(sql, null);

       if (cursor != null) {
           try {
               if (cursor.moveToFirst()) {
                   do {
                       classeList.add(cursor.getString(cursor.getColumnIndex("tipo")));
                   } while (cursor.moveToNext());
               }
           } finally {
               cursor.close();
           }
       }

       db.close();
       return classeList;
   }
    public Cursor retornaTodosLista() {

        Cursor cursor;
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select _id, (tipo)as linha from sentimentos where _id is not null";

        sql = sql + "order by tipo";

        cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }
    @SuppressLint("Range")
    public Integer sentimentoisId(String sentimentoTipo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Integer id = null;

        String sql = "SELECT _id FROM Sentimentos WHERE tipo = ? LIMIT 1";
        Cursor cursor = db.rawQuery(sql, new String[]{sentimentoTipo});

        try {
            if (cursor.moveToFirst()) {
                id = cursor.getInt(cursor.getColumnIndex("_id"));
            }
        } finally {
            cursor.close();
            db.close();
        }

        return id;
    }

}