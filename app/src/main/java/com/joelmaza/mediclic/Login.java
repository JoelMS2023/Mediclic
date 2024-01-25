package com.joelmaza.mediclic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    Button btn_ingresar;
    TextView TengounacuentaTXT;
    Toolbar toolbar;
    EditText  editText_email, editText_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TengounacuentaTXT=(TextView) findViewById(R.id.TengounacuentaTXT);




        toolbar.setOnClickListener(view -> {
            finish();

        });




        TengounacuentaTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registro.class));

            }
        });












    }
}