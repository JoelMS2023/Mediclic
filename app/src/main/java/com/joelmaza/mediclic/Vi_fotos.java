package com.joelmaza.mediclic;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.github.chrisbanes.photoview.PhotoView;
import com.bumptech.glide.Glide;

import java.util.Objects;

public class Vi_fotos extends AppCompatActivity {
    PhotoView photoView;
    String link, titulo;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vi_fotos);

        PhotoView photoView = (PhotoView) findViewById(R.id.imagen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String link = Objects.requireNonNull(getIntent().getExtras()).getString("url");
        Glide.with(this).load(link).into(photoView);
        toolbar.setOnClickListener(view -> finish());

    }
}