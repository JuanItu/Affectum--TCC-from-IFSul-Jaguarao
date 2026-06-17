package com.example.affectum.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.affectum.R;
import com.example.affectum.bean.Sentimentos;
import com.example.affectum.dao.SentimentosDao;

import java.util.List;

public class RegistroSentimento extends AppCompatActivity {

    private EditText nomeEditText;
    private Button salvarButton, deleteButton;
    private ListView listView;
    private SentimentosDao sentimentosDao;
    private SimpleCursorAdapter adapter;
    private Cursor sentimentosList;
    private int selectedItemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_sentimento);

        // Define a orientação da tela como retrato
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        nomeEditText = findViewById(R.id.editTextText);
        salvarButton = findViewById(R.id.Cadastrar);
        listView = findViewById(R.id.listview);
        deleteButton = findViewById(R.id.Deletar);

        sentimentosDao = new SentimentosDao(this);
        ListView();

        //sentimentosList = sentimentosDao.retornaSentimentos();
        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sentimentosList);
        //listView.setAdapter(adapter);

        salvarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sentimentoNome = nomeEditText.getText().toString().trim();
                if (!sentimentoNome.isEmpty()) {
                    Sentimentos sentimentos = new Sentimentos();
                    sentimentos.setTipo(sentimentoNome);

                    if (selectedItemId == -1) {
                        // Inserindo um novo item
                        sentimentosDao.salva(sentimentos);
                        Toast.makeText(getApplicationContext(), "Cadastrado", Toast.LENGTH_LONG).show();
                    } else {
                        // Atualizando um item existente
                        sentimentos.setId(selectedItemId);
                        sentimentosDao.salva(sentimentos);
                        Toast.makeText(getApplicationContext(), "Editado", Toast.LENGTH_LONG).show();
                        selectedItemId = -1;
                    }

                    // Atualiza a lista após o cadastro ou edição
                    //sentimentosList.clear();
                    //sentimentosList.addAll(sentimentosDao.retornaSentimentos());
                    adapter.notifyDataSetChanged();

                    adapter.changeCursor(sentimentosDao.retornaSentimentos());
                    nomeEditText.setText("");
                }
            }
        });
        //Comentado erros tecnicos
        /*listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedItemId = position;
            String nome = sentimentosList.getString(position);
            nomeEditText.setText(nome);
            Toast.makeText(getApplicationContext(), "Item selecionado!", Toast.LENGTH_LONG).show();
        });
        */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) adapter.getItem(position);
                if (cursor != null) {
                    int idColumnIndex = cursor.getColumnIndex("_id");
                    int tipoColumnIndex = cursor.getColumnIndex("tipo");
                    if (idColumnIndex != -1 && tipoColumnIndex != -1) {
                        selectedItemId = cursor.getInt(idColumnIndex);
                        String nome = cursor.getString(tipoColumnIndex);
                        nomeEditText.setText(nome);
                        Toast.makeText(getApplicationContext(), "Item selecionado!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Alguma das colunas não foi encontrada no Cursor
                        Toast.makeText(getApplicationContext(), "Colunas não encontradas no Cursor", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItemId != -1) {
                    sentimentosDao.exclui(selectedItemId);
                    Toast.makeText(getApplicationContext(), "Excluído", Toast.LENGTH_LONG).show();

                    // Atualiza a lista após a exclusão
                    adapter.changeCursor(sentimentosDao.retornaSentimentos());

                    nomeEditText.setText("");
                    selectedItemId = -1;
                } else {
                    Toast.makeText(getApplicationContext(), "Selecione um item para excluir", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
    private void ListView(){
        Cursor sentimentoCursor = sentimentosDao.retornaSentimentos();
        String[] fromColumns = {"tipo"};
        int[] toViews = {android.R.id.text1};
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, sentimentoCursor, fromColumns, toViews, 0);
        listView.setAdapter(adapter);


    }
}