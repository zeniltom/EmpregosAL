package br.com.empregosal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import br.com.empregosal.model.Usuario;
import br.com.empregosal.model.Vaga;

public class VagasEmpregoAdapter extends ArrayAdapter<Vaga> {

    private ArrayList<Vaga> vagas;
    private Context context;
    private Vaga vaga;
    private DatabaseReference firebase;
    private Empresa empresaPesquisda;
    private Usuario usuarioPesquisado;
    private Candidatura candidaturaP;

    public VagasEmpregoAdapter(Context c, ArrayList<Vaga> objects) {
        super(c, 0, objects);
        this.vagas = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        final int posicao = position;

        // Verifica se a lista está vazia
        if (vagas != null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_vagas_usuario, parent, false);

            TextView vagaCargo = view.findViewById(R.id.tv_vaga_emprego_cargo);
            TextView vagaEmpresa = view.findViewById(R.id.tv_vaga_emprego_empresa);
            TextView vagaLocalizacao = view.findViewById(R.id.tv_vaga_emprego_localizacao);
            TextView vagaDataAnuncio = view.findViewById(R.id.tv_vaga_emprego_data);
            Button botaoCandidatar = view.findViewById(R.id.bt_candidatar_se);
            Preferencias preferencias = new Preferencias(getContext());
            String idUsuarioLogado = preferencias.getIdentificador();

            vaga = vagas.get(position);
            vagaCargo.setText(vaga.getCargo());
            vagaEmpresa.setText(vaga.getNomeEmpresa());
            vagaLocalizacao.setText(vaga.getLocalizacao());

            dataPostagem(vagaDataAnuncio);

            consultaUsuario(idUsuarioLogado);

            consultaEmpresa();

            botaoCandidatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//Função de vincular vaga ao usuário logado
                    candidatarVaga(posicao, empresaPesquisda, usuarioPesquisado);
                }
            });
        }

        return view;
    }

    private void consultaEmpresa() {
        Query consultaEmpress = ConfiguracaoFirebase.getFirebase().child("empresas")
                .orderByChild("idEmpresa")
                .equalTo(vaga.getIdEmpresa());

        consultaEmpress.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    empresaPesquisda = dados.getValue(Empresa.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void consultaUsuario(String idUsuarioLogado) {
        Query consultaUsuario = ConfiguracaoFirebase.getFirebase().child("usuarios")
                .orderByChild("idUsuario")
                .equalTo(idUsuarioLogado);

        consultaUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    usuarioPesquisado = dados.getValue(Usuario.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void dataPostagem(TextView vagaDataAnuncio) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String vd = vaga.getData();
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

    private void candidatarVaga(final int posicao, Empresa empresaPesquisda, Usuario usuarioPesquisado) {
        Vaga vaga = vagas.get(posicao);

        candidaturaP = null;
        final String unico = vaga.getIdVaga() + usuarioPesquisado.getIdUsuario();

        final String idEmpresa = vaga.getIdEmpresa();
        final Candidatura candidatura = new Candidatura();
        candidatura.setIdUsuario(usuarioPesquisado.getIdUsuario());
        candidatura.setIdEmpresa(idEmpresa);
        candidatura.setNomeUsuario(usuarioPesquisado.getNome());
        candidatura.setNomeEmpresa(empresaPesquisda.getNome());
        candidatura.setNomeVaga(vaga.getCargo());
        candidatura.setIdVaga(vaga.getIdVaga());
        candidatura.setIdVagaUsuario(unico);

        Query pesquisa = ConfiguracaoFirebase.getFirebase().child("candidaturas")
                .orderByChild("idVagaUsuario")
                .equalTo(unico);


        pesquisa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    candidaturaP = dados.getValue(Candidatura.class);
                }

                if (candidaturaP == null) {

                    firebase = ConfiguracaoFirebase.getFirebase().child("candidaturas");
                    firebase.child(unico).setValue(candidatura);

                    Toast.makeText(getContext(), "Inscrição realizada com sucesso na vaga "
                            + candidatura.getNomeVaga(), Toast.LENGTH_SHORT).show();
                }

                if (candidaturaP != null && candidaturaP.getIdVagaUsuario().equals(unico)) {
                    Toast.makeText(getContext(), "Já Inscrito na vaga "
                            + candidatura.getNomeVaga(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
