package com.joelmaza.mediclic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btn_ingresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_ingresar= (Button) findViewById(R.id.btn_ingresar);



        btn_ingresar.setOnClickListener(view -> {

            Intent i = new Intent();
            i.setClass(this,Login.class);
            startActivity(i);

        });
    }
}