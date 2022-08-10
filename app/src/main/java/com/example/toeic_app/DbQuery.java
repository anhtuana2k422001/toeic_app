package com.example.toeic_app;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;


import java.util.Map;
import java.util.Objects;

public class DbQuery {
    public static FirebaseFirestore g_firestore;


    // Lư thông tin userData vào FireBase
    public static void createUserData(String email,String name, MyCompleteListener myCompleteListener){
        Map<String, Object> userData = new ArrayMap<>();
        userData.put("email_id", email);
        userData.put("name", name);
        userData.put("total_score", 0);

         // Lấy bảng cần lưu
        DocumentReference userDoc = g_firestore.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

        WriteBatch batch = g_firestore.batch();

        batch.set(userDoc, userData);

        DocumentReference countDoc = g_firestore.collection("users").document("total_users");
        batch.update(countDoc, "count", FieldValue.increment(1));

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        myCompleteListener.onSuccess();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myCompleteListener.onFailure();
                    }
                });
    }

}
