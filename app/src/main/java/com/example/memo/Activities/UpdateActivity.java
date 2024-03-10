package com.example.memo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.memo.R;
import com.example.memo.databinding.ActivityUpdateBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpdateActivity extends AppCompatActivity {

    ActivityUpdateBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    String id;

    String item, quantity, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        id = getIntent().getStringExtra("id");
        String getItem = getIntent().getStringExtra("item");
        String getQuantity = getIntent().getStringExtra("quantity");
        String getPrice = getIntent().getStringExtra("price");


        binding.edtItem.setText(getItem);
        binding.edtQuantity.setText(getQuantity);
        binding.edtPrice.setText(getPrice);


        binding.btnUpdate.setOnClickListener(view -> {

            UpdateValues();

        });

        binding.btnDelete.setOnClickListener(view -> {

            deleteValues();

        });


    }

    private void deleteValues() {

        firestore.collection("AllLists").document(firebaseAuth.getUid())
                .collection("MyList")
                .document(id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(UpdateActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void UpdateValues() {

        item = binding.edtItem.getText().toString().trim();
        quantity = binding.edtQuantity.getText().toString().trim();
        price = binding.edtPrice.getText().toString().trim();

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
            UpdatesValuesInFirestore(item, quantity, price);
            binding.progressBar3.setVisibility(View.VISIBLE);
        }



    }

    private void UpdatesValuesInFirestore(String item, String quantity, String price) {

        firestore.collection("AllLists").document(firebaseAuth.getUid())
                .collection("MyList").document(id)
                .update("item", item, "quantity", quantity,"price", price)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        binding.progressBar3.setVisibility(View.GONE);
                        Toast.makeText(UpdateActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.progressBar3.setVisibility(View.GONE);
                        Toast.makeText(UpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}