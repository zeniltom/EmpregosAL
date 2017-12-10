package br.com.empregosal.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.com.empregosal.R;
import br.com.empregosal.adapter.TabAdapterCurriculo;
import br.com.empregosal.helper.SlidingTabLayout;

public class CurriculoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curriculo);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("EmpregosAL");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //Tabs
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slt_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        //Configurar sliding tab
        slidingTabLayout.setDistributeEvenly(true);
        //Cor no item selecionado
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        //Configurar Adapter
        TabAdapterCurriculo tabAdapterCurriculo = new TabAdapterCurriculo(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapterCurriculo);

        slidingTabLayout.setViewPager(viewPager);
    }
}
