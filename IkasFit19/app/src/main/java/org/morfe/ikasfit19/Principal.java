package org.morfe.ikasfit19;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Principal extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Map<String, Object> user = new HashMap<>();
        user.put("nombre", "David");
        user.put("edad", "33");
        user.put("apellidos", "Nuño");

// Add a new document with a generated ID
        db.collection("usuarios")
                .add(user);

    }
}
