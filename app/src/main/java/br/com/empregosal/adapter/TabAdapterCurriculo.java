package br.com.empregosal.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.empregosal.activity.fragment.ExperienciaFragment;
import br.com.empregosal.activity.fragment.FormacaoFragment;
import br.com.empregosal.activity.fragment.IdiomasFragment;
import br.com.empregosal.activity.fragment.ObjetivosFragment;

public class TabAdapterCurriculo extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"EXPERIÊNCIA", "FORMAÇÃO", "IDIOMAS", "OBJETIVOS"};

    public TabAdapterCurriculo(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) { //Retorna os fragmentos
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new ExperienciaFragment();
                break;
            case 1:
                fragment = new FormacaoFragment();
                break;
            case 2:
                fragment = new IdiomasFragment();
                break;
            case 3:
                fragment = new ObjetivosFragment();
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
