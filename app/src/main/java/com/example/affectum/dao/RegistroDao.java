package com.example.affectum.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.affectum.bean.Registros;
import com.example.affectum.view.Registro;

import java.util.ArrayList;
import java.util.List;

public class RegistroDao extends SQLiteOpenHelper {

    public static final String NOME_BANCO = "banco.db";
    private static final int VERSAO_BANCO = 1;
    Context cX;

    public RegistroDao(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
        cX = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Registro ( _id integer primary key autoincrement, nome text, dataNascimento text, cidade text, numeroContato text, genero text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Registro ( _id integer primary key autoincrement, nome text, dataNascimento text, cidade text, numeroContato text, genero text);");
    }

    public void createTableRegistro(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS Registro ( _id integer primary key autoincrement, nome text, dataNascimento text, cidade text, numeroContato text, genero text);");
    }

    /*public void salva(Registros registro) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = null;

        if (registro.getId() == null) {
            sql = "INSERT INTO Registro (nome, dataNascimento, cidade, numeroContato, genero) VALUES ('" + registro.getNome() + "', '" + registro.getDataNascimento() + "', '" + registro.getCidade()+ "', '" + registro.getNumeroContato() + "', '" + registro.getGenero();
        } else {
            sql = "UPDATE Registro SET nome = '" + registro.getNome() + "', dataNascimento = '" + registro.getDataNascimento() + "', cidade = '" + registro.getCidade() + "', numerContato = '" + registro.getNumeroContato() + "', genero = '" + registro.getGenero() + "' WHERE _id = " + registro.getId();
        }

        try {
            db.execSQL(sql);
        } finally {
            db.close();
        }
    } */
    public boolean salva(Registros registro) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = null;

        if (registro.getId() == null) {
            sql = "INSERT INTO Registro (nome, dataNascimento) VALUES ('" + registro.getNome() + "', '" + registro.getDataNascimento() + "')";
        } else {
            sql = "UPDATE Registro SET nome = '" + registro.getNome() + "', dataNascimento = '" + registro.getDataNascimento() + "' WHERE _id = " + registro.getId();
        }

        try {
            db.execSQL(sql);
        } finally {
            db.close();
        }
        return true;
    }


    public Cursor retornaTodos(Registros registro) {

        Cursor cursor;
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select * from Registro where _id is not null";

        if (registro != null) {
            if (registro.getId() != null) {
                sql = sql + " and _id = " + registro.getId();
            }

            if(registro.getId() != null) {
                sql = sql + "and nome = '" + registro.getNome() + "' ";
            }
            if(registro.getId() != null) {
                sql = sql + "and dataNascimento = '" + registro.getDataNascimento() + "' ";
            }
            if(registro.getId() != null) {
                sql = sql + "and cidade = '" + registro.getCidade() + "' ";
            }
            if(registro.getId() != null) {
                sql = sql + "and numeroContato = '" + registro.getNumeroContato() + "' ";
            }  if(registro.getId() != null) {
                sql = sql + "and genero = '" + registro.getGenero() + "' ";
            }
        }

        sql = sql + "order by nome asc";

        cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    @SuppressLint("Range")
    public Registros retornaRegistro(Registros registro){

        Registros retornaRegistro = null;

        Cursor cursor;
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select * from Registro where _id is not null ";

        if (registro != null) {
            if (registro.getId() != null) {
                sql = sql + " and _id = " + registro.getId();
            }
            if (registro.getNome() != null) {
                sql = sql + " and dataNascimento = '" + registro.getDataNascimento() + "' ";
            }
            if (registro.getNome() != null) {
                sql = sql + " and nome = '" + registro.getNome() + "' ";
            }
            if (registro.getCidade() != null) {
                sql = sql + " and cidade = '" + registro.getCidade() + "' ";
            }
            if (registro.getNumeroContato() != null) {
                sql = sql + " and numeroContato = '" + registro.getNumeroContato() + "' ";
            }
        }

        sql = sql + "order by nome asc ";

        cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            if (cursor.moveToFirst()){

                retornaRegistro = new Registros();
                retornaRegistro.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                retornaRegistro.setDataNascimento(cursor.getString(cursor.getColumnIndex("dataNascimento")));
                retornaRegistro.setNome(cursor.getString(cursor.getColumnIndex("dataNascimento")));
                retornaRegistro.setNumeroContato((cursor.getString(cursor.getColumnIndex("numeroContato"))));
                retornaRegistro.setCidade(cursor.getString(cursor.getColumnIndex("cidade")));
                retornaRegistro.setGenero(cursor.getString(cursor.getColumnIndex("genero")));
            }
        }
        db.close();

        return retornaRegistro;
    }

    public void exclui(Registros registro) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "delete from Registro where _id = " + registro.getId();
        try {
            db.execSQL(sql);
        } finally {
            db.close();
        }
    }

    @SuppressLint("Range")
    public List<String> retornaRegistro(){
        List<String> list = new ArrayList<>();

        String sql = "select datNascimento, nome, cidade, numeroContato, genero from Registros where _id is not null";

        sql = sql + " order by nome";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        list.add("");

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex("nome"))); // pode ser dataNascimento no lugar de nome //
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    public Cursor retornaRegistroCursor() {
        String sql = "SELECT _id, " +
                "IFNULL(nome, '') || ' : ' || IFNULL(dataNascimento, '') AS linha " +
                "FROM Registro " +
                "WHERE _id IS NOT NULL " +
                "ORDER BY nome";

        Log.d("SQL_QUERY", "Query: " + sql);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("_id");
                int linhaIndex = cursor.getColumnIndex("linha");

                if (idIndex != -1 && linhaIndex != -1) {
                    String linha = cursor.getString(linhaIndex);

                    Log.d("CursorData", "ID: " + cursor.getInt(idIndex) + ", Linha: " + linha);
                } else {
                    Log.e("CursorData", "Índice de um dos campos não encontrado no Cursor.");
                }
            } while (cursor.moveToNext());
        }

        Log.d("CursorRowCount", "Número de linhas no Cursor: " + cursor.getCount());

        return cursor;
    }

    public Cursor retornaTodaLista() {

        Cursor cursor;
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select _id, (dataNascimento, nome, cidade, numeroContato, generor)as linha from Especie where _id is not null";

        sql = sql + "order by nome";

        cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }
}