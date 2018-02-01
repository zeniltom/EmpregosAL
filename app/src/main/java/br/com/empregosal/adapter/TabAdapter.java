package br.com.empregosal.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.empregosal.activity.fragment.CandidaturaFragment;
import br.com.empregosal.activity.fragment.VagasEmpregoFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"VAGAS DISPONÍVEIS", "CANDIDATURAS"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) { //Retorna os fragmentos
        Fragment fragment = null;

        switch (position){
            case 0:
               fragment = new VagasEmpregoFragment();
                break;
            case 1:
               fragment = new CandidaturaFragment();
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
