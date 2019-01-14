package org.morfe.ikasfit19.BaseDatos;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BaseDatos {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public BaseDatos() {
    }

   public void guardar(){
       Map<String, Object> user = new HashMap<>();
       user.put("nombre", "BEA");
       user.put("edad", "33");
       user.put("apellidos", "Nuño");

// Add a new document with a generated ID
       db.collection("usuarios")
               .add(user);

   }
}
