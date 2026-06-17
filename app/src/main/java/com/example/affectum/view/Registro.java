package com.example.affectum.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.affectum.R;
import com.example.affectum.bean.Registros;
import com.example.affectum.dao.RegistroDao;
import com.example.affectum.dao.RegistroEmocoesDao;
import com.example.affectum.dao.SentimentosDao;

public class Registro extends AppCompatActivity {

    private EditText textNome;
    private EditText textDataNascimento;
    private Button botaoCadastrar;
    private RegistroDao registroDao;
    private SentimentosDao sentimentosDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        textNome = findViewById(R.id.text_nome);
        textDataNascimento = findViewById(R.id.text_aniversario);
        botaoCadastrar = findViewById(R.id.botaoCadastrar);
        registroDao = new RegistroDao(this);
        sentimentosDao = new SentimentosDao(this);

        RegistroDao registroDao = new RegistroDao(getBaseContext());
        registroDao.createTableRegistro();

        RegistroEmocoesDao registroEmocoesDao = new RegistroEmocoesDao(getBaseContext());
        registroEmocoesDao.createTableRegistroEmocoes();

        SentimentosDao sentimentosDao = new SentimentosDao(getBaseContext());
        sentimentosDao.createTableSentimentos();

        String[] sentimentos = {"Alegria", "Depressão", "Ansiedade", "Raiva", "Nojo", "Tristeza", "Alívio", "Medo", "Outros"};
        for (String sentimento : sentimentos) {
            ContentValues values = new ContentValues();
            values.put("tipo", sentimento);
            long newRowId = sentimentosDao.getWritableDatabase().insert("Sentimentos", null, values);
            if (newRowId != -1) {
                Log.d("InsertTest", "Inserção bem-sucedida! ID: " + newRowId);
            } else {
                Log.e("InsertTest", "Falha na inserção");
            }
        }

        textDataNascimento.setInputType(InputType.TYPE_CLASS_NUMBER);
        textDataNascimento.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        textDataNascimento.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            private String old = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdating) return;

                String str = s.toString().replaceAll("[^\\d]", "");
                String formatted = "";

                if (str.length() > 8) str = str.substring(0, 8);

                for (int i = 0; i < str.length(); i++) {
                    if (i == 2 || i == 4) formatted += "/";
                    formatted += str.charAt(i);
                }

                if (formatted.equals(old)) return;

                isUpdating = true;
                old = formatted;
                textDataNascimento.setText(formatted);
                textDataNascimento.setSelection(formatted.length());
                isUpdating = false;
            }
        });

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = textNome.getText().toString().trim();
                String dataNascimento = textDataNascimento.getText().toString().trim();

                if (!nome.isEmpty() && !dataNascimento.isEmpty()) {
                    Registros registro = new Registros();
                    registro.setNome(nome);
                    registro.setDataNascimento(dataNascimento);

                    if (registroDao.salva(registro)) {
                        Intent intent = new Intent(Registro.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Cadastrado com sucesso", Toast.LENGTH_LONG).show();
                        textNome.setText("");
                        textDataNascimento.setText("");
                    } else {
                        Log.e("ErroCadastro", "Erro ao cadastrar");
                        Toast.makeText(getApplicationContext(), "Erro ao cadastrar", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}