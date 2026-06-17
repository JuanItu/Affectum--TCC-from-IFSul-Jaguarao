package com.example.affectum.view;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.affectum.R;
import com.example.affectum.dao.RegistroDao;
import com.example.affectum.dao.RegistroEmocoesDao;
import com.example.affectum.dao.SentimentosDao;

public class MainActivity extends AppCompatActivity {


    private Button botao1, botao2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Define a orientação da tela como retrato
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        RegistroDao registroDao = new RegistroDao(getBaseContext());
        registroDao.createTableRegistro();

        RegistroEmocoesDao registroEmocoesDao = new RegistroEmocoesDao(getBaseContext());
        registroEmocoesDao.createTableRegistroEmocoes();

        SentimentosDao sentimentosDao = new SentimentosDao(getBaseContext());
        sentimentosDao.createTableSentimentos();

        botao1 = findViewById(R.id.emocao);
        botao2 = findViewById(R.id.salvos);

        botao1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(MainActivity.this, EmocoesTela.class);
               startActivity(intent);
            }
        });

        botao2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Listsalvos.class);
                startActivity(intent);
            }
        });
    }
}