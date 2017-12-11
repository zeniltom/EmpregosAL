package br.com.empregosal.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
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

import br.com.empregosal.R;
import br.com.empregosal.adapter.TabAdapter;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.helper.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private FirebaseAuth usuarioFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager conexao = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conexao.getNetworkInfo(0).isConnected()) {
            Toast.makeText(MainActivity.this, "Conexão 3G", Toast.LENGTH_SHORT).show();
        } else if (conexao.getNetworkInfo(1).isConnected()) {
            Toast.makeText(MainActivity.this, "Conexão WIFi", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Desconectado", Toast.LENGTH_SHORT).show();
        }

        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("EmpregosAL");
        setSupportActionBar(toolbar);

        //Tabs
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slt_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        //Configurar sliding tab
        slidingTabLayout.setDistributeEvenly(true);
        //Cor no item selecionado
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        //Configurar Adapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ConnectivityManager conexao = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conexao.getNetworkInfo(0).isConnected()) {
            Toast.makeText(MainActivity.this, "Conexão 3G", Toast.LENGTH_SHORT).show();
        } else if (conexao.getNetworkInfo(1).isConnected()) {
            Toast.makeText(MainActivity.this, "Conexão WIFi", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Desconectado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_sair:
                deslogarUsuario();
                return true;
            case R.id.item_perfil:
                alterarDadosUsuario();
                return true;
            case R.id.item_configurações:
                return true;
            case R.id.item_curriculo:
                abrirCurriculo();
                return true;
            case R.id.item_pesquisa:
                return true;
            default:
                return super.onOptionsItemSelected(item); //Padrão para Android
        }
    }

    private void abrirCurriculo() {

        Intent intent = new Intent(MainActivity.this, CurriculoActivity.class);
        startActivity(intent);
    }

    private void alterarDadosUsuario() {

        Intent intent = new Intent(MainActivity.this, DadosUsuarioActivity.class);
        startActivity(intent);
    }

    private void deslogarUsuario() {

        usuarioFirebase.signOut();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
