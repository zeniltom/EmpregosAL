package br.com.empregosal.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import br.com.empregosal.R;
import br.com.empregosal.adapter.ExperienciasRecyclerAdapter;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.model.Candidatura;
import br.com.empregosal.model.Experiencia;
import br.com.empregosal.model.Usuario;
import br.com.empregosal.model.Vaga;

public class DetalhesCandidatoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView nome;
    private TextView cidade;
    private TextView uf;
    private Button botaoContratar;
    private ImageView imageView;
    private ArrayList<Experiencia> experiencias;
    private Candidatura candidaturaP;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerExperiencias;
    private RecyclerView reciclerView;
    private RecyclerView.Adapter adapter;
    private AlertDialog.Builder dialog;

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
        setContentView(R.layout.activity_detalhe_candidato);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detalhes do Candidato");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        nome = findViewById(R.id.tv_detalhes_nome_usuario);
        cidade = findViewById(R.id.tv_detalhes_cidade_usuario);
        uf = findViewById(R.id.tv_detalhes_uf_usuario);
        botaoContratar = findViewById(R.id.botao_contratar);
        imageView = findViewById(R.id.imgUser);
        reciclerView = findViewById(R.id.recycler_view);

        experiencias = new ArrayList<>();

        Usuario usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        final Vaga vaga = (Vaga) getIntent().getSerializableExtra("vaga");
        final String candidaturaId = getIntent().getStringExtra("candidaturaID");

        nome.setText(usuario.getNome());
        cidade.setText(usuario.getCidade());
        uf.setText(usuario.getUf());

        carregarFotoPerfil(usuario);

        adapter = new ExperienciasRecyclerAdapter(experiencias);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        reciclerView.setLayoutManager(mLayoutManager);
        reciclerView.setItemAnimator(new DefaultItemAnimator());
        reciclerView.setAdapter(adapter);

        botaoContratar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contratar(vaga);
            }
        });

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

    private void contratar(final Vaga vaga) {
        dialog = new AlertDialog.Builder(DetalhesCandidatoActivity.this, R.style.dialogEmpregosAL)
                .setTitle("Confirmação")
                .setMessage("Deseja contratar este candidato para a vaga de " + vaga.getCargo() + "?");

        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Usar uma Thread a parte para realizar este processamento
                //Contrata o usuário para a vaga, e excluir os referentes
                // a vaga nos nós de 'vagas' e 'candidaturas'

                DatabaseReference queryCandidatura = ConfiguracaoFirebase.getFirebase();
                DatabaseReference queryVaga = ConfiguracaoFirebase.getFirebase();

                queryCandidatura.child("candidaturas").orderByChild("idVaga")//Deleta as  candidaturas
                        .equalTo(vaga.getIdVaga()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dados : dataSnapshot.getChildren()) {
                            dados.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                queryVaga.child("vagas").orderByChild("idVaga")//Deleta a vaga específica
                        .equalTo(vaga.getIdVaga()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dados : dataSnapshot.getChildren()) {
                            dados.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Toast.makeText(getApplicationContext(), "Contratado", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.create();
        dialog.show();
    }

    private void carregarFotoPerfil(Usuario usuario) {
        StorageReference referenciaStorage = FirebaseStorage.getInstance().getReference();
        StorageReference stream = referenciaStorage.
                child("usuarios_imagens").
                child(usuario.getIdUsuario())
                .child("perfil.png");

        //disabling use of both the disk and memory caches
        Glide.with(getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(stream)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }
}
