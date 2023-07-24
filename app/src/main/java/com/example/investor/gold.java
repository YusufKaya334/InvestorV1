package com.example.investor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class gold extends AppCompatActivity {
    public ListView listView;
    public ArrayAdapter<String> adapter;
    public ArrayList<String> data;
    private FirebaseFirestore firebaseFirestore;
    public EditText editText;
    public TextView textView ,textView2;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.anasayfagecis1:
                Intent intent=new Intent(gold.this,Options_menu.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold);
        editText=findViewById(R.id.editTextTextPersonName);
        textView=findViewById(R.id.altinverisi);
        textView2=findViewById(R.id.altinverisi2);
        firebaseFirestore=FirebaseFirestore.getInstance();

        showdata();

    }
    private void  showdata(){
        listView = findViewById(R.id.listviewdeneme1);
        data = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);

        RequestQueue requestQueue2= Volley.newRequestQueue(this);
        String url="https://finans.truncgil.com/v2/today.json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject ceyrek = response.getJSONObject("ceyrek_altin");
                            JSONObject yarim = response.getJSONObject("yarim_altin");
                            JSONObject tam=response.getJSONObject("tam_altin");


                            String ceyrekalis = ceyrek.getString("Buying");
                            String ceyreksatis=ceyrek.getString("Selling");
                            String yarimalis=yarim.getString("Buying");
                            String yarimsatis=yarim.getString("Selling");
                            String tamalis=tam.getString("Buying");
                            String tamsatış=tam.getString("Selling");


                            adapter.add("Çeyrek altın alış: " + ceyrekalis +"   Çeyrek altın satış: "+ ceyreksatis);
                            adapter.add("Yarım altın alış:  "+ yarimalis+ "   Yarım altın satış: "+ yarimsatis);
                            adapter.add("Tam altın alış: "+ tamalis+ " Tam altın satış:  "+ tamsatış);


                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                    if(position==0) {
                                        textView.setText(ceyrekalis);
                                        textView2.setText(ceyreksatis);
                                    }

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        requestQueue2.add(request);
    }

    public  void altinal(View view) throws ParseException {
        String textview_data=textView.getText().toString();
        String getdovizadet=editText.getText().toString();
        Integer dovizadet=Integer.parseInt(getdovizadet);
        String cleanString = textview_data.replace(",", ".");
        double doviz_alis=Double.parseDouble(cleanString);

        firebaseFirestore.collection("Bakiye").document("BKVBMYh6EMSnxSgmERWy").get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {

                        Double db_bakiye = documentSnapshot.getDouble("Bakiye");
                        if(doviz_alis*dovizadet>db_bakiye){
                            Toast.makeText(this, "BAKİYEDEN DAHA YÜKSEK BİR MİKTAR GİREMEZSİNİZ!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            double yeni_bakiye = db_bakiye - (doviz_alis * dovizadet);
                            Map<String,Object> update_bakiye=new HashMap<>();
                            update_bakiye.put("Bakiye",yeni_bakiye);
                            firebaseFirestore.collection("Bakiye").document("BKVBMYh6EMSnxSgmERWy").update(update_bakiye)
                                    .addOnSuccessListener(aVoid ->{

                                    }).addOnFailureListener(e ->{
                                        Toast.makeText(this, "Veri güncellenemedi", Toast.LENGTH_SHORT).show();
                                    } );
                        }

                    }

                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "veri çekilemedi.", Toast.LENGTH_SHORT).show();
                });

    }
    public  void altinsat(View view) throws ParseException {
        String textview_data=textView.getText().toString();

        String getdovizadet=editText.getText().toString();
        Integer dovizadet=Integer.parseInt(getdovizadet);
        String cleanString = textview_data.replace(",", ".");
        double doviz_alis=Double.parseDouble(cleanString);


        firebaseFirestore.collection("Bakiye").document("BKVBMYh6EMSnxSgmERWy").get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Double db_bakiye = documentSnapshot.getDouble("Bakiye");
                        double yeni_bakiye = db_bakiye + (doviz_alis * dovizadet);
                        Map<String,Object> update_bakiye=new HashMap<>();
                        update_bakiye.put("Bakiye",yeni_bakiye);
                        firebaseFirestore.collection("Bakiye").document("BKVBMYh6EMSnxSgmERWy").update(update_bakiye)
                                .addOnSuccessListener(aVoid ->{

                                }).addOnFailureListener(e ->{} );
                    }

                }).addOnFailureListener(e -> {

                });

    }

}