package com.armin.droxoft.diyelimki;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Home extends Activity {


    private String sharedPrefIdAl(){
        SharedPreferences sharedPreferences = getSharedPreferences("kullaniciverileri" , Context.MODE_PRIVATE);
        return sharedPreferences.getString("userid" , "defaultuserid");
    }
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.home);
        tanimlar();
        tanimlarSoruOlusturma();
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

    private void tanimlarSoruOlusturma(){
        final EditText editTextWhatIf = (EditText) findViewById(R.id.editText);
        final EditText editTextBut = (EditText) findViewById(R.id.editText2);
        Button buttonSoruyuGonder = (Button) findViewById(R.id.button);
        buttonSoruyuGonder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!editTextWhatIf.getText().toString().equals("")&&!editTextBut.getText().toString().equals("")){
                    ServerSoruyuGonder sSG = new ServerSoruyuGonder(editTextWhatIf.getText().toString(),
                            editTextBut.getText().toString(),"1",sharedPrefIdAl());
                    sSG.execute();
                }
            }
        });
    }

    private class ServerSoruyuGonder extends AsyncTask<String,Void,String>{
        String whatif,result,userid,kategori;
        String charset , query;
        public ServerSoruyuGonder(String whatif,String result , String kategori , String userid){
            this.whatif = whatif;
            this.result = result;
            this.kategori = kategori;
            this.userid = userid;
            charset = "UTF-8";
            String param1 = "whatif";
            String param2 = "result";
            String param3 = "kategori";
            String param4 = "userid";
            try {
                query = String.format("param1=%s&param2=%s&param3=%s&param4=%s", URLEncoder.encode(param1,charset),
                        URLEncoder.encode(param2,charset), URLEncoder.encode(param3,charset),URLEncoder.encode(param4,charset));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        protected String doInBackground(String... params) {
            URLConnection connection = null;
            try {
                connection =new URL("http://185.22.187.60/diyelimki/add_question.php?whatif="+whatif+
                        "&result="+result+"&kategori="+kategori+"&userid="+userid).openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            try {
                OutputStream output = new BufferedOutputStream(connection.getOutputStream());
                output.write(query.getBytes(charset));
                output.close();
                InputStream is = connection.getInputStream();
            }catch (IOException exception) {
                Log.i("tago" , "hata var " + exception.getMessage());
            }
            return "haha";
        }
    }
}
