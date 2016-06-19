package com.armin.droxoft.diyelimki;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class SoruSayfasi extends Activity {

    int hangisorudasin;
    TextView textviewwhatif , textviewresult ;
    Button evetButton,hayirButton;
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.sorusayfasi);
        tanimlar();
        hangisorudasin = 0;
        soruyugetir(hangisorudasin);
    }

    private void soruyugetir(int a) {
        DatabaseClassSorular dCS = new DatabaseClassSorular(this);
        dCS.open();
        String soruid = dCS.soruidcek(a);
        String whatif = dCS.whatifcek(a);
        String result = dCS.resultcek(a);
        String yes = dCS.yescek(a);
        String no  = dCS.nocek(a);
        dCS.close();
        textviewwhatif.setText(whatif);
        textviewresult.setText(result);
    }

    private void tanimlar() {
        ImageButton homebutton = (ImageButton) findViewById(R.id.imageButton);
        textviewwhatif = (TextView) findViewById(R.id.textView2);
        textviewresult = (TextView) findViewById(R.id.textView4);
        evetButton = (Button) findViewById(R.id.bEvet);
        hayirButton = (Button) findViewById(R.id.bHayir);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SoruSayfasi.this,Home.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slideinleft, R.anim.slideoutright);
            }
        });
        final Animation ButtonAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim);
        evetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evetButton.startAnimation(ButtonAnim);
            }
        });
        final Animation ButtonAnim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim);
        hayirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hayirButton.startAnimation(ButtonAnim2);
            }
        });
    }
}
