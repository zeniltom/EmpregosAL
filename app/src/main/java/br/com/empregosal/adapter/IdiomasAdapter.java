package br.com.empregosal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.empregosal.R;
import br.com.empregosal.model.Idioma;

public class IdiomasAdapter extends ArrayAdapter<Idioma> {

    private ArrayList<Idioma> idiomas;
    private Context context;

    public IdiomasAdapter(Context c, ArrayList<Idioma> objects) {
        super(c, 0, objects);
        this.idiomas = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        // Verifica se a lista está vazia
        if (idiomas != null) {

            // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.lista_idiomas, parent, false);

            // recupera elemento para exibição
            TextView idiomaNome = view.findViewById(R.id.tv_idioma);
            TextView nivelIdioma = view.findViewById(R.id.tv_nivel_idioma);

            Idioma idioma = idiomas.get(position);
            idiomaNome.setText(idioma.getIdioma());
            nivelIdioma.setText(idioma.getNivel());
        }

        return view;

    }
}
