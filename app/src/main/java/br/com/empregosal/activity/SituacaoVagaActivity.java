package br.com.empregosal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.empregosal.R;
import br.com.empregosal.model.Vaga;

public class SituacaoVagaActivity extends AppCompatActivity {

    private TextView descricao;
    private TextView botaoVerMais;
    private View separador_detalhes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situacao_vaga);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Situação da Vaga");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        TextView cargo = findViewById(R.id.tv_cargo_detalhes);
        TextView localizacao = findViewById(R.id.tv_localizacao_detalhes);
        descricao = findViewById(R.id.tv_descricao_detalhes);
        TextView data = findViewById(R.id.tv_data_anuncio_detalhes);
        TextView area_proficional = findViewById(R.id.tv_area_profissional_detalhes);
        TextView tipo_contrato = findViewById(R.id.tv_contrato_detalhes);
        TextView nivel_hierarquico = findViewById(R.id.tv_nivel_hierarquico__detalhes);
        TextView nivel_estudos = findViewById(R.id.tv_nivel_estudos_detalhes);
        TextView jornada = findViewById(R.id.tv_jornada_detalhes);
        TextView faixa_salarial = findViewById(R.id.tv_faixa_salarial_detalhes);
        TextView cep = findViewById(R.id.tv_cep_detalhes);
        TextView qtd = findViewById(R.id.tv_qtd_detalhes);
        Button botaoVerCandidatos = findViewById(R.id.botao_ver_candidatos);
        botaoVerMais = findViewById(R.id.botao_ver_mais_detalhes);
        separador_detalhes = findViewById(R.id.separador_descricao_vaga_detalhes);

        final Bundle dadosPassados = getIntent().getExtras();

        if (dadosPassados != null) {

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String vd = String.valueOf(dadosPassados.get("vaga_data"));
            Date dataHoje = new Date();
            Date dataAnuncio = null;

            try {
                dataAnuncio = format.parse(vd);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long diferenca = (dataHoje.getTime() - dataAnuncio.getTime());
            long diferencaSegundos = diferenca / (1000);
            long diferencaMinutos = diferenca / (1000 * 60);
            long diferencaHoras = diferenca / (1000 * 60 * 60);
            long diferencaDias = diferenca / (1000 * 60 * 60 * 24);
            long diferencaMeses = diferenca / (1000 * 60 * 60 * 24) / 30;

            if (diferencaMeses > 0) {
                data.setText("Há " + diferencaMeses + " meses");

            } else if (diferencaDias > 0) {
                data.setText("Há " + diferencaDias + " dias");

            } else if (diferencaHoras > 0) {
                data.setText("Há " + diferencaHoras + " horas");

            } else if (diferencaMinutos > 0) {
                data.setText("Há " + diferencaMinutos + " minutos");

            } else if (diferencaSegundos > 0) {
                data.setText("Há alguns segundos");
            }

            cargo.setText(dadosPassados.getString("vaga_cargo"));
            localizacao.setText(dadosPassados.getString("vaga_localizacao"));
            descricao.setText(dadosPassados.getString("vaga_descricao"));
            area_proficional.setText(dadosPassados.getString("vaga_area_profissional"));
            tipo_contrato.setText(dadosPassados.getString("vaga_tipo_contrato"));
            nivel_hierarquico.setText(dadosPassados.getString("vaga_nivel_hierarquico"));
            nivel_estudos.setText(dadosPassados.getString("vaga_nivel_estudos"));
            jornada.setText(dadosPassados.getString("vaga_jornada"));
            faixa_salarial.setText(dadosPassados.getString("vaga_faixa_salarial"));
            cep.setText(dadosPassados.getString("vaga_cep"));
            qtd.setText(dadosPassados.getString("vaga_qtd"));
        }

        botaoVerMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descricao.setVisibility(View.VISIBLE);
                botaoVerMais.setVisibility(View.GONE);
                separador_detalhes.setVisibility(View.VISIBLE);

            }
        });

        botaoVerCandidatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verCandidatos();
            }
        });
    }

    private void verCandidatos() {
        Intent intent = new Intent(getApplicationContext(), ListaCandidatosActivity.class);

        Vaga vaga = (Vaga) getIntent().getSerializableExtra("vaga");
        intent.putExtra("vaga", vaga);

        Log.i("#CLASSE nome", vaga.getCargo());

        startActivity(intent);
    }
}
