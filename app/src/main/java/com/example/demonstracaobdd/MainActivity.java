package com.example.demonstracaobdd;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import static java.sql.Types.VARCHAR;

public class MainActivity extends AppCompatActivity {
    // ATRIBUTOS
    // atributos relativos aos objetos gráficos da interface
    private EditText txtArtista;
    private EditText txtGenero;
    private Button btnAdiciona;
    private ListView listaArtistas;
    // cursor com os dados recuperados do BD
    Cursor cursorArtistas;
    // adapter da lista de artistas
    AdapterArtistas adapterArtistas;
    // referência para o banco de dados
    SQLiteDatabase bd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ligando atributos com os IDs dos objetos gráficos
        txtArtista = findViewById( R.id.txtArtista );
        txtGenero = findViewById( R.id.txtGenero );
        btnAdiciona = findViewById( R.id.btnAdiciona );
        listaArtistas = findViewById( R.id.listaArtistas );
        // configurando o escutador do botão adiciona
        btnAdiciona.setOnClickListener( new EscutadorAdiciona() );
        // configurar a lista com o escutador de cliques comuns
        listaArtistas.setOnItemClickListener( new EscutadorCliqueComum() );

        // banco de dados...
        // abrindo ou criando o banco de dados
        bd = openOrCreateDatabase( "artistasmusicas", MODE_PRIVATE, null );
        // string para comandos SQL
        String cmd;
        // criar a tabela artistas, se a mesma não existir
        cmd = "CREATE TABLE IF NOT EXISTS artistas (";
        cmd = cmd + "id INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR, genero VARCHAR)";
        bd.execSQL( cmd );
        // criar a tabela musicas, se a mesma não existir
        cmd = "CREATE TABLE IF NOT EXISTS musicas (";
        cmd = cmd + "id INTEGER PRIMARY KEY AUTOINCREMENT, idArtista INTEGER, titulo VARCHAR)";
        bd.execSQL( cmd );

        // configurando a lista...
        // criando cursor com os dados vindos do banco
        cursorArtistas = bd.rawQuery( "SELECT _rowid_ _id, id, nome, genero FROM artistas", null );
        // criando o objeto adapter, passando o cursor
        adapterArtistas = new AdapterArtistas( this, cursorArtistas );
        // associando o adapter a lista de artistas
        listaArtistas.setAdapter(adapterArtistas);
    }

    // classe interna, escutador do botão Adiciona
    private class EscutadorAdiciona implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // variáveis para pegar os dados
            String artista, genero;
            // pegando os dados na interface
            artista = txtArtista.getText().toString();
            genero = txtGenero.getText().toString();
            // montando SQL para inserir dados
            String cmd = "INSERT INTO artistas (nome, genero) VALUES ('";
            cmd = cmd + artista;
            cmd = cmd + "', '";
            cmd = cmd + genero;
            cmd = cmd + "')";
            // executando comando
            bd.execSQL( cmd );
            // limpando a interface
            txtArtista.setText("");
            txtGenero.setText("");
            // renovando o cursor do adapter, já que temos novos dados no bd
            cursorArtistas = bd.rawQuery( "SELECT _rowid_ _id, id, nome, genero FROM artistas", null );
            adapterArtistas.changeCursor(cursorArtistas);
        }
    }

    private class EscutadorCliqueComum implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            // recupera o cursor, posicionado na linha relativa ao item clicado
            // Cursor c = adapterArtistas.getCursor();
            Cursor c = (Cursor) adapterArtistas.getItem(i);
            // criando o intent para abrir a outra activity
            Intent intent = new Intent( getApplicationContext(), MusicasActivity.class );
            // colocando o id do artista dentro do intent
            intent.putExtra( "id", c.getInt(c.getColumnIndex("id")) );
            intent.putExtra( "nome", c.getString(c.getColumnIndex("nome")));
            // iniciando a outra activity
            startActivity(intent);
        }
    }


}