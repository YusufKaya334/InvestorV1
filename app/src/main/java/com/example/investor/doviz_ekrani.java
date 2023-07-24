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

public class doviz_ekrani extends AppCompatActivity {

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
                Intent intent=new Intent(doviz_ekrani.this,Options_menu.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doviz_ekrani);

        editText=findViewById(R.id.editTextTextPersonName);
        textView=findViewById(R.id.altinverisi);
        textView2=findViewById(R.id.altinverisi2);
        firebaseFirestore=FirebaseFirestore.getInstance();
        showdata();

    }
    private void  showdata(){
        listView = findViewById(R.id.listviewdeneme);
        data = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);

        RequestQueue requestQueue2=Volley.newRequestQueue(this);
        String url="https://finans.truncgil.com/today.json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject usdObject = response.getJSONObject("USD");
                            JSONObject eurObject = response.getJSONObject("EUR");
                            JSONObject gbpObject = response.getJSONObject("GBP");
                            JSONObject chfObject = response.getJSONObject("CHF");
                            JSONObject cadObject = response.getJSONObject("CAD");
                            JSONObject audObject= response.getJSONObject("AUD");

                            String usdAlis = usdObject.getString("Alış");
                            String eurAlis = eurObject.getString("Alış");
                            String gbpAlis = gbpObject.getString("Alış");
                            String chfAlis = chfObject.getString("Alış");
                            String cadAlis = cadObject.getString("Alış");
                            String audAlis= audObject.getString("Alış");

                            String usdSatisFiyatı=usdObject.getString("Satış");
                            String eurosatisfiyati= eurObject.getString("Satış");
                            String  gbpsatisfiyati=gbpObject.getString("Satış");
                            String chfsatisfiyati=chfObject.getString("Satış");
                            String cadsatisfiyati=cadObject.getString("Satış");
                            String audsatisfiyati=audObject.getString("Satış");

                            adapter.add("USD Alış: " + usdAlis +"   USD Satış: "+ usdSatisFiyatı);
                            adapter.add("EUR Alış: " + eurAlis +"   EUR Satış: "+ eurosatisfiyati);
                            adapter.add("GBP Alış: " + gbpAlis +"   GBP Satış: "+ gbpsatisfiyati);
                            adapter.add("CHF Alış: " + chfAlis +"   CHF Satış: "+ chfsatisfiyati);
                            adapter.add("CAD Alış: " + cadAlis +"   CAD Satış: "+ cadsatisfiyati);
                            adapter.add("AUD Alış: " + audAlis +"   AUD Satış: "+ audsatisfiyati );

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                    if(position==0){
                                        textView.setText(usdAlis); textView2.setText(usdSatisFiyatı);
                                    } else if(position==1){
                                        textView.setText(eurAlis);  textView2.setText(eurosatisfiyati);
                                    } else if(position==2){
                                        textView.setText(gbpAlis);  textView2.setText(gbpsatisfiyati);
                                    } else if(position==3){
                                        textView.setText(chfAlis); textView2.setText(chfsatisfiyati);
                                    } else if(position==4){
                                        textView.setText(cadAlis); textView2.setText(cadsatisfiyati);
                                    } else if(position==5){
                                        textView.setText(audAlis); textView2.setText(audsatisfiyati);
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

    public  void dovizal(View view) throws ParseException {
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
                        else {
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
    public  void dovizsat(View view) throws ParseException {
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