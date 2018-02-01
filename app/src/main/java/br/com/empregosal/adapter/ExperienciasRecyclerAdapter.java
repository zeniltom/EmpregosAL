package br.com.empregosal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.empregosal.R;
import br.com.empregosal.model.Experiencia;

public class ExperienciasRecyclerAdapter extends RecyclerView.Adapter<ExperienciasRecyclerAdapter.MyViewHolder> {

    private ArrayList<Experiencia> experiencias;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nomeEmpresa, cargoExp, data_inicio_exp, data_final_exp, atividades_exp;

        public MyViewHolder(View view) {
            super(view);
            nomeEmpresa = (TextView) view.findViewById(R.id.exp_nome_empresa);
            cargoExp = (TextView) view.findViewById(R.id.exp_cargo);
            data_inicio_exp = (TextView) view.findViewById(R.id.exp_data_inicio);
            data_final_exp = (TextView) view.findViewById(R.id.exp_data_final);
            atividades_exp = (TextView) view.findViewById(R.id.exp_descricao_atv);
        }
    }

    public ExperienciasRecyclerAdapter(ArrayList<Experiencia> experiencias) {
        this.experiencias = experiencias;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.experiencia_lista_linha, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Experiencia movie = experiencias.get(position);
        holder.nomeEmpresa.setText(movie.getNomeDaEmpresa());
        holder.cargoExp.setText(movie.getCargo());
        holder.data_inicio_exp.setText(movie.getDataInicio());
        holder.data_final_exp.setText(movie.getDataSaida());
        holder.atividades_exp.setText(movie.getDescricaoAtividades());
    }

    @Override
    public int getItemCount() {
        return experiencias.size();
    }
}
