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
import br.com.empregosal.activity.ExperienciaActivity;
import br.com.empregosal.adapter.ExperienciaAdapter;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.helper.Preferencias;
import br.com.empregosal.model.Experiencia;

public class ExperienciaFragment extends Fragment {

    private ListView listView;
    private TextView vazia;
    private FloatingActionButton fab;
    private ArrayAdapter adapter;
    private ArrayList<Experiencia> experiencias;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerExperiencias;
    private TextView qtd_experiencia;

    public ExperienciaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerExperiencias);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerExperiencias);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Instânciar objetos
        experiencias = new ArrayList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_experiencia, container, false);

        //Monta listview e adapter
        qtd_experiencia = view.findViewById(R.id.qtd_experiencias);
        fab = view.findViewById(R.id.fabAddExperiencia);
        listView = (ListView) view.findViewById(R.id.lv_experiencias);
        vazia = (TextView) view.findViewById(R.id.vazia);
        listView.setEmptyView(vazia);
        /*adapter = new ArrayAdapter(
                getActivity(),;
                R.layout.lista_contato,
                contatos
        );*/
        adapter = new ExperienciaAdapter(getActivity(), experiencias);
        listView.setAdapter(adapter);

        //Recuperar experiencias do firebase
        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("experiencias")
                .child(identificadorUsuarioLogado);

        //Listener para recuperar experiencias
        valueEventListenerExperiencias = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Limpar lista
                experiencias.clear();

                //Listar experiencias
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Experiencia experiencia = dados.getValue(Experiencia.class);
                    experiencias.add(experiencia);

                    //Se não possui experiências, o TextView qtd_experiencia some
                    if (experiencias.size() == 0) qtd_experiencia.setVisibility(View.GONE);

                    if (experiencias.size() == 1)
                        qtd_experiencia.setText(String.valueOf(experiencias.size()) + " Experiencia");

                    if (experiencias.size() > 1)
                        qtd_experiencia.setText(String.valueOf(experiencias.size()) + " Experiencias");
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
                Intent intent = new Intent(getActivity(), ExperienciaActivity.class);
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
