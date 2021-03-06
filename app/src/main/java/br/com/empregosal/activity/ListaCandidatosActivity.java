package br.com.empregosal.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.empregosal.R;
import br.com.empregosal.adapter.UsuarioAdapter;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.model.Usuario;
import br.com.empregosal.model.Vaga;

public class ListaCandidatosActivity extends AppCompatActivity {

    private TextView vazio;
    private ListView listaPesquisa;
    private DatabaseReference databaseReference;
    private ArrayList<Usuario> usuarioLista = new ArrayList<>();
    private ArrayAdapter usuarioArrayAdapter;
    private Vaga vaga;
    private String candidaturaId;
    private TextView resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_candidatos);

        listaPesquisa = findViewById(R.id.lv_pesquisa);
        vazio = findViewById(R.id.tv_pesquisa_vaga_vazio);
        resultado = findViewById(R.id.tv_resultado_vaga);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Lista de Candidatos");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        vaga = (Vaga) getIntent().getSerializableExtra("vaga");

        Log.i("#VAGA -> ", vaga.getCargo());
        Log.i("#VAGA -> ", vaga.getNomeEmpresa());

        inicializarFirebase();

        listaPesquisa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selecionarCandidato(position, vaga);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        listarUsuarios(vaga);
    }

    private void selecionarCandidato(int position, Vaga vaga) {
        Usuario usuario = usuarioLista.get(position);

        Intent intent = new Intent(getApplicationContext(), DetalhesCandidatoActivity.class);
        intent.putExtra("usuario_idUsuario", usuario.getIdUsuario());
        intent.putExtra("usuario", usuario);
        intent.putExtra("vaga", vaga);
        intent.putExtra("candidaturaID", candidaturaId);

        startActivity(intent);
    }

    private void listarUsuarios(final Vaga vaga) {
        Query query;
        query = databaseReference.child("candidaturas").
                orderByChild("idVaga").equalTo(vaga.getIdVaga());

        //A PARTIR DESTA CANDIDATURA, LISTA OS USUÁRIOS QUE POSSUEM ESSA CANDIDATURA

        usuarioLista.clear();
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("#CANDIDATURA ID - > ", dataSnapshot.getKey());
                candidaturaId = dataSnapshot.getKey();

                Query queryUsuario = databaseReference.child("usuarios")
                        .orderByChild("idUsuario")
                        .equalTo((String) dataSnapshot.child("idUsuario").getValue());

                queryUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                            Usuario u = objSnapshot.getValue(Usuario.class);
                            usuarioLista.add(u);
                        }
                        usuarioArrayAdapter = new UsuarioAdapter(getApplicationContext(), usuarioLista);
                        resultado.setText("Resultado(s) da vaga: " + usuarioArrayAdapter.getCount() + "\n" + vaga.getCargo());

                        listaPesquisa.setAdapter(usuarioArrayAdapter);
                        listaPesquisa.setEmptyView(vazio);
                        usuarioArrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void inicializarFirebase() {
        FirebaseDatabase firebaseDatabase = ConfiguracaoFirebase.getFirebase().getDatabase();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_candidato, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_filtrar_nome:
                filtrarPorNome();
                return true;
            default:
                return super.onOptionsItemSelected(item); //Padrão para Android
        }
    }

    private void filtrarPorNome() {
        Query query;
        query = databaseReference.child("candidaturas").
                orderByChild("idVaga").equalTo(vaga.getIdVaga());

        usuarioLista.clear();
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Query queryUsuario = databaseReference.child("usuarios")
                        .orderByChild("nome")
                        .equalTo((String) dataSnapshot.child("nomeUsuario").getValue());

                queryUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                            Usuario u = objSnapshot.getValue(Usuario.class);
                            usuarioLista.add(u);
                        }

                        usuarioArrayAdapter = new UsuarioAdapter(getApplicationContext(), usuarioLista);
                        listaPesquisa.setEmptyView(vazio);
                        listaPesquisa.setAdapter(usuarioArrayAdapter);
                        usuarioArrayAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "Filtrado por nome", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
