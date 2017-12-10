package br.com.empregosal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import br.com.empregosal.model.Empresa;

public class DadosEmpresaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference firebase;
    private FirebaseAuth usuarioFirebase;
    private EditText nome;
    private EditText cnpj;
    private EditText razao_social;
    private EditText descricao_empresa;
    private EditText setor;
    private EditText cep;
    private EditText endereco;
    private EditText estado;
    private EditText cidade;
    private EditText numero;
    private EditText complemento;
    private ValueEventListener valueEventListenerUsuario;
    private Button bt_alterar;

    @Override
    protected void onStart() {
        super.onStart();
        reference.child("empresas").child(usuarioFirebase.getCurrentUser().getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Empresa empresa = dataSnapshot.getValue(Empresa.class);

                        nome.setText(empresa.getNome());
                        cnpj.setText(empresa.getCNPJ());
                        razao_social.setText(empresa.getRazaoSocial());
                        descricao_empresa.setText(empresa.getDescricao());
                        setor.setText(empresa.getSetor());
                        cep.setText(empresa.getCEP());
                        endereco.setText(empresa.getEndereco());
                        estado.setText(empresa.getEstado());
                        cidade.setText(empresa.getCidade());
                        numero.setText(empresa.getNumero());
                        complemento.setText(empresa.getComplemento());

                        toolbar.setTitle("Edição Dados Pessoais");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_empresa);

        nome = findViewById(R.id.et_nome_alterar_empresa);
        cnpj = findViewById(R.id.et_cnpj_alterar);
        razao_social = findViewById(R.id.et_razao_social_alterar);
        descricao_empresa = findViewById(R.id.et_descricao_empresa_alterar);
        setor = findViewById(R.id.et_setor_alterar);
        cep = findViewById(R.id.ed_cep_empresa_alterar);
        endereco = findViewById(R.id.ed_endereco_empresa_alterar);
        estado = findViewById(R.id.et_uf_empresa_alterar);
        cidade = findViewById(R.id.et_cidade_empresa_alterar);
        numero = findViewById(R.id.et_numero_empresa_alterar);
        complemento = findViewById(R.id.et_complemento_empresa_alterar);
        bt_alterar = findViewById(R.id.bt_alterar_dados_empresa);

        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("EmpregosAL");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        bt_alterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference.child("empresas").child(usuarioFirebase.getCurrentUser().getUid()).
                        addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Empresa empresa = dataSnapshot.getValue(Empresa.class);

                                HashMap<String, Object> dados = new HashMap<String, Object>();
                                dados.put("nome", nome.getText().toString());
                                dados.put("cnpj", cnpj.getText().toString());
                                dados.put("razaoSocial", razao_social.getText().toString());
                                dados.put("descricao", descricao_empresa.getText().toString());
                                dados.put("setor", setor.getText().toString());
                                dados.put("cep", cep.getText().toString());
                                dados.put("endereco", endereco.getText().toString());
                                dados.put("estado", estado.getText().toString());
                                dados.put("cidade", cidade.getText().toString());
                                dados.put("numero", numero.getText().toString());
                                dados.put("complemento", complemento.getText().toString());

                                reference.child("empresas").child(usuarioFirebase.getCurrentUser().getUid()).updateChildren(dados);
                                finish();
                                Toast.makeText(DadosEmpresaActivity.this, "Dados alterados com sucesso!", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_sair:
                deslogarUsuario();
                return true;
            case R.id.item_configurações:
                return true;
            case R.id.item_pesquisa:
                return true;
            default:
                return super.onOptionsItemSelected(item); //Padrão para Android
        }
    }

    private void deslogarUsuario() {

        usuarioFirebase.signOut();

        Intent intent = new Intent(DadosEmpresaActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
