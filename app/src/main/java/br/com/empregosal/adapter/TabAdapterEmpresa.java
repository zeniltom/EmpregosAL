package br.com.empregosal.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.empregosal.activity.fragment.GerenciaFragment;
import br.com.empregosal.activity.fragment.InicioFragment;

public class TabAdapterEmpresa extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"CANDIDATOS", "MINHAS VAGAS"};

    public TabAdapterEmpresa(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) { //Retorna os fragmentos
        Fragment fragment = null;

        switch (position){
            case 0:
               fragment = new InicioFragment();
                break;
            case 1:
               fragment = new GerenciaFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() { //Retorna quantidade de abas
        return tituloAbas.length;
    }

    @Override
    public CharSequence getPageTitle(int position) { //Recupera os titulos das abas
        return tituloAbas[position];
    }
}
