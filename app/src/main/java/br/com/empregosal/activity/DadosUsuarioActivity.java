package br.com.empregosal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import br.com.empregosal.model.Usuario;
import br.com.empregosal.viacep.org.ceprest.model.ViaCEP;
import dmax.dialog.SpotsDialog;

public class DadosUsuarioActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference firebase;
    private FirebaseAuth usuarioFirebase;
    private EditText nome;
    private EditText cpf;
    private EditText datanasc;
    private EditText nasc;
    private EditText numero;
    private EditText endereco;
    private EditText uf;
    private EditText complemento;
    private ValueEventListener valueEventListenerUsuario;
    private Button bt_alterar;
    private Button bt_consulta_cep;
    private EditText cep;
    private ViaCEP viacep;
    private EditText cidade;
    private SpotsDialog progressDialog;
    private String[] genero = new String[]{"Selecione", "Masculino", "Feminino"};
    private String[] estado_civil = new String[]{"Selecione", "Casado", "Divorciado", "Separado", "Solteiro", "Viúvo"};
    private Spinner spinner_sexo;
    private Spinner spinner_estado_civil;

    @Override
    protected void onStart() {
        super.onStart();
        reference.child("usuarios").child(usuarioFirebase.getCurrentUser().getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Usuario usuario = dataSnapshot.getValue(Usuario.class);

                        nome.setText(usuario.getNome());
                        cpf.setText(usuario.getCPF());
                        datanasc.setText(usuario.getData_nascimento());
                        nasc.setText(usuario.getNacionalidade());
                        endereco.setText(usuario.getEndereco());
                        cep.setText(usuario.getCEP());
                        cidade.setText(usuario.getCidade());
                        uf.setText(usuario.getUf());
                        complemento.setText(usuario.getComplemento());
                        numero.setText(usuario.getNumero());

                        carregarSpinners(usuario);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(DadosUsuarioActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_usuario);

        nome = findViewById(R.id.et_nome_alterar);
        cpf = findViewById(R.id.et_cpf_alterar);
        datanasc = findViewById(R.id.et_datanasc_alterar);
        nasc = findViewById(R.id.et_nac_alterar);
        cep = findViewById(R.id.et_cep_usuario_alterar);
        numero = findViewById(R.id.et_numero_usuario_alterar);
        endereco = findViewById(R.id.et_endereco_usuario_alterar);
        uf = findViewById(R.id.et_uf_usuario_alterar);
        cidade = findViewById(R.id.et_cidade_usuario_alterar);
        complemento = findViewById(R.id.et_complemento_usuario_alterar);
        bt_alterar = findViewById(R.id.bt_alterar_dados);
        bt_consulta_cep = findViewById(R.id.bt_consultar_cep);
        spinner_sexo = findViewById(R.id.spinner_sexo);
        spinner_estado_civil = findViewById(R.id.spinner_estado_civil);

        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edição Dados Pessoais");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        ArrayAdapter<String> arrayAdapterGenero = new ArrayAdapter<String>(this, R.layout.spinner_item, genero);
        arrayAdapterGenero.setDropDownViewResource(R.layout.spinner_dropdown_item);
        ArrayAdapter<String> arrayAdapterEstadoCivil = new ArrayAdapter<String>(this, R.layout.spinner_item, estado_civil);
        arrayAdapterEstadoCivil.setDropDownViewResource(R.layout.spinner_dropdown_item);


        spinner_sexo.setAdapter(arrayAdapterGenero);
        spinner_sexo.setPrompt("Selecione");
        spinner_estado_civil.setAdapter(arrayAdapterEstadoCivil);
        spinner_estado_civil.setPrompt("Selecione");

        bt_consulta_cep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarCep();
            }

        });

        bt_alterar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alterarDados();
            }
        });
    }

    private void alterarDados() {
        progressDialog = new SpotsDialog(DadosUsuarioActivity.this, "Salvando alterações...", R.style.dialogEmpregosAL);
        progressDialog.setCancelable(false);
        progressDialog.show();
        consultarCep();

        reference.child("usuarios").child(usuarioFirebase.getCurrentUser()
                .getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                HashMap<String, Object> dados = new HashMap<String, Object>();
                dados.put("nome", nome.getText().toString());
                dados.put("cpf", cpf.getText().toString());
                dados.put("data_nascimento", datanasc.getText().toString());
//                dados.put("estado_civ", spinner_estado_civil.getSelectedItem().toString());
                dados.put("nacionalidade", nasc.getText().toString());
//                dados.put("sexo", spinner_sexo.getSelectedItem().toString());
                dados.put("CEP", cep.getText().toString());
                dados.put("endereco", endereco.getText().toString());
                dados.put("cidade", cidade.getText().toString());
                dados.put("uf", uf.getText().toString());
                dados.put("complemento", complemento.getText().toString());
                dados.put("numero", numero.getText().toString());

                if (spinner_sexo.getSelectedItem().toString().equals("Selecione")) {
                    dados.put("sexo", "");
                } else {
                    dados.put("sexo", spinner_sexo.getSelectedItem().toString());
                }

                if (spinner_estado_civil.getSelectedItem().toString().equals("Selecione")) {
                    dados.put("estado_civ", "");
                } else {
                    dados.put("estado_civ", spinner_estado_civil.getSelectedItem().toString());
                }

                progressDialog.dismiss();

                reference.child("usuarios").child(usuarioFirebase.getCurrentUser().getUid()).updateChildren(dados);
                reference.keepSynced(true);

                Toast.makeText(DadosUsuarioActivity.this, "Dados alterados com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void consultarCep() {
        progressDialog = new SpotsDialog(DadosUsuarioActivity.this, "Procurando...", R.style.dialogEmpregosAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

        final String cep_dados = cep.getText().toString();

        if (!cep_dados.isEmpty()) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    viacep = new ViaCEP();
                    viacep.buscar(cep_dados);

                    handler.sendEmptyMessage(0);
                }
            }).start();
        } else {
            progressDialog.dismiss();
            Toast.makeText(DadosUsuarioActivity.this, "Preencha o CEP", Toast.LENGTH_LONG).show();
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message m) {
            switch (m.what) {
                case 0:
                    uf.setText(viacep.getUf());
                    cidade.setText(viacep.getLocalidade());

                    if (viacep.getLocalidade() == null) {
                        Toast.makeText(DadosUsuarioActivity.this, "CEP não encontrado!", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                    break;
            }
        }
    };


    private void carregarSpinners(Usuario usuario) {

        if (usuario.getSexo().equals("Masculino")) {
            spinner_sexo.setSelection(1);
        } else if (usuario.getSexo().equals("Feminino")) {
            spinner_sexo.setSelection(2);
        } else {
            spinner_sexo.setSelection(0);
        }

        if (usuario.getEstado_civ().equals("Casado")) {
            spinner_estado_civil.setSelection(1);
        } else if (usuario.getEstado_civ().equals("Divorciado")) {
            spinner_estado_civil.setSelection(2);
        } else if (usuario.getEstado_civ().equals("Separado")) {
            spinner_estado_civil.setSelection(3);
        } else if (usuario.getEstado_civ().equals("Solteiro")) {
            spinner_estado_civil.setSelection(4);
        } else if (usuario.getEstado_civ().equals("Viúvo")) {
            spinner_estado_civil.setSelection(5);
        } else {
            spinner_sexo.setSelection(0);
        }
    }

    private void deslogarUsuario() {

        usuarioFirebase.signOut();

        Intent intent = new Intent(DadosUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
