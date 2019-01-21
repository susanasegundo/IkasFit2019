package org.morfe.ikasfit19.BaseDatos;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class BaseDatos  {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public BaseDatos() {
    }


    public boolean guardarUsuario(){

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("fecha", "15/01/2020");
        user.put("pasosTotales", 3000);
        user.put("posicionRanking", 2);

        // Add a new document with a generated ID
        db.collection("usuarios")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        return true;
    }

    public boolean bucarUsuario(){
        return true;
    }
}
