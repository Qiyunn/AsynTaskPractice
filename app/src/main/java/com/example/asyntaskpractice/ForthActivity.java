package com.example.asyntaskpractice;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ForthActivity extends AppCompatActivity {
     private Button button;
     private ProgressDialog progressDialog;
     private String URL="https://api.androidhive.info/progressdialog/hive.jpg";
     private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forth);
        imageView=findViewById(R.id.four_image_view);
        button=findViewById(R.id.four_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DownloadFile downloadFile=new DownloadFile();
               downloadFile.execute(URL);
            }
        });

    }

    private class DownloadFile extends AsyncTask<String,Integer,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(ForthActivity.this
            );

            progressDialog.setTitle("progress bar");
            progressDialog.setMessage("Doenload,please wait");
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url=new URL(strings[0]);
                Log.d("data","url is :"+url);
                URLConnection connection=url.openConnection();
                connection.connect();
                int fileLenth=connection.getContentLength();
                Log.d("data","the fileLenth is:"+fileLenth);
                String filePath= Environment.getExternalStorageDirectory().getPath();

                InputStream input=new BufferedInputStream(url.openStream());
                OutputStream output=new FileOutputStream(filePath+"/"+"image.png");

                byte[] data = new byte[1024];
                long total=0;
                int count;
                while((count=input.read(data))!= -1){
                    Log.d("data","the initial total:"+total);
                     total+=count;
                     publishProgress((int)(total*100/fileLenth));
                     Log.d("data","the progress is:"+(int)(total*100/fileLenth));
                     output.write(data,0,count);
                }
                output.flush();
                output.close();
                input.close();
                File file=new File(filePath+"/"+"image.png");
//                bitmap= BitmapFactory.decodeStream(new FileInputStream(file));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
//            imageView.setImageBitmap();
        }
    }
}
