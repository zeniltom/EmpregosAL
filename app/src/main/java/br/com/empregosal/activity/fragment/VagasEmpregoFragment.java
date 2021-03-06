package br.com.empregosal.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.empregosal.R;
import br.com.empregosal.activity.DetalhesVagaActivity;
import br.com.empregosal.adapter.VagasEmpregoAdapter;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.model.Candidatura;
import br.com.empregosal.model.Vaga;
import dmax.dialog.SpotsDialog;

public class VagasEmpregoFragment extends Fragment {

    private ListView listView;
    private TextView vazia;
    private ArrayAdapter adapter;
    private ArrayList<Vaga> vagas;
    private ArrayList<Candidatura> candidaturas;
    private Query firebase;
    private ValueEventListener valueEventListenerVagas;
    private TextView qtd_vaga;

    public VagasEmpregoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerVagas);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerVagas);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.i("setUserVisibleHint", "Está visível para o usuário");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        ConnectivityManager conexao = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conexao.getNetworkInfo(0).isConnected()) {
            Log.i("Conexão 3G", conexao.getNetworkInfo(0).toString());
        } else if (conexao.getNetworkInfo(1).isConnected()) {
            Log.i("Conexão WIFi", conexao.getNetworkInfo(1).toString());
        } else {
            Log.i("Desconectado", conexao.getNetworkInfo(0).getState().name());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vagas = new ArrayList<>();
        candidaturas = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_vagas_emprego, container, false);

        qtd_vaga = view.findViewById(R.id.qtd_vagas_empregos);
        listView = view.findViewById(R.id.lv_candidaturas);
        vazia = view.findViewById(R.id.vazia_vaga_emprego);
        listView.setEmptyView(vazia);
        adapter = new VagasEmpregoAdapter(getActivity(), vagas);
        listView.setAdapter(adapter);

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("vagas");

        carregarVagasEmpregos();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // recupera dados a serem passados
                Vaga vaga = vagas.get(position);

                // enviando dados para conversa activity
                Intent intent = new Intent(getContext(), DetalhesVagaActivity.class);
                intent.putExtra("vaga_cargo", vaga.getCargo());
                intent.putExtra("vaga_localizacao", vaga.getLocalizacao());
                intent.putExtra("vaga_descricao", vaga.getDescricao());
                intent.putExtra("vaga_data", vaga.getData());
                intent.putExtra("vaga_tipo_contrato", vaga.getTipoContrato());
                intent.putExtra("vaga_nome_empresa", vaga.getNomeEmpresa());
                intent.putExtra("vaga_area_profissional", vaga.getAreaProfissional());
                intent.putExtra("vaga_tipo_contrato", vaga.getTipoContrato());
                intent.putExtra("vaga_nivel_hierarquico", vaga.getNivelHierarquico());
                intent.putExtra("vaga_nivel_estudos", vaga.getNivelEstudos());
                intent.putExtra("vaga_jornada", vaga.getJornada());
                intent.putExtra("vaga_faixa_salarial", vaga.getFaixaSalarial());
                intent.putExtra("vaga_cep", vaga.getCEP());
                intent.putExtra("vaga_qtd", vaga.getQtd());
                intent.putExtra("vaga", vaga);

                startActivity(intent);
            }
        });

        return view;
    }

    private void carregarVagasEmpregos() {
        final SpotsDialog dialog = new SpotsDialog(getContext(), "Carregando...", R.style.dialogEmpregosAL);
        dialog.setCancelable(false);
        dialog.show();

        valueEventListenerVagas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Limpar lista
                vagas.clear();

                //Listar experiencias
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Vaga vaga = dados.getValue(Vaga.class);
                    vagas.add(vaga);

                    //Se não possui experiências, o TextView qtd_experiencia some
                    if (vagas.size() == 0) qtd_vaga.setVisibility(View.GONE);

                    if (vagas.size() == 1)
                        qtd_vaga.setText(String.valueOf(vagas.size()) + " Vaga");

                    if (vagas.size() > 1)
                        qtd_vaga.setText(String.valueOf(vagas.size()) + " Vagas");
                }
                dialog.dismiss();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }
}

