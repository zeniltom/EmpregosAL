package br.com.empregosal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.empregosal.R;
import br.com.empregosal.model.Experiencia;

public class ExperienciaAdapter extends ArrayAdapter<Experiencia>{

    private ArrayList<Experiencia> experiencias;
    private Context context;

    public ExperienciaAdapter(Context c, ArrayList<Experiencia> objects) {
        super(c, 0, objects);
        this.experiencias = objects;
        this.context = c;
    }

    public int Qtd(){
        return experiencias.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        // Verifica se a lista está vazia
        if (experiencias != null) {

            // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.lista_experiencias, parent, false);

            // recupera elemento para exibição
            TextView nomeEmpresa = view.findViewById(R.id.tv_nome_empresa);
            TextView cargoEmpresa = view.findViewById(R.id.tv_cargo);
            TextView dataInicio = view.findViewById(R.id.tv_data_inicio);
            TextView dataSaida = view.findViewById(R.id.tv_data_saida);

            Experiencia experiencia = experiencias.get(position);
            nomeEmpresa.setText(experiencia.getNomeDaEmpresa());
            cargoEmpresa.setText(experiencia.getCargo());
            dataInicio.setText(experiencia.getDataInicio());
            dataSaida.setText(experiencia.getDataSaida());
        }

        return view;

    }
}
