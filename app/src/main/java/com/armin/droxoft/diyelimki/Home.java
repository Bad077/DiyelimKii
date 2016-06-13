package com.armin.droxoft.diyelimki;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    }
}
