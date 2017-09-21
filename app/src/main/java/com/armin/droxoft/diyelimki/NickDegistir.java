package com.armin.droxoft.diyelimki;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class NickDegistir extends Activity {

    private String sharedPrefIdAl() {
        SharedPreferences sP = getSharedPreferences("kullaniciverileri" , Context.MODE_PRIVATE);
        return sP.getString("userid" , "defaultuserid");
    }

    private void sharedPrefNickKaydet(String nick) {
        SharedPreferences sP = getSharedPreferences("kullaniciverileri" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.putString("nick" , nick);
        editor.apply();
    }

    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.nickdegistir);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        final TextView tv1 = (TextView) findViewById(R.id.textView10);
        final EditText et = (EditText) findViewById(R.id.editText4);
        ImageButton btn = (ImageButton) findViewById(R.id.butontamam);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nick = et.getText().toString();
                sharedPrefNickKaydet(nick);
                String id = sharedPrefIdAl();
                ServerNickKaydet sNK = new ServerNickKaydet(nick,id);
                sNK.execute(id);
                finish();
            }
        });
        et.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String nick = et.getText().toString();
                    sharedPrefNickKaydet(nick);
                    String id = sharedPrefIdAl();
                    ServerNickKaydet sNK = new ServerNickKaydet(nick,id);
                    sNK.execute(id);
                    finish();
                    return true;
                }
                return false;
            }
        });
        et.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int uzunluk = s.length();
                tv1.setText(String.valueOf(15-uzunluk));
            }

            public void afterTextChanged(Editable s) {

            }
        });
    }

    private class ServerNickKaydet extends AsyncTask<String,Void,String> {
        String charset,query,kullaniciadi,id;

        private ServerNickKaydet(String kullaniciadi , String id){
            this.kullaniciadi = kullaniciadi;
            this.id = id;
            charset = "UTF-8";
            String param1 = "userid";
            String param2 = "nick";
            try {
                query = String.format("param1=%s&param2=%s" ,URLEncoder.encode(param1, charset) , URLEncoder.encode(param2,charset));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL("http://185.22.187.17/diyelimki/nickdegistir.php?id="+id+"&nick="+kullaniciadi).openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            connection.setRequestProperty("Accept", "* /*");
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            try {
                OutputStream output = new BufferedOutputStream(connection.getOutputStream());
                output.write(query.getBytes(charset));
                output.close();
                InputStream is = connection.getInputStream();
            } catch (IOException exception) {

            }
            return "palaba";
        }
    }
}
