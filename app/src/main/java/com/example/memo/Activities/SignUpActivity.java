package com.example.memo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.memo.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;

    // String userName, userEmail, userPassword, conPassword;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();


        binding.buttonSignup.setOnClickListener(view -> checkValues());

        binding.buttonLogin.setOnClickListener(view -> {

            startActivity(new Intent(this, LoginActivity.class));
            finish();

        });


    }

    private void checkValues() {

        String userName = binding.edtName.getText().toString().trim();
        String userEmail = binding.edtEmail.getText().toString().trim();
        String userPassword = binding.edtPassword.getText().toString().trim();
        String conPassword = binding.edtConfrimPassword.getText().toString().trim();

        if (userName.isEmpty()) {
            binding.edtName.setError("Please Enter Your Name!");
            binding.edtName.requestFocus();
        } else if (userEmail.isEmpty()) {
            binding.edtEmail.setError("Email is required!");
            binding.edtEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            binding.edtEmail.setError("Please Enter a Valid Email!");
            binding.edtEmail.requestFocus();
        } else if (userPassword.isEmpty()) {
            binding.edtPassword.setError("Please Set a Password!");
            binding.edtPassword.requestFocus();
        } else if (userPassword.length() < 7) {
            binding.edtPassword.setError("Password must contain at least 7-characters!");
            binding.edtPassword.requestFocus();
        } else if (conPassword.isEmpty()) {
            binding.edtConfrimPassword.setError("Please Confirm Your Password!");
            binding.edtConfrimPassword.requestFocus();
        } else if (!conPassword.equals(userPassword)) {
            binding.edtConfrimPassword.setError("Password not match!");
            binding.edtConfrimPassword.requestFocus();
        } else {

            registerUser(userName, userEmail, userPassword);
        }


    }

    private void registerUser(String userName, String userEmail, String userPassword) {

        binding.progressBar2.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnSuccessListener(authResult -> {

            binding.progressBar2.setVisibility(View.GONE);

            Toast.makeText(SignUpActivity.this, "Welcome " + userName, Toast.LENGTH_SHORT).show();
            binding.progressBar2.setVisibility(View.GONE);
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            //intent.putExtra("name", userName);
            startActivity(intent);
            finish();


        }).addOnFailureListener(e -> Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        binding.progressBar2.setVisibility(View.GONE);
    }

}