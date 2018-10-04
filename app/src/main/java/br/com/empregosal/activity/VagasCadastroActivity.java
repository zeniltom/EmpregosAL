package br.com.empregosal.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import br.com.empregosal.R;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.helper.Preferencias;
import br.com.empregosal.model.Empresa;
import br.com.empregosal.model.Vaga;

public class VagasCadastroActivity extends AppCompatActivity {

    private Button bt_salvar_vaga;
    private Toolbar toolbar;
    private EditText cargo;
    private EditText area_profissional;
    private EditText nivel_hieraquico_vaga;
    private EditText tipo_contrato_vaga;
    private EditText nivel_estudos_vaga;
    private EditText jornava_vaga;
    private EditText descricao_vaga;
    private EditText qtd_vaga;
    private EditText faixa_salarial_vaga;
    private EditText localizacao_vaga;
    private String idUsuarioLogado;
    private DatabaseReference firebase;
    private Empresa empresa;
    private Vaga vaga;
    private Query consultaEmpress;
    private Empresa empresaPesquisda;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vagas_cadastro);

        cargo = findViewById(R.id.et_cargo);
        area_profissional = findViewById(R.id.et_area_profissional_vaga);
        nivel_hieraquico_vaga = findViewById(R.id.et_nivel_hieraquico_vaga);
        tipo_contrato_vaga = findViewById(R.id.et_tipo_contrato_vaga);
        nivel_estudos_vaga = findViewById(R.id.et_nivel_estudo_vaga);
        jornava_vaga = findViewById(R.id.et_jornada_vaga);
        descricao_vaga = findViewById(R.id.et_descricao_vaga);
        qtd_vaga = findViewById(R.id.et_qtd_vaga);
        faixa_salarial_vaga = findViewById(R.id.et_faixa_salarial_vaga);
        localizacao_vaga = findViewById(R.id.et_localizacao_vaga);
        bt_salvar_vaga = findViewById(R.id.bt_salvar_vaga);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Anunciar Vaga");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        // dados do usuário logado
        Preferencias preferencias = new Preferencias(VagasCadastroActivity.this);
        idUsuarioLogado = preferencias.getIdentificador();

        consultaEmpress = ConfiguracaoFirebase.getFirebase().child("empresas")
                .orderByChild("idEmpresa")
                .equalTo(idUsuarioLogado);

        consultaEmpress.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Empresa empresa = dados.getValue(Empresa.class);
                    empresaPesquisda = empresa;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        bt_salvar_vaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date data = new Date();
                String dataFormatada = formataData.format(data);

                Calendar c = Calendar.getInstance();
                Locale brasil = new Locale("pt", "BR");
                DateFormat dataBr = DateFormat.getDateInstance(DateFormat.FULL, brasil);

                vaga = new Vaga();
                vaga.setCargo(cargo.getText().toString());
                vaga.setAreaProfissional(area_profissional.getText().toString());
                vaga.setNivelHierarquico(nivel_hieraquico_vaga.getText().toString());
                vaga.setTipoContrato(tipo_contrato_vaga.getText().toString());
                vaga.setNivelEstudos(nivel_estudos_vaga.getText().toString());
                vaga.setJornada(jornava_vaga.getText().toString());
                vaga.setDescricao(descricao_vaga.getText().toString());
                vaga.setQtd(qtd_vaga.getText().toString());
                vaga.setFaixaSalarial(faixa_salarial_vaga.getText().toString());
                vaga.setLocalizacao(localizacao_vaga.getText().toString());
                vaga.setIdEmpresa(idUsuarioLogado);
                vaga.setNomeEmpresa(empresaPesquisda.getNome());
                vaga.setCEP(empresaPesquisda.getCEP());
                vaga.setData(dataFormatada.toString());
                vaga.setDataAnuncio(dataBr.format(c.getTime()));

                firebase = ConfiguracaoFirebase.getFirebase().child("vagas");
                DatabaseReference autoId = firebase.push();
                vaga.setIdVaga(autoId.getKey());

                if (!cargo.getText().toString().isEmpty()
                        && !cargo.getText().toString().isEmpty()
                        && !area_profissional.getText().toString().isEmpty()
                        && !nivel_hieraquico_vaga.getText().toString().isEmpty()
                        && !tipo_contrato_vaga.getText().toString().isEmpty()
                        && !nivel_estudos_vaga.getText().toString().isEmpty()
                        && !jornava_vaga.getText().toString().isEmpty()
                        && !descricao_vaga.getText().toString().isEmpty()
                        && !qtd_vaga.getText().toString().isEmpty()
                        && !faixa_salarial_vaga.getText().toString().isEmpty()
                        && !localizacao_vaga.getText().toString().isEmpty()) {

                    cadastrarVaga(idUsuarioLogado, vaga);
                } else {
                    Toast.makeText(VagasCadastroActivity.this, "Um ou mais campos estão vazios", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void cadastrarVaga(String idUsuarioLogado, Vaga vaga) {

        try {

            firebase = ConfiguracaoFirebase.getFirebase().child("vagas");
            firebase.child(vaga.getIdVaga())
                    .setValue(vaga);

            Toast.makeText(VagasCadastroActivity.this, "Sucesso ao cadastrar vaga", Toast.LENGTH_LONG).show();
            finish();

        } catch (Exception e) {
            Toast.makeText(VagasCadastroActivity.this, "Erro ao cadastrar vaga", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
