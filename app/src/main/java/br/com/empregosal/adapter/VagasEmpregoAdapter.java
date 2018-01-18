package br.com.empregosal.adapter;

import android.content.Context;
import android.graphics.Color;
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
    private Query pesquisa;
    private Query consultaUsuario;
    private Query consultaEmpress;
    private Empresa empresaPesquisda;
    private Usuario usuarioPesquisado;
    private Candidatura candidaturaP;

    public VagasEmpregoAdapter(Context c, ArrayList<Vaga> objects) {
        super(c, 0, objects);
        this.vagas = objects;
        this.context = c;
    }

    public int Qtd() {
        return vagas.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        final int posicao = position;

        // Verifica se a lista está vazia
        if (vagas != null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_vagas_usuario, parent, false);

            // recupera elemento para exibição
            TextView vagaCargo = view.findViewById(R.id.tv_vaga_emprego_cargo);
            TextView vagaEmpresa = view.findViewById(R.id.tv_vaga_emprego_empresa);
            TextView vagaLocalizacao = view.findViewById(R.id.tv_vaga_emprego_localizacao);
            TextView vagaDataAnuncio = view.findViewById(R.id.tv_vaga_emprego_data);
            Button botaoCandidatar = view.findViewById(R.id.bt_candidatar_se);

            Preferencias preferencias = new Preferencias(getContext());
            final String idUsuarioLogado = preferencias.getIdentificador();

            vaga = vagas.get(position);
            vagaCargo.setText(vaga.getCargo());
            vagaEmpresa.setText(vaga.getNomeEmpresa());
            vagaLocalizacao.setText(vaga.getLocalizacao());

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
                    .equalTo(vaga.getIdEmpresa());

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

            if (analisarVaga(posicao, vaga) == true){
                botaoCandidatar.setBackgroundColor(Color.GREEN);
                botaoCandidatar.setEnabled(false);
                botaoCandidatar.setText("Inscrito");
            }

            botaoCandidatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Função de vincular vaga ao usuário logado
                    candidatarVaga(posicao, idUsuarioLogado, empresaPesquisda, usuarioPesquisado, vaga);
                }
            });
        }
        return view;
    }

    private boolean analisarVaga(final int posicao, Vaga vaga) {
            vaga = vagas.get(posicao);
            candidaturaP = null;

            pesquisa = ConfiguracaoFirebase.getFirebase().child("candidaturas")
                    .orderByChild("idVaga")
                    .equalTo(vaga.getIdVaga());

            pesquisa.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        candidaturaP = dados.getValue(Candidatura.class);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        if (candidaturaP != null){
            return true;
        }else {
            return false;
        }
    }

    private void candidatarVaga(int posicao, String idUsuarioLogado, Empresa empresaPesquisda, Usuario usuarioPesquisado, Vaga vaga) {
        vaga = vagas.get(posicao);
        candidaturaP = null;

        //Validar se já existe cadastro na vaga

        String idEmpresa = vaga.getIdEmpresa();
        final Candidatura candidatura = new Candidatura();
        candidatura.setIdUsuario(idUsuarioLogado);
        candidatura.setIdEmpresa(idEmpresa);
        candidatura.setNomeUsuario(usuarioPesquisado.getNome());
        candidatura.setNomeEmpresa(empresaPesquisda.getNome());
        candidatura.setNomeVaga(vaga.getCargo());
        candidatura.setIdVaga(vaga.getIdVaga());

        pesquisa = ConfiguracaoFirebase.getFirebase().child("candidaturas")
                .orderByChild("idVaga")
                .equalTo(vaga.getIdVaga());

        pesquisa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    candidaturaP = dados.getValue(Candidatura.class);
                    System.out.println("CandidaturaP    " + candidaturaP.getIdVaga());
                    System.out.println("Nome da Vaga    " + candidaturaP.getNomeVaga());
                    System.out.println("ID do Usuario   " + candidaturaP.getIdUsuario());
                    System.out.println("Nome do Usuario " + candidaturaP.getNomeUsuario());
                    System.out.println("ID da Empresa   " + candidaturaP.getIdEmpresa());
                    System.out.println("Nome da empresa " + candidaturaP.getNomeEmpresa());

                }

                if (candidaturaP == null) {

                    try {

                        firebase = ConfiguracaoFirebase.getFirebase().child("candidaturas");
                        firebase.push()
                                .setValue(candidatura);

                        System.out.println(candidatura.getIdVaga());
                        System.out.println(candidatura.getNomeVaga());
                        System.out.println(candidatura.getIdUsuario());
                        System.out.println(candidatura.getNomeUsuario());
                        System.out.println(candidatura.getIdEmpresa());
                        System.out.println(candidatura.getNomeEmpresa());

                        Toast.makeText(getContext(), "Sucesso ao candidatar na vaga", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Erro ao candicatar na vaga", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "Já cadastrado na vaga " + candidaturaP.getNomeVaga(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
