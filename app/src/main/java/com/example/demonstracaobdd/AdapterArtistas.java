package com.example.demonstracaobdd;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AdapterArtistas extends CursorAdapter {
    public AdapterArtistas(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }
    // O método newView apenas "infla" o xml do item da lista e retorna.
    // Não há preenchimento de dados neste momento.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemDaLista = inflater.inflate(R.layout.item_lista, parent, false);
        return itemDaLista;
    }
    // O método bindView pega os dados lidos do bd no cursor, e coloca no item da lista
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView lblNome = view.findViewById( R.id.lblNome );
        TextView lblGenero = view.findViewById( R.id.lblGenero );
        // extrair as informações da posição atual do cursor
        String nome = cursor.getString( cursor.getColumnIndex("nome") );
        String genero = cursor.getString( cursor.getColumnIndex("genero") );
        lblNome.setText( nome );
        lblGenero.setText( genero );
    }
}