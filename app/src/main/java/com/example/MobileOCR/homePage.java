package com.example.MobileOCR;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class homePage extends AppCompatActivity {

    Button CaptureAndDetectBt;
    Button DownloadBt;
    Button UploadBt;
    Button ChooseAndDetect;
    Button ViewFile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        CaptureAndDetectBt=findViewById(R.id.Capture_and_detect);
        DownloadBt=findViewById(R.id.download_File);
        UploadBt=findViewById(R.id.Upload);
        ChooseAndDetect=findViewById(R.id.chooseAndDetect_button);
        ViewFile=findViewById(R.id.viewFile_button);

        CaptureAndDetectBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(homePage.this,MainActivity.class);
                startActivity(intent);
            }
        });


        UploadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(homePage.this,uploadFile.class);
                startActivity(intent);
            }
        });

        DownloadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(homePage.this,downloadFile.class);
                startActivity(intent);
            }
        });


        ChooseAndDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(homePage.this,chooseAndDetect.class);
                startActivity(intent);
            }
        });

        ViewFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(homePage.this,viewFile.class);
                startActivity(intent);
            }
        });








    }

}
