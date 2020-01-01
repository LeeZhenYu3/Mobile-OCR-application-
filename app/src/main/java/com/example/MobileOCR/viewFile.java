package com.example.MobileOCR;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewFile extends AppCompatActivity {

    ListView listView;
    FirebaseDatabase database;
    DatabaseReference mRef;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;


    textFile textFileName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_file);


        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        if(connected==true){
            textFileName=new textFile();
            listView=findViewById(R.id.listView);
            database=FirebaseDatabase.getInstance();
            mRef=database.getReference("textFile");
            list =new ArrayList<>();
            adapter=new ArrayAdapter<>(viewFile.this,R.layout.text_name,R.id.fileName,list);
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds:dataSnapshot.getChildren()){

                        textFileName=ds.getValue(textFile.class);
                        list.add(textFileName.getTextFileName());


                    }
                    listView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


                }
            });

        }else{
            Toast.makeText(viewFile.this,"Please connected to the internet to get the filename from database",Toast.LENGTH_LONG).show();
        }
    }
}
