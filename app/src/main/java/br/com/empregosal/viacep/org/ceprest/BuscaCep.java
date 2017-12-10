package br.com.empregosal.viacep.org.ceprest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.empregosal.R;
import br.com.empregosal.viacep.org.ceprest.model.ViaCEP;

public class BuscaCep extends AppCompatActivity {
    private EditText editTextCEP;
    private Button buttonPesquisar;
    private TextView textViewLogradouro;
    private TextView textViewComplemento;
    private TextView textViewBairro;
    private TextView textViewLocalidade;
    private TextView textViewUF;
    private TextView textViewUnidade;
    private TextView textViewIBGE;
    private TextView textViewGIA;
    private ViaCEP viacep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_cep);

        editTextCEP = (EditText) findViewById(R.id.editTextCEP);
        buttonPesquisar = (Button) findViewById(R.id.buttonPesquisar);
        textViewLogradouro = (TextView) findViewById(R.id.textViewLogradouro);
        textViewComplemento = (TextView) findViewById(R.id.textViewComplemento);
        textViewBairro = (TextView) findViewById(R.id.textViewBairro);
        textViewLocalidade = (TextView) findViewById(R.id.textViewLocalidade);
        textViewUF = (TextView) findViewById(R.id.textViewUF);
        textViewUnidade = (TextView) findViewById(R.id.textViewUnidade);
        textViewIBGE = (TextView) findViewById(R.id.textViewIBGE);
        textViewGIA = (TextView) findViewById(R.id.textViewGIA);

        buttonPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String cep = editTextCEP.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        viacep = new ViaCEP();
                        viacep.buscar(cep);

                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }
        });
    }

    Handler handler = new Handler() {
        public void handleMessage(Message m) {
            switch (m.what) {
                case 0:
                    textViewLogradouro.setText(viacep.getLogradouro());
                    textViewComplemento.setText(viacep.getComplemento());
                    textViewBairro.setText(viacep.getBairro());
                    textViewLocalidade.setText(viacep.getLocalidade());
                    textViewUF.setText(viacep.getUf());
                    textViewUnidade.setText(viacep.getUnidade());
                    textViewIBGE.setText(viacep.getIbge());
                    textViewGIA.setText(viacep.getGia());
                    break;
            }
        }
    };
}