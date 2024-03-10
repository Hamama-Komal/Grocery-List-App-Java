package com.example.memo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.memo.databinding.ActivityCreateBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class CreateActivity extends AppCompatActivity {

    ActivityCreateBinding binding;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;

    String item, quantity, price, currentDate ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();



        binding.btnAdd.setOnClickListener(view -> {

            checkValues();

        });

    }

    private void checkValues() {



        item = binding.edtItem.getText().toString().trim();
        quantity = binding.edtQuantity.getText().toString().trim();
        price = binding.edtPrice.getText().toString().trim();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MM YYYY HH:mm", Locale.getDefault());
        currentDate = sdf.format(new Date());


        if(quantity.isEmpty()){
            quantity = "--";
        }

        if (price.isEmpty()) {
            price = "--";
        }

        if (item.isEmpty()) {
            Toast.makeText(this, "Please enter item first", Toast.LENGTH_SHORT).show();
        }
        else {

            addValuesInFirestore(item, quantity, price, currentDate);
            binding.progressBar3.setVisibility(View.VISIBLE);
        }

    }

    private void addValuesInFirestore(String item, String quantity, String price, String currentDate) {

        String id = UUID.randomUUID().toString();

        HashMap<String,Object> myList = new HashMap<>();
        myList.put("id",id);
        myList.put("item", item);
        myList.put("quantity", quantity);
        myList.put("price",price);
        myList.put("date", currentDate);



        firestore.collection("AllLists").document(firebaseUser.getUid()).collection("MyList")
                .document(id).set(myList).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        binding.progressBar3.setVisibility(View.GONE);
                        Toast.makeText(CreateActivity.this, "Item Added", Toast.LENGTH_SHORT).show();

                        binding.edtItem.setText("");
                        binding.edtQuantity.setText("");
                        binding.edtPrice.setText("");

                        startActivity(new Intent(CreateActivity.this, MainActivity.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        binding.progressBar3.setVisibility(View.GONE);
                        Toast.makeText(CreateActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                    }
                });


    }
}