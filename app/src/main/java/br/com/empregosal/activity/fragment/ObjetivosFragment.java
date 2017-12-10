package br.com.empregosal.activity.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.empregosal.R;
import br.com.empregosal.activity.ObjetivosActivity;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.model.Objetivo;

public class ObjetivosFragment extends Fragment {

    private Button bt_salvar;
    private EditText tipo_jornada;
    private EditText tipo_contrato;
    private EditText nivel_hierarquico_objetivos;
    private EditText salario_mensal_desejado;
    private FloatingActionButton fab;
    private DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth usuarioFirebase;


    public ObjetivosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        firebase.child("objetivo").child(usuarioFirebase.getCurrentUser().getUid()).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists() == true) {

                            for (DataSnapshot dados : dataSnapshot.getChildren()) {

                                Objetivo objetivo = dados.getValue(Objetivo.class);
                                tipo_jornada.setText(objetivo.getTipoJornada());
                                tipo_contrato.setText(objetivo.getTipoContrato());
                                nivel_hierarquico_objetivos.setText(objetivo.getNivelHierarquico());
                                salario_mensal_desejado.setText(objetivo.getSalarioDesejado());
                            }
                        } else {
                            tipo_jornada.setHint("Preencha a experiência");
                            tipo_contrato.setHint("Preencha a experiência");
                            nivel_hierarquico_objetivos.setHint("Preencha a experiência");
                            salario_mensal_desejado.setHint("Preencha a experiência");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_objetivos, container, false);

        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao();

        tipo_jornada = view.findViewById(R.id.et_tipo_jornada);
        tipo_contrato = view.findViewById(R.id.et_tipo_contrato);
        nivel_hierarquico_objetivos = view.findViewById(R.id.et_nivel_hieraquico_objetivos);
        salario_mensal_desejado = view.findViewById(R.id.et_salario_mensal_desejado);
        bt_salvar = view.findViewById(R.id.bt_salvar_objetivos);
        fab = view.findViewById(R.id.fabAddObjetivos);

        tipo_jornada.setEnabled(false);
        tipo_contrato.setEnabled(false);
        nivel_hierarquico_objetivos.setEnabled(false);
        salario_mensal_desejado.setEnabled(false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ObjetivosActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
