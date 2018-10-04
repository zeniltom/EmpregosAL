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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.empregosal.R;
import br.com.empregosal.activity.FormacaoActivity;
import br.com.empregosal.adapter.FormacaoAdapter;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.helper.Preferencias;
import br.com.empregosal.model.Formacao;
import dmax.dialog.SpotsDialog;

public class FormacaoFragment extends Fragment {

    private ArrayAdapter adapter;
    private ArrayList<Formacao> formacoes;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerFormacoes;
    private TextView qtd_formacao;

    public FormacaoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerFormacoes);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerFormacoes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Instânciar objetos
        formacoes = new ArrayList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_formacao, container, false);

        //Monta listview e adapter
        qtd_formacao = view.findViewById(R.id.qtd_formacoes);
        FloatingActionButton fab = view.findViewById(R.id.fabAddFormacao);
        ListView listView = view.findViewById(R.id.lv_formacoes);
        TextView vazia = view.findViewById(R.id.vazia_formacao);
        listView.setEmptyView(vazia);
        /*adapter = new ArrayAdapter(
                getActivity(),;
                R.layout.lista_contato,
                contatos
        );*/
        adapter = new FormacaoAdapter(getActivity(), formacoes);
        listView.setAdapter(adapter);

        //Recuperar experiencias do firebase
        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("formacao")
                .child(identificadorUsuarioLogado);

        //Listener para recuperar experiencias
        carregarFormacoes();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FormacaoActivity.class);
                startActivity(intent);
            }
        });

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

    private void carregarFormacoes() {
        final SpotsDialog dialog = new SpotsDialog(getContext(), "Carregando...", R.style.dialogEmpregosAL);
        dialog.setCancelable(false);
        dialog.show();

        valueEventListenerFormacoes = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Limpar lista
                formacoes.clear();

                //Listar experiencias
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Formacao formacao = dados.getValue(Formacao.class);
                    formacoes.add(formacao);

                    //Se não possui experiências, o TextView qtd_experiencia some
                    if (formacoes.size() == 0) qtd_formacao.setVisibility(View.GONE);

                    if (formacoes.size() == 1)
                        qtd_formacao.setText(String.valueOf(formacoes.size()) + " Formação");

                    if (formacoes.size() > 1)
                        qtd_formacao.setText(String.valueOf(formacoes.size()) + " Formações");
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
