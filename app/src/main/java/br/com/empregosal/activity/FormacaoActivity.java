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
import br.com.empregosal.model.Formacao;

public class FormacaoActivity extends AppCompatActivity {

    private Button bt_salvar;
    private Toolbar toolbar;
    private EditText nome_curso;
    private EditText nivel_curso;
    private EditText nome_instituicao;
    private EditText estado_curso;
    private EditText data_inicio;
    private EditText data_conclusao;
    private EditText uf_curso;

    private Formacao formacao;
    private String idUsuarioLogado;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formacao);

        nome_curso = findViewById(R.id.et_nome_curso);
        nivel_curso = findViewById(R.id.et_nivel_curso);
        nome_instituicao = findViewById(R.id.et_nome_instituicao);
        estado_curso = findViewById(R.id.et_estado_curso);
        data_inicio = findViewById(R.id.et_data_inicio_curso);
        data_conclusao = findViewById(R.id.et_data_conclusao);
        estado_curso = findViewById(R.id.et_estado_curso);
        bt_salvar = findViewById(R.id.bt_salvar_formacao);
        uf_curso = findViewById(R.id.et_uf_curso);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Nova Formação");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        // dados do usuário logado
        Preferencias preferencias = new Preferencias(FormacaoActivity.this);
        idUsuarioLogado = preferencias.getIdentificador();

        bt_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                formacao = new Formacao();
                formacao.setNomeCurso(nome_curso.getText().toString());
                formacao.setNivelCurso(nivel_curso.getText().toString());
                formacao.setNomeInstituicao(nome_instituicao.getText().toString());
                formacao.setEstadoCurso(estado_curso.getText().toString());
                formacao.setDataInicio(data_inicio.getText().toString());
                formacao.setDataConclusao(data_conclusao.getText().toString());

                if (!nome_curso.getText().toString().isEmpty()
                        && !nivel_curso.getText().toString().isEmpty()
                        && !nome_instituicao.getText().toString().isEmpty()
                        && !estado_curso.getText().toString().isEmpty()
                        && !data_inicio.getText().toString().isEmpty()
                        && !data_conclusao.getText().toString().isEmpty()
                        && !estado_curso.getText().toString().isEmpty()) {

                    cadastrarFormacao(idUsuarioLogado, formacao);
                } else {
                    Toast.makeText(FormacaoActivity.this, "Um ou mais campos estão vazios", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void cadastrarFormacao(String idUsuarioLogado, Formacao formacao) {

        try {

            firebase = ConfiguracaoFirebase.getFirebase().child("formacao");
            firebase.child(idUsuarioLogado)
                    .push()
                    .setValue(formacao);

            Toast.makeText(FormacaoActivity.this, "Sucesso ao cadastrar formacao", Toast.LENGTH_LONG).show();
            finish();

        } catch (Exception e) {
            Toast.makeText(FormacaoActivity.this, "Erro ao cadastrar formacao", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
