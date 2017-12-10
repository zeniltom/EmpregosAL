package br.com.empregosal.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.google.firebase.auth.UserProfileChangeRequest;

import br.com.empregosal.R;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.helper.Preferencias;
import br.com.empregosal.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private EditText cpf;
    private Button botaoCadastrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private FirebaseUser firebaseUser;
    ProgressDialog progressDialog;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);
        
        nome = (EditText) findViewById(R.id.edit_cadastro_nome_empresa);
        email = (EditText) findViewById(R.id.edit_cadastro_email_empresa);
        senha = (EditText) findViewById(R.id.edit_cadastro_senha_empresa);
        cpf = (EditText) findViewById(R.id.edit_cadastro_cnpj);
        botaoCadastrar = (Button) findViewById(R.id.bt_cadastrar);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {   // Tratando o NumberFormatException do CPF vazio no cadastro
                    usuario = new Usuario();
                    usuario.setNome(nome.getText().toString());
                    usuario.setEmail(email.getText().toString());
                    usuario.setSenha(senha.getText().toString());
                    usuario.setCPF(cpf.getText().toString());

                    if (!nome.getText().toString().isEmpty() && !email.getText().toString().isEmpty()
                            && !senha.getText().toString().isEmpty() && !cpf.getText().toString().isEmpty()) {
                        cadastrarUsuario();
                    } else {
                        Toast.makeText(CadastroUsuarioActivity.this, "Um ou mais campos estão vazios", Toast.LENGTH_LONG).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(CadastroUsuarioActivity.this, "Somente números!", Toast.LENGTH_LONG).show();
                    Log.i("NumberFormatException", e.getMessage());

                }
            }
        });
    }

    private void cadastrarUsuario() {
        progressDialog = new ProgressDialog(CadastroUsuarioActivity.this);
        progressDialog.setMessage("Cadastrando...");
        progressDialog.show();

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Salvar com id
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    UserProfileChangeRequest atualizarPerfil = new UserProfileChangeRequest.Builder().setDisplayName(usuario.getNome()).build();
                    usuarioFirebase.updateProfile(atualizarPerfil);

                    String identificadorUsuario = usuarioFirebase.getUid();

                    usuario.setIdUsuario(identificadorUsuario);
                    usuario.setTipo("usuario"); //Tipo de Usuário
                    usuario.salvar();

                    String tipo = usuario.getTipo();
                    //Salvando nas preferencias do usuario
                    Preferencias preferencias = new Preferencias(CadastroUsuarioActivity.this);
                    preferencias.salvarDados(identificadorUsuario, tipo);

                    Toast.makeText(CadastroUsuarioActivity.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_LONG).show();

                    abrirLoginUsuario();
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
                    Toast.makeText(CadastroUsuarioActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void abrirLoginUsuario() {
        Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

