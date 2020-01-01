package com.example.MobileOCR;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;





public class SaveTextActivity extends AppCompatActivity {

    EditText Display;
    EditText FileName;
    Button saveFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_text);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);
        }

        Display =findViewById(R.id.Edit_text);
        Bundle bn =getIntent().getExtras();
        String name = bn.getString("abc");
        Display.setText(name);

        FileName=findViewById(R.id.fileName_text);

        saveFile=findViewById(R.id.save_button);

        saveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String filename=FileName.getText().toString();
                if(filename.matches("")){
                    Toast.makeText(SaveTextActivity.this,"Please enter a filename",Toast.LENGTH_LONG).show();
                    return;
                }
                String content=Display.getText().toString();

                saveTextAsFile(filename,content);
            }
        });



    }

    private void saveTextAsFile(String filename,String text){
        String fileName=filename+".txt";
        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        File file=new File(directory,fileName);

        int num=0;
        while(file.exists()){
            num=num+1;
            fileName=filename+num+".txt";
            file=new File(directory,fileName);
        }

        try{
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(text.getBytes());
            fos.close();
            Toast.makeText(SaveTextActivity.this,"You file is saved",Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}
