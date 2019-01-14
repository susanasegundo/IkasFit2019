package org.morfe.ikasfit19;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;

public class Principal extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    }
}
