package com.example.investor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.investor.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        auth=FirebaseAuth.getInstance();
    }
    public void signin(View view){
        String email_signin=binding.email1.getText().toString();
        String password_signin=binding.password1.getText().toString();
        if(email_signin.equals("") || password_signin.equals("")){
            Toast.makeText(this,"Enter your password and email.",Toast.LENGTH_SHORT).show();
        }else{
            auth.signInWithEmailAndPassword(email_signin,password_signin).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent=new Intent(MainActivity.this,Loading_Screen.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
    public void signup(View view){
        String email=binding.email1.getText().toString();
        String password=binding.password1.getText().toString();
        if(email.equals("") || password.equals("")){
            Toast.makeText(this,"Enter your password and email.",Toast.LENGTH_SHORT).show();
        }else{
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent=new Intent(MainActivity.this,Loading_Screen.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}