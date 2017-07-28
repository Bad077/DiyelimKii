package com.armin.droxoft.diyelimki;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

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
import java.util.Calendar;
import java.util.List;


public class SoruSayfasi extends Activity implements RewardedVideoAdListener {

    private String sharedPrefUserIdAl() {
        SharedPreferences sharedPreferences = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userid", "defaultuserid");
    }

    private void sharedPrefKullaniciKacinciSorudaKaydet(String soruid){
        SharedPreferences sharedPreferences = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("kacincisoruda", soruid);
        editor.apply();
    }

    private String sharedPrefKullaniciKacinciSorudaAl(){
        SharedPreferences sharedPreferences = getSharedPreferences("kullaniciverileri" , Context.MODE_PRIVATE);
        return sharedPreferences.getString("kacincisoruda" , "0");
    }


    int soruSirasi ;
    int soruHakki = 11;
    TextView textWhatif , textResult , textKalansoru;
    List<String> rowidler , soruidler ,whatifler , resultlar , yesler , nolar , soranuseridler;
    private RewardedVideoAd reklamObjesi;

    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.sorusayfasi);
        reklamKurulumu();
        databasedenSorulariAl();
        tanimlar();
        soruHakkiSistemi();
        sonrakisoru();
    }

    private void reklamKurulumu(){
        reklamObjesi = MobileAds.getRewardedVideoAdInstance(this);
        reklamObjesi.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
    }

    private void databasedenSorulariAl(){
        soruSirasi = Integer.valueOf(sharedPrefKullaniciKacinciSorudaAl())-1;
        DatabaseClassSorular dB = new DatabaseClassSorular(this);
        dB.open();
        rowidler = dB.databasedenrowidcek();
        soruidler = dB.databasedensoruidcek();
        whatifler = dB.databasedenwhatifcek();
        resultlar = dB.databasedenresultcek();
        yesler = dB.databasedenyescek();
        nolar = dB.databasedennocek();
        soranuseridler = dB.databasedenuseridcek();
        dB.close();

    }

    private void tanimlar() {
        ImageButton coinbutton = (ImageButton) findViewById(R.id.coinButton);
        coinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(SoruSayfasi.this);
                dialog.setContentView(R.layout.coindialog);
                dialog.setTitle("Başlık");
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Android custom dialog example!");
                Button buttonReklam = (Button) dialog.findViewById(R.id.buttonReklam);
                buttonReklam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (reklamObjesi.isLoaded()) {
                            reklamObjesi.show();
                        }
                    }
                });

                dialog.show();
            }
        });
        ImageButton homebutton = (ImageButton) findViewById(R.id.imageButton);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SoruSayfasi.this,Home.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slideinleft, R.anim.slideoutright);
            }
        });
        textWhatif = (TextView) findViewById(R.id.textWhatif);
        textResult = (TextView) findViewById(R.id.textResult);
        textKalansoru = (TextView) findViewById(R.id.textkalansoru);
        final Animation ButtonAnim_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim_out);
        final Animation ButtonAnim_out_late = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim__out_late);
        final Animation ButtonAnim_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim_in);
        final RelativeLayout LayEvetHayir = (RelativeLayout) findViewById(R.id.LayEvetHayir);
        final RelativeLayout LayStat = (RelativeLayout) findViewById(R.id.layIstatistik);
        final ImageButton evetButton = (ImageButton) findViewById(R.id.bEvet);
        final ImageButton hayirButton = (ImageButton) findViewById(R.id.bHayir);
        final ImageButton StatButton = (ImageButton) findViewById(R.id.bEvet2);
        final ImageButton ShareButton = (ImageButton) findViewById(R.id.bHayir2);
        evetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tago", "bevet");
                evetButton.startAnimation(ButtonAnim_out);
                hayirButton.startAnimation(ButtonAnim_out_late);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LayEvetHayir.setVisibility(View.INVISIBLE);
                        LayStat.setVisibility(View.VISIBLE);
                        StatButton.startAnimation(ButtonAnim_in);
                        ShareButton.startAnimation(ButtonAnim_in);

                    }
                }, 800);

                String soruid = soruidler.get(soruSirasi-1);
                soruEvetCevaplandi(soruid);

            }
        });

        hayirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tago", "bhayir");
                hayirButton.startAnimation(ButtonAnim_out);
                evetButton.startAnimation(ButtonAnim_out_late);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LayEvetHayir.setVisibility(View.INVISIBLE);
                        LayStat.setVisibility(View.VISIBLE);
                        StatButton.startAnimation(ButtonAnim_in);
                        ShareButton.startAnimation(ButtonAnim_in);

                    }
                }, 800);

                String soruid = soruidler.get(soruSirasi-1);
                soruHayirCevaplandi(soruid);
            }
        });
        StatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatButton.startAnimation(ButtonAnim_out);
                ShareButton.startAnimation(ButtonAnim_out_late);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LayStat.setVisibility(View.INVISIBLE);
                        LayEvetHayir.setVisibility(View.VISIBLE);
                        evetButton.startAnimation(ButtonAnim_in);
                        hayirButton.startAnimation(ButtonAnim_in);

                    }
                }, 800);

                sonrakisoru();
            }
        });
        ShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareButton.startAnimation(ButtonAnim_out);
                StatButton.startAnimation(ButtonAnim_out_late);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LayStat.setVisibility(View.INVISIBLE);
                        LayEvetHayir.setVisibility(View.VISIBLE);
                        evetButton.startAnimation(ButtonAnim_in);
                        hayirButton.startAnimation(ButtonAnim_in);

                    }
                }, 800);
            }
        });
    }

    private void soruHakkiSistemi(){
        textKalansoru.setText(String.valueOf(soruHakki));

    }

    private void soruEvetCevaplandi(String soruid) {
        String uyum;
        int yessayisi = Integer.valueOf(yesler.get(soruSirasi-1));
        int nosayisi = Integer.valueOf(nolar.get(soruSirasi-1));
        Log.i("tago " ,"yes sayisi " + String.valueOf(yessayisi)+ " no sayisi " +String.valueOf(nosayisi));
        if(yessayisi>nosayisi){
            uyum="1";
        }else{
            uyum = "0";
        }
        ServerEvetCevapVer sECV = new ServerEvetCevapVer(soruid,uyum);
        sECV.execute();
    }

    private void soruHayirCevaplandi(String soruid){
        String uyum;
        int yessayisi = Integer.valueOf(yesler.get(soruSirasi-1));
        int nosayisi = Integer.valueOf(nolar.get(soruSirasi-1));
        Log.i("tago " ,"yes sayisi " + String.valueOf(yessayisi)+ " no sayisi " +String.valueOf(nosayisi));
        if(yessayisi>nosayisi){
            uyum="0";
        }else{
            uyum = "1";
        }
        ServerHayirCevapVer sHCV = new ServerHayirCevapVer(soruid,uyum);
        sHCV.execute();
    }

    private void sonrakisoru(){
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        Log.i("tago" , "seconds " + String.valueOf(seconds));
        soruHakki--;
        soruHakkiSistemi();
        if (soruSirasi == -1) {
            soruSirasi=0;
        }
        String soruid = soruidler.get(soruSirasi);
        String whatif = whatifler.get(soruSirasi);
        String result = resultlar.get(soruSirasi);
        textWhatif.setText(whatif);
        textResult.setText(result);
        sharedPrefKullaniciKacinciSorudaKaydet(soruid);
        soruSirasi++;
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Log.i("tago" , "onRewardedVideoAdLoaded");;
    }
    @Override
    public void onRewardedVideoAdOpened() {
        Log.i("tago" , "onRewardedVideoAdOpened");
    }
    @Override
    public void onRewardedVideoStarted() {
        Log.i("tago" , "onRewardedVideoStarted");
    }
    @Override
    public void onRewardedVideoAdClosed() {
        Log.i("tago" , "onRewardedVideoAdClosed");
    }
    @Override
    public void onRewarded(RewardItem rewardItem) {
        Log.i("tago" , "onRewarded");
    }
    @Override
    public void onRewardedVideoAdLeftApplication() {
        Log.i("tago" , "onRewardedVideoAdLeftApplication");
    }
    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Log.i("tago" , "onRewardedVideoAdFailedToLoad");
    }
    @Override
    public void onResume() {
        reklamObjesi.resume(this);
        super.onResume();
    }
    @Override
    public void onPause() {
        reklamObjesi.pause(this);
        super.onPause();
    }
    @Override
    public void onDestroy() {
        reklamObjesi.destroy(this);
        super.onDestroy();
    }

    private void loadRewardedVideoAd() {
        reklamObjesi.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
    }

    private class ServerEvetCevapVer extends AsyncTask<String, Void, String> {

        String charset,query;
        String userid = sharedPrefUserIdAl();
        String soruid,uyum;

        public ServerEvetCevapVer(String soruid,String uyum){
            charset = "UTF-8";
            this.soruid = soruid;
            this.uyum = uyum;
            String param1 = "soruid";
            String param2 = "userid";
            String param3 = "status";
            try {
                query = String.format("param1=%s&param2=%s&param3=%s" , URLEncoder.encode(param1, charset) , URLEncoder.encode(param2,charset)
                        , URLEncoder.encode(param3,charset));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            URLConnection connection = null;
            try {
                connection = new URL("http://185.22.187.60/diyelimki/yes.php?id="+soruid+"&userid="+userid+"&status="+uyum).openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            connection.setRequestProperty("Accept", "* /*");
            try {
                OutputStream output = new BufferedOutputStream(connection.getOutputStream());
                output.write(query.getBytes(charset));
                output.close();
                InputStream is = connection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "zamama";
        }
    }

    private class ServerHayirCevapVer extends AsyncTask<String, Void, String> {

        String charset,query;
        String userid = sharedPrefUserIdAl();
        String soruid,uyum;

        public ServerHayirCevapVer(String soruid , String uyum){
            charset = "UTF-8";
            this.soruid = soruid;
            this.uyum = uyum;
            String param1 = "soruid";
            String param2 = "userid";
            String param3 = "status";
            try {
                query = String.format("param1=%s&param2=%s&param3=%s" , URLEncoder.encode(param1, charset) , URLEncoder.encode(param2,charset)
                        , URLEncoder.encode(param3,charset));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            URLConnection connection = null;
            try {
                connection = new URL("http://185.22.187.60/diyelimki/no.php?id=" + soruid + "&userid=" + userid + "&status="+uyum).openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            connection.setRequestProperty("Accept", "* /*");
            try {
                OutputStream output = new BufferedOutputStream(connection.getOutputStream());
                output.write(query.getBytes(charset));
                output.close();
                InputStream is = connection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "zamama";
        }
    }
}
