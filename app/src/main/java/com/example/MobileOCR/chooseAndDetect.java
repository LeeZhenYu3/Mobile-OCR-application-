package com.example.MobileOCR;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class chooseAndDetect extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private TesseractOCR mTessOCR;
    private Context context;


    public String s;


    @BindView(R.id.ocr_image)
    ImageView firstImage;

    @BindView(R.id.ocr_text)
    TextView ocrText;

    Button Start;
    Button Save;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_and_detect);
        context=chooseAndDetect.this;

        ButterKnife.bind(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Start=findViewById(R.id.Choose_button);
        Save=findViewById(R.id.save_button);

        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSaveButton();
            }
        });


        String language = "eng";
        mTessOCR = new TesseractOCR(this, language);

    }

    void onClickSaveButton(){

        String Data = ocrText.getText().toString();
        Intent intent = new Intent (chooseAndDetect.this,SaveTextActivity.class);
        intent.putExtra("abc",Data);
        startActivity(intent);
        //finish();



    }

    public void checkPermission(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            try{
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},555);
            }catch(Exception e){

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==555 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            pickImage();
        }else{
            checkPermission();
        }

    }

    public void pickImage(){
        CropImage.startPickImageActivity(chooseAndDetect.this);

    }


    private void cropRequest(Uri imageUri){
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(chooseAndDetect.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            cropRequest(imageUri);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());

                    firstImage.setImageBitmap(bitmap);
                    doOCR(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }

    }




    private void doOCR(final Bitmap bitmap) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(this, "Processing",
                    "Doing OCR...", true);
        } else {
            mProgressDialog.show();
        }
        new Thread(new Runnable() {
            public void run() {
                final String srcText = mTessOCR.getOCRResult(bitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (srcText != null && !srcText.equals("")) {
                            s =srcText.trim() ;
                            ocrText.setText(srcText);


                        }
                        mProgressDialog.dismiss();
                    }
                });

            }
        }).start();

    }




}
