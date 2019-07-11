package com.example.asyntaskpractice;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NewAsync extends AppCompatActivity {

    private TextView textView;
    Button button;
    private Handler handler;
    private int x=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_async);

        handler=new Handler();
        textView=findViewById(R.id.new_textview);
        button=findViewById(R.id.new_button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0;i<10;i++){
                            try {
                                x=i;
                                Thread.sleep(1000);
//                                handler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                        textView.setText(""+x);
//                                    }
//                                });
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setText("done");
                                    }
                                },10000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(NewAsync.this, "hi", Toast.LENGTH_SHORT).show();
                                        textView.setText(""+x);
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                thread.start();
            }
        });
    }


}
