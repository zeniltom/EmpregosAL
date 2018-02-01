package br.com.empregosal.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.empregosal.R;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.helper.Preferencias;
import br.com.empregosal.model.Candidatura;
import br.com.empregosal.model.Vaga;

public class DetalhesVagaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView cartaoSobre;
    private CardView cartaoDescricao;
    private CardView cartaoDetalhes;
    private TextView cargo;
    private TextView descricao;
    private TextView localizacao;
    private TextView data;
    private TextView area_proficional;
    private TextView tipo_contrato;
    private TextView nivel_hierarquico;
    private TextView nivel_estudos;
    private TextView jornada;
    private TextView faixa_salarial;
    private TextView cep;
    private TextView qtd;
    private TextView nome_empresa;
    private Button botaoCandidatar;
    private TextView botaoVerMais;
    private View separador_detalhes;
    private Candidatura candidaturaP;
    private Query pesquisa;
    private DatabaseReference firebase;

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
        localizacao = findViewById(R.id.tv_localizacao_detalhes);
        descricao = findViewById(R.id.tv_descricao_detalhes);
        data = findViewById(R.id.tv_data_anuncio_detalhes);
        nome_empresa = findViewById(R.id.tv_nome_empresa_detalhes);
        area_proficional = findViewById(R.id.tv_area_profissional_detalhes);
        tipo_contrato = findViewById(R.id.tv_contrato_detalhes);
        nivel_hierarquico = findViewById(R.id.tv_nivel_hierarquico__detalhes);
        nivel_estudos = findViewById(R.id.tv_nivel_estudos_detalhes);
        jornada = findViewById(R.id.tv_jornada_detalhes);
        faixa_salarial = findViewById(R.id.tv_faixa_salarial_detalhes);
        cep = findViewById(R.id.tv_cep_detalhes);
        qtd = findViewById(R.id.tv_qtd_detalhes);
        botaoCandidatar = findViewById(R.id.botao_detalhes_candidatar);
        botaoVerMais = findViewById(R.id.botao_ver_mais_detalhes);
        separador_detalhes = findViewById(R.id.separador_descricao_vaga_detalhes);

        Animation animation = new TranslateAnimation(1000, 0, 0, 0);
        animation.setDuration(500);
        cartaoSobre.startAnimation(animation);
        cartaoDescricao.startAnimation(animation);
        cartaoDetalhes.startAnimation(animation);

        final Bundle dadosPassados = getIntent().getExtras();

        if (dadosPassados != null) {

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String vd = String.valueOf(dadosPassados.get("vaga_data"));
            Date dataHoje = new Date();
            Date dataAnuncio = null;

            try {
                dataAnuncio = format.parse(vd.toString());
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
            nome_empresa.setText(dadosPassados.getString("vaga_nome_empresa"));
        }

        botaoCandidatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferencias preferencias = new Preferencias(DetalhesVagaActivity.this);
                String idUsuarioLogado = preferencias.getIdentificador();
                Vaga vaga = (Vaga) getIntent().getSerializableExtra("vaga");
                candidatarVaga(vaga, idUsuarioLogado);
            }
        });

        botaoVerMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descricao.setVisibility(View.VISIBLE);
                botaoVerMais.setVisibility(View.GONE);
                separador_detalhes.setVisibility(View.VISIBLE);

            }
        });
    }

    private void candidatarVaga(Vaga vaga, String idUsuarioLogado) {
        Vaga vagaEscolhida = vaga;
        candidaturaP = null;

        //Validar se já existe cadastro na vaga

        String idEmpresa = vagaEscolhida.getIdEmpresa();
        final Candidatura candidatura = new Candidatura();
        candidatura.setIdUsuario(idUsuarioLogado);
        candidatura.setIdEmpresa(idEmpresa);
        candidatura.setNomeUsuario(idUsuarioLogado.toString());
        candidatura.setNomeEmpresa(vagaEscolhida.getNomeEmpresa());
        candidatura.setNomeVaga(vagaEscolhida.getCargo());
        candidatura.setIdVaga(vagaEscolhida.getIdVaga());

        pesquisa = ConfiguracaoFirebase.getFirebase().child("candidaturas")
                .orderByChild("idVaga")
                .equalTo(vaga.getIdVaga());

        pesquisa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    candidaturaP = dados.getValue(Candidatura.class);

                }

                if (candidaturaP == null) {

                    try {

                        firebase = ConfiguracaoFirebase.getFirebase().child("candidaturas");
                        firebase.push()
                                .setValue(candidatura);

                        Toast.makeText(DetalhesVagaActivity.this, "Sucesso ao candidatar na vaga", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(DetalhesVagaActivity.this, "Erro ao candicatar na vaga", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(DetalhesVagaActivity.this, "Já cadastrado na vaga " + candidaturaP.getNomeVaga(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
