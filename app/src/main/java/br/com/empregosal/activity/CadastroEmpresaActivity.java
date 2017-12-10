package br.com.empregosal.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import br.com.empregosal.R;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.helper.Preferencias;
import br.com.empregosal.model.Empresa;

public class CadastroEmpresaActivity extends AppCompatActivity {

    private EditText nome_empresa;
    private EditText email_empresa;
    private EditText senha_empresa;
    private EditText cnpj;
    private Button botaoCadastrarEmpresa;
    private Empresa empresa;
    private FirebaseAuth autenticacao;
    private FirebaseUser firebaseUser;
    ProgressDialog progressDialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_empresa);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);
        
        nome_empresa = (EditText) findViewById(R.id.edit_cadastro_nome_empresa);
        email_empresa = (EditText) findViewById(R.id.edit_cadastro_email_empresa);
        senha_empresa = (EditText) findViewById(R.id.edit_cadastro_senha_empresa);
        cnpj = (EditText) findViewById(R.id.edit_cadastro_cnpj);
        botaoCadastrarEmpresa = (Button) findViewById(R.id.bt_cadastrar_empresa);

        botaoCadastrarEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {   // Tratando o NumberFormatException do CPF vazio no cadastro
                    empresa = new Empresa();
                    empresa.setNome(nome_empresa.getText().toString());
                    empresa.setEmail(email_empresa.getText().toString());
                    empresa.setSenha(senha_empresa.getText().toString());
                    empresa.setCNPJ(cnpj.getText().toString());

                    if (!nome_empresa.getText().toString().isEmpty() && !email_empresa.getText().toString().isEmpty()
                            && !senha_empresa.getText().toString().isEmpty() && !cnpj.getText().toString().isEmpty()) {
                        cadastrarEmpresa();
                    } else {
                        Toast.makeText(CadastroEmpresaActivity.this, "Um ou mais campos estão vazios", Toast.LENGTH_LONG).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(CadastroEmpresaActivity.this, "Somente números", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void cadastrarEmpresa() {
        progressDialog = new ProgressDialog(br.com.empregosal.activity.CadastroEmpresaActivity.this);
        progressDialog.setMessage("Cadastrando...");
        progressDialog.show();

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                empresa.getEmail(),
                empresa.getSenha()
        ).addOnCompleteListener(br.com.empregosal.activity.CadastroEmpresaActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Salvar com id
                    FirebaseUser empresaFirebase = task.getResult().getUser();

                    String identificadorEmpresa = empresaFirebase.getUid();

                    empresa.setIdEmpresa(identificadorEmpresa);
                    empresa.setTipo("empresa"); //Tipo de Usuário
                    empresa.salvar();

                    //Salvando nas preferencias do empresa
                    Preferencias preferencias = new Preferencias(CadastroEmpresaActivity.this);
                    preferencias.salvarDados(identificadorEmpresa, empresa.getTipo());

                    Toast.makeText(br.com.empregosal.activity.CadastroEmpresaActivity.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_LONG).show();

                    abrirLoginEmpresa();
                    progressDialog.dismiss();

                } else {
                    progressDialog.dismiss();

                    String erroExcecao = "";

                    try {
                        throw task.getException();

                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte, contendo mais caracteres, letras e números!";

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "O E-mail digitado é inválido, digite um novo E-mail!";

                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Esse E-mail já está em uso!";

                    } catch (FirebaseNetworkException e) {
                        erroExcecao = "Não há conexão com a internet!";

                    } catch (Exception e) {
                        erroExcecao = "Ao efetuar o cadastro!";
                        e.printStackTrace();
                    }
                    Toast.makeText(br.com.empregosal.activity.CadastroEmpresaActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void abrirLoginEmpresa() {
        Intent intent = new Intent(br.com.empregosal.activity.CadastroEmpresaActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}


