package br.com.empregosal.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import br.com.empregosal.R;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.helper.Preferencias;
import br.com.empregosal.model.Experiencia;

public class ExperienciaActivity extends AppCompatActivity {

    private Button bt_salvar;
    private Toolbar toolbar;
    private EditText nome_empresa;
    private EditText cargo;
    private EditText salario;
    private EditText nivel_hieraquico;
    private EditText area_profissional;
    private EditText especialidade;
    private EditText data_inicio;
    private EditText data_saida;
    private EditText descricao_atividades;
    private Experiencia experiencia;
    private String idUsuarioLogado;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiencia);

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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Nova Experiência");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        // dados do usuário logado
        Preferencias preferencias = new Preferencias(ExperienciaActivity.this);
        idUsuarioLogado = preferencias.getIdentificador();

        bt_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                experiencia = new Experiencia();
                experiencia.setNomeDaEmpresa(nome_empresa.getText().toString());
                experiencia.setCargo(cargo.getText().toString());
                experiencia.setSalario(salario.getText().toString());
                experiencia.setNivelHieraquico(nivel_hieraquico.getText().toString());
                experiencia.setAreaProfissional(area_profissional.getText().toString());
                experiencia.setEspecialidade(especialidade.getText().toString());
                experiencia.setDataInicio(data_inicio.getText().toString());
                experiencia.setDataSaida(data_saida.getText().toString());
                experiencia.setDescricaoAtividades(descricao_atividades.getText().toString());

                if (!nome_empresa.getText().toString().isEmpty()
                        && !cargo.getText().toString().isEmpty()
                        && !salario.getText().toString().isEmpty()
                        && !nivel_hieraquico.getText().toString().isEmpty()
                        && !area_profissional.getText().toString().isEmpty()
                        && !especialidade.getText().toString().isEmpty()
                        && !data_inicio.getText().toString().isEmpty()
                        && !data_saida.getText().toString().isEmpty()
                        && !descricao_atividades.getText().toString().isEmpty()) {

                    cadastrarExperiencia(idUsuarioLogado, experiencia);
                } else {
                    Toast.makeText(ExperienciaActivity.this, "Um ou mais campos estão vazios", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void cadastrarExperiencia(String idUsuarioLogado, Experiencia experiencia) {

        try {

            firebase = ConfiguracaoFirebase.getFirebase().child("experiencias");
            firebase.child(idUsuarioLogado)
                    .push()
                    .setValue(experiencia);

            Toast.makeText(ExperienciaActivity.this, "Sucesso ao cadastrar experiencia", Toast.LENGTH_LONG).show();
            finish();

        } catch (Exception e) {
            Toast.makeText(ExperienciaActivity.this, "Erro ao cadastrar experiencia", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
