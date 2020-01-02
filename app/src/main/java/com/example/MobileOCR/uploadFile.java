package com.example.MobileOCR;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class uploadFile extends AppCompatActivity {

    Button chooseFileBt;
    Button uploadFileBt;
    TextView selectedFile;
    Uri textUri;
    EditText filename_userInput;



     public String fileName;


    ProgressDialog progressDialog;
    long id=0;

    FirebaseStorage storage;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference mRef=database.getReference();
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);

        filename_userInput=findViewById(R.id.fileName_text);

        storage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();


        mRef=database.getReference().child("textFile");
        mStorageRef=FirebaseStorage.getInstance().getReference("textFile");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    id=(dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        textFile textFileName=new textFile();
        chooseFileBt=findViewById(R.id.choose_file);
        uploadFileBt=findViewById(R.id.download);
        selectedFile=findViewById(R.id.textView);

        chooseFileBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(uploadFile.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    selectTextFile();
                }else{
                    ActivityCompat.requestPermissions(uploadFile.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},9);
                }

            }
        });

        uploadFileBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s=filename_userInput.getText().toString();
                if(s.matches("")){
                    Toast.makeText(uploadFile.this,"Please enter a filename",Toast.LENGTH_LONG).show();
                    return;
                }

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                }
                else
                    connected = false;

                if (connected==true){
                    if(textUri!=null){
                        uploadFileActivity(textUri);
                        mRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    id=(dataSnapshot.getChildrenCount());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        textFileName.setTextFileName(fileName);

                        mRef.child(String.valueOf(id+1)).setValue(textFileName);

                    }else{
                        Toast.makeText(uploadFile.this,"Please select a File",Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(uploadFile.this,"Please connect to internet to upload file",Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    private void uploadFileActivity(Uri textUri){

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading File...");
        progressDialog.show();

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat format=new SimpleDateFormat("d-M-yyyy HH:mm:ss");
        String date=format.format(calendar.getTime());

        String s=filename_userInput.getText().toString();

         fileName=s+" "+date;

        UploadTask uploadTask=mStorageRef.child(fileName).putFile(textUri);


        Task<Uri> task=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){

                }
                return mStorageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){

                }
                progressDialog.dismiss();
                Toast.makeText(uploadFile.this,"File successful upload",Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            selectTextFile();
        }else{
            Toast.makeText(uploadFile.this,"Please provide permission..",Toast.LENGTH_LONG).show();
        }
    }

    private void selectTextFile(){
        Intent intent=new Intent();
        intent.setType("text/plain");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            textUri = data.getData();
            selectedFile.setText("A File is selected :" + data.getData().getLastPathSegment());


        } else {
            Toast.makeText(uploadFile.this, "Please select a file", Toast.LENGTH_LONG).show();
        }

    }
}
