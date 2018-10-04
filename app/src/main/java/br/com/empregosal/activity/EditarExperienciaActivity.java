package br.com.empregosal.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import br.com.empregosal.R;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.model.Experiencia;
import dmax.dialog.SpotsDialog;

public class EditarExperienciaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth experienciaFirebase;
    private EditText nome_empresa, cargo, salario, nivel_hieraquico, area_profissional,
                     especialidade, data_inicio, data_saida, descricao_atividades;
    private Button bt_salvar;
    private String idXP;

    @Override
    protected void onStart() {
        super.onStart();
        Experiencia xp = (Experiencia) getIntent().getSerializableExtra("experiencia");

        final SpotsDialog dialog = new SpotsDialog(EditarExperienciaActivity.this, "Carregando...", R.style.dialogEmpregosAL);
        dialog.setCancelable(false);
        dialog.show();

        nome_empresa.setText(xp.getNomeDaEmpresa());
        cargo.setText(xp.getCargo());
        salario.setText(xp.getSalario());
        nivel_hieraquico.setText(xp.getNivelHieraquico());
        area_profissional.setText(xp.getAreaProfissional());
        especialidade.setText(xp.getEspecialidade());
        data_inicio.setText(xp.getDataInicio());
        data_saida.setText(xp.getDataSaida());
        descricao_atividades.setText(xp.getDescricaoAtividades());
        idXP = xp.getIdExperiencia();
        dialog.dismiss();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_experiencia);

        nome_empresa = findViewById(R.id.et_nome_empresa);
        cargo = findViewById(R.id.et_cargo);
        salario = findViewById(R.id.et_salario);
        nivel_hieraquico = findViewById(R.id.et_nivel_hieraquico);
        area_profissional = findViewById(R.id.et_area_profissional);
        especialidade = findViewById(R.id.et_especialidade);
        data_inicio = findViewById(R.id.et_data_inicio);
        data_saida = findViewById(R.id.et_data_saida);
        descricao_atividades = findViewById(R.id.et_atividades);
        bt_salvar = findViewById(R.id.bt_salvar_experiencia);

        experienciaFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Edição Experiência");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        bt_salvar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alterarDados();
            }
        });
    }

    private void alterarDados() {
        SpotsDialog progressDialog = new SpotsDialog(EditarExperienciaActivity.this, "Salvando alterações...", R.style.dialogEmpregosAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

        reference.child("experiencias").child(experienciaFirebase.getCurrentUser()
                .getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String, Object> dados = new HashMap<>();
                dados.put("nomeDaEmpresa", nome_empresa.getText().toString());
                dados.put("cargo", cargo.getText().toString());
                dados.put("salario", salario.getText().toString());
                dados.put("nivelHieraquico", nivel_hieraquico.getText().toString());
                dados.put("areaProfissional", area_profissional.getText().toString());
                dados.put("especialidade", especialidade.getText().toString());
                dados.put("dataInicio", data_inicio.getText().toString());
                dados.put("dataSaida", data_saida.getText().toString());
                dados.put("descricaoAtividades", descricao_atividades.getText().toString());

                reference.child("experiencias").child(experienciaFirebase.getCurrentUser().getUid())
                        .child(idXP).updateChildren(dados);

                Toast.makeText(EditarExperienciaActivity.this, "Experiência alterada com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
