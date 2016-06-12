package com.armin.droxoft.diyelimki;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
    }
}
