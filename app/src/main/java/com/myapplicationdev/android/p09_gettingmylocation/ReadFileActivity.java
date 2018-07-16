package com.myapplicationdev.android.p09_gettingmylocation;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadFileActivity extends AppCompatActivity {

    ListView lv;
    Button btnRefresh;
    TextView tvNum;
    ArrayAdapter<String> aa;
    ArrayList<String> al = new ArrayList<String>();
    String folderLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_file);
        lv = findViewById(R.id.lv);
        btnRefresh = findViewById(R.id.btnRefresh);
        tvNum = findViewById(R.id.tvNum);

        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al);
        lv.setAdapter(aa);
        read();
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read();
            }
        });
    }

    private void read() {
        //Code for file reading
        folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFolder";
        File targetFile = new File(folderLocation, "data.txt");

        if (targetFile.exists() == true) {
            al.clear();
            try {
                FileReader reader = new FileReader(targetFile);
                BufferedReader br = new BufferedReader(reader);

                String line = br.readLine();
                while (line != null) {
                    al.add(line+"");
                    line = br.readLine();
                }
                br.close();
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Toast.makeText(ReadFileActivity.this, "Failed to read!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            tvNum.setText("Number of records:"+al.size());
            aa.notifyDataSetChanged();
        }
    }
}
