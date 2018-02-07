package br.com.empregosal.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.empregosal.R;
import br.com.empregosal.model.Registro;

public class DetalhesRegistroActivity extends AppCompatActivity {

    private TextView descricao;
    private TextView botaoVerMais;
    private View separador_detalhes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_registro);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Detalhes da Vaga");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        TextView candidato = findViewById(R.id.tv_candidato_nome);
        TextView cargo = findViewById(R.id.tv_cargo_detalhes);
        TextView localizacao = findViewById(R.id.tv_localizacao_detalhes);
        descricao = findViewById(R.id.tv_descricao_detalhes);
        TextView data = findViewById(R.id.tv_data_anuncio_detalhes);
        TextView nome_empresa = findViewById(R.id.tv_nome_empresa_detalhes);
        TextView area_proficional = findViewById(R.id.tv_area_profissional_detalhes);
        TextView tipo_contrato = findViewById(R.id.tv_contrato_detalhes);
        TextView nivel_hierarquico = findViewById(R.id.tv_nivel_hierarquico__detalhes);
        TextView nivel_estudos = findViewById(R.id.tv_nivel_estudos_detalhes);
        TextView jornada = findViewById(R.id.tv_jornada_detalhes);
        TextView faixa_salarial = findViewById(R.id.tv_faixa_salarial_detalhes);
        TextView cep = findViewById(R.id.tv_cep_detalhes);
        TextView qtd = findViewById(R.id.tv_qtd_detalhes);
        botaoVerMais = findViewById(R.id.botao_ver_mais_detalhes);
        separador_detalhes = findViewById(R.id.separador_descricao_vaga_detalhes);

        Registro registro = (Registro) getIntent().getSerializableExtra("registro");


        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String vd = String.valueOf(registro.getData());
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

        candidato.setText(registro.getNomeUsuario());
        cargo.setText(registro.getCargo());
        localizacao.setText(registro.getLocalizacao());
        descricao.setText(registro.getDescricao());
        area_proficional.setText(registro.getAreaProfissional());
        tipo_contrato.setText(registro.getTipoContrato());
        nivel_hierarquico.setText(registro.getNivelHierarquico());
        nivel_estudos.setText(registro.getNivelEstudos());
        jornada.setText(registro.getJornada());
        faixa_salarial.setText(registro.getFaixaSalarial());
        cep.setText(registro.getCEP());
        qtd.setText(registro.getQtd());
        nome_empresa.setText(registro.getNomeEmpresa());

        botaoVerMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descricao.setVisibility(View.VISIBLE);
                botaoVerMais.setVisibility(View.GONE);
                separador_detalhes.setVisibility(View.VISIBLE);
            }
        });
    }
}
