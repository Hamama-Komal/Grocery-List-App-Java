package com.example.memo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.memo.Activities.CreateActivity;
import com.example.memo.Activities.LoginActivity;
import com.example.memo.AdapterClasses.ListItemAdapter;
import com.example.memo.ModelClasses.ListItemModel;
import com.example.memo.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    ArrayList<ListItemModel> itemList;
    ListItemAdapter adapter;

    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        checkUser();




        itemList = new ArrayList<>();
       // adapter = new ListItemAdapter(this, itemList);
        binding.listRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.listRecycler.setHasFixedSize(true);

        fetchListData();


        binding.iconLogout.setOnClickListener(view -> {

            showLogoutDialog();
        });



       binding.createButton.setOnClickListener(view -> {

           startActivity(new Intent(this, CreateActivity.class));
           finishAffinity();

       });

       binding.deleteButton.setOnClickListener(view -> {

           clearList();

       });
    }

    private void clearList() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Do you really want to delete your all data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               clearAllDataFromList();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();


    }

    private void clearAllDataFromList() {

        firestore.collection("AllLists")
                .document(firebaseUser.getUid())
                .collection("MyList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Delete each document
                                firestore.collection("AllLists")
                                        .document(firebaseUser.getUid())
                                        .collection("MyList")
                                        .document(document.getId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                // Document successfully deleted
                                                Toast.makeText(MainActivity.this, "Your List is clear now!", Toast.LENGTH_SHORT).show();
                                                finish();
                                                startActivity(new Intent(MainActivity.this, MainActivity.class));
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }
                        }
                    }
                });


    }

    private void fetchListData() {

        firestore.collection("AllLists").document(firebaseUser.getUid()).collection("MyList")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        itemList.clear();

                        for (DocumentSnapshot ds : task.getResult()) {
                            ListItemModel model = new ListItemModel(
                                    ds.getString("id"),
                                    ds.getString("item"),
                                    ds.getString("quantity"),
                                    ds.getString("price"),
                                    ds.getString("date")
                            );

                            itemList.add(model);

                        }

                        adapter = new ListItemAdapter(MainActivity.this,itemList);
                        binding.listRecycler.setAdapter(adapter);

                    }
                });

    }

    private void showLogoutDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Logout Account")
                .setMessage("Are you sure you want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    private void checkUser() {

        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finishAffinity();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchListData();
    }
}