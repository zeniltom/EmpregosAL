package br.com.empregosal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.empregosal.R;
import br.com.empregosal.model.Formacao;

public class FormacaoAdapter extends ArrayAdapter<Formacao> {

    private ArrayList<Formacao> formacoes;
    private Context context;

    public FormacaoAdapter(Context c, ArrayList<Formacao> objects) {
        super(c, 0, objects);
        this.formacoes = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        // Verifica se a lista está vazia
        if (formacoes != null) {

            // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.lista_formacao, parent, false);

            // recupera elemento para exibição
            TextView nomeInstituicao = view.findViewById(R.id.tv_nome_instituicao);
            TextView nivelCurso = view.findViewById(R.id.tv_nivel_curso);
            TextView dataInicio = view.findViewById(R.id.tv_data_inicio_curso);
            TextView dataConcl = view.findViewById(R.id.tv_data_conclusao);

            Formacao formacao = formacoes.get(position);
            nomeInstituicao.setText(formacao.getNomeInstituicao());
            nivelCurso.setText(formacao.getNivelCurso());
            dataInicio.setText(formacao.getDataInicio());
            dataConcl.setText(formacao.getDataConclusao());
        }

        return view;

    }
}
