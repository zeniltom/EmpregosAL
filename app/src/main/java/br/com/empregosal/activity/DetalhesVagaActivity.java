package br.com.empregosal.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import br.com.empregosal.R;

public class DetalhesVagaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView cartaoSobre;
    private CardView cartaoDescricao;
    private CardView cartaoDetalhes;
    private TextView cargo;
    private TextView descricao;
    private TextView localizacao;
    private TextView data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_vaga);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detalhes da Vaga");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        cartaoSobre = findViewById(R.id.cartaoSobre);
        cartaoDescricao = findViewById(R.id.cartaoDescricao);
        cartaoDetalhes = findViewById(R.id.cartaoDetalhes);
        cargo = findViewById(R.id.tv_cargo_detalhes);
        descricao = findViewById(R.id.tv_descricao_detalhes);
        localizacao = findViewById(R.id.tv_localizacao_detalhes);
        data = findViewById(R.id.tv_data_anuncio_detalhes);

        Bundle dadosPassados = getIntent().getExtras();

        if (dadosPassados != null){
            cargo.setText(dadosPassados.getString("vaga_cargo"));
            localizacao.setText(dadosPassados.getString("vaga_localizacao"));
            descricao.setText(dadosPassados.getString("vaga_descricao"));
            data.setText(dadosPassados.getString("vaga_data"));
        }
    }
}
