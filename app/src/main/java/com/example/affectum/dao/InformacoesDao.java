package com.example.affectum.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.affectum.bean.Informacoes;

import java.util.ArrayList;
import java.util.List;

public class InformacoesDao extends SQLiteOpenHelper {

    public static final String NOME_BANCO = "banco.db";
    private static final int VERSAO_BANCO = 1;
    Context cX;

    public InformacoesDao(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
        cX = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE if not exists Informacoes (_id integer primary key autoincrement, id_Registro integer, id_Sentimento , data text, hora text, acontecimento text, infoExtra);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("CREATE TABLE IF not exists Informacoes (_id integer primary key autoincrement, id_Registro integer, id_Sentimeto integer, data text, hora text, acontecimeto text, infoExtra text)");
    }

    public void salva(Informacoes informacoes) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = null;

        if (informacoes.getId() == null) {
            sql = "INSERT INTO Especie (id_Registro, id_Sentimeto, data, hora, acontecimento,infoExtra) VALUES ('" + informacoes.getIdRegistro() + "', '" + informacoes.getIdSentimento() + "', '" + informacoes.getData() + "', '" + informacoes.getHora() + "', '" + informacoes.getAcontecimento() + "', '" + informacoes.getData();
        } else {
            sql = "UPDATE Especie SET idRegistro = '" + informacoes.getIdRegistro() + "', idSentimento = '" + informacoes.getIdSentimento() + "', data = '" + informacoes.getData() + "', hora = '" + informacoes.getHora() + "' WHERE _id = " + informacoes.getId();
        }

        try {
            db.execSQL(sql);
        } finally {
            db.close();
        }
    }

    public Cursor retornaTodos(Informacoes informacoes) {

        Cursor cursor;
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select * from Informacoes where _id is not null";

        if (informacoes != null) {
            if (informacoes.getId() != null) {
                sql = sql + " and _id = " + informacoes.getId();
            }

            if(informacoes.getId() != null) {
                sql = sql + "and idRegistro = '" + informacoes.getIdRegistro() + "' ";
            }
            if(informacoes.getId() != null) {
                sql = sql + "and idSentimeto = '" + informacoes.getIdSentimento() + "' ";
            }
            if(informacoes.getId() != null) {
                sql = sql + "and data = '" + informacoes.getData() + "' ";
            }
            if(informacoes.getId() != null) {
                sql = sql + "and hora = '" + informacoes.getHora() + "' ";
            }if(informacoes.getId() != null) {
                sql = sql + "and acontecimeto = '" + informacoes.getAcontecimento() + "' ";
            }if(informacoes.getId() != null) {
                sql = sql + "and infoExtra = '" + informacoes.getInfoExtra() + "' ";
            }
        }

        sql = sql + "order by idRegistro asc";

        cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    @SuppressLint("Range")
    public Informacoes retornaInformacoes(Informacoes informacoes){

        Informacoes retornaInformacoes = null;

        Cursor cursor;
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select * from Informacoes where _id is not null ";

        if (informacoes != null) {
            if (informacoes.getId() != null) {
                sql = sql + " and _id = " + informacoes.getId();
            }
            if (informacoes.getIdRegistro() != null) {
                sql = sql + " and idRegistro = '" + informacoes.getIdRegistro() + "' ";
            }
            if (informacoes.getIdSentimento() != null) {
                sql = sql + " and idSentimento = '" + informacoes.getIdSentimento() + "' ";
            }
            if (informacoes.getData() != null) {
                sql = sql + " and data = '" + informacoes.getData() + "' ";
            }
            if (informacoes.getHora() != null) {
                sql = sql + " and hora = '" + informacoes.getHora() + "' ";
            }
            if (informacoes.getAcontecimento() != null) {
                sql = sql + " and acontecimento = '" + informacoes.getAcontecimento() + "' ";
            }
            if (informacoes.getInfoExtra() != null) {
                sql = sql + " and infoExtra = '" + informacoes.getInfoExtra() + "' ";
            }
            }
            sql = sql + "order by idRegistro asc ";

            cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {

                    retornaInformacoes = new Informacoes();
                    retornaInformacoes.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                    retornaInformacoes.setIdRegistro(Integer.valueOf(cursor.getString(cursor.getColumnIndex("idRegistro"))));
                    retornaInformacoes.setIdSentimento(Integer.valueOf(cursor.getString(cursor.getColumnIndex("idSentimeto"))));
                    retornaInformacoes.setData(cursor.getString(cursor.getColumnIndex("data")));
                    retornaInformacoes.setHora(cursor.getString(cursor.getColumnIndex("hora")));
                    retornaInformacoes.setAcontecimento(cursor.getString(cursor.getColumnIndex("acontecimento")));
                    retornaInformacoes.setInfoExtra(cursor.getString(cursor.getColumnIndex("infoExtra")));
                }
            }
            db.close();

            return retornaInformacoes;
        }

        public void exclui(Informacoes informacoes) {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "delete from Informacoes where _id = " + informacoes.getId();
            try {
                db.execSQL(sql);
            } finally {
                db.close();
            }
        }

        @SuppressLint("Range")
        public List<String> retornaInformacoes(){
            List<String> list = new ArrayList<>();

            String sql = "select idRegistro, idSentimeto, data, hora, acontecimento, infoExtra from Informacoes where _id is not null";

            sql = sql + " order by idRegistro";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);

            list.add("");

            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(cursor.getColumnIndex("IdRegistro"))); // tem q ta certo... //
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

            return list;
        }

        public Cursor retornaTodosLista() {

            Cursor cursor;
            SQLiteDatabase db = getReadableDatabase();

            String sql = "select _id, (idRegistro, idSentimeto, data, hora, acontecimento, infoExtra)as linha from Especie where _id is not null";

            sql = sql + "order by idRegistro";

            cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }
            db.close();
            return cursor;
        }
    }
