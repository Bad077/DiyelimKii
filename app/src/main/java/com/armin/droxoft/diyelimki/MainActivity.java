package com.armin.droxoft.diyelimki;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private boolean sharedPrefIlkGirisAl() {
        SharedPreferences sharedPreferences = getSharedPreferences("kullaniciverileri" , Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("ilkgiris" , true);

    }

    private void sharedPrefIlkGirisKaydet(boolean b) {
        SharedPreferences sharedPreferences = getSharedPreferences("kullaniciverileri" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("ilkgiris" , b);
        editor.apply();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.girissayfasi);
        girisanimasyonu();
        boolean ilkgiris = sharedPrefIlkGirisAl();
        if(ilkgiris) {
            sorularidatabaseeyukle();
        }
    }

    private void girisanimasyonu() {
        Thread a = new Thread(){
            public void run(){
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
        //Sorulari database e yukleme islemi
        sharedPrefIlkGirisKaydet(false);
        Intent i = new Intent(MainActivity.this,SoruSayfasi.class);
        startActivity(i);
    }
}
