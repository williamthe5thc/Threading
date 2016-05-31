package com.example.jordan.threading;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProgressBar mProgress;
    private final Handler mHandler = new Handler();
    private ListView listView;
    private ArrayList<String> numberS;
    private ArrayAdapter<String> adapter;
    private int count;
    String number;
    private Thread _loadThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        listView = (ListView) findViewById(R.id.ListView);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numberS = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.ListView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, numberS);
        listView.setAdapter(adapter);
    }

    public void create_btn(View v) {
        new Thread(new Runnable() {
            public void run() {

                listView = (ListView) findViewById(R.id.ListView);
                final String fileName = "numbers.txt";
                final String fileContents = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10";
                final FileOutputStream[] outputStream = new FileOutputStream[1];
                mProgress = (ProgressBar) findViewById(R.id.progressBar);

                try {
                    mHandler.post(new Runnable() {
                        public void run() {
                            mProgress.setProgress(0);
                        }
                    });
                    outputStream[0] = openFileOutput(fileName, Context.MODE_PRIVATE);
                    outputStream[0].write(fileContents.getBytes());
                    mHandler.post(new Runnable() {
                        public void run() {
                            mProgress.setProgress(100);
                        }
                    });
                    outputStream[0].close();
                    Thread.sleep(250);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void load_btnTest(View v) {
        numberS = new ArrayList<String>();
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, numberS);
        listView = (ListView) findViewById(R.id.ListView);

        count = 0;
        try {
            FileInputStream inputStream = openFileInput("numbers.txt");
            InputStreamReader inputreader = new InputStreamReader(inputStream);
            final BufferedReader bufferedReader = new BufferedReader(inputreader);

            new Thread(new Runnable() {

                public void run() {
                    try {
                        while (((number = bufferedReader.readLine()) != null)) {
                            try {
                                _loadThread.sleep(250);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            numberS.add(number);
                            count += 10;

                            mHandler.post(new Runnable() {
                                public void run() {
                                    mProgress.setProgress(count);
                                    listView.setAdapter(adapter);
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void clear_btn(View v) {
        listView = (ListView) findViewById(R.id.ListView);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
    }
}