package com.example.memo.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.memo.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

   ActivityLoginBinding binding;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();




        binding.buttonSignup.setOnClickListener(view -> {

            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        });

        binding.buttonLogin.setOnClickListener(view -> {

             checkInputValues();

        });
    }

    private void checkInputValues() {

        String email = binding.edtEmail.getText().toString().trim();
        String password  = binding.edtPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            binding.edtEmail.setError("Email is required");
            binding.edtEmail.requestFocus();
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {

            binding.edtEmail.setError("Valid Email is required");
            binding.edtEmail.requestFocus();

        }
        else if (TextUtils.isEmpty(password))
        {

            binding.edtPassword.setError("Password is required");
            binding.edtPassword.requestFocus();

        }
        else {


            loginUser(email, password);
        }
    }

    private void loginUser(String userEmail, String userPassword) {

        binding.progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
                else{
                    try{
                        task.getException();
                    }
                    catch (Exception e)
                    {
                        Log.e(TAG, e.toString());
                        Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                }

                binding.progressBar.setVisibility(View.GONE);
            }
        });


    }

    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser() != null){

             // Toast.makeText(this, "Welcome Back!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();

        }
    }
}