package com.armin.droxoft.diyelimki;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HazırlananSoruAdapter extends ArrayAdapter{

    LayoutInflater layoutInflater;
    Context context;
    int resource;
    ArrayList<HazirlananSoru> hazirlanansorulararraylist;


    public HazırlananSoruAdapter(Context context, int resource, ArrayList<HazirlananSoru> hazirlanansorulararraylist){
        super(context,resource,hazirlanansorulararraylist);
        this.context = context;
        this.resource = resource;
        this.hazirlanansorulararraylist = hazirlanansorulararraylist;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return hazirlanansorulararraylist.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final HazirlananSoruHolder hazirlananSoruHolder;
        Log.i("tago" , "get view pos " + position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(resource, null);
            hazirlananSoruHolder = new HazirlananSoruHolder();
            hazirlananSoruHolder.id = (TextView) convertView.findViewById(R.id.textviewid);
            hazirlananSoruHolder.whatif = (TextView) convertView.findViewById(R.id.textviewwhatiff);
            hazirlananSoruHolder.result = (TextView) convertView.findViewById(R.id.textviewresultt);
            hazirlananSoruHolder.evett = (TextView) convertView.findViewById(R.id.textviewevett);
            hazirlananSoruHolder.hayirr = (TextView) convertView.findViewById(R.id.textviewhayirr);
            hazirlananSoruHolder.toplamm = (TextView) convertView.findViewById(R.id.textviewtoplamm);
            convertView.setTag(hazirlananSoruHolder);
        } else {
            hazirlananSoruHolder = (HazirlananSoruHolder) convertView.getTag();
        }
        String idhash = "#" + hazirlanansorulararraylist.get(position).getId();
        hazirlananSoruHolder.id.setText(idhash);
        Log.i("tago" ,"get viewın içinden " +  hazirlanansorulararraylist.get(position).getWhatif());
        hazirlananSoruHolder.whatif.setText(hazirlanansorulararraylist.get(position).getWhatif());
        hazirlananSoruHolder.result.setText(hazirlanansorulararraylist.get(position).getResult());
        if(Integer.valueOf(hazirlanansorulararraylist.get(position).getToplamcevap()) == 0){
            hazirlananSoruHolder.evett.setText("0");
            hazirlananSoruHolder.hayirr.setText("0");
        }else{
            int evetoran = 100 * Integer.valueOf(hazirlanansorulararraylist.get(position).getEvetsayisi())/
                    Integer.valueOf(hazirlanansorulararraylist.get(position).getToplamcevap());
            String evetorani = "Evet: %"+ String.valueOf(evetoran);
            hazirlananSoruHolder.evett.setText(evetorani);
            int hayiroran = 100 * Integer.valueOf(hazirlanansorulararraylist.get(position).getHayirsayisi())/
                    Integer.valueOf(hazirlanansorulararraylist.get(position).getToplamcevap());
            String hayirorani = "Hayır: %"+ String.valueOf(hayiroran);
            hazirlananSoruHolder.hayirr.setText(hayirorani);
        }
        String toplam = "Toplam: " + hazirlanansorulararraylist.get(position).getToplamcevap();
        hazirlananSoruHolder.toplamm.setText(toplam);
        return convertView;
    }

    static class HazirlananSoruHolder {
        public TextView id, whatif, result,evett,hayirr,toplamm;
    }
}
