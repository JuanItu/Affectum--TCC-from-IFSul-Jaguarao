package com.example.affectum.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.affectum.R;
import com.example.affectum.dao.RegistroDao;
import com.example.affectum.dao.RegistroEmocoesDao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Listsalvos extends AppCompatActivity {

    private Button botao1;
    private Button botaoExportarPdf;
    private TextView nomedia;
    private ListView listView;
    private RegistroDao registroDao;
    private RegistroEmocoesDao registroEmocoesDao;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listsalvos);

        registroDao = new RegistroDao(this);
        registroEmocoesDao = new RegistroEmocoesDao(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        botao1 = findViewById(R.id.voltar);
        botaoExportarPdf = findViewById(R.id.botaoExportarPdf);
        nomedia = findViewById(R.id.nomedia);
        listView = findViewById(R.id.lista);

        lista();
        ListView();

        botao1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Listsalvos.this, MainActivity.class);
                startActivity(intent);
            }
        });

        botaoExportarPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Listsalvos.this)
                        .setTitle("Exportar PDF")
                        .setMessage("Ao exportar, todos os registros de emoções serão apagados do app. Deseja continuar?")
                        .setPositiveButton("Sim, exportar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                gerarPdf();
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });
    }

    private void ListView() {
        Cursor cursor = registroEmocoesDao.retornaRegistroEmocoes();
        String[] fromColumns = {"linha"};
        int[] toViews = {android.R.id.text1};
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, fromColumns, toViews, 0);
        listView.setAdapter(adapter);
    }

    private void lista() {
        Cursor registroCursor = registroDao.retornaRegistroCursor();
        if (registroCursor != null && registroCursor.getCount() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            registroCursor.moveToFirst();
            int idIndex = registroCursor.getColumnIndex("_id");
            int linhaIndex = registroCursor.getColumnIndex("linha");
            if (idIndex != -1 && linhaIndex != -1) {
                do {
                    String linha = registroCursor.getString(linhaIndex);
                    stringBuilder.append(linha).append("\n");
                } while (registroCursor.moveToNext());
                nomedia.setText(stringBuilder.toString());
            } else {
                Toast.makeText(getApplicationContext(), "Erro ao carregar dados.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Nenhum dado para exibir", Toast.LENGTH_SHORT).show();
        }
    }

    private void limparDados() {
        SQLiteDatabase db = registroEmocoesDao.getWritableDatabase();
        db.execSQL("DELETE FROM RegistroEmocoes");
        db.close();

        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null,
                new String[]{"linha"}, new int[]{android.R.id.text1}, 0);
        listView.setAdapter(adapter);
    }

    @SuppressLint("Range")
    private void gerarPdf() {
        PdfDocument document = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        int yPos = 50;
        int margin = 40;
        int pageWidth = 595;

        titlePaint.setColor(Color.parseColor("#0096A3"));
        titlePaint.setTextSize(28f);
        titlePaint.setFakeBoldText(true);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Affectum - Relatório Emocional", pageWidth / 2f, yPos, titlePaint);
        yPos += 30;

        paint.setColor(Color.GRAY);
        paint.setTextSize(11f);
        paint.setTextAlign(Paint.Align.CENTER);
        String dataGeracao = "Gerado em: " + new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        canvas.drawText(dataGeracao, pageWidth / 2f, yPos, paint);
        yPos += 25;

        paint.setColor(Color.parseColor("#0096A3"));
        paint.setStrokeWidth(1.5f);
        canvas.drawLine(margin, yPos, pageWidth - margin, yPos, paint);
        yPos += 20;

        Cursor registroCursor = registroDao.retornaRegistroCursor();
        if (registroCursor != null && registroCursor.moveToFirst()) {
            paint.setColor(Color.parseColor("#004D52"));
            paint.setTextSize(14f);
            paint.setFakeBoldText(true);
            paint.setTextAlign(Paint.Align.LEFT);
            String nomeLinha = registroCursor.getString(registroCursor.getColumnIndex("linha"));
            canvas.drawText("Paciente: " + nomeLinha, margin, yPos, paint);
            yPos += 30;
            registroCursor.close();
        }

        paint.setFakeBoldText(false);
        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(1f);
        canvas.drawLine(margin, yPos, pageWidth - margin, yPos, paint);
        yPos += 20;

        paint.setColor(Color.parseColor("#0096A3"));
        paint.setTextSize(13f);
        paint.setFakeBoldText(true);
        canvas.drawText("Histórico de Emoções:", margin, yPos, paint);
        yPos += 20;

        Cursor emocoesCursor = registroEmocoesDao.retornaRegistroEmocoes();
        paint.setColor(Color.DKGRAY);
        paint.setTextSize(11f);
        paint.setFakeBoldText(false);

        if (emocoesCursor != null && emocoesCursor.moveToFirst()) {
            int linhaIndex = emocoesCursor.getColumnIndex("linha");
            do {
                String linha = emocoesCursor.getString(linhaIndex);
                String[] partes = linha.split("\n");
                for (String parte : partes) {
                    if (yPos > 800) {
                        document.finishPage(page);
                        PdfDocument.PageInfo newPageInfo = new PdfDocument.PageInfo.Builder(595, 842, document.getPages().size() + 1).create();
                        page = document.startPage(newPageInfo);
                        canvas = page.getCanvas();
                        yPos = 40;
                    }
                    canvas.drawText(parte.trim(), margin + 10, yPos, paint);
                    yPos += 16;
                }

                paint.setColor(Color.parseColor("#CCCCCC"));
                paint.setStrokeWidth(0.5f);
                canvas.drawLine(margin + 10, yPos, pageWidth - margin, yPos, paint);
                yPos += 10;

                paint.setColor(Color.DKGRAY);
                paint.setTextSize(11f);

            } while (emocoesCursor.moveToNext());
            emocoesCursor.close();
        } else {
            paint.setColor(Color.GRAY);
            canvas.drawText("Nenhum registro encontrado.", margin, yPos, paint);
        }

        document.finishPage(page);

        String nomeArquivo = "Affectum_Relatorio_" +
                new SimpleDateFormat("ddMMyyyy_HHmm", Locale.getDefault()).format(new Date()) + ".pdf";

        File dir = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "");
        if (!dir.exists()) dir.mkdirs();

        File arquivo = new File(dir, nomeArquivo);

        try {
            document.writeTo(new FileOutputStream(arquivo));
            document.close();

            limparDados();

            Toast.makeText(this, "PDF salvo! Registros apagados.", Toast.LENGTH_LONG).show();

            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", arquivo);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Abrir PDF com..."));

        } catch (IOException e) {
            document.close();
            Toast.makeText(this, "Erro ao salvar PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}