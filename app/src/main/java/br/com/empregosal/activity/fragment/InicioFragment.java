package br.com.empregosal.activity.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.empregosal.R;
import br.com.empregosal.activity.DetalhesUsuarioActivity;
import br.com.empregosal.adapter.UsuarioAdapter;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.helper.Preferencias;
import br.com.empregosal.model.Usuario;
import dmax.dialog.SpotsDialog;

public class InicioFragment extends Fragment {
    private ListView listView;
    private TextView vazia;
    private ArrayAdapter adapter;
    private ArrayList<Usuario> usuarios;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerUsuarios;
    private Query query;

    public InicioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        query.addListenerForSingleValueEvent(valueEventListenerUsuarios);
    }

    @Override
    public void onStop() {
        super.onStop();
        query.removeEventListener(valueEventListenerUsuarios);
    }

    @Override
    public void onResume() {
        super.onResume();
        conexao();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        usuarios = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        conexao();

        listView = (ListView) view.findViewById(R.id.lv_usuarios);
        vazia = (TextView) view.findViewById(R.id.vazia_usuarios);
        listView.setEmptyView(vazia);
        adapter = new UsuarioAdapter(getActivity(), usuarios);
        listView.setAdapter(adapter);

        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();

        DatabaseReference oneRef = ConfiguracaoFirebase.getFirebase();

        query = ConfiguracaoFirebase.getFirebase().child("usuarios")
                .orderByChild("nome");

        carregarCandidatos();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Usuario usuario = usuarios.get(position);

                Intent intent = new Intent(getContext(), DetalhesUsuarioActivity.class);
                intent.putExtra("usuario_cep", usuario.getCEP());
                intent.putExtra("usuario_cidade", usuario.getCidade());
                intent.putExtra("usuario_complemento", usuario.getComplemento());
                intent.putExtra("usuario_data_nascimento", usuario.getData_nascimento());
                intent.putExtra("usuario_email", usuario.getEmail());
                intent.putExtra("usuario_endereco", usuario.getEndereco());
                intent.putExtra("usuario_estado_civ", usuario.getEstado_civ());
                intent.putExtra("usuario_idUsuario", usuario.getIdUsuario());
                intent.putExtra("usuario_nacionalidade", usuario.getNacionalidade());
                intent.putExtra("usuario_nome", usuario.getNome());
                intent.putExtra("usuario_numero", usuario.getNumero());
                intent.putExtra("usuario_sexo", usuario.getSexo());
                intent.putExtra("usuario_uf", usuario.getUf());
                intent.putExtra("usuario", usuario); //Passa a classe completa para activity

                startActivity(intent);

            }
        });

        return view;
    }

    private void carregarCandidatos() {
        final SpotsDialog dialog = new SpotsDialog(getContext(), "Carregando...", R.style.dialogEmpregosAL);
        dialog.setCancelable(false);
        dialog.show();

        valueEventListenerUsuarios = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuarios.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Usuario usuario = dados.getValue(Usuario.class);
                    usuarios.add(usuario);
                }
                dialog.dismiss();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
    }

    private void conexao() {
        ConnectivityManager conexao = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conexao.getNetworkInfo(0).isConnected()) {
            Log.i("Conexão 3G", conexao.getNetworkInfo(0).toString());
        } else if (conexao.getNetworkInfo(1).isConnected()) {
            Log.i("Conexão WIFi", conexao.getNetworkInfo(1).toString());
        } else {
            Toast.makeText(getContext(), "Desconectado", Toast.LENGTH_SHORT).show();
        }
    }

}
