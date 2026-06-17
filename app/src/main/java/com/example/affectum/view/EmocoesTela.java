package com.example.affectum.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.affectum.R;
import com.example.affectum.bean.RegistroEmocoes;
import com.example.affectum.bean.Sentimentos;
import com.example.affectum.dao.RegistroEmocoesDao;
import com.example.affectum.dao.SentimentosDao;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class EmocoesTela extends AppCompatActivity {

    private Spinner spinnerSentimentos;
    private RegistroEmocoesDao registroEmocoesDao;
    private SentimentosDao sentimentosDao;
    private EditText textBox;
    private Button botaosalvar;
    private Button voltar;
    private ArrayAdapter<String> adapter; // Alteração para ArrayAdapter<String>
    private Integer idr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emocoes_tela);

        // Define a orientação da tela como retrato
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        spinnerSentimentos = findViewById(R.id.spinner);
        botaosalvar = findViewById(R.id.botaosalvar);
        textBox = findViewById(R.id.text_box);
        voltar = findViewById(R.id.voltar);

        registroEmocoesDao = new RegistroEmocoesDao(this);
        sentimentosDao = new SentimentosDao(this);
        setupSpinner(); // Método para configurar o Spinner

        botaosalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String observacao = textBox.getText().toString().trim();
                String sentimentosSpinner = spinnerSentimentos.getSelectedItem().toString().trim();
                if (!observacao.isEmpty() && !sentimentosSpinner.isEmpty()) {

                    RegistroEmocoes re = new RegistroEmocoes();
                    re.setObservacao(observacao);

                    SentimentosDao sentdao = new SentimentosDao(getBaseContext());
                    idr = sentdao.sentimentoisId(sentimentosSpinner);
                    if (idr == null) {
                        Toast.makeText(getApplicationContext(), "Classe não encontrada", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Sentimentos sentimentos = new Sentimentos(idr);
                    re.setSentimento(sentimentos);

                    registroEmocoesDao.salva(re);

                    // Atualizar o Spinner após salvar
                    updateSpinner();

                    textBox.setText("");
                    Toast.makeText(getApplicationContext(), "Cadastrado com sucesso", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Não foi possível cadastrar", Toast.LENGTH_LONG).show();
                }
            }
        });

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmocoesTela.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupSpinner(){
        List<String> sentimentosList = sentimentosDao.retornaSentimentoList();
        if(sentimentosList != null && !sentimentosList.isEmpty()) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sentimentosList);
            spinnerSentimentos.setAdapter(adapter);
        } else {
            Toast.makeText(getApplicationContext(), "Nenhum dado para exibir no ListView", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para atualizar o Spinner
    private void updateSpinner() {
        List<String> sentimentosList = sentimentosDao.retornaSentimentoList();
        adapter.clear();
        adapter.addAll(sentimentosList);
        adapter.notifyDataSetChanged();
    }
}
