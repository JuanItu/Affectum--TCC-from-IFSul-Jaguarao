package com.example.affectum.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.affectum.bean.RegistroEmocoes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegistroEmocoesDao extends SQLiteOpenHelper {

    public static final String NOME_BANCO = "banco.db";
    private static final int VERSAO_BANCO = 1;
    Context cX;

    public RegistroEmocoesDao(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
        cX = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS RegistroEmocoes (_id INTEGER PRIMARY KEY AUTOINCREMENT, sentimento TEXT, data TIMESTAMP DEFAULT CURRENT_TIMESTAMP, observacao TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("CREATE TABLE IF NOT EXISTS RegistroEmocoes (_id INTEGER PRIMARY KEY AUTOINCREMENT, sentimento TEXT, data TIMESTAMP DEFAULT CURRENT_TIMESTAMP, observacao TEXT);");
    }

    public void createTableRegistroEmocoes(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS RegistroEmocoes (_id INTEGER PRIMARY KEY AUTOINCREMENT, sentimento TEXT, data TIMESTAMP DEFAULT CURRENT_TIMESTAMP, observacao TEXT);");
    }

    public boolean salva(RegistroEmocoes registroemocoes) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = null;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTimeStamp = dateFormat.format(calendar.getTime());

        if (registroemocoes.getId() == null) {
            // Se o ID for nulo, significa que é uma inserção
            sql = "INSERT INTO registroEmocoes (sentimento, data, observacao) VALUES ('" + registroemocoes.getSentimento().getId() + "', '" + currentTimeStamp + "', '" + registroemocoes.getObservacao() + "')";
        } else {
            // Caso contrário, é uma atualização
            sql = "UPDATE registroEmocoes SET sentimento = '" + registroemocoes.getSentimento().getId() + "', data = '" + currentTimeStamp + "', observacao = '" + registroemocoes.getObservacao() + "' WHERE _id = " + registroemocoes.getId();
        }

        try {
            db.execSQL(sql);
        } finally {
            db.close();
        }
        return true;
    }




    public List<String> getAllEmotions() {
        List<String> emotionList = new ArrayList<>();
        String selectQuery = "SELECT sentimento FROM RegistroEmocoes";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("sentimento");
            do {
                if (columnIndex != -1) {
                    emotionList.add(cursor.getString(columnIndex));
                } else {
                    // Se a coluna não for encontrada, registre um aviso ou realize outra ação apropriada
                    Log.e("getAllEmotions", "Índice da coluna 'sentimento' não encontrado no cursor");
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return emotionList;
    }

    public Cursor retornaEmcoes(){
        String sql = "select _id, data, observacao, sentimento from registroEmocoes where _id is not null order by _id";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        db.close();
        return cursor;
    }

    public Cursor retornaRegistroEmocoes() {
        String sql = "SELECT re._id, " +
                "s.tipo || ' : ' || re.observacao || ' \nData: ' || strftime('%d/%m/%Y %H:%M:%S', re.data) AS linha " +
                "FROM RegistroEmocoes re " +
                "INNER JOIN Sentimentos s ON re.sentimento = s._id " +
                "WHERE re._id IS NOT NULL " +
                "ORDER BY re.data DESC";

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




    // Adicione outras operações de banco de dados conforme necessário
}