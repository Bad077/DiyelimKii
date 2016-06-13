package com.armin.droxoft.diyelimki;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;


public class SoruSayfasi extends Activity {

    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.sorusayfasi);
        tanimlar();
    }

    private void tanimlar() {
        final Animation ButtonAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim);
        final Button evetButton = (Button) findViewById(R.id.bEvet);
        evetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tago", "bevet");
                evetButton.startAnimation(ButtonAnim);
            }
        });
        final Animation ButtonAnim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim);
        final Button hayirButton = (Button) findViewById(R.id.bHayir);
        hayirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tago", "bhayir");
                hayirButton.startAnimation(ButtonAnim2);
            }
        });
        ImageButton homebutton = (ImageButton) findViewById(R.id.imageButton);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SoruSayfasi.this,Home.class);
                startActivity(i);
                overridePendingTransition(R.anim.slideinleft, R.anim.slideoutright);
            }
        });
    }
}
