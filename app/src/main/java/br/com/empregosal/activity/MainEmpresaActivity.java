package br.com.empregosal.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    protected void onResume() {
        super.onResume();
        conexao();
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

        SearchView searchView = new SearchView(this);
        searchView.setOnQueryTextListener(new Pesquisa());
        return true;
    }

    public class Pesquisa implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            Log.i("onQueryTextSubmit -> ", query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            Log.i("onQueryTextChange -> ", newText);
            return false;
        }
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
        startActivity(intent);
    }

    private void alterarDadosEmpresa() {

        Intent intent = new Intent(MainEmpresaActivity.this, DadosEmpresaActivity.class);
        startActivity(intent);
    }

    private void conexao() {
        ConnectivityManager conexao = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conexao.getNetworkInfo(0).isConnected()) {
            Log.i("Conexão 3G", conexao.getNetworkInfo(0).toString());
        } else if (conexao.getNetworkInfo(1).isConnected()) {
            Log.i("Conexão WIFi", conexao.getNetworkInfo(1).toString());
        } else {
            Toast.makeText(getApplicationContext(), "Desconectado", Toast.LENGTH_SHORT).show();
        }
    }

    private void deslogarUsuario() {

        usuarioFirebase.signOut();

        Intent intent = new Intent(MainEmpresaActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

