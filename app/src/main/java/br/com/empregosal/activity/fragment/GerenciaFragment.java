package br.com.empregosal.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import br.com.empregosal.activity.SituacaoVagaActivity;
import br.com.empregosal.adapter.VagaAdapter;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.helper.Preferencias;
import br.com.empregosal.model.Vaga;
import dmax.dialog.SpotsDialog;

public class GerenciaFragment extends Fragment {

    private ListView listView;
    private TextView vazia;
    private FloatingActionButton fab;
    private ArrayAdapter adapter;
    private ArrayList<Vaga> vagas;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerVagas;
    private TextView qtd_vaga;
    private Query query;

    public GerenciaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        query.addListenerForSingleValueEvent(valueEventListenerVagas);
    }

    @Override
    public void onStop() {
        super.onStop();
        query.removeEventListener(valueEventListenerVagas);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Instânciar objetos
        vagas = new ArrayList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gerencia, container, false);

        //Monta listview e adapter
        qtd_vaga = view.findViewById(R.id.qtd_vagas);
        listView = (ListView) view.findViewById(R.id.lv_vagas);
        vazia = (TextView) view.findViewById(R.id.vazia_vaga);
        listView.setEmptyView(vazia);
        /*adapter = new ArrayAdapter(
                getActivity(),;
                R.layout.lista_contato,
                contatos
        );*/
        adapter = new VagaAdapter(getActivity(), vagas);
        listView.setAdapter(adapter);

        //Recuperar experiencias do firebase
        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();

        DatabaseReference oneRef = ConfiguracaoFirebase.getFirebase();

        query = ConfiguracaoFirebase.getFirebase().child("vagas")
                .orderByChild("idEmpresa")
                .equalTo(identificadorUsuarioLogado);
        carregarVagas();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Vaga vaga = vagas.get(position);

                Intent intent = new Intent(getContext(), SituacaoVagaActivity.class);
                intent.putExtra("vaga", vaga);

                startActivity(intent);
            }
        });

        return view;
    }

    private void carregarVagas() {
        final SpotsDialog dialog = new SpotsDialog(getContext(), "Carregando...", R.style.dialogEmpregosAL);
        dialog.setCancelable(false);
        dialog.show();

        //Listener para recuperar experiencias
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