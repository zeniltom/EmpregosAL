package br.com.empregosal.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.empregosal.R;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.helper.Permissao;
import br.com.empregosal.helper.Preferencias;
import br.com.empregosal.model.Empresa;
import br.com.empregosal.model.Usuario;
import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button botaoLogar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private SpotsDialog progressDialog;
    private String identificadorUsuarioLogado;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerEmpresa;
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Permissao.validaPermissao(1, this, permissoesNecessarias);


        verificarUsuarioLogado();

        email = findViewById(R.id.edit_login_email);
        senha = findViewById(R.id.edit_login_senha);
        botaoLogar = (Button) findViewById(R.id.bt_logar);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = new Usuario();
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());

                if (!email.getText().toString().isEmpty() && !senha.getText().toString().isEmpty())  //Validação de campos
                    validarLogin();
                else
                    Toast.makeText(LoginActivity.this, "Um ou mais campos estão vazios", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void verificarUsuarioLogado() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.getCurrentUser();
        if (autenticacao.getCurrentUser() != null) {
            abrirTelaPrincipal();
        }
    }

    private void validarLogin() {
        progressDialog = new SpotsDialog(this, "Entrando...", R.style.dialogEmpregosAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            identificadorUsuarioLogado = task.getResult().getUser().getUid();

                                            firebase = ConfiguracaoFirebase.getFirebase()
                                                    .child("usuarios")
                                                    .child(identificadorUsuarioLogado);

                                            valueEventListenerUsuario = new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    Usuario usuarioRecuperado = dataSnapshot.getValue(Usuario.class);

                                                    //Salvando nas preferencias do usuario
                                                    Preferencias preferencias = new Preferencias(LoginActivity.this);

                                                    //TENTE USAR UM IF AQUI EM BAIXO PARA VERIFICAR O GETTIPO SE É NULO MANDE PARA EMPRESA
                                                    if (usuarioRecuperado == null) {
                                                        firebase = ConfiguracaoFirebase.getFirebase()
                                                                .child("empresas")
                                                                .child(identificadorUsuarioLogado);

                                                        valueEventListenerEmpresa = new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                                Empresa empresaRecuperada = dataSnapshot.getValue(Empresa.class);

                                                                Preferencias preferencias1 = new Preferencias(LoginActivity.this);

                                                                preferencias1.salvarDados(identificadorUsuarioLogado, empresaRecuperada.getTipo());
                                                                abrirTelaPrincipal();
                                                                progressDialog.dismiss();
                                                                Toast.makeText(LoginActivity.this, "Sucesso ao logar!", Toast.LENGTH_LONG).show();

                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        };

                                                        firebase.addListenerForSingleValueEvent(valueEventListenerEmpresa);
                                                    } else {

                                                        preferencias.salvarDados(identificadorUsuarioLogado, usuarioRecuperado.getTipo());

                                                        abrirTelaPrincipal();
                                                        progressDialog.dismiss();
                                                        Toast.makeText(LoginActivity.this, "Sucesso ao logar!", Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            };

                                            firebase.addListenerForSingleValueEvent(valueEventListenerUsuario);

                                        } else {
                                            progressDialog.dismiss();
                                            String erroExcecao = "";

                                            try {
                                                throw task.getException();

                                            } catch (FirebaseAuthInvalidUserException e) {
                                                erroExcecao = "E-mail não existe ou desativado!";

                                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                                erroExcecao = "Senha digitada incorreta!";

                                            } catch (IllegalArgumentException e) {
                                                erroExcecao = "Campos vazios!";

                                            } catch (FirebaseApiNotAvailableException e) {
                                                erroExcecao = "É necessário o 'Google Play Services' para prosseguir!";

                                            } catch (FirebaseNetworkException e) {
                                                erroExcecao = "Sem concexão!";
                                                e.printStackTrace();

                                            } catch (Exception e) {
                                                erroExcecao = "Ao efetuar o cadastro!";
                                                e.printStackTrace();
                                            }
                                            Toast.makeText(LoginActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }

        );
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int resultado : grantResults) {

            if (resultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            } else {
                Toast.makeText(LoginActivity.this, "Permissão concedida!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void alertaValidacaoPermissao() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar este app, é necessário aceitar as permissões");
        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void abrirTelaPrincipal() {

        Preferencias preferencias = new Preferencias(LoginActivity.this);
        String tipo_usuario = preferencias.getTipoUsuario();

        if (tipo_usuario.equals("usuario")) {

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            //Cria um novo registro de telas abertas e apaga as antigas
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            Log.i("$$$$ usuario aqui $$$$", tipo_usuario.toString());

        } else {
            Intent intent = new Intent(LoginActivity.this, MainEmpresaActivity.class);
            //Cria um novo registro de telas abertas e apaga as antigas
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            Log.i("$$$ empresa aqui $$$", tipo_usuario.toString());
        }
    }

    public void abrirCadastroUsuario(View view) {

        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }

    public void abrirCadastroEmpresa(View view) {

        Intent intent = new Intent(LoginActivity.this, CadastroEmpresaActivity.class);
        startActivity(intent);
    }
}
