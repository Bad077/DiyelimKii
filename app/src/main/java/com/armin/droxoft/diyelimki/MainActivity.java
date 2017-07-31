package com.armin.droxoft.diyelimki;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private boolean sharedPrefIlkGirisAl() {
        SharedPreferences sharedPreferences = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("ilkgiris", true);

    }

    private void sharedPrefUserIdKaydet(String userid) {
        SharedPreferences sharedPreferences = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userid", userid);
        editor.apply();
    }

    private String sharedPrefUserIdAl() {
        SharedPreferences sharedPreferences = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userid", "defaultuserid");
    }

    private void sharedPrefIlkGirisKaydet(boolean b) {
        SharedPreferences sharedPreferences = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("ilkgiris", b);
        editor.apply();
    }

    private void sharedPrefNickKaydet(String nick){
        SharedPreferences sharedPreferences = getSharedPreferences("kullaniciverileri" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nick", nick);
        editor.apply();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileAds.initialize(this, "ca-app-pub-2381885275528561~4035589141");
        if (!sharedPrefUserIdAl().equals("defaultuserid")) {
            //adamın ilk girisi degil ona gore hareket planı
            setContentView(R.layout.gecissayfasi);
            girisanimasyonu();
            sorularidatabaseeyukle();
        } else {
            setContentView(R.layout.girissayfasi);
            ImageButton girisbutonu = (ImageButton) findViewById(R.id.buttongiris);
            girisbutonu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText editTextisim = (EditText) findViewById(R.id.editText3);
                    if (editTextisim.getText().toString().length() < 3) {
                        //alert
                        Log.i("tago", "kullanıcı adı en az 3 harfli olabilir");
                    } else if (editTextisim.getText().toString().length() > 40) {
                        //alert
                        Log.i("tago", "kullanıcı adı en fazla 40 harfli olabilir");
                    } else {
                        String kullaniciadi = editTextisim.getText().toString();
                        sharedPrefNickKaydet(kullaniciadi);
                        kullanicikaydi(kullaniciadi);
                        sorularidatabaseeyukle();
                    }

                }
            });
        }
    }

    private void kullanicikaydi(String kullaniciadi) {
            ServerKullaniciKaydet sKK = new ServerKullaniciKaydet(kullaniciadi);
            sKK.execute();
    }

    private void girisanimasyonu() {
        Thread a = new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Giris Animasyonu
                    }
                });
            }
        };
        a.start();
    }

    private void sorularidatabaseeyukle() {
        ServerSorulariCek sSC = new ServerSorulariCek();
        sSC.execute("3");
    }

    private class ServerKullaniciKaydet extends AsyncTask<String, Void, String> {

        String charset,query,kullaniciadi;
        public ServerKullaniciKaydet(String kullaniciadi){
            this.kullaniciadi = kullaniciadi;
            charset = "UTF-8";
            String param1 = "kullaniciadi";
            try {
                query = String.format("param1=%s" ,URLEncoder.encode(param1, charset));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL("http://185.22.187.60/diyelimki/add_user.php?username="+kullaniciadi).openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            connection.setRequestProperty("Accept", "* /*");
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            try {
                OutputStream output = new BufferedOutputStream(connection.getOutputStream());
                output.write(query.getBytes(charset));
                output.close();
                BufferedReader in;
                if (connection.getResponseCode() == 200) {
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    JSONArray jsonArray = new JSONArray(in.readLine());
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String userid = jsonObject.optString("id");
                    sharedPrefUserIdKaydet(userid);
                }
            } catch (IOException exception) {

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "haha";
        }
    }

    private class ServerSorulariCek extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
            Log.i("tago", "tago");
            String charset = "UTF-8";
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL("http://185.22.187.60/diyelimki/sorugetir.php?").openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            connection.setRequestProperty("Accept", "* /*");
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            try {
                BufferedReader in;
                if (connection.getResponseCode() == 200) {
                    DatabaseClassSorular dCS = new DatabaseClassSorular(MainActivity.this);
                    dCS.open();
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputline = in.readLine();
                    JSONArray jsonArray = new JSONArray(inputline);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int soruid = jsonObject.optInt("id");
                        String whatif = jsonObject.optString("whatif");
                        String result = jsonObject.optString("result");
                        int yes = jsonObject.optInt("yes");
                        int no = jsonObject.optInt("no");
                        String userid = jsonObject.optString("userid");
                        Log.i("tago" , "bosluklu sıra " + soruid + " " + whatif + " " + result + " " + yes + " " + no + " " + userid);
                        dCS.olustur(String.valueOf(soruid),whatif,result,String.valueOf(yes),String.valueOf(no),userid);
                    }
                    dCS.close();
                }
            } catch (IOException exception) {
                Log.i("tago", "inputline IO");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "haha";
        }

        @Override
        protected void onPostExecute(String s) {
            Intent i = new Intent(MainActivity.this, SoruSayfasi.class);
            startActivity(i);
        }

    }
}
