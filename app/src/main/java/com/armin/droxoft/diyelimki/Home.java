package com.armin.droxoft.diyelimki;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;

public class Home extends Activity {

    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.home);
        tanimlar();
    }

    private void tanimlar() {
        Button geributonu = (Button) findViewById(R.id.button3);
        geributonu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Home.this, SoruSayfasi.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slideinright,R.anim.slideoutleft);
            }
        });
        TabHost thost = (TabHost) findViewById(R.id.tabHost);
        TabHost.TabSpec tspec1 , tspec2, tspec3;
        thost.setup();
        tspec1 = thost.newTabSpec("Kategori");
        tspec1.setIndicator("Kategori");
        tspec1.setContent(R.id.tab1);
        thost.addTab(tspec1);
        tspec2 = thost.newTabSpec("Soru Oluştur");
        tspec2.setIndicator("Soru Oluştur");
        tspec2.setContent(R.id.tab2);
        thost.addTab(tspec2);
        tspec3 = thost.newTabSpec("Istatistikler");
        tspec3.setIndicator("Istatistikler");
        tspec3.setContent(R.id.tab3);
        thost.addTab(tspec3);
        thost.setOnTabChangedListener(new AnimatedTabHostListener(thost));
        ImageButton kategorigenel = (ImageButton) findViewById(R.id.imageButton2);
        kategorigenel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Home.this,SoruSayfasi.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slideinright,R.anim.slideoutleft);
            }
        });
        ImageButton kategoriaa = (ImageButton) findViewById(R.id.imageButton3);
        kategorigenel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Home.this,SoruSayfasi.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slideinright,R.anim.slideoutleft);
            }
        });
        ImageButton kategoribb = (ImageButton) findViewById(R.id.imageButton4);
        kategorigenel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Home.this,SoruSayfasi.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slideinright,R.anim.slideoutleft);
            }
        });
        ImageButton kategoricc = (ImageButton) findViewById(R.id.imageButton5);
        kategorigenel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Home.this,SoruSayfasi.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slideinright,R.anim.slideoutleft);
            }
        });
        ImageButton kategoridd = (ImageButton) findViewById(R.id.imageButton6);
        kategorigenel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Home.this,SoruSayfasi.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slideinright,R.anim.slideoutleft);
            }
        });
        ImageButton kategoriee = (ImageButton) findViewById(R.id.imageButton7);
        kategorigenel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Home.this,SoruSayfasi.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slideinright,R.anim.slideoutleft);
            }
        });
    }
}
