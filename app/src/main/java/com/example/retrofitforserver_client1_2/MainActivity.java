package com.example.retrofitforserver_client1_2;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    ImageView imageView;
    String imagePath;
    Toolbar toolbar;
    String fileDownloadStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(MainActivity.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);



        imageView = (ImageView) findViewById(R.id.imageView);

        Button button = (Button) findViewById(R.id.fab);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(imagePath!=null) {
                    System.out.println("File to upload: " + imagePath);
                    uploadImage();
                } else {
                    Toast.makeText(getApplicationContext(), "Please select image", Toast.LENGTH_LONG).show();
                }

            }
        });

        Button btn_Download = findViewById(R.id.download_btn);

        btn_Download.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(fileDownloadStr != null){
                    downloadImage(fileDownloadStr);
                    System.out.println("File " + fileDownloadStr + " was downloaded");
                } else Toast.makeText(MainActivity.this, "Pick and upload image!", Toast.LENGTH_LONG).show();
            }
        });
    }

//    private void downloadImage(final String path){
////        final ProgressDialog progressDialog;
////        progressDialog = new ProgressDialog(MainActivity.this);
////        progressDialog.setMessage("loading...");
////        progressDialog.show();
////
////        FileApi service = RetroClient.getApiService();
////
////        service.downloadFile(path).enqueue(new Callback<ResponseBody>() {
////            @Override
////            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
////                try {
////                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyy");
////                    File newFilePath = new File(Environment.getExternalStorageDirectory() + File.separator + simpleDateFormat.format(new Date()
////                    ) +  "_image.jpg");
////                    FileOutputStream fileOutputStream = new FileOutputStream(newFilePath);
////                    InputStream is = new URL(fileDownloadStr).openStream();
////                    write(is, fileOutputStream);
////                    Toast.makeText(MainActivity.this, "Downloading finished succesfully!", Toast.LENGTH_LONG);
////                } catch (FileNotFoundException e) {
////                    e.printStackTrace();
////                } catch (MalformedURLException e){
////                    e.printStackTrace();
////                } catch (IOException e){
////                    e.printStackTrace();
////                }
////            }
////
////            @Override
////            public void onFailure(Call<ResponseBody> call, Throwable t) {
////
////            }
////        });
////}

    private  void downloadImage(final String path){
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();

        FileApi service = RetroClient.getApiService();

        service.downloadFile(path).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
//                Toast.makeText(MainActivity.this, "Image downloaded successfully!", Toast.LENGTH_LONG);

                if(response.isSuccessful()){

                    InputStream is;
                    FileOutputStream os;

                    try {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyHHmmss");
                        File newFilePath = new File(Environment.getExternalStorageDirectory() + File.separator + simpleDateFormat.format(new Date()
                        ) +  "_image.jpg");

                        newFilePath.createNewFile();

                        is = response.body().byteStream();
                        os = new FileOutputStream(newFilePath);
                        write(is, os);
                        Toast.makeText(MainActivity.this, "Image was downloaded successfully!", Toast.LENGTH_LONG).show();


                        is.close();
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else{
                    Toast.makeText(MainActivity.this, "Image downloading was not successfull =(", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

                Toast.makeText(MainActivity.this, "Cannot download image!", Toast.LENGTH_LONG);
            }
        });
    }

    private void write(InputStream inputStream, FileOutputStream fileOutputStream){

            // todo change the file location/name according to your needs
//                File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + "Future Studio Icon.png");

//            InputStream inputStream = null;
//                OutputStream outputStream = null;

            try {
                byte[] buffer = new byte[4096];

//                long fileSize = body.contentLength();
//                long fileSizeDownloaded = 0;
//
//                inputStream = body.byteStream();
//                    outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(buffer);

                    if (read == -1) {
                        break;
                    }

                    fileOutputStream.write(buffer, 0, read);

//                    fileSizeDownloaded += read;

//                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                fileOutputStream.flush();
                inputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void uploadImage() {

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();

        FileApi service = RetroClient.getApiService();

        File file = new File(imagePath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Call<Respond> resultCall = service.uploadImage(body);

        resultCall.enqueue(new Callback<Respond>() {
            @Override
            public void onResponse(Call<Respond> call, Response<Respond> response) {

                progressDialog.dismiss();

                // Response Success or Fail
                if (response.isSuccessful()) {
                   /* if (response.body().getError()==true)

                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();

                    else*/
                    System.out.println("PHOTO: " + response.body().getFileDownloadUri());
                    fileDownloadStr = response.body().getFileName();

                    Toast.makeText(MainActivity.this, "Image was uploaded successfully!", Toast.LENGTH_LONG).show();
                    /*Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();*/
                } else {
                    Toast.makeText(MainActivity.this, "Uploading was not successfull =(", Toast.LENGTH_LONG).show();
                    /*Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();*/
                }

//                imageView.setImageDrawable(null);
//                imagePath = null;

            }

            @Override
            public void onFailure(Call<Respond> call, Throwable t) {
                progressDialog.dismiss();

                Toast.makeText(MainActivity.this, "Cannot upload image!", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void showImagePopup(View view) {
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);

        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Choose image");
        startActivityForResult(chooserIntent, 100);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            if (data == null) {
                Toast.makeText(getApplicationContext(),"Unable to pick image",Toast.LENGTH_LONG).show();
                return;
            }

            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
         /*
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);*/
            imagePath = getRealPathFromURI(imageUri);

            //    Picasso.with(getApplicationContext()).load(new File(imagePath))
            //            .into(imageView);

            //   Toast.makeText(getApplicationContext(),"Please reselect your image",Toast.LENGTH_LONG).show();
           /*     cursor.close();

            } else {

                Toast.makeText(getApplicationContext(),"Unable to load image",Toast.LENGTH_LONG).show();
            }*/
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }




}
