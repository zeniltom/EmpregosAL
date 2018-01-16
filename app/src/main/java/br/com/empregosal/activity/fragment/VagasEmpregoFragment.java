package br.com.empregosal.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.empregosal.R;
import br.com.empregosal.activity.DetalhesVagaActivity;
import br.com.empregosal.adapter.VagasEmpregoAdapter;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.helper.Preferencias;
import br.com.empregosal.model.Vaga;

public class VagasEmpregoFragment extends Fragment {

    private ListView listView;
    private TextView vazia;
    private FloatingActionButton fab;
    private ArrayAdapter adapter;
    private ArrayList<Vaga> vagas;
    private DatabaseReference firebase;
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
            Toast.makeText(getContext(), "Carregando vagas - Conexão 3G", Toast.LENGTH_SHORT).show();
        } else if (conexao.getNetworkInfo(1).isConnected()) {
            Toast.makeText(getContext(), "Carregando vagas - Conexão WIFi", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Carregando vagas - Desconectado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Instânciar objetos
        vagas = new ArrayList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vagas_emprego, container, false);

        //Monta listview e adapter
        qtd_vaga = view.findViewById(R.id.qtd_vagas_empregos);
        fab = view.findViewById(R.id.fabAddVagaEmprego);
        listView = (ListView) view.findViewById(R.id.lv_candidaturas);
        vazia = (TextView) view.findViewById(R.id.vazia_vaga_emprego);
        listView.setEmptyView(vazia);
        /*adapter = new ArrayAdapter(
                getActivity(),;
                R.layout.lista_contato,
                contatos
        );*/
        adapter = new VagasEmpregoAdapter(getActivity(), vagas);
        listView.setAdapter(adapter);

        //Recuperar experiencias do firebase
        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("vagas");

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
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), VagaActivity.class);
//                startActivity(intent);
            }
        });

        //Ao clicar na experiencia, editar
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Criar nova activity para editar os dados da experiencia

                // recupera dados a serem passados
                Vaga vaga = vagas.get(position);

                // enviando dados para conversa activity
                Intent intent = new Intent(getContext(), DetalhesVagaActivity.class);
                intent.putExtra("vaga_cargo", vaga.getCargo());
                intent.putExtra("vaga_localizacao", vaga.getLocalizacao());
                intent.putExtra("vaga_descricao", vaga.getDescricao());
                intent.putExtra("vaga_data", vaga.getData());

                startActivity(intent);
            }
        });

        return view;
    }
}

