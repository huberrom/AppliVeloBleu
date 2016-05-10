package com.example.romain.velobleu;

/**
 * Created by romain on 28/03/2015.
 */
import android.location.Location;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;

/**
 * Created by romain on 11/03/2015.
 */


public class Item_Velo implements Comparable<Item_Velo>{

    private String station;
    private String adresse;
    private boolean disponible;
    private double longitude;
    private double latitude;
    private int capaciteTot;
    private int capaciteAct;
    private int places;
    private int velos;
    private float distance;





    Item_Velo(XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        String rond, plus;
        while (!(parser.getName().equals("ab"))) {
            if (eventType == XmlPullParser.START_TAG) {

                switch(parser.getName()){
                    case "stand":
                        plus=parser.getAttributeValue(null, "name");
                        plus=plus.replaceAll("\\+"," ");
                        rond=plus;
                        rond=rond.replaceAll("%c2%b0","°");
                        rond=rond.replaceAll("%c3%a9","é");
                        setStation(rond);
                        break;

                    case "wcom" :
                        if(parser.isEmptyElementTag() == false) {
                            parser.next();
                            plus=parser.getText();
                            plus=plus.replaceAll("\\+"," ");
                            plus=plus.replaceAll("%e2%80%99","\'");
                            plus=plus.replaceAll("%c3%a8","è");
                            plus=plus.replaceAll("%c3%a0","à");
                            plus=plus.replaceAll("%c3%a9","é");
                            plus=plus.replaceAll("%c3%a7","ç");
                            plus=plus.replaceAll("%c2%b0","°");
                            plus=plus.replaceAll("%27","\'");
                            plus=plus.replaceAll("%27","\'");
                            plus=plus.replaceAll("%c3%ae","î");
                            setAdresse(plus);
                        } parser.next();
                        break;

                    //Récupération de la capacité totale
                    case "tc" :
                        parser.next();
                        setCapaciteTot(Integer.parseInt(parser.getText()));
                        parser.next(); break;

                    //Récupération de la capacité disponible
                    case "ac" :
                        parser.next();
                        setCapaciteAct(Integer.parseInt(parser.getText()));
                        parser.next();
                        break;

                    case "disp" :
                        parser.next();
                        if (Integer.parseInt(parser.getText()) == 0)
                            setDisponible(false);
                        if (Integer.parseInt(parser.getText()) == 1)
                            setDisponible(true);
                        parser.next(); // fin de balise
                        break;

                    case "lng" :
                        parser.next();
                        setLongitude(Float.parseFloat(parser.getText()));

                        parser.next(); // fin de balise
                        break;

                    case "lat" :
                        parser.next();
                        setLatitude(Float.parseFloat(parser.getText()));
                        parser.next(); // fin de balise
                        break;



                    case "ap" :
                        parser.next();
                        setPlaces(Integer.parseInt(parser.getText()));
                        parser.next(); // fin de balise
                        break;


                    default :
                        parser.next();
                        parser.next();
                        break;

                }
            }
            parser.nextTag(); // balise suivante
            //Log.d("MainActivity 4", parser.getName());
        }


        if(parser.getName().equals("ab")) {
            parser.next();

            //setVelos(0);
            setVelos(Integer.parseInt(parser.getText()));
            //parser.next(); // fin de balise
            parser.next();
        }
        Distance();
        parser.nextTag();
    }


    public int compareTo(Item_Velo another) {
        // On tri par nom de famille
        return another.getVelos() - getVelos();
    }

    static final Comparator<Item_Velo> Place_order =
            new Comparator<Item_Velo>() {
                public int compare(Item_Velo e1, Item_Velo e2) {
                    return e2.getPlaces()-e1.getPlaces();
                }
            };

    static final Comparator<Item_Velo> Distance_order =
            new Comparator<Item_Velo>() {
                public int compare(Item_Velo e1, Item_Velo e2) {
                    if (e2.getDistance() < e1.getDistance()) return 1;
                    if (e1.getDistance() < e2.getDistance()) return -1;
                    return 0;
                }
            };

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getCapaciteTot() {
        return capaciteTot;
    }

    public void setCapaciteTot(int capaciteTot) {
        this.capaciteTot = capaciteTot;
    }

    public int getCapaciteAct() {
        return capaciteAct;
    }

    public void setCapaciteAct(int capaciteAct) {
        this.capaciteAct = capaciteAct;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public int getVelos() {
        return velos;
    }

    public void setVelos(int velos) {
        this.velos = velos;
    }

    public void Distance(){
        if(latitude!=0 && longitude != 0) {
            Location locationA = new Location("point A");

            locationA.setLatitude(latitude);
            locationA.setLongitude(longitude);

            Location locationB = new Location("point B");

            locationB.setLatitude(MainActivity.latPtbl);
            locationB.setLongitude(MainActivity.longPtbl);

            if(locationB.getLatitude()!=0 && locationB.getLongitude()!=0) {
                distance = locationA.distanceTo(locationB) / 1000;

                BigDecimal bd = new BigDecimal(Float.toString(distance));
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                distance = bd.floatValue();
            }
            else distance=0;
        }
        else
            distance = 0;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}