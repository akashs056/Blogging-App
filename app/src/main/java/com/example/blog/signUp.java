package com.example.blog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.blog.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class signUp extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        
        binding.SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=binding.emailET.getText().toString();
                String pass=binding.passwordET.getText().toString();
                auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        userModel user=new userModel(binding.nameET.getText().toString(),binding.professionET.getText().toString(),email,pass);
                        String id=task.getResult().getUser().getUid();
                        database.getReference().child("Users").child(id).setValue(user);
                        Toast.makeText(signUp.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent=new Intent(signUp.this, home.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(signUp.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
                    }
                    }
                });
            }
        });

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(signUp.this,Login.class);
                startActivity(intent);
            }
        });
    }
}