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

import java.util.ArrayList;

import br.com.empregosal.R;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.helper.Preferencias;
import br.com.empregosal.model.Candidatura;
import br.com.empregosal.model.Empresa;
import br.com.empregosal.model.Usuario;

public class CandidaturasEmpregoAdapter extends ArrayAdapter<Candidatura> {

    private ArrayList<Candidatura> candidaturas;
    private Context context;
    private Candidatura candidatura;
    private DatabaseReference firebase;
    private Query pesquisa;
    private Query consultaUsuario;
    private Query consultaEmpress;
    private Empresa empresaPesquisda;
    private Usuario usuarioPesquisado;
    private Candidatura candidaturaP;

    public CandidaturasEmpregoAdapter(Context c, ArrayList<Candidatura> objects) {
        super(c, 0, objects);
        this.candidaturas = objects;
        this.context = c;
    }

    public int Qtd() {
        return candidaturas.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        final int posicao = position;

        // Verifica se a lista está vazia
        if (candidaturas != null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_candidatos, parent, false);

            // recupera elemento para exibição
            TextView nomeVaga = view.findViewById(R.id.tv_nome_vaga_candidatura);
            TextView nomeEmpresa = view.findViewById(R.id.tv_nome_empresa_vaga_candidatura);

            Preferencias preferencias = new Preferencias(getContext());
            final String idUsuarioLogado = preferencias.getIdentificador();

            Candidatura candidatura = candidaturas.get(position);
            nomeVaga.setText(candidatura.getNomeVaga());
            nomeEmpresa.setText(candidatura.getNomeEmpresa());

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
                    .equalTo(candidatura.getIdEmpresa());

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
