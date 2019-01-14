package org.morfe.ikasfit19.Ventanas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.firestore.FirebaseFirestore;
import org.morfe.ikasfit19.BaseDatos.BaseDatos;
import org.morfe.ikasfit19.R;


public class Principal extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        BaseDatos bd = new BaseDatos();
        bd.guardar();

    }
}
