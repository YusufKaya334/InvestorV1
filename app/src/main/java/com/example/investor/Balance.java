package com.example.investor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Balance extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private TextView txt1;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.anasayfagecis1:
                Intent intent=new Intent(Balance.this,Options_menu.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        txt1=findViewById(R.id.bakiyegoster);
        firebaseFirestore=FirebaseFirestore.getInstance();

    }

    public void show_balance(View view){

        firebaseFirestore.collection("Bakiye").document("BKVBMYh6EMSnxSgmERWy").get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {

                        Double db_bakiye = documentSnapshot.getDouble("Bakiye");
                        String new_balance=db_bakiye.toString();
                        txt1.setText(new_balance);

                    }

                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "veri çekilemedi.", Toast.LENGTH_SHORT).show();
                });


    }
}