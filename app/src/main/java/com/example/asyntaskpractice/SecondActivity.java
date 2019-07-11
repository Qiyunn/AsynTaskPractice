package com.example.asyntaskpractice;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class SecondActivity extends AppCompatActivity {

    Button button;
    ImageView imageView;
    ProgressDialog progressDialog;
   ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        button=findViewById(R.id.second_button);
        imageView=findViewById(R.id.second_image_view);

        progressBar=findViewById(R.id.progress_bar);
        progressBar.setMax(100);
        progressBar.setProgress(0);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyTask().execute("https://api.androidhive.info/progressdialog/hive.jpg");
            }
        });
    }

    class MyTask extends AsyncTask<String,Integer, Bitmap>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(SecondActivity.this);
            progressDialog.setTitle("My Dialog");
            progressDialog.setMessage("Download Please wait...");
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();


        }

        @Override
        protected Bitmap doInBackground(String... abc) {
//           String ImageUrl=abc[0];
           Bitmap bitmap=null;

            try {
                URL mUrl=new URL(abc[0]);
                Log.d("data","the url is:"+mUrl);
                HttpURLConnection connnection =(HttpURLConnection)  mUrl.openConnection();
                connnection.connect();

//                InputStream input = new BufferedInputStream(mUrl.openStream());
                InputStream input=connnection.getInputStream();
                OutputStream output=new FileOutputStream("/sdcard/downloadedfile.jpg");
//

                int fileLength=connnection.getContentLength();
                Log.d("data","the filelength is:"+fileLength);
                byte[] data = new byte[1024];
                int downLoadSize=0;
                int count;
                while( (count=input.read(data))!= -1){
                    Log.d("data","initial download is:"+downLoadSize);
                    downLoadSize+=count;
                    Log.d("data","the download is"+downLoadSize*100);
                    publishProgress((downLoadSize*100/fileLength));
                    output.write(data,0,count);
                }
                output.flush();
                input.close();
                output.close();
                File file=new File("/sdcard/downloadedfile.jpg");

                bitmap = BitmapFactory.decodeStream(new FileInputStream(file));

            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d("data","the values is:"+values[0]);
            progressDialog.setProgress(values[0]);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//            progressDialog.dismiss();
            imageView.setImageBitmap(bitmap);
        }
    }
}
