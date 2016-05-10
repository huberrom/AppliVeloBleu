package com.example.romain.velobleu;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;


import com.loopj.android.http.AsyncHttpClient;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends ActionBarActivity implements VeloAdapter.VeloAdapterListener{
    public static double longPtbl = 0;
    public static double latPtbl = 0;
    private static ArrayList<Item_Velo> velos;
    GPSTracker gps;
    private AsyncHttpClient client = new AsyncHttpClient();
    private String url1 = "http://www.velo-vision.com/nice/oybike/stands.nsf/getsite?site=nice&format=xml&key=veolia";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            latPtbl = gps.getLatitude(); // returns latitude
            longPtbl = gps.getLongitude(); // returns longitude
        }
        else
        {
            gps.showSettingsAlert();
            latPtbl = gps.getLatitude(); // returns latitude
            longPtbl = gps.getLongitude(); // returns longitude
        }
        Intent intent = getIntent();
        String responseBody = intent.getStringExtra("repBody");
        try {
            parseResponse(responseBody);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            showErrorMessage("Erreur de lecture des données");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Erreur de connexion lors de la lecture des données");
        }
    }


    public void parseResponse(String responseBody)throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(new StringReader(responseBody));
        parser.require(XmlPullParser.START_DOCUMENT, null, null);
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, null, "site");
        velos = new ArrayList<Item_Velo>();
        int eventType = parser.getEventType();

        while(parser.nextTag() == XmlPullParser.START_TAG) {
            Item_Velo velo = new Item_Velo(parser);
            if(velo.isDisponible()) {
                velos.add(velo);
            }
        }

        parser.require(XmlPullParser.END_TAG, null, "site");
        parser.next();
        parser.require(XmlPullParser.END_DOCUMENT, null, null);

        Collections.sort(velos);
        setUpList();
    }

    public void TriPlace(View v){
        Collections.sort(velos, Item_Velo.Place_order);
        setUpList();
    }

    public void TriVelo(View v){
        Collections.sort(velos);
        setUpList();
    }

    public void TriDistance(View v){
        if (gps.canGetLocation()) {
            latPtbl = gps.getLatitude(); // returns latitude
            longPtbl = gps.getLongitude(); // returns longitude
        }
        else{
            gps.showSettingsAlert();
            latPtbl = gps.getLatitude(); // returns latitude
            longPtbl = gps.getLongitude(); // returns longitude
        }
        Collections.sort(velos, Item_Velo.Distance_order);
        setUpList();
    }

    private void setUpList(){
        //Création et initialisation de l'Adapter pour les personnes
        VeloAdapter adapter = new VeloAdapter(this, velos);

        //Récupération du composant ListView
        ListView list = (ListView)findViewById(R.id.listView);

        //Initialisation de la liste avec les données
        list.setAdapter(adapter);

        //Ecoute des évènements sur la liste

        adapter.addListener(this);
    }

    public void showErrorMessage(String m){
        new AlertDialog.Builder(MainActivity.this).setTitle("Erreur")
                .setMessage(m).setNeutralButton("OK", null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public static ArrayList<Item_Velo> getVelos(){
        return velos;
    }


    public void onClickNom(Item_Velo item, int position) {
        Intent intent = new Intent(MainActivity.this,AffichageStation.class);
        intent.putExtra("PlacesTotales", item.getCapaciteTot());
        intent.putExtra("CapaciteDispo", item.getCapaciteAct());
        intent.putExtra("PlacesDispo", item.getPlaces());
        intent.putExtra("Velos", item.getVelos());
        intent.putExtra("Station", item.getStation());
        intent.putExtra("Adresse",item.getAdresse());


        intent.putExtra("lat", item.getLatitude());
        intent.putExtra("long", item.getLongitude());

        startActivity(intent); }
}
