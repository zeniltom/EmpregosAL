package br.com.empregosal;

import android.app.Application;

public class EmpregosAL extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
