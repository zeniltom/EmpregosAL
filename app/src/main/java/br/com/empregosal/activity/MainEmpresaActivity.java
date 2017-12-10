package br.com.empregosal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.empregosal.R;
import br.com.empregosal.adapter.TabAdapterEmpresa;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.helper.SlidingTabLayout;
import br.com.empregosal.viacep.org.ceprest.BuscaCep;

public class MainEmpresaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth usuarioFirebase;

    @Override
    protected void onStart() {
        super.onStart();
        reference.child("empresas").child(usuarioFirebase.getCurrentUser().getUid()).child("nome").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String nome = dataSnapshot.getValue(String.class);
                        toolbar.setTitle(nome);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainEmpresaActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_empresa);

        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao();
        reference = ConfiguracaoFirebase.getFirebase();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("EmpregosAL");
        setSupportActionBar(toolbar);

        //Tabs
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slt_tabsEmpresa);
        viewPager = (ViewPager) findViewById(R.id.vp_paginaEmpresa);

        //Configurar sliding tab
        slidingTabLayout.setDistributeEvenly(true);
        //Cor no item selecionado
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        //Configurar Adapter
        TabAdapterEmpresa tabAdapterEmpresa = new TabAdapterEmpresa(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapterEmpresa);

        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_empresa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_sair:
                deslogarUsuario();
                return true;
            case R.id.item_perfil:
                alterarDadosEmpresa();
                return true;
            case R.id.item_configurações:
                return true;
            case R.id.item_pesquisa:
                abrirCep();
                return true;
            case R.id.item_anunciar_vaga:
                cadastrarVaga();
                return true;
            default:
                return super.onOptionsItemSelected(item); //Padrão para Android
        }
    }

    private void abrirCep() {
        Intent intent = new Intent(MainEmpresaActivity.this, BuscaCep.class);
        startActivity(intent);
    }

    private void cadastrarVaga() {

        Intent intent = new Intent(MainEmpresaActivity.this, VagasCadastroActivity.class);
        startActivity(intent);    }

    private void alterarDadosEmpresa() {

        Intent intent = new Intent(MainEmpresaActivity.this, DadosEmpresaActivity.class);
        startActivity(intent);
    }

    private void deslogarUsuario() {

        usuarioFirebase.signOut();

        Intent intent = new Intent(MainEmpresaActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

