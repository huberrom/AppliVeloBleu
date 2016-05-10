package com.example.romain.velobleu;

/**
 * Created by romain on 28/03/2015.
 */
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by romain on 11/03/2015.
 */
public class VeloAdapter extends BaseAdapter {

    private ArrayList<Item_Velo> mListV;

    //Le contexte dans lequel est présent notre adapter
    private Context mContext;

    //Un mécanisme pour gérer l'affichage graphique depuis un layout XML
    private LayoutInflater mInflater;


    public VeloAdapter(Context context,ArrayList<Item_Velo> aListV) {
        mContext = context;
        mListV = aListV;
        mInflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return mListV.size();
    }

    public Object getItem(int position) {
        return mListV.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout layoutItem;
        //(1) : Réutilisation des layouts
        if (convertView == null) {
            //Initialisation de notre item à partir du  layout XML "personne_layout.xml"
            layoutItem = (RelativeLayout) mInflater.inflate(R.layout.station_layout, parent, false);
        } else {
            layoutItem = (RelativeLayout) convertView;
        }

        //(2) : Récupération des TextView de notre layout
        TextView tv_Station = (TextView)layoutItem.findViewById(R.id.TV_Station_Resume);
        TextView tv_Adresse = (TextView)layoutItem.findViewById(R.id.TV_Adresse);
        TextView tv_Places = (TextView)layoutItem.findViewById(R.id.TV_Places);
        TextView tv_Dispo = (TextView)layoutItem.findViewById(R.id.TV_Dispo);
        TextView tv_Distance = (TextView)layoutItem.findViewById(R.id.TV_Distance);
        RelativeLayout relative = (RelativeLayout) layoutItem.findViewById(R.id.RL);

            //(3) : Renseignement des valeurs
            tv_Station.setText(mListV.get(position).getStation());
            tv_Adresse.setText(mListV.get(position).getAdresse());
            tv_Places.setText(mListV.get(position).getPlaces() + " places");
            tv_Dispo.setText(mListV.get(position).getVelos() + " velos");

        if(MainActivity.latPtbl!=0 && MainActivity.longPtbl!=0) {
            if (mListV.get(position).getDistance() > 1)
                tv_Distance.setText(mListV.get(position).getDistance() + " km");
            else {
                float d = mListV.get(position).getDistance() * 1000;
                tv_Distance.setText((int) d + " m");
            }
        }
        else{
            tv_Distance.setText("Desactivé");
        }

            //(4) Changement de la couleur du fond de notre item
            if (mListV.get(position).getVelos() > 3) {
                tv_Dispo.setTextColor(Color.rgb(0, 204, 0));
            } else {
                if (mListV.get(position).getVelos() == 0)
                    tv_Dispo.setTextColor(Color.RED);
                else
                    tv_Dispo.setTextColor(Color.argb(128, 255, 165, 0));
            }

            if (mListV.get(position).getPlaces() > 3) {
                tv_Places.setTextColor(Color.rgb(0, 204, 0));
            } else {
                if (mListV.get(position).getPlaces() == 0)
                    tv_Places.setTextColor(Color.RED);
                else
                    tv_Places.setTextColor(Color.argb(128, 255, 165, 0));
            }

            if(MainActivity.latPtbl!=0 && MainActivity.longPtbl!=0) {
                if (mListV.get(position).getDistance() * 1000 <= 300) {
                    tv_Distance.setTextColor(Color.rgb(0, 204, 0));
                } else {
                    if (mListV.get(position).getDistance() * 1000 > 800)
                        tv_Distance.setTextColor(Color.RED);
                    else
                        tv_Distance.setTextColor(Color.argb(128, 255, 165, 0));
                }
            }
        else
                tv_Distance.setTextColor(Color.RED);

            relative.setTag(position);
            relative.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Integer position = (Integer) v.getTag();
                    sendListener(mListV.get(position), position);
                }

            });
        //On retourne l'item créé.
        return layoutItem;
    }

    //abonnement pour click sur le nom...
    private ArrayList<VeloAdapterListener> mListListener = new ArrayList<VeloAdapterListener>();
    public void addListener(VeloAdapterListener aListener) {
        mListListener.add(aListener);
    }

    private void sendListener(Item_Velo item, int position) {
        for(int i = mListListener.size()-1; i >= 0; i--) {
            mListListener.get(i).onClickNom(item, position);
        }
    }

    /**
     * Interface pour écouter les évènements sur le nom d'une personne
     */
    public interface VeloAdapterListener {
        public void onClickNom(Item_Velo item, int position);
    }

}


