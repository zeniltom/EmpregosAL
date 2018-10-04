package br.com.empregosal.activity.fragment;

import android.content.Intent;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.empregosal.R;
import br.com.empregosal.activity.DetalhesRegistroActivity;
import br.com.empregosal.adapter.RegistroAdapter;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.helper.Preferencias;
import br.com.empregosal.model.Registro;
import dmax.dialog.SpotsDialog;

public class RegistroFragment extends Fragment {

    private ListView listView;
    private TextView vazia;
    private ArrayAdapter adapter;
    private ArrayList<Registro> registros;
    private Query firebase;
    private ValueEventListener valueEventListenerRegistros;
    private TextView qtd_registro;

    public RegistroFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerRegistros);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerRegistros);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        registros = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_registro, container, false);

        qtd_registro = view.findViewById(R.id.qtd_registro_empregos);
        listView = (ListView) view.findViewById(R.id.lv_candidaturas);
        vazia = (TextView) view.findViewById(R.id.vazia_registro_emprego);
        listView.setEmptyView(vazia);
        adapter = new RegistroAdapter(getActivity(), registros);
        listView.setAdapter(adapter);

        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();

        firebase = ConfiguracaoFirebase.getFirebase().child("registro")
                .orderByChild("idEmpresa")
                .equalTo(identificadorUsuarioLogado);

        Log.i("#Registro", identificadorUsuarioLogado);

        carregarRegistros();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Registro registro = registros.get(position);

                Intent intent = new Intent(getContext(), DetalhesRegistroActivity.class);
                intent.putExtra("registro", registro);
                startActivity(intent);
            }
        });

        return view;
    }

    private void carregarRegistros() {
        final SpotsDialog dialog = new SpotsDialog(getContext(), "Carregando...", R.style.dialogEmpregosAL);
        dialog.setCancelable(false);
        dialog.show();
        valueEventListenerRegistros = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Limpar lista
                registros.clear();

                //Listar experiencias
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Registro registro = dados.getValue(Registro.class);
                    registros.add(registro);
                    Log.i("#Registro", registro.getNomeEmpresa());

                    //Se não possui experiências, o TextView qtd_experiencia some
                    if (registros.size() == 0) qtd_registro.setVisibility(View.GONE);

                    if (registros.size() == 1)
                        qtd_registro.setText(String.valueOf(registros.size()) + " Registro");

                    if (registros.size() > 1)
                        qtd_registro.setText(String.valueOf(registros.size()) + " Registros");
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

