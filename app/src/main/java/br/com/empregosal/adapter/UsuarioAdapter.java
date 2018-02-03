package br.com.empregosal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = null;

        if (usuarios != null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_usuarios, parent, false);

            TextView usuarioNome = view.findViewById(R.id.tv_nome_usuario);
            TextView usuarioCidade = view.findViewById(R.id.tv_cidade_usuario);
            TextView usuarioUF = view.findViewById(R.id.tv_uf_usuario);
            ImageView imageView = view.findViewById(R.id.imgUser);
            final TextView usuarioXP = view.findViewById(R.id.tv_xp_usuario);

            final Usuario usuario = usuarios.get(position);
            usuarioNome.setText(usuario.getNome());
            usuarioCidade.setText(usuario.getCidade());
            usuarioUF.setText(usuario.getUf());

            StorageReference referenciaStorage = FirebaseStorage.getInstance().getReference();
            StorageReference stream = referenciaStorage.
                    child("usuarios_imagens").
                    child(usuario.getIdUsuario())
                    .child("perfil.png");

            //disabling use of both the disk and memory caches
            Glide.with(getContext().getApplicationContext())
                    .using(new FirebaseImageLoader())
                    .load(stream)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageView);

            Query experiencia = ConfiguracaoFirebase.getFirebase()
                    .child("experiencias")
                    .child(usuario.getIdUsuario()).orderByChild("dataInicio").limitToFirst(1);

            //Mostra a experiência do usuário na tela de "Candidatos" da Empresa
            experiencia.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
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
