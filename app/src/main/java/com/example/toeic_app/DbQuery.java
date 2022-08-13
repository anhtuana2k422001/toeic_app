package com.example.toeic_app;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DbQuery {
    public static FirebaseFirestore g_firestore;
    public static List<CategoryModel>  g_catList = new ArrayList<>();    // khai báo list danh mục
    public static int g_selected_cat_index = 0; // lấy danh mục chọn

    public static List<TestModel> g_testlist = new ArrayList<>(); // Khai báo list test
    public static int g_selected_test_index = 0; // lấy chỉ mục bài

    public static List<QuestionModel> g_quesList = new ArrayList<>(); // Khai báo list test

    public static  ProfileModel myProfile = new ProfileModel("TLD", "null");


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

    // Lấy thông tin user
    public static void getUserData(final  MyCompleteListener completeListener){
        g_firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        myProfile.setName(documentSnapshot.getString("name"));
                        myProfile.setEmail(documentSnapshot.getString("email_id)"));

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });

    }

    // Load tất cả các danh mục lên giao diện
    public static void loadCategories(MyCompleteListener completeListener){
        g_catList.clear();
        g_firestore.collection("quiz").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String , QueryDocumentSnapshot> docList = new ArrayMap<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            docList.put(doc.getId(), doc);
                        }
                        QueryDocumentSnapshot catListDoc = docList.get("categories");

                        long catCount = catListDoc.getLong("count"); // Lấy ra tổng

                        for(int i = 1; i <= catCount; i++){
                            String CatID = catListDoc.getString("cat" + String.valueOf(i)+ "_id");
                            QueryDocumentSnapshot catDoc = docList.get(CatID);
                            int noOfTest = catDoc.getLong("no_of_tests").intValue();
                            String catName = catDoc.getString("name");
                            g_catList.add(new CategoryModel( CatID, catName, noOfTest));
                        }

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    //  Load tất các các bài trong danh mục
    public static void loadTestData(final  MyCompleteListener completeListener){
        g_testlist.clear();
        g_firestore.collection("quiz").document(g_catList.get(g_selected_cat_index).getDocID())
                .collection("tests_list").document("tests_info")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int noOfTests = g_catList.get(g_selected_cat_index).getNoOfTests();
                        for(int i =1; i <= noOfTests; i++ ){
                            g_testlist.add(new TestModel(
                                    documentSnapshot.getString("test" + String.valueOf(i) + "_id"
                                    ), 0, documentSnapshot.getLong("test" + String.valueOf(i) + "_time").intValue()
                            ));
                        }
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }


    // Lấy thông tin câu hỏi
    public static void LoadQuestions(MyCompleteListener myCompleteListener){
        g_quesList.clear();
        g_firestore.collection("questions")
                .whereEqualTo("category", g_catList.get(g_selected_cat_index).getDocID())
                .whereEqualTo("test", g_testlist.get(g_selected_test_index).getTestID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot doc : queryDocumentSnapshots){
                            g_quesList.add(new QuestionModel(
                                    doc.getString("question"),
                                    doc.getString("A"),
                                    doc.getString("B"),
                                    doc.getString("C"),
                                    doc.getString("D"),
                                    doc.getLong("answer").intValue()
                            ));
                        }
                        myCompleteListener.onSuccess();

                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myCompleteListener.onFailure();
                    }
                });
    }

    // load thông tin user
    public static void loadData(MyCompleteListener completeListener){
        loadCategories(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                getUserData(completeListener);
            }

            @Override
            public void onFailure() {
                completeListener.onFailure();
            }
        });

    }

}
