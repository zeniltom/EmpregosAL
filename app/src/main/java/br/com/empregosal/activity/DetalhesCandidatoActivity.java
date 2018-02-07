package br.com.empregosal.activity;

import android.content.DialogInterface;
import android.content.Intent;
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
import br.com.empregosal.adapter.FormacaoRecyclerAdapter;
import br.com.empregosal.adapter.IdiomaRecyclerAdapter;
import br.com.empregosal.adapter.ObjetivoRecyclerAdapter;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.model.Experiencia;
import br.com.empregosal.model.Formacao;
import br.com.empregosal.model.Idioma;
import br.com.empregosal.model.Objetivo;
import br.com.empregosal.model.Usuario;
import br.com.empregosal.model.Vaga;

public class DetalhesCandidatoActivity extends AppCompatActivity {

    private ImageView imageView;
    private ArrayList<Experiencia> experiencias;
    private ArrayList<Formacao> formacoes;
    private ArrayList<Objetivo> objetivos;
    private ArrayList<Idioma> idiomas;
    private DatabaseReference firebaseExp;
    private DatabaseReference firebaseFor;
    private DatabaseReference firebaseObj;
    private DatabaseReference firebaseIdi;
    private ValueEventListener valueEventListenerExperiencias;
    private ValueEventListener valueEventListenerFormacoes;
    private ValueEventListener valueEventListenerObjetivo;
    private ValueEventListener valueEventListenerIdioma;
    private RecyclerView.Adapter adapterExperiencia;
    private RecyclerView.Adapter adapterFormacao;
    private RecyclerView.Adapter adapterObjetivo;
    private RecyclerView.Adapter adapterIdioma;

    @Override
    public void onStart() {
        super.onStart();
        firebaseExp.addValueEventListener(valueEventListenerExperiencias);
        firebaseFor.addValueEventListener(valueEventListenerFormacoes);
        firebaseIdi.addValueEventListener(valueEventListenerIdioma);
        firebaseObj.addValueEventListener(valueEventListenerObjetivo);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseExp.removeEventListener(valueEventListenerExperiencias);
        firebaseFor.removeEventListener(valueEventListenerFormacoes);
        firebaseIdi.removeEventListener(valueEventListenerIdioma);
        firebaseObj.removeEventListener(valueEventListenerObjetivo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_candidato);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Detalhes do Candidato");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        TextView nome = findViewById(R.id.tv_detalhes_nome_usuario);
        TextView cidade = findViewById(R.id.tv_detalhes_cidade_usuario);
        TextView uf = findViewById(R.id.tv_detalhes_uf_usuario);
        Button botaoContratar = findViewById(R.id.botao_contratar);
        imageView = findViewById(R.id.imgUser);
        RecyclerView reciclerViewEx = findViewById(R.id.rc_experiencia);
        RecyclerView reciclerVieFo = findViewById(R.id.rc_formacao);
        RecyclerView reciclerVieOj = findViewById(R.id.rc_objetivo);
        RecyclerView reciclerVieId = findViewById(R.id.rc_idioma);

        experiencias = new ArrayList<>();
        formacoes = new ArrayList<>();
        idiomas = new ArrayList<>();
        objetivos = new ArrayList<>();

        Usuario usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        final Vaga vaga = (Vaga) getIntent().getSerializableExtra("vaga");

        nome.setText(usuario.getNome());
        cidade.setText(usuario.getCidade());
        uf.setText(usuario.getUf());

        carregarFotoPerfil(usuario);

        adapterExperiencia = new ExperienciasRecyclerAdapter(experiencias);
        adapterFormacao = new FormacaoRecyclerAdapter(formacoes);
        adapterObjetivo = new ObjetivoRecyclerAdapter(objetivos);
        adapterIdioma= new IdiomaRecyclerAdapter(idiomas);
        RecyclerView.LayoutManager layoutManagerExp = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager layoutManagerFor = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager layoutManagerIdi = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager layoutManagerObj = new LinearLayoutManager(getApplicationContext());
        reciclerViewEx.setLayoutManager(layoutManagerExp);
        reciclerViewEx.setItemAnimator(new DefaultItemAnimator());
        reciclerViewEx.setAdapter(adapterExperiencia);
        reciclerVieFo.setLayoutManager(layoutManagerFor);
        reciclerVieFo.setItemAnimator(new DefaultItemAnimator());
        reciclerVieFo.setAdapter(adapterFormacao);
        reciclerVieId.setLayoutManager(layoutManagerIdi);
        reciclerVieId.setItemAnimator(new DefaultItemAnimator());
        reciclerVieId.setAdapter(adapterIdioma);
        reciclerVieOj.setLayoutManager(layoutManagerObj);
        reciclerVieOj.setItemAnimator(new DefaultItemAnimator());
        reciclerVieOj.setAdapter(adapterObjetivo);

        botaoContratar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contratar(vaga);
            }
        });

        firebaseExp = ConfiguracaoFirebase.getFirebase()
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
                adapterExperiencia.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        firebaseFor = ConfiguracaoFirebase.getFirebase()
                .child("formacao")
                .child(usuario.getIdUsuario());

        //Listener para recuperar experiencias
        valueEventListenerFormacoes = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Limpar lista
                formacoes.clear();

                //Listar experiencias
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Formacao formacao = dados.getValue(Formacao.class);
                    formacoes.add(formacao);
                }
                adapterFormacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        firebaseIdi = ConfiguracaoFirebase.getFirebase()
                .child("idiomas")
                .child(usuario.getIdUsuario());

        //Listener para recuperar experiencias
        valueEventListenerIdioma = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Limpar lista
                idiomas.clear();

                //Listar experiencias
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Idioma idioma = dados.getValue(Idioma.class);
                    idiomas.add(idioma);
                }
                adapterIdioma.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        firebaseObj = ConfiguracaoFirebase.getFirebase()
                .child("objetivo")
                .child(usuario.getIdUsuario());

        //Listener para recuperar experiencias
        valueEventListenerObjetivo = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Limpar lista
                objetivos.clear();

                //Listar experiencias
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Objetivo objetivo = dados.getValue(Objetivo.class);
                    objetivos.add(objetivo);
                }
                adapterObjetivo.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
    }

    private void contratar(final Vaga vaga) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(DetalhesCandidatoActivity.this, R.style.dialogEmpregosAL)
                .setTitle("Confirmação")
                .setMessage("Deseja contratar este candidato para a vaga de "
                        + vaga.getCargo() + "?");

        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Usar uma Thread a parte para realizar este processamento
                //Contrata o usuário para a vaga, e excluir os referentes
                // a vaga nos nós de 'vagas' e 'candidaturas'

                DatabaseReference queryCandidatura = ConfiguracaoFirebase.getFirebase();
                DatabaseReference queryVaga = ConfiguracaoFirebase.getFirebase();

                queryCandidatura.child("candidaturas").orderByChild("idVaga")//Deleta as candidaturas
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
                        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                    }
                });

                Toast.makeText(getApplicationContext(), "Contratado", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(DetalhesCandidatoActivity.this, MainEmpresaActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //Futuramente, não apagar a candidatura nem a vaga. Criar um novo nó na estrutura
                //para armazenar o registro da vaga e candidatura, contendo a empresa, o usuário

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
