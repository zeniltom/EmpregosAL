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
import br.com.empregosal.model.Registro;

public class RegistroAdapter extends ArrayAdapter<Registro> {

    private ArrayList<Registro> registros;
    private Context context;
    private Registro registro;

    public RegistroAdapter(Context c, ArrayList<Registro> objects) {
        super(c, 0, objects);
        this.registros = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        // Verifica se a lista está vazia
        if (registros != null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_registros, parent, false);

            // recupera elemento para exibição
            TextView registroCargo = view.findViewById(R.id.tv_registro_emprego_cargo);
            TextView registroCandidato = view.findViewById(R.id.tv_registro_candidato);
            TextView registroEmpresa = view.findViewById(R.id.tv_registro_emprego_empresa);
            TextView registroLocalizacao = view.findViewById(R.id.tv_registro_emprego_localizacao);
            TextView registroDataAnuncio = view.findViewById(R.id.tv_registro_emprego_data);

            registro = registros.get(position);
            registroCargo.setText(registro.getCargo());
            registroEmpresa.setText(registro.getNomeEmpresa());
            registroLocalizacao.setText(registro.getLocalizacao());
            registroCandidato.setText(registro.getNomeUsuario());

            dataRegistro(registroDataAnuncio);
        }
        return view;
    }

    private void dataRegistro(TextView registroDataAnuncio) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String vd = registro.getData();
        Date dataHoje = new Date();
        Date dataAnuncio = null;

        try {
            dataAnuncio = format.parse(vd);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diferenca = 0;
        if (dataAnuncio != null) {
            diferenca = (dataHoje.getTime() - dataAnuncio.getTime());
        }
        long diferencaSegundos = diferenca / (1000);
        long diferencaMinutos = diferenca / (1000 * 60);
        long diferencaHoras = diferenca / (1000 * 60 * 60);
        long diferencaDias = diferenca / (1000 * 60 * 60 * 24);
        long diferencaMeses = diferenca / (1000 * 60 * 60 * 24) / 30;

        if (diferencaMeses > 0) {
            registroDataAnuncio.setText("Há " + diferencaMeses + " meses");

        } else if (diferencaDias > 0) {
            registroDataAnuncio.setText("Há " + diferencaDias + " dias");

        } else if (diferencaHoras > 0) {
            registroDataAnuncio.setText("Há " + diferencaHoras + " horas");

        } else if (diferencaMinutos > 0) {
            registroDataAnuncio.setText("Há " + diferencaMinutos + " minutos");

        } else if (diferencaSegundos > 0) {
            registroDataAnuncio.setText("Há alguns segundos");
        }
    }
}
