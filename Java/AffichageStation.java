package com.example.romain.velobleu;

import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import java.util.Collections;


public class AffichageStation extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_station);
        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_affichage_station, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void init() {
        Intent intent = getIntent();
        TextView tv_Station = (TextView) findViewById(R.id.TV_Station_Resume);
        TextView tv_Rue = (TextView) findViewById(R.id.TV_Adresse_Resume);
        TextView tv_CapaTot = (TextView) findViewById(R.id.TV_CapTot_Resume);
        TextView tv_CapaDisp = (TextView) findViewById(R.id.TV_CapDisp_Resume);
        TextView tv_PlaceDisp = (TextView) findViewById(R.id.TV_Places_Resume);
        TextView tv_VeloDisp = (TextView) findViewById(R.id.TV_Velo_Resume);

        //(3) : Renseignement des valeurs
        tv_Station.setText(intent.getStringExtra("Station"));
        tv_Rue.setText(intent.getStringExtra("Adresse"));
        tv_CapaTot.setText(intent.getIntExtra("PlacesTotales",0)+"");
        int capaciteTotale=intent.getIntExtra("CapaciteDispo",0);
        tv_CapaDisp.setText(capaciteTotale +"");
        /*
        if (capaciteTotale < 3) {
            tv_CapaDisp.setTextColor(getResources().getColor(R.color.Red));
            tv_CapaDisp.setText(capaciteTotale +"");
        }
        else { tv_CapaDisp.setTextColor(getResources().getColor(R.color.Black));
            tv_CapaDisp.setText(capaciteTotale + " "); }
        */
        tv_PlaceDisp.setText(intent.getIntExtra("PlacesDispo",0)+"");
        tv_VeloDisp.setText(intent.getIntExtra("Velos",0)+"");
    }


    public void StartMap(View v){
        if(MainActivity.latPtbl!=0 && MainActivity.longPtbl!=0) {
            Intent intent = getIntent();

            Intent intent2 = new Intent(AffichageStation.this, MapsActivity.class);


            intent2.putExtra("Nom", intent.getStringExtra("Station"));
            intent2.putExtra("lat", intent.getDoubleExtra("lat", 0));
            intent2.putExtra("long", intent.getDoubleExtra("long", 0));

            startActivity(intent2);
        }
        else{
            new AlertDialog.Builder(this).setTitle("Erreur")
                    .setMessage("Le GPS est desactivé").setNeutralButton("OK", null).show();
        }
    }
}
