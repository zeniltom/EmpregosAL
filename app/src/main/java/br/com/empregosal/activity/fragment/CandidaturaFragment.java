package br.com.empregosal.activity.fragment;


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
import br.com.empregosal.adapter.CandidaturaAdapter;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.helper.Preferencias;
import br.com.empregosal.model.Candidatura;

public class CandidaturaFragment extends Fragment {

    private ListView listView;
    private TextView vazia;
    private ArrayAdapter adapter;
    private ArrayList<Candidatura> candidaturas;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerCandidaturas;
    private TextView qtd_candidaturas;
    private Query query;

    public CandidaturaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        query.addListenerForSingleValueEvent(valueEventListenerCandidaturas);
    }

    @Override
    public void onStop() {
        super.onStop();
        query.removeEventListener(valueEventListenerCandidaturas);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.i("setUserVisibleHint", "Está visível para o usuário");
            query.addListenerForSingleValueEvent(valueEventListenerCandidaturas);

        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i("onHiddenChanged", "Está invisível para o usuário");
        query.removeEventListener(valueEventListenerCandidaturas);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Instânciar objetos
        candidaturas = new ArrayList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_candidaturas, container, false);

        //Monta listview e adapter
        qtd_candidaturas = view.findViewById(R.id.qtd_candidaturas);
        listView = (ListView) view.findViewById(R.id.lv_candidaturas);
        vazia = (TextView) view.findViewById(R.id.vazia_candidaturas);
        listView.setEmptyView(vazia);
        /*adapter = new ArrayAdapter(
                getActivity(),;
                R.layout.lista_contato,
                contatos
        );*/
        adapter = new CandidaturaAdapter(getActivity(), candidaturas);
        listView.setAdapter(adapter);

        //Recuperar experiencias do firebase
        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();

        firebase = ConfiguracaoFirebase.getFirebase();

        query = firebase.child("candidaturas")
                .orderByChild("idUsuario")
                .equalTo(identificadorUsuarioLogado);

        //Listener para recuperar experiencias
        valueEventListenerCandidaturas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Limpar lista
                candidaturas.clear();

                //Listar experiencias
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Candidatura candidatura = dados.getValue(Candidatura.class);
                    candidaturas.add(candidatura);

                    //Se não possui experiências, o TextView qtd_experiencia some
                    if (candidaturas.size() == 0) qtd_candidaturas.setVisibility(View.GONE);

                    if (candidaturas.size() == 1)
                        qtd_candidaturas.setText(String.valueOf(candidaturas.size()) + " Candidatura");

                    if (candidaturas.size() > 1)
                        qtd_candidaturas.setText(String.valueOf(candidaturas.size()) + " Candidaturas");
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        //Ao clicar na experiencia, editar
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Criar nova activity para editar os dados da experiencia

//                // recupera dados a serem passados
//                Experiencia experiencia = experiencias.get(position);
//
//                // enviando dados para conversa activity
//                intent.putExtra("nome", experiencia.getNomeDaEmpresa());
//                intent.putExtra("email", experiencia.getCargo());
//
//                startActivity(intent);

            }
        });

        return view;
    }
}
