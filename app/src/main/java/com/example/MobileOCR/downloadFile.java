package com.example.MobileOCR;

import android.app.DownloadManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class downloadFile extends AppCompatActivity {

    EditText filename_userInput;
    StorageReference storageReference;
    StorageReference ref;
    Button downloadFileBt;
    String Filename;
    String day;
    String month;
    String year;
    DatePicker simpleDatePicker;
    NumberPicker pickHour;
    NumberPicker pickMinute;
    NumberPicker pickSecond;

    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_file);

        downloadFileBt=findViewById(R.id.download);
        filename_userInput=findViewById(R.id.fileName_text);
        simpleDatePicker = findViewById(R.id.simpleDatePicker);
        pickHour=findViewById(R.id.numpicker_hours);
        pickHour.setMaxValue(23);
        pickMinute=findViewById(R.id.numpicker_minutes);
        pickMinute.setMaxValue(59);
        pickSecond=findViewById(R.id.numpicker_seconds);
        pickSecond.setMaxValue(59);
        textView=findViewById(R.id.textView2);

        downloadFileBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=filename_userInput.getText().toString();
                if(s.matches("")){
                    Toast.makeText(downloadFile.this,"Please enter a filename",Toast.LENGTH_LONG).show();
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
                if(connected==true){
                    download();
                }else{
                    Toast.makeText(downloadFile.this,"Please connect to the internet to download the file",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void download(){

        String s=filename_userInput.getText().toString();
         day =""+simpleDatePicker.getDayOfMonth();
         month = "" + (simpleDatePicker.getMonth() + 1);
         year = "" + simpleDatePicker.getYear();

        String hour=""+pickHour.getValue();
        String minute=""+pickMinute.getValue();
        String second=""+pickSecond.getValue();
        Filename=s+" "+day+"-"+month+"-"+year+" "+hour+":"+minute+":"+second;




        storageReference=FirebaseStorage.getInstance().getReference("textFile");
        ref=storageReference.child(""+Filename);

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url=uri.toString();
                downloadFile(downloadFile.this,""+Filename,".txt",DIRECTORY_DOWNLOADS,url);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(downloadFile.this,"The request file is not exist in the database " +
                        "please check on view file to get correct filename with the date and time ",Toast.LENGTH_LONG).show();


            }
        });

        textView.setText(Filename);
    }

    public void downloadFile(Context context, String filename, String fileExtension, String destinationDirectory, String url){

        DownloadManager downloadManager= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri=Uri.parse(url);
        DownloadManager.Request request= new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationDirectory,filename+fileExtension);

        downloadManager.enqueue(request);
    }







}
