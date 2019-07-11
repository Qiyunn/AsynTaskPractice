package com.example.asyntaskpractice;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class ThirdActivity extends Activity {
    private static final String FILE_NAME = "test.pdf";
    private static final String PDF_URL = "http://clfile.imooc.com/class/assist/118/1328281/AsyncTask.pdf";
    private ProgressBar mProgressBar;
    private Button mDownloadBtn;
    private TextView mStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        initView();
        setListener();
    }

    private void initView() {
        mProgressBar =  findViewById(R.id.progressBar);
        mDownloadBtn = findViewById(R.id.download);
        mStatus =  findViewById(R.id.status);
    }

    private void setListener() {
        mDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AsyncTask实例必须在主线程创建
                DownloadAsyncTask asyncTask = new DownloadAsyncTask();
                asyncTask.execute(PDF_URL);
            }
        });
    }

    private class DownloadAsyncTask extends AsyncTask<String, Integer, Boolean> {
        private String mFilePath;//下载文件的保存路径
        @Override
        protected Boolean doInBackground(String... params) {
            if (params != null && params.length > 0) {
                String pdfUrl = params[0];
                try {
                    URL url = new URL(pdfUrl);
                    URLConnection urlConnection = url.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    int contentLength = urlConnection.getContentLength();//获取内容总长度
                    mFilePath = Environment.getExternalStorageDirectory() + File.separator + FILE_NAME;
                    //若存在同名文件则删除
                    File pdfFile = new File(mFilePath);
                    if (pdfFile.exists()) {
                        boolean result = pdfFile.delete();
                        if (!result) {
                            return false;
                        }
                    }
                    int downloadSize = 0;//已经下载的大小
                    byte[] bytes = new byte[1024];
                    int length;
                    OutputStream out = new FileOutputStream(mFilePath);
                    while ((length = in.read(bytes)) != -1) {
                        out.write(bytes, 0, length);
                        downloadSize += length;
                        publishProgress(downloadSize / contentLength * 100);
                    }
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
            return true;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDownloadBtn.setText("下载中");
            mDownloadBtn.setEnabled(false);
            mStatus.setText("下载中");
            mProgressBar.setProgress(0);
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mDownloadBtn.setText("下载完成");
            mStatus.setText(aBoolean ? "下载完成" + mFilePath : "下载失败");
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values != null && values.length > 0) {
                mProgressBar.setProgress(values[0]);
            }
        }
    }

}

