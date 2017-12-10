package br.com.empregosal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.empregosal.R;
import br.com.empregosal.model.Vaga;

public class VagaAdapter extends ArrayAdapter<Vaga>{

    private ArrayList<Vaga> vagas;
    private Context context;

    public VagaAdapter(Context c, ArrayList<Vaga> objects) {
        super(c, 0, objects);
        this.vagas = objects;
        this.context = c;
    }

    public int Qtd(){
        return vagas.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        // Verifica se a lista está vazia
        if (vagas != null) {

            // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.lista_vagas, parent, false);

            // recupera elemento para exibição
            TextView vagaCargo = view.findViewById(R.id.tv_vaga_cargo);
            TextView vagaArea = view.findViewById(R.id.tv_area_vaga);
            TextView vagaLocalizacao = view.findViewById(R.id.tv_vaga_localizacao);

            Vaga vaga = vagas.get(position);
            vagaCargo.setText(vaga.getCargo());
            vagaArea.setText(vaga.getAreaProfissional());
            vagaLocalizacao.setText(vaga.getLocalizacao());
        }

        return view;

    }
}
