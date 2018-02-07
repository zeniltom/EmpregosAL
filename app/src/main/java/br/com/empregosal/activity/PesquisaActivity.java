package br.com.empregosal.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.empregosal.R;
import br.com.empregosal.adapter.VagasEmpregoAdapter;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.model.Vaga;

public class PesquisaActivity extends AppCompatActivity {

    private EditText textoPesquisa;
    private TextView vazio;
    private ListView listaPesquisa;
    private DatabaseReference databaseReference;
    private ArrayList<Vaga> vagaLista = new ArrayList<>();
    private ArrayAdapter vagaArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);

        textoPesquisa = findViewById(R.id.et_pesquisa);
        listaPesquisa = findViewById(R.id.lv_pesquisa);
        Button botao_pesquisa = findViewById(R.id.bt_pesquisa_vaga);
        vazio = findViewById(R.id.tv_pesquisa_vaga_vazio);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Pesquisa de Vagas");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        listaPesquisa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clicarItem(position);
            }
        });

        inicializarFirebase();

        botao_pesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String palavra = textoPesquisa.getText().toString().trim();
                pesquisarPalavra(palavra);
            }
        });
    }

    private void clicarItem(int position) {
        Vaga vaga = vagaLista.get(position);

        // enviando dados para conversa activity
        Intent intent = new Intent(getApplicationContext(), DetalhesVagaActivity.class);
        intent.putExtra("vaga_cargo", vaga.getCargo());
        intent.putExtra("vaga_localizacao", vaga.getLocalizacao());
        intent.putExtra("vaga_descricao", vaga.getDescricao());
        intent.putExtra("vaga_data", vaga.getData());
        intent.putExtra("vaga_tipo_contrato", vaga.getTipoContrato());
        intent.putExtra("vaga_nome_empresa", vaga.getNomeEmpresa());
        intent.putExtra("vaga_area_profissional", vaga.getAreaProfissional());
        intent.putExtra("vaga_tipo_contrato", vaga.getTipoContrato());
        intent.putExtra("vaga_nivel_hierarquico", vaga.getNivelHierarquico());
        intent.putExtra("vaga_nivel_estudos", vaga.getNivelEstudos());
        intent.putExtra("vaga_jornada", vaga.getJornada());
        intent.putExtra("vaga_faixa_salarial", vaga.getFaixaSalarial());
        intent.putExtra("vaga_cep", vaga.getCEP());
        intent.putExtra("vaga_qtd", vaga.getQtd());
        intent.putExtra("vaga", vaga);

        startActivity(intent);
    }

    private void pesquisarPalavra(String palavra) {
        consultarVagaCargo(palavra);

        consultarVagaLocalizade(palavra);
    }

    private void consultarVagaCargo(String palavra) {
        Query query; // 5º passo - cria o objeto que ira receber os dados da pesquisa
        if (palavra.equals("")) {

            query = databaseReference.child("vagas");
        } else {

            query = databaseReference.child("vagas")
                    .orderByChild("cargo").startAt(palavra).endAt(palavra + "\uf8ff");
        }

        // impo a list<Pessoa>
        vagaLista.clear();

        // implemento o metodo na query
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {// separo cada objeto contido na dataSnapshot
                    Vaga v = objSnapshot.getValue(Vaga.class);// salvo cada um deles na variavel pessoa;
                    vagaLista.add(v);// adiciono na list<Pessoa>
                }

                // inicializo o arrayAdapter passando o contexto da aplicação
                // a tipo de layout da lista, e a list<Pessoa> contendo os objetos
                vagaArrayAdapter = new VagasEmpregoAdapter(getApplicationContext(), vagaLista);
                // incluo na ListView o arrayAdapter
                listaPesquisa.setAdapter(vagaArrayAdapter);
                vazio.setText("Sem resultados");
                listaPesquisa.setEmptyView(vazio);
                vagaArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void consultarVagaLocalizade(String palavra) {
        Query queryLocalidade;
        if (palavra.equals("")) {
            queryLocalidade = databaseReference.child("vagas");
        } else {
            queryLocalidade = databaseReference.child("vagas")
                    .orderByChild("localizacao").startAt(palavra).endAt(palavra + "\uf8ff");
        }

        vagaLista.clear();

        queryLocalidade.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    Vaga v = objSnapshot.getValue(Vaga.class);
                    vagaLista.add(v);
                }

                vagaArrayAdapter = new VagasEmpregoAdapter(getApplicationContext(), vagaLista);
                listaPesquisa.setAdapter(vagaArrayAdapter);
                vazio.setText("Sem resultados");
                listaPesquisa.setEmptyView(vazio);
                vagaArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        pesquisarPalavra("");
    }

    private void inicializarFirebase() {
        FirebaseDatabase firebaseDatabase = ConfiguracaoFirebase.getFirebase().getDatabase();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pesquisa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.item_filtrar_data:
                filtrarPorData();
                return true;
            case R.id.item_filtrar_localizacao:
                filtrarPorLocalizacao();
                return true;
            case R.id.item_filtrar_area:
                filtrarPorArea();
                return true;
            default:
                return super.onOptionsItemSelected(item); //Padrão para Android
        }
    }

    private void filtrarPorArea() {
        Query query;
        query = databaseReference.child("vagas")
                .orderByChild("areaProfissional");

        vagaLista.clear();

        Toast.makeText(getApplicationContext(), "Filtrado por Área", Toast.LENGTH_SHORT).show();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    Vaga v = objSnapshot.getValue(Vaga.class);
                    vagaLista.add(v);
                }

                vagaArrayAdapter = new VagasEmpregoAdapter(getApplicationContext(), vagaLista);


                listaPesquisa.setAdapter(vagaArrayAdapter);
                vazio.setText("Sem resultados");
                listaPesquisa.setEmptyView(vazio);
                vagaArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void filtrarPorLocalizacao() {
        Query query;
        query = databaseReference.child("vagas").orderByChild("localizacao");

        vagaLista.clear();

        Toast.makeText(getApplicationContext(), "Filtrado por Data", Toast.LENGTH_SHORT).show();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    Vaga v = objSnapshot.getValue(Vaga.class);
                    vagaLista.add(v);
                }

                vagaArrayAdapter = new VagasEmpregoAdapter(getApplicationContext(), vagaLista);


                listaPesquisa.setAdapter(vagaArrayAdapter);
                vazio.setText("Sem resultados");
                listaPesquisa.setEmptyView(vazio);
                vagaArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void filtrarPorData() {
        Query query;
        query = databaseReference.child("vagas").orderByChild("data").limitToLast(10);

        vagaLista.clear();

        Toast.makeText(getApplicationContext(), "Filtrado por Data", Toast.LENGTH_SHORT).show();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    Vaga v = objSnapshot.getValue(Vaga.class);
                    vagaLista.add(v);
                }

                vagaArrayAdapter = new VagasEmpregoAdapter(getApplicationContext(), vagaLista);


                listaPesquisa.setAdapter(vagaArrayAdapter);
                vazio.setText("Sem resultados");
                listaPesquisa.setEmptyView(vazio);
                vagaArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
