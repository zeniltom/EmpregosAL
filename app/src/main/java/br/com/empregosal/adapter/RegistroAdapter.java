package br.com.empregosal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.com.empregosal.R;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.helper.Preferencias;
import br.com.empregosal.model.Candidatura;
import br.com.empregosal.model.Empresa;
import br.com.empregosal.model.Registro;
import br.com.empregosal.model.Usuario;

public class RegistroAdapter extends ArrayAdapter<Registro> {

    private ArrayList<Registro> registros;
    private Context context;
    private Registro registro;
    private DatabaseReference firebase;
    private Query pesquisa;
    private Query consultaUsuario;
    private Query consultaEmpress;
    private Empresa empresaPesquisda;
    private Usuario usuarioPesquisado;
    private Candidatura candidaturaP;
    Usuario user = null;
    Registro job = null;

    public RegistroAdapter(Context c, ArrayList<Registro> objects) {
        super(c, 0, objects);
        this.registros = objects;
        this.context = c;
    }

    public int Qtd() {
        return registros.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        final int posicao = position;

        // Verifica se a lista está vazia
        if (registros != null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_registros, parent, false);

            // recupera elemento para exibição
            TextView registroCargo = view.findViewById(R.id.tv_registro_emprego_cargo);
            TextView registroEmpresa = view.findViewById(R.id.tv_registro_emprego_empresa);
            TextView registroLocalizacao = view.findViewById(R.id.tv_registro_emprego_localizacao);
            TextView registroDataAnuncio = view.findViewById(R.id.tv_registro_emprego_data);

            Preferencias preferencias = new Preferencias(getContext());
            final String idUsuarioLogado = preferencias.getIdentificador();

            registro = registros.get(position);
            registroCargo.setText(registro.getCargo());
            registroEmpresa.setText(registro.getNomeEmpresa());
            registroLocalizacao.setText(registro.getLocalizacao());

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String vd = registro.getData();
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

            consultaUsuario = ConfiguracaoFirebase.getFirebase().child("usuarios")
                    .orderByChild("idUsuario")
                    .equalTo(idUsuarioLogado);

            consultaUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        Usuario usuario = dados.getValue(Usuario.class);
                        usuarioPesquisado = usuario;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            consultaEmpress = ConfiguracaoFirebase.getFirebase().child("empresas")
                    .orderByChild("idEmpresa")
                    .equalTo(registro.getIdEmpresa());

            consultaEmpress.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        Empresa empresa = dados.getValue(Empresa.class);
                        empresaPesquisda = empresa;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        return view;
    }
}
