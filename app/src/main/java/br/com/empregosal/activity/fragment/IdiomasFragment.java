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
import br.com.empregosal.activity.IdiomasActivity;
import br.com.empregosal.adapter.IdiomasAdapter;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.helper.Preferencias;
import br.com.empregosal.model.Idioma;

public class IdiomasFragment extends Fragment {

    private ListView listView;
    private TextView vazia;
    private FloatingActionButton fab;
    private ArrayAdapter adapter;
    private ArrayList<Idioma> idiomas;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerIdiomas;
    private TextView qtd_idiomas;

    public IdiomasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerIdiomas);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerIdiomas);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Instânciar objetos
        idiomas = new ArrayList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_idiomas, container, false);

        //Monta listview e adapter
        qtd_idiomas = view.findViewById(R.id.qtd_idiomas);
        fab = view.findViewById(R.id.fabAddIdiomas);
        listView = (ListView) view.findViewById(R.id.lv_idiomas);
        vazia = (TextView) view.findViewById(R.id.vazia_idioma);
        listView.setEmptyView(vazia);
        /*adapter = new ArrayAdapter(
                getActivity(),;
                R.layout.lista_contato,
                contatos
        );*/
        adapter = new IdiomasAdapter(getActivity(), idiomas);
        listView.setAdapter(adapter);

        //Recuperar experiencias do firebase
        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("idiomas")
                .child(identificadorUsuarioLogado);

        //Listener para recuperar experiencias
        valueEventListenerIdiomas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Limpar lista
                idiomas.clear();

                //Listar experiencias
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Idioma idioma = dados.getValue(Idioma.class);
                    idiomas.add(idioma);

                    //Se não possui experiências, o TextView qtd_experiencia some
                    if (idiomas.size() == 0) qtd_idiomas.setVisibility(View.GONE);

                    if (idiomas.size() == 1)
                        qtd_idiomas.setText(String.valueOf(idiomas.size()) + " Formação");

                    if (idiomas.size() > 1)
                        qtd_idiomas.setText(String.valueOf(idiomas.size()) + " Formações");
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
                Intent intent = new Intent(getActivity(), IdiomasActivity.class);
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

}
