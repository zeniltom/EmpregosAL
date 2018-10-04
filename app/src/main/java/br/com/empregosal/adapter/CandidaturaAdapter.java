package br.com.empregosal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.empregosal.R;
import br.com.empregosal.model.Candidatura;

public class CandidaturaAdapter extends ArrayAdapter<Candidatura>{

    private ArrayList<Candidatura> candidaturas;
    private Context context;

    public CandidaturaAdapter(Context c, ArrayList<Candidatura> objects) {
        super(c, 0, objects);
        this.candidaturas = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        // Verifica se a lista está vazia
        if (candidaturas != null) {

            // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.lista_candidaturas, parent, false);

            // recupera elemento para exibição
            TextView nomeVaga = view.findViewById(R.id.tv_nome_vaga_candidatura);
            TextView nomeEmpresa = view.findViewById(R.id.tv_nome_empresa_vaga_candidatura);

            Candidatura candidatura = candidaturas.get(position);
            nomeVaga.setText(candidatura.getNomeVaga());
            nomeEmpresa.setText(candidatura.getNomeEmpresa());
        }

        return view;

    }
}
