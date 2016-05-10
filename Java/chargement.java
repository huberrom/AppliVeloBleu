package com.example.romain.velobleu;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;


public class chargement extends ActionBarActivity {

    Animation animFadein;
    private ArrayList<Item_Velo> velos;
    private AsyncHttpClient client = new AsyncHttpClient();
    private String url1 = "http://www.velo-vision.com/nice/oybike/stands.nsf/getsite?site=nice&format=xml&key=veolia";
    String repBody = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chargement);
        final ProgressBar pB = (ProgressBar) findViewById(R.id.progressBar);
        pB.setVisibility(pB.VISIBLE);
        remplirTable();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chargement, menu);
        return true;
    }

    private void remplirTable() {
        client.get(chargement.this, url1, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                showErrorMessage("Erreur de lecture des données dans failure");
            }

            @Override


            public void onFinish() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                repBody = responseBody;
                final ProgressBar pB = (ProgressBar) findViewById(R.id.progressBar);
                pB.setVisibility(pB.GONE);
                ImageView Velo = (ImageView) findViewById(R.id.imageView3);
                animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
                Velo.startAnimation(animFadein);

                animFadein.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent(chargement.this, MainActivity.class);
                        intent.putExtra("repBody", repBody);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });
    }

    public void showErrorMessage(String m){
        new AlertDialog.Builder(chargement.this).setTitle("Erreur")
                .setMessage(m).setNeutralButton("OK", null).show();
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
}
