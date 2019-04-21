package com.example.ravindervissapragada.helpmerecycle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner garbage = (Spinner) findViewById(R.id.trash);
        String[] items = {"plastic","battery","paper","can","bottle","other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        garbage.setAdapter(adapter);
        Button next = (Button) findViewById(R.id.enter);
    }
}
