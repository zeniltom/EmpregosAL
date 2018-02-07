package br.com.empregosal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.empregosal.R;
import br.com.empregosal.model.Formacao;

public class FormacaoRecyclerAdapter extends RecyclerView.Adapter<FormacaoRecyclerAdapter.MyViewHolder> {

    private ArrayList<Formacao> formacoes;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nomeInstituicao, nomeCurso, nivelCurso, estadoCurso, dataInicio, dataConclusao;

        public MyViewHolder(View view) {
            super(view);
            nomeInstituicao = view.findViewById(R.id.formacao_instituicao);
            nomeCurso = view.findViewById(R.id.formacao_nome_curso);
            nivelCurso = view.findViewById(R.id.formacao_nivel_curso);
            estadoCurso = view.findViewById(R.id.formacao_estado_curso);
            dataInicio = view.findViewById(R.id.formacao_data_inicio);
            dataConclusao = view.findViewById(R.id.formacao_data_conclusao);
        }
    }

    public FormacaoRecyclerAdapter(ArrayList<Formacao> formacoes) {
        this.formacoes = formacoes;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.formacao_lista_linha, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Formacao formacao = formacoes.get(position);
        holder.nomeInstituicao.setText(formacao.getNomeInstituicao());
        holder.nomeCurso.setText(formacao.getNomeCurso());
        holder.nivelCurso.setText(formacao.getNivelCurso());
        holder.estadoCurso.setText(formacao.getEstadoCurso());
        holder.dataInicio.setText(formacao.getDataInicio());
        holder.dataConclusao.setText(formacao.getDataConclusao());
    }

    @Override
    public int getItemCount() {
        return formacoes.size();
    }
}
