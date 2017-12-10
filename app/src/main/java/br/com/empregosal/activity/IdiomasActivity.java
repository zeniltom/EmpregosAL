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
import br.com.empregosal.model.Idioma;

public class IdiomasActivity extends AppCompatActivity {

    private Button bt_salvar;
    private Toolbar toolbar;
    private EditText nome_idioma;
    private EditText nivel_idioma;
    private Idioma idioma;
    private String idUsuarioLogado;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idiomas);

        nome_idioma = findViewById(R.id.et_nome_idioma);
        nivel_idioma = findViewById(R.id.et_nivel_idioma);
        bt_salvar = findViewById(R.id.bt_salvar_idioma);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Idioma");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        // dados do usuário logado
        Preferencias preferencias = new Preferencias(IdiomasActivity.this);
        idUsuarioLogado = preferencias.getIdentificador();

        bt_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idioma = new Idioma();
                idioma.setIdioma(nome_idioma.getText().toString());
                idioma.setNivel(nivel_idioma.getText().toString());

                if (!nome_idioma.getText().toString().isEmpty()
                        && !nivel_idioma.getText().toString().isEmpty()) {

                    cadastrarIdioma(idUsuarioLogado, idioma);
                } else {
                    Toast.makeText(IdiomasActivity.this, "Um ou mais campos estão vazios", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void cadastrarIdioma(String idUsuarioLogado, Idioma idioma) {

        try {

            firebase = ConfiguracaoFirebase.getFirebase().child("idiomas");
            firebase.child(idUsuarioLogado)
                    .push()
                    .setValue(idioma);

            Toast.makeText(IdiomasActivity.this, "Sucesso ao cadastrar formacao", Toast.LENGTH_LONG).show();
            finish();

        } catch (Exception e) {
            Toast.makeText(IdiomasActivity.this, "Erro ao cadastrar formacao", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
