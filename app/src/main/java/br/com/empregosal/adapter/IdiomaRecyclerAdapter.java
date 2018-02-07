package br.com.empregosal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.empregosal.R;
import br.com.empregosal.model.Idioma;

public class IdiomaRecyclerAdapter extends RecyclerView.Adapter<IdiomaRecyclerAdapter.MyViewHolder> {

    private ArrayList<Idioma> idiomas;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView idiomaIdioma, idiomaNivel;

        public MyViewHolder(View view) {
            super(view);
            idiomaIdioma = (TextView) view.findViewById(R.id.idioma_idioma);
            idiomaNivel = (TextView) view.findViewById(R.id.idioma_nivel);
        }
    }

    public IdiomaRecyclerAdapter(ArrayList<Idioma> idiomas) {
        this.idiomas = idiomas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.idioma_lista_linha, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Idioma idioma = idiomas.get(position);
        holder.idiomaIdioma.setText(idioma.getIdioma());
        holder.idiomaNivel.setText(idioma.getNivel());
    }

    @Override
    public int getItemCount() {
        return idiomas.size();
    }
}
