package com.example.affectum.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;

import com.example.affectum.R;
import com.example.affectum.bean.RegistroEmocoes;
import com.example.affectum.dao.InformacoesDao;
import com.example.affectum.dao.RegistroDao;
import com.example.affectum.dao.RegistroEmocoesDao;
import com.example.affectum.dao.SentimentosDao;

public class telaCarregamento extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_carregamento);

        // Define a orientação da tela como retrato
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                boolean isFirstRun = preferences.getBoolean("isFirstRun", true);

                Intent mainIntent;
                if (isFirstRun) {
                    mainIntent = new Intent(telaCarregamento.this, Registro.class);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isFirstRun", false);
                    editor.apply();
                } else {
                    mainIntent = new Intent(telaCarregamento.this, MainActivity.class);
                }

                startActivity(mainIntent);
                finish();
            }
        }, 2000);
    }
}