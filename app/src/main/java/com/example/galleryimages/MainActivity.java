package com.example.galleryimages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<String> imagePaths = new ArrayList<>();
    MyGridAdapter adapter;
    GridView gridview;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridview = findViewById(R.id.gridview);
        btn= findViewById(R.id.button);
        btn.setOnClickListener(this::onClick);
        adapter = new MyGridAdapter(MainActivity.this,imagePaths);

    }

    public void onClick(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);
        } else {
            // Permission has already been granted, proceed with reading the images
            new ShowImages(MainActivity.this).execute();
        }
    }
    private class ShowImages extends AsyncTask<String, Integer, String>  {
        private ProgressDialog mProgressDialog;
        private int mProgress;

        public ShowImages(Context context) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Showing pictures.......");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            mProgress = 0;
        }



        @Override
        protected void onPreExecute() {
            // Show the progress dialog
            mProgressDialog.show();
        }


        @Override
        protected String doInBackground(String... urls) {
            while (mProgress <= 100) {
                try {
                    Thread.sleep(10);
                    mProgress++;
                    publishProgress(mProgress);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection, null, null, MediaStore.Images.Media.DATE_ADDED + " DESC");
                // Iterate through the cursor and add the image paths to the list
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        @SuppressLint("Range") String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        imagePaths.add(imagePath);
                    }
                    cursor.close();
                }
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            } catch (Exception e) {
                // Handle any exceptions that may occur
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            gridview.setAdapter(new MyGridAdapter(MainActivity.this, imagePaths));
            mProgressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Pictures are successfully displayed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProgressUpdate(Integer... values) {
            mProgressDialog.setProgress(values[0]);
        }

    }
}