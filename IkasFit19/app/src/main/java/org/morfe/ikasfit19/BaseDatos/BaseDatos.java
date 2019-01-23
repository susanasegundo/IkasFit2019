package org.morfe.ikasfit19.BaseDatos;


import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class BaseDatos  {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public BaseDatos() {

    }
    public boolean guardarUsuario( FirebaseAuth mAuth){

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("fecha", "21/01/2020");
        user.put("pasosTotales", 50000);


        // Add a new document with a generated ID
        db.collection("usuarios").document(mAuth.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

        return true;
    }
}



