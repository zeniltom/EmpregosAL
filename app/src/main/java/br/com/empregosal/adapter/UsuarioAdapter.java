package br.com.empregosal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.empregosal.R;
import br.com.empregosal.config.ConfiguracaoFirebase;
import br.com.empregosal.model.Experiencia;
import br.com.empregosal.model.Usuario;

public class UsuarioAdapter extends ArrayAdapter<Usuario> {

    private ArrayList<Usuario> usuarios;
    private Context context;

    public UsuarioAdapter(Context c, ArrayList<Usuario> objects) {
        super(c, 0, objects);
        this.usuarios = objects;
        this.context = c;
    }

    public int Qtd() {
        return usuarios.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = null;

        if (usuarios != null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.lista_usuarios, parent, false);

            // recupera elemento para exibição
            TextView usuarioNome = view.findViewById(R.id.tv_nome_usuario);
            TextView usuarioCidade = view.findViewById(R.id.tv_cidade_usuario);
            TextView usuarioUF = view.findViewById(R.id.tv_uf_usuario);
            final TextView usuarioXP = view.findViewById(R.id.tv_xp_usuario);

            final Usuario usuario = usuarios.get(position);
            usuarioNome.setText(usuario.getNome());
            usuarioCidade.setText(usuario.getCidade());
            usuarioUF.setText(usuario.getUf());

            Query experiencia = ConfiguracaoFirebase.getFirebase()
                    .child("experiencias")
                    .child(usuario.getIdUsuario()).orderByChild("dataInicio").limitToFirst(1);

            experiencia.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dados : dataSnapshot.getChildren()){
                        Experiencia xp = dados.getValue(Experiencia.class);
                        usuarioXP.setText(xp.getCargo());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return view;
    }
}
