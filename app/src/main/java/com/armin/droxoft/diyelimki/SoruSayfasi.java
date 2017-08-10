package com.armin.droxoft.diyelimki;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ClipDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAd;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class SoruSayfasi extends Activity implements RewardedVideoAdListener {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void sharedPrefDurumKaydet(String durum) {
        SharedPreferences sharedPreferences = getSharedPreferences("kullaniciverileri" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("durum" , durum);
        editor.apply();
    }

    private void sharedPrefKullaniciKacinciSorudaKaydet(String soruid){
        SharedPreferences sharedPreferences = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("kacincisoruda", soruid);
        editor.apply();
    }

    private void sharedPrefCoinKaydet(String coin){
        SharedPreferences sharedPreferences = getSharedPreferences("kullaniciverileri" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("coin" , coin);
        editor.apply();
    }

    private String sharedPrefUserIdAl() {
        SharedPreferences sharedPreferences = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userid", "defaultuserid");
    }

    private String sharedPrefKullaniciKacinciSorudaAl(){
        SharedPreferences sharedPreferences = getSharedPreferences("kullaniciverileri" , Context.MODE_PRIVATE);
        return sharedPreferences.getString("kacincisoruda" , "0");
    }

    private String sharedPrefDurumAl(){
        SharedPreferences sharedPreferences = getSharedPreferences("kullaniciverileri" , Context.MODE_PRIVATE);
        return sharedPreferences.getString("durum" , "defaultdurum");
    }

    private String sharedPrefCoinAl(){
        SharedPreferences sharedPreferences = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        return sharedPreferences.getString("coin" , "defaultcoin");
    }

    int flag;
    int soruSirasi ;
    int soruHakki = 15;
    ImageButton butonSorual , butonReklamizle;
    TextView textWhatif , textResult , textKalanSoru, textviewcoin , textviewCevap , textviewAynifikirde, textviewFarklifikirde,textcoin,textcoinn;
    List<String> rowidler , soruidler ,whatifler , resultlar , yesler , nolar , soranuseridler;
    private RewardedVideoAd reklamObjesi;
    private ClipDrawable clipDrawable;
    public static final int MAX_LEVEL = 10000;
    public static final int LEVEL_DIFF = 100;
    public static final int DELAY = 10 ;
    private int currentlevel = 0;
    private int fromlevel = 0;
    private int tolevel = 0;
    private Handler yukariHandler = new Handler();
    private Runnable animateupimage = new Runnable() {
        public void run() {
            doUpAnimation(fromlevel,tolevel);
        }
    };


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
        textviewcoin = (TextView) findViewById(R.id.textviewcoin);
        textviewcoin.setText(sharedPrefCoinAl());
        ImageView image1= (ImageView) findViewById(R.id.imageView1);
        clipDrawable = (ClipDrawable) image1.getDrawable();
        clipDrawable.setLevel(0);
        ImageButton coinbutton = (ImageButton) findViewById(R.id.coinButton);
        coinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(SoruSayfasi.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.coindialog);
                dialog.setTitle("Başlık");
                textcoin = (TextView) dialog.findViewById(R.id.text);
                textcoin.setText(sharedPrefCoinAl());
                ImageButton buttonReklam = (ImageButton) dialog.findViewById(R.id.buttonReklam);
                buttonReklam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (reklamObjesi.isLoaded()) {
                            flag = 1;
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
        textviewCevap = (TextView) findViewById(R.id.textviewCevap);
        textviewAynifikirde = (TextView) findViewById(R.id.textviewAynifikirde);
        textviewFarklifikirde= (TextView) findViewById(R.id.textviewTersfikirde);
        textKalanSoru = (TextView) findViewById(R.id.texviewkalansoru);
        String bab = "15/15";
        textKalanSoru.setText(bab);
        final Animation ButtonAnim_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim_out);
        final Animation ButtonAnim_out_late = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim__out_late);
        final Animation ButtonAnim_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim_in);
        final RelativeLayout LayEvetHayir = (RelativeLayout) findViewById(R.id.LayEvetHayir);
        final RelativeLayout LayStat = (RelativeLayout) findViewById(R.id.layIstatistik);
        final ImageButton evetButton = (ImageButton) findViewById(R.id.bEvet);
        final ImageButton hayirButton = (ImageButton) findViewById(R.id.bHayir);
        final LinearLayout StatButton = (LinearLayout) findViewById(R.id.bEvet2);
        final ImageButton ShareButton = (ImageButton) findViewById(R.id.bHayir2);
        evetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                istatistikleriCek("evet",soruSirasi);
            }
        });

        hayirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                istatistikleriCek("hayir",soruSirasi);
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

                sonrakisoru();
            }
        });
    }

    private void soruHakkiSistemi(){
        String durum = sharedPrefDurumAl();
        String sonsoruhakki = String.valueOf(15);
        if(!durum.equals("defaultdurum")) {
            if(durum.length()==19){
                sonsoruhakki= durum.substring(18, 19);
            }else if(durum.length()==20){
                sonsoruhakki = durum.substring(18,20);
            }
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
            String date = df.format(Calendar.getInstance().getTime());

            String eskigun = durum.substring(0, 2);
            String eskiay = durum.substring(3, 5);
            String eskiyil = durum.substring(6, 10);
            String eskisaat = durum.substring(12, 14);
            String eskidakika = durum.substring(15, 17);
            String mevcutgun = date.substring(0, 2);
            String mevcutay = date.substring(3, 5);
            String mevcutyil = date.substring(6, 10);
            String mevcutsaat = date.substring(12, 14);
            String mevcutdakika = date.substring(15, 17);
            if (!eskiyil.equals(mevcutyil) || !eskiay.equals(mevcutay)) {
                soruHakki = 15;
            } else if (eskigun.equals(mevcutgun)) {
                soruHakki = Integer.valueOf(sonsoruhakki);
            } else if (Integer.valueOf(mevcutgun) - Integer.valueOf(eskigun) > 1) {
                soruHakki = 15;
            } else {
                int mevcutdeger = (Integer.valueOf(mevcutsaat) * 60) + Integer.valueOf(mevcutdakika);
                int gecmisdeger = (Integer.valueOf(eskisaat) * 60) + Integer.valueOf(eskidakika);
                int fark = mevcutdeger - gecmisdeger;
                if (fark > 1440) {
                    soruHakki = 15;
                } else {
                    soruHakki = Integer.valueOf(sonsoruhakki);
                }

            }

        }
    }

    private void soruEvetCevaplandi(String soruid) {
        String uyum;
        int yessayisi = Integer.valueOf(yesler.get(soruSirasi-1));
        int nosayisi = Integer.valueOf(nolar.get(soruSirasi-1));
        if(yessayisi>nosayisi){
            uyum="1";
        }else{
            uyum = "0";
        }
        ServerEvetCevapVer sECV = new ServerEvetCevapVer(soruid,uyum);
        sECV.execute();
        soruHakki--;
        ilerlemeIslemi(String.valueOf(soruHakki));
    }

    private void soruHayirCevaplandi(String soruid){
        String uyum;
        int yessayisi = Integer.valueOf(yesler.get(soruSirasi-1));
        int nosayisi = Integer.valueOf(nolar.get(soruSirasi-1));
        if(yessayisi>nosayisi){
            uyum="0";
        }else{
            uyum = "1";
        }
        ServerHayirCevapVer sHCV = new ServerHayirCevapVer(soruid,uyum);
        sHCV.execute();
        soruHakki--;
        ilerlemeIslemi(String.valueOf(soruHakki));
    }

    private void sonrakisoru() {
        if (soruHakki < 1) {
            final Dialog dialog = new Dialog(SoruSayfasi.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.hakkinizbitti);
            dialog.setTitle("Başlık");
            textcoinn = (TextView) dialog.findViewById(R.id.textcoinn);
            textcoinn.setText(sharedPrefCoinAl());
            butonSorual = (ImageButton) dialog.findViewById(R.id.buttonReklam2);
            butonReklamizle = (ImageButton) dialog.findViewById(R.id.buttonReklam);
            if(Integer.valueOf(sharedPrefCoinAl())<100){
                butonReklamizle.setVisibility(View.VISIBLE);
                butonSorual.setVisibility(View.INVISIBLE);
            }
            butonReklamizle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reklamObjesi.isLoaded()) {
                        flag = 2;
                        reklamObjesi.show();
                    }
                }
            });
            butonSorual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // SORU ALMA ISLEMI
                    Log.i("tago" , "soru almaya haziriz");
                }
            });
            dialog.show();
        } else {
            if (soruSirasi == -1) {
                soruSirasi = 0;
            }
            String soruid = soruidler.get(soruSirasi);
            String whatif = whatifler.get(soruSirasi);
            String result = resultlar.get(soruSirasi);
            textWhatif.setText(whatif);
            textResult.setText(result);
            sharedPrefKullaniciKacinciSorudaKaydet(soruid);
            soruSirasi++;
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
            String date = df.format(Calendar.getInstance().getTime());
            String durum = date + " " + String.valueOf(soruHakki);
            sharedPrefDurumKaydet(durum);
        }
    }

    private void ilerlemeIslemi(String soruHakki){
        String kalansoru = String.valueOf(soruHakki)+"/15";
        textKalanSoru.setText(kalansoru);
        int temmplevel = ((15-Integer.parseInt(soruHakki))*MAX_LEVEL)/15;
        Log.i("tago" , "ilerleme islemi calistirildi temmplevel " + temmplevel);
        if(tolevel==temmplevel || temmplevel > MAX_LEVEL){
            return;
        }
        tolevel = (temmplevel<=MAX_LEVEL)?temmplevel:tolevel;
        if(tolevel>fromlevel){
            fromlevel = tolevel;
            yukariHandler.post(animateupimage);
        }
    }

    private void istatistikleriCek(String nededi ,int soruSirasi){
        if(nededi.equals("evet")){
            String yessayisi = yesler.get(soruSirasi-1);
            String nosayisi = nolar.get(soruSirasi-1);
            int sizingibidusunen = Integer.valueOf(yessayisi)*100/(Integer.valueOf(yessayisi) + Integer.valueOf(nosayisi));
            int hayirdiyen = 100 - sizingibidusunen;
            textviewCevap.setText("EVET!");
            String xx = "% " + String.valueOf(sizingibidusunen)+ " Sizin gibi düşünüyor";
            textviewAynifikirde.setText(xx);
            String yy ="% " + String.valueOf(hayirdiyen) + " hayır dedi" ;
            textviewFarklifikirde.setText(yy);
        }else{
            String yessayisi = yesler.get(soruSirasi-1);
            String nosayisi = nolar.get(soruSirasi-1);
            int sizingibidusunen = Integer.valueOf(nosayisi)*100/(Integer.valueOf(yessayisi) + Integer.valueOf(nosayisi));
            int evetdiyen = 100 - sizingibidusunen;
            textviewCevap.setText("HAYIR!");
            String xx = "% " + String.valueOf(sizingibidusunen)+ " Sizin gibi düşünüyor";
            textviewAynifikirde.setText(xx);
            String yy ="% " + String.valueOf(evetdiyen) + " evet dedi" ;
            textviewFarklifikirde.setText(yy);
        }
    }

    private void doUpAnimation(int fromlevel, int tolevel) {
        currentlevel = currentlevel + LEVEL_DIFF;
        clipDrawable.setLevel(currentlevel);
        if(currentlevel<=tolevel){
            yukariHandler.postDelayed(animateupimage,DELAY);
        }else {
            yukariHandler.removeCallbacks(animateupimage);
            fromlevel = tolevel;
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
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
        String mevcutcoin = sharedPrefCoinAl();
        String yenicoin = String.valueOf(Integer.valueOf(mevcutcoin)+100);
        sharedPrefCoinKaydet(yenicoin);
        ServerCoinGuncelle sCG = new ServerCoinGuncelle(yenicoin);
        sCG.execute();
        textviewcoin.setText(sharedPrefCoinAl());
        if(flag==1){
            textcoin.setText(sharedPrefCoinAl());
        }else if(flag==2){
            butonSorual.setVisibility(View.VISIBLE);
            butonReklamizle.setVisibility(View.INVISIBLE);
            textcoinn.setText(sharedPrefCoinAl());
        }
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
                connection = new URL("http://185.22.187.17/diyelimki/yes.php?id="+soruid+"&userid="+userid+"&status="+uyum).openConnection();
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
                connection = new URL("http://185.22.187.17/diyelimki/no.php?id=" + soruid + "&userid=" + userid + "&status="+uyum).openConnection();
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

    private class ServerCoinGuncelle extends AsyncTask<String, Void, String>{

        String charset,query,coin;
        String userid = sharedPrefUserIdAl();


        public ServerCoinGuncelle(String coin){
            charset = "UTF-8";
            this.coin = coin;
            String param1 = "userid";
            String param2 = "coin";
            try {
                query = String.format("param1=%s&param2=%s" , URLEncoder.encode(param1, charset) , URLEncoder.encode(param2,charset));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            URLConnection connection = null;
            try {
                connection = new URL("http://185.22.187.17/diyelimki/coinguncelle.php?userid="+userid+"&coin="+coin).openConnection();
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
