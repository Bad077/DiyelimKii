package com.armin.droxoft.diyelimki;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Home extends Activity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private String sharedPrefIdAl() {
        SharedPreferences sharedPreferences = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userid", "defaultuserid");
    }

    private String sharedPrefNickAl(){
        SharedPreferences sharedPreferences = getSharedPreferences("kullaniciverileri" , Context.MODE_PRIVATE);
        return sharedPreferences.getString("nick" , "defaultnick");
    }


    TextView textviewEvetOrani, textviewHayirOrani, textviewEvetSayisi , textviewHayirSayisi,textviewuyumlulukyuzdesi,textviewNick ;
    ImageButton buttonNickDegistir;
    HazırlananSoruAdapter hazırlananSoruAdapter;
    ArrayList<HazirlananSoru> hazirlananSoruArrayList;
    ListView hazirlanansorularlistview;

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
        final RelativeLayout relativelayoutyazi = (RelativeLayout) findViewById(R.id.relativelayoutyazi);
        final EditText editTextyazi = (EditText) findViewById(R.id.edittextyazi);
        Button buttonYaziyiOnayla = (Button) findViewById(R.id.buttonyaziyionayla);
        buttonYaziyiOnayla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View vview = Home.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(vview.getWindowToken(), 0);
                }
                String hangiedittext = String.valueOf(editTextyazi.getTag());
                if(hangiedittext.equals("whatif")){
                    editTextWhatIf.setText(editTextyazi.getText());
                    relativelayoutyazi.setVisibility(View.INVISIBLE);
                }else if(hangiedittext.equals("result")){
                    editTextBut.setText(editTextyazi.getText());
                    relativelayoutyazi.setVisibility(View.INVISIBLE);
                }
            }
        });
        editTextWhatIf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativelayoutyazi.setVisibility(View.VISIBLE);
                editTextyazi.setTag("whatif");
                relativelayoutyazi.setY(400);
            }
        });
        editTextBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativelayoutyazi.setVisibility(View.VISIBLE);
                editTextyazi.setTag("result");
            }
        });
        ImageButton buttonSoruyuGonder = (ImageButton) findViewById(R.id.gonder_button);
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
        hazirlanansorularlistview = (ListView) findViewById(R.id.listviewyazdiginsorular);
        textviewEvetOrani = (TextView) findViewById(R.id.textviewevetorani);
        textviewHayirOrani = (TextView) findViewById(R.id.textviewhayirorani);
        textviewEvetSayisi = (TextView) findViewById(R.id.textviewevetsayisi);
        textviewHayirSayisi = (TextView) findViewById(R.id.textviewhayirsayisi);
        textviewuyumlulukyuzdesi = (TextView) findViewById(R.id.textviewuyumlulukorani);
        textviewNick = (TextView) findViewById(R.id.textviewnick);
        String nick = sharedPrefNickAl();
        textviewNick.setText(nick);
        buttonNickDegistir = (ImageButton) findViewById(R.id.buttonnickdegistir);
        buttonNickDegistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, NickDegistir.class);
                startActivity(i);
            }
        });
        ServerIstatistikCek sIR = new ServerIstatistikCek(userid);
        sIR.execute();
        ServerHazirlananSorulariCek sHSC = new ServerHazirlananSorulariCek(userid);
        sHSC.execute();

    }

    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();
        String nick = sharedPrefNickAl();
        textviewNick.setText(nick);
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
                connection = new URL("http://185.22.187.17/diyelimki/kullanicidansoru.php?whatif=" + URLEncoder.encode(whatif, charset) +
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
                connection = (HttpURLConnection) new URL("http://185.22.187.17/diyelimki/userstat.php?id=" + userid).openConnection();
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
            textviewEvetSayisi.setText(totalyes);
            textviewHayirSayisi.setText(totalno);
            if(Integer.valueOf(totalyes)==0 && Integer.valueOf(totalno)==0){
                textviewEvetOrani.setText(String.valueOf(0));
                textviewHayirOrani.setText(String.valueOf(0));
            }else if(Integer.valueOf(totalyes)==0){
                textviewEvetOrani.setText(String.valueOf(0));
                textviewHayirOrani.setText(String.valueOf(100));
            }else if(Integer.valueOf(totalno)==0){
                textviewEvetOrani.setText(String.valueOf(100));
                textviewHayirOrani.setText(String.valueOf(0));
            }else{
                int yesyuzdesi =(100*Integer.valueOf(totalyes))/(Integer.valueOf(totalyes)+Integer.valueOf(totalno));
                textviewEvetOrani.setText(String.valueOf(yesyuzdesi));
                int hayiryudesi = (100*Integer.valueOf(totalno))/(Integer.valueOf(totalyes)+Integer.valueOf(totalno));
                textviewHayirOrani.setText(String.valueOf(hayiryudesi));
            }
            if(Integer.valueOf(agree)==0 && Integer.valueOf(disagree)==0){
                textviewuyumlulukyuzdesi.setText(String.valueOf(0));
            }else if(Integer.valueOf(agree)==0){
                textviewuyumlulukyuzdesi.setText(String.valueOf(0));
            }else if(Integer.valueOf(disagree)==0){
                textviewuyumlulukyuzdesi.setText(String.valueOf(100));
            }else{
                int uyumyuzdesi = (100*Integer.valueOf(agree))/(Integer.valueOf(agree)+Integer.valueOf(disagree));
                textviewuyumlulukyuzdesi.setText(String.valueOf(uyumyuzdesi));
            }
        }
    }

    private class ServerHazirlananSorulariCek extends AsyncTask<String,Void,String>{
        String userid,charset,param1,query;
        String id,whatif,result,yes,no;

        public ServerHazirlananSorulariCek(String userid) {
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
                connection = (HttpURLConnection) new URL("http://185.22.187.17/diyelimki/hazirlanansorularicek.php?id=" + userid).openConnection();
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
                    hazirlananSoruArrayList = new ArrayList<>();
                    JSONArray jsono = new JSONArray(inputline);
                    for(int i = 0 ; i<jsono.length(); i++){
                        JSONObject jsonObject = jsono.getJSONObject(i);
                        HazirlananSoru hazirlananSoru = new HazirlananSoru();
                        hazirlananSoru.setId(jsonObject.optString("id"));
                        hazirlananSoru.setWhatif(jsonObject.optString("whatif"));
                        hazirlananSoru.setResult(jsonObject.optString("result"));
                        hazirlananSoru.setEvetsayisi(jsonObject.optString("yes"));
                        hazirlananSoru.setHayirsayisi(jsonObject.optString("no"));
                        hazirlananSoru.setToplamcevap(String.valueOf(Integer.valueOf(jsonObject.optString("yes"))+Integer.valueOf(jsonObject.optString("no"))));
                        hazirlananSoruArrayList.add(hazirlananSoru);
                        Log.i("tago" , jsonObject.optString("id") + " " + jsonObject.optString("whatif") + " " + jsonObject.optString("result") + " " + jsonObject.optString("yes") + " " + jsonObject.optString("no"));
                    }
                }
            }catch (IOException exception){
                exception.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "bolunduduygularımuykularım";
        }

        protected void onPostExecute(String s) {
            hazırlananSoruAdapter = new HazırlananSoruAdapter(Home.this, R.layout.hazirlanansoru, hazirlananSoruArrayList);
            hazirlanansorularlistview.setAdapter(hazırlananSoruAdapter);
        }
    }
}
