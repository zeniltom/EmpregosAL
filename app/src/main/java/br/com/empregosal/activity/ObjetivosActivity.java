package br.com.empregosal.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import br.com.empregosal.R;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.helper.Preferencias;
import br.com.empregosal.model.Objetivo;

public class ObjetivosActivity extends AppCompatActivity {

    private Button bt_salvar;
    private EditText tipo_jornada;
    private EditText tipo_contrato;
    private EditText nivel_hierarquico_objetivos;
    private EditText salario_mensal_desejado;
    private FloatingActionButton fab;
    private TextView vazio;
    private Toolbar toolbar;

    private Objetivo objetivo;
    private String idUsuarioLogado;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objetivos);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Objetivo");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        tipo_jornada = findViewById(R.id.et_tipo_jornada);
        tipo_contrato = findViewById(R.id.et_tipo_contrato);
        nivel_hierarquico_objetivos = findViewById(R.id.et_nivel_hieraquico_objetivos);
        salario_mensal_desejado = findViewById(R.id.et_salario_mensal_desejado);
        bt_salvar = findViewById(R.id.bt_salvar_objetivos);
        fab = findViewById(R.id.fabAddObjetivos);

        bt_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferencias preferencias = new Preferencias(ObjetivosActivity.this);
                String idUsuarioLogado = preferencias.getIdentificador();

                objetivo = new Objetivo();
                objetivo.setTipoJornada(tipo_jornada.getText().toString());
                objetivo.setTipoContrato(tipo_contrato.getText().toString());
                objetivo.setNivelHierarquico(nivel_hierarquico_objetivos.getText().toString());
                objetivo.setSalarioDesejado(salario_mensal_desejado.getText().toString());

                if (!tipo_jornada.getText().toString().isEmpty()
                        && !tipo_contrato.getText().toString().isEmpty()
                        && !nivel_hierarquico_objetivos.getText().toString().isEmpty()
                        && !salario_mensal_desejado.getText().toString().isEmpty()) {

                    cadastrarFormacao(idUsuarioLogado, objetivo);
                } else {
                    Toast.makeText(ObjetivosActivity.this, "Um ou mais campos estão vazios", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void cadastrarFormacao(String idUsuarioLogado, Objetivo objetivo) {

        try {

            firebase = ConfiguracaoFirebase.getFirebase().child("objetivo");
            firebase.child(idUsuarioLogado)
                    .push()
                    .setValue(objetivo);

            Toast.makeText(ObjetivosActivity.this, "Sucesso ao cadastrar objetivo", Toast.LENGTH_LONG).show();
            finish();
            //Adicionar botão de alterar e esconder o de cadastrar
        } catch (Exception e) {
            Toast.makeText(ObjetivosActivity.this, "Erro ao cadastrar objetivo", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
