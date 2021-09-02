package com.hauntersoft.indiewallpapers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class FullImageActivity extends AppCompatActivity {

    private ImageView fullImage;
    private Button applyBtn;
    private AdView mAdView;
    private final int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        fullImage = findViewById(R.id.fullImage);
        applyBtn = findViewById(R.id.apply);

        Glide.with(this).load(getIntent().getStringExtra("image")).into(fullImage);

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(FullImageActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        setBackground();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(FullImageActivity.this, "Deu ruim",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    requestStoragePermission();
                }

            }
        });

        loadBannerAd();
    }

    //Carrega intent para definir wallpaper
    private void setBackground() throws IOException {
        Bitmap bitmap = ((BitmapDrawable)fullImage.getDrawable()).getBitmap();
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri wallUri = getImageUri(bitmap);
        intent.setDataAndType(wallUri, "image/*");
        intent.putExtra("mimeType", "image/*");
        startActivity(Intent.createChooser(intent, getResources().getText(R.string.wallpaper_intent)));
    }

    //Carrega o Banner de anúncio
    private void loadBannerAd(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    //Transforma bitmap em uri
    public Uri getImageUri(Bitmap inImage) throws IOException {
        File tempDir;
        tempDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //tempDir = new File(tempDir.getAbsolutePath()+"/.temp/");
        //tempDir.mkdir();
        File tempFile = File.createTempFile("image", ".jpg", tempDir);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] bitmapData = bytes.toByteArray();
        //write the bytes in file
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(bitmapData);
        fos.flush();
        fos.close();
        return FileProvider.getUriForFile(FullImageActivity.this,
                FullImageActivity.this.getApplicationContext().getPackageName() + ".provider",
                tempFile);
    }

    //Pedir permissão
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(FullImageActivity.this,
                                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    //Pedir mais permissão
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    setBackground();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Deu ruim",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

}