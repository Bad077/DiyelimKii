package com.armin.droxoft.diyelimki;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.girissayfasi);
        kullanicikaydi();
        girisanimasyonu();
        boolean ilkgiris = sharedPrefIlkGirisAl();
        if (true) {
            sorularidatabaseeyukle();
        }
    }

    private void kullanicikaydi() {
        if (sharedPrefUserIdAl().equals("defaultuserid")) {
            ServerKullaniciKaydet sKK = new ServerKullaniciKaydet();
            sKK.execute();
        }
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
        //Sorulari database e yukleme islemi
        sharedPrefIlkGirisKaydet(false);
        ServerSorulariCek sSC = new ServerSorulariCek();
        sSC.execute("3");
    }

    private class ServerKullaniciKaydet extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Log.i("tago", "girdi");
            String charset = "UTF-8";
            String query = null;
            String param1 = "id";
            try {
                query = String.format("param1=%s", URLEncoder.encode(param1, charset));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL("http://185.22.187.60/diyelimki/add_user.php?id=188").openConnection();
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
            String query = null;
            String param1 = "kategori";
            try {
                query = String.format("param1=%s", URLEncoder.encode(param1, charset));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL("http://185.22.187.60/diyelimki/sorugetir.php?kategori=" + params[0]).openConnection();
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
                Log.i("tago", "inputline ");
                OutputStream output = new BufferedOutputStream(connection.getOutputStream());
                output.write(query.getBytes(charset));
                output.close();
                BufferedReader in;
                if (connection.getResponseCode() == 200) {
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputline = in.readLine();
                    JSONArray jsonArray = new JSONArray(inputline);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int soruid = jsonObject.optInt("id");
                        String whatif = jsonObject.optString("whatif");
                        String result = jsonObject.optString("result");
                        String kategori = jsonObject.optString("kategori");
                        int yes = jsonObject.optInt("yes");
                        int no = jsonObject.optInt("no");
                        String userid = jsonObject.optString("userid");
                    }
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
