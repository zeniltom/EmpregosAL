package br.com.empregosal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.empregosal.R;
import br.com.empregosal.model.Objetivo;

public class ObjetivoRecyclerAdapter extends RecyclerView.Adapter<ObjetivoRecyclerAdapter.MyViewHolder> {

    private ArrayList<Objetivo> objetivos;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tipoJornada, tipoContrato, nivelHierarquico, salarioDesejado;

        MyViewHolder(View view) {
            super(view);
            tipoJornada = view.findViewById(R.id.objetivo_jornada);
            tipoContrato = view.findViewById(R.id.objetivo_tipo_contrato);
            nivelHierarquico = view.findViewById(R.id.objetivo_nivel_hierarquico);
            salarioDesejado = view.findViewById(R.id.objetivo_salario_desejado);
        }
    }

    public ObjetivoRecyclerAdapter(ArrayList<Objetivo> objetivos) {
        this.objetivos = objetivos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.objetivo_lista_linha, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Objetivo objetivo = objetivos.get(position);
        holder.tipoJornada.setText(objetivo.getTipoJornada());
        holder.tipoContrato.setText(objetivo.getTipoContrato());
        holder.nivelHierarquico.setText(objetivo.getNivelHierarquico());
        holder.salarioDesejado.setText(objetivo.getSalarioDesejado());
    }

    @Override
    public int getItemCount() {
        return objetivos.size();
    }
}
