package com.maria.rayo.radmap;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.R.*;

public class Inicio extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
                Intent intent = new Intent(Inicio.this,MapsActivity.class);
                startActivity(intent);

            }
    }

