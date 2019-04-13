package com.project.photoshoot;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.project.photoshoot.main.pdf.PDFRender;
import com.project.photoshoot.models.ImageBookModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class ImageBookPaymentActivity extends AppCompatActivity {


    List<ImageBookModel> mImagesForBook;
    ProgressDialog mProgressDialog;


    boolean permission = false;
    String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_book_payment);

        //TODO Improve UI

        String json = getIntent().getStringExtra("data");




        Type type = new TypeToken<List<ImageBookModel>>() {}.getType();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new ImageBookModel.UriDeserializer())
                .create();
        mImagesForBook  = gson.fromJson(json,type);


        URL = mImagesForBook.get(0).getPhotoUrl();




    }


    public void done(View v) {

        if(permission)
            new  DownloadImage().execute(URL);
        else {
            if(isReadStoragePermissionGranted() && isReadStoragePermissionGranted()) {
                permission = true;
            }
        }
    }







    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        File file;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ImageBookPaymentActivity.this);
            mProgressDialog.setTitle("Downloading Images");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
            try {
                this.file = saveToFile(result);

                mImagesForBook.get(0).setPhotoURI(Uri.fromFile(file));


                new PDFRender(mImagesForBook).createPDF(ImageBookPaymentActivity.this);



            } catch (IOException e) {
                e.printStackTrace();
                Log.d("$$", "onPostExecute: " + e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ImageBookPaymentActivity.this, "Error in Downloading", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            mProgressDialog.dismiss();
        }

        public File saveToFile(Bitmap pictureBitmap) throws IOException {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            OutputStream fOut = null;
            Integer counter = 0;
            File file = new File(path, "temp.jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
            fOut = new FileOutputStream(file);
            pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
            return file;
        }
    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public void uploadFromFile(Uri uri) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"happyshoots12325@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Photobook");
        i.putExtra(Intent.EXTRA_TEXT   , "[Address here]" + "<"+FirebaseAuth.getInstance().getCurrentUser().getUid()+">");
        i.putExtra(Intent.EXTRA_STREAM, uri);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {

            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
        }

    }
}

