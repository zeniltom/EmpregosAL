package br.com.empregosal.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.empregosal.R;
import br.com.empregosal.adapter.ExperienciasRecyclerAdapter;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.model.Candidatura;
import br.com.empregosal.model.Experiencia;
import br.com.empregosal.model.Usuario;
import br.com.empregosal.model.Vaga;

public class DetalhesUsuarioActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView nome;
    private TextView cidade;
    private TextView uf;
    private ArrayList<Experiencia> experiencias;
    private Candidatura candidaturaP;
    private Query pesquisa;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerExperiencias;
    private RecyclerView reciclerView;
    private RecyclerView.Adapter adapter;

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerExperiencias);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerExperiencias);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_usuario);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detalhes do Candidato");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        nome = findViewById(R.id.tv_detalhes_nome_usuario);
        cidade = findViewById(R.id.tv_detalhes_cidade_usuario);
        uf = findViewById(R.id.tv_detalhes_uf_usuario);
        reciclerView = findViewById(R.id.recycler_view);

        experiencias = new ArrayList<>();
        
        Usuario usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        nome.setText(usuario.getNome());
        cidade.setText(usuario.getCidade());
        uf.setText(usuario.getUf());

        adapter = new ExperienciasRecyclerAdapter(experiencias);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        reciclerView.setLayoutManager(mLayoutManager);
        reciclerView.setItemAnimator(new DefaultItemAnimator());
        reciclerView.setAdapter(adapter);

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("experiencias")
                .child(usuario.getIdUsuario());

        //Listener para recuperar experiencias
        valueEventListenerExperiencias = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Limpar lista
                experiencias.clear();

                //Listar experiencias
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Experiencia experiencia = dados.getValue(Experiencia.class);
                    experiencias.add(experiencia);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

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

                        Toast.makeText(DetalhesUsuarioActivity.this, "Sucesso ao candidatar na vaga", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(DetalhesUsuarioActivity.this, "Erro ao candicatar na vaga", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(DetalhesUsuarioActivity.this, "Já cadastrado na vaga " + candidaturaP.getNomeVaga(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
