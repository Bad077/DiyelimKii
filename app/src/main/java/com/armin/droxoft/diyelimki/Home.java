package com.armin.droxoft.diyelimki;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;
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

public class Home extends Activity {

    TextView textviewyesyuzdesi;
    TextView textviewuyumlulukyuzdesi;

    private String sharedPrefIdAl() {
        SharedPreferences sharedPreferences = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userid", "defaultuserid");
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.home);
        tanimlar();
        tanimlarSoruOlusturma();
        tanimlarIstatistikBolumu();
    }

    private void tanimlar() {
        ImageButton geributonu = (ImageButton) findViewById(R.id.button3);
        geributonu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Home.this, SoruSayfasi.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slideinright, R.anim.slideoutleft);
            }
        });
        TabHost thost = (TabHost) findViewById(R.id.tabHost);
        TabHost.TabSpec tspec1, tspec2;
        thost.setup();
        tspec1 = thost.newTabSpec("Profil");
        Drawable d = ContextCompat.getDrawable(this, R.mipmap.profil_ttab);
        tspec1.setIndicator("", d);
        tspec1.setContent(R.id.tab3);
        thost.addTab(tspec1);
        tspec2 = thost.newTabSpec("Soru Yaz");
        Drawable d2 = ContextCompat.getDrawable(this, R.mipmap.soruyaz_tab);
        tspec2.setIndicator("", d2);
        tspec2.setContent(R.id.tab2);
        thost.addTab(tspec2);
//        tspec1 = thost.newTabSpec("İstatistikler");
//        tspec1.setIndicator("İstatistikler");
//        tspec1.setContent(R.id.tab3);
//        thost.addTab(tspec1);
//        tspec2 = thost.newTabSpec("Soru Oluştur");
//        tspec2.setIndicator("Soru Oluştur");
//        tspec2.setContent(R.id.tab2);
//        thost.addTab(tspec2);
        thost.setOnTabChangedListener(new AnimatedTabHostListener(thost));

    }

    private void tanimlarSoruOlusturma() {
        final EditText editTextWhatIf = (EditText) findViewById(R.id.editText);
        final EditText editTextBut = (EditText) findViewById(R.id.editText2);
        Button buttonSoruyuGonder = (Button) findViewById(R.id.button);
        final String userid = sharedPrefIdAl();
        buttonSoruyuGonder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!editTextWhatIf.getText().toString().equals("") && !editTextBut.getText().toString().equals("")&&
                        editTextWhatIf.getText().toString().length()>4 && editTextBut.getText().toString().length()>4){
                    ServerSoruyuGonder sSG = new ServerSoruyuGonder(editTextWhatIf.getText().toString(), editTextBut.getText().toString(), userid);
                    sSG.execute();
                }if(editTextWhatIf.getText().toString().length()<5){
                    //
                    Log.i("tago" , "whatifi boş gecme");
                }if(editTextBut.getText().toString().length()<5){
                    //
                    Log.i("tago" , "resultı bos gecme");
                }
            }
        });
    }

    private void tanimlarIstatistikBolumu() {
        String userid = sharedPrefIdAl();
        textviewyesyuzdesi = (TextView) findViewById(R.id.textView12);
        textviewuyumlulukyuzdesi = (TextView) findViewById(R.id.textView14);
        ServerIstatistikCek sIR = new ServerIstatistikCek(userid);
        sIR.execute();

    }

    private class ServerSoruyuGonder extends AsyncTask<String, Void, String> {
        String whatif, result, userid;
        String charset, query;

        public ServerSoruyuGonder(String whatif, String result, String userid) {
            this.whatif = whatif;
            this.result = result;
            this.userid = userid;
            charset = "UTF-8";
            String param1 = "whatif";
            String param2 = "result";
            String param3 = "userid";
            try {
                query = String.format("param1=%s&param2=%s&param3=%s", URLEncoder.encode(param1, charset),
                        URLEncoder.encode(param2, charset), URLEncoder.encode(param3, charset));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        protected String doInBackground(String... params) {
            URLConnection connection = null;
            try {
                connection = new URL("http://185.22.187.60/diyelimki/kullanicidansoru.php?whatif=" + URLEncoder.encode(whatif, charset) +
                        "&result=" + URLEncoder.encode(result, charset) + "&userid=" + URLEncoder.encode(userid, charset)).openConnection();
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
            } catch (IOException exception) {

            }
            return "haha";
        }
    }

    private class ServerIstatistikCek extends AsyncTask<String, Void, String> {

        String userid,charset,param1,query;
        String totalyes,totalno,agree,disagree;
        public ServerIstatistikCek(String userid) {
            this.userid = userid;
            charset = "UTF-8";
            param1 = "userid";
            try {
                query = String.format("param1=%s", URLEncoder.encode(param1, charset));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }


        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL("http://185.22.187.60/diyelimki/userstat.php?id=" + userid).openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setDoInput(true);
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
                    String inputline = in.readLine();
                    JSONArray jsono = new JSONArray(inputline);
                    JSONObject jsonObject = jsono.getJSONObject(0);
                    totalyes = jsonObject.optString("totalyes");
                    totalno = jsonObject.optString("totalno");
                    agree = jsonObject.optString("agree");
                    disagree = jsonObject.optString("disagree");
                    Log.i("tago" , totalyes + " " + totalno + " " + agree + " " + disagree);
                }
            }catch (IOException exception){
                exception.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "bolunduduygularımuykularım";
        }

        protected void onPostExecute(String s) {
            int yesyuzdesi =(100*Integer.valueOf(totalyes))/(Integer.valueOf(totalyes)+Integer.valueOf(totalno));
            int uyumyuzdesi = (100*Integer.valueOf(agree))/(Integer.valueOf(agree)+Integer.valueOf(disagree));
            textviewyesyuzdesi.setText(String.valueOf(yesyuzdesi));
            textviewuyumlulukyuzdesi.setText(String.valueOf(uyumyuzdesi));
        }
    }

}
