package br.com.empregosal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
            TextView vagaEmpresa = view.findViewById(R.id.tv_vaga_empresa);
            TextView vagaArea = view.findViewById(R.id.tv_area_vaga);
            TextView vagaLocalizacao = view.findViewById(R.id.tv_vaga_localizacao);
            TextView vagaDataAnuncio = view.findViewById(R.id.tv_vaga_data);

            Vaga vaga = vagas.get(position);
            vagaCargo.setText(vaga.getCargo());
            vagaEmpresa.setText(vaga.getNomeEmpresa());
            vagaArea.setText(vaga.getAreaProfissional());
            vagaLocalizacao.setText(vaga.getLocalizacao());
            vagaDataAnuncio.setText(vaga.getDataAnuncio());

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String vd = vaga.getData();
            Date dataHoje = new Date();
            Date dataAnuncio = null;

            try {
                dataAnuncio = format.parse(vd.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long diferenca = (dataHoje.getTime() - dataAnuncio.getTime());
            long diferencaSegundos = diferenca / (1000);
            long diferencaMinutos = diferenca / (1000 * 60);
            long diferencaHoras = diferenca / (1000 * 60 * 60);
            long diferencaDias = diferenca / (1000 * 60 * 60 * 24);
            long diferencaMeses = diferenca / (1000 * 60 * 60 * 24) / 30;

            if (diferencaMeses > 0) {
                vagaDataAnuncio.setText("Há " + diferencaMeses + " meses");

            } else if (diferencaDias > 0) {
                vagaDataAnuncio.setText("Há " + diferencaDias + " dias");

            } else if (diferencaHoras > 0) {
                vagaDataAnuncio.setText("Há " + diferencaHoras + " horas");

            } else if (diferencaMinutos > 0) {
                vagaDataAnuncio.setText("Há " + diferencaMinutos + " minutos");

            } else if (diferencaSegundos > 0) {
                vagaDataAnuncio.setText("Há alguns segundos");
            }
        }

        return view;

    }
}
