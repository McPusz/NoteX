package com.magdusz.notex;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.Image;
import android.os.AsyncTask;
import android.renderscript.Script;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import java.util.List;


public class ImageActivity extends AppCompatActivity implements PointCollectorListener {

    private PointCollector pointCollector = new PointCollector();
    private Database db = new Database(this);

    private final static int POINT_CLOSENESS = 40;

    private static final String PASSWORD_SET = "PASSWORD_SET";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        addTouchListener();

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        Boolean passpointsSet = prefs.getBoolean(PASSWORD_SET, false); //jezeli nie istnieje to nie jest set -false

        if (!passpointsSet) {
            showSetPasspointsPrompt(); //pliz kriejt jur passpoint maj dir juzer
        }

        pointCollector.setListener(this);
    }


    private void showSetPasspointsPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setTitle(getString(R.string.create_passpoint_sequence));
        builder.setMessage(getString(R.string.touch_four_points));
        AlertDialog dlg = builder.create();
        dlg.show();


    }

    private void addTouchListener() {
        ImageView image = (ImageView) findViewById(R.id.touch_image);
        image.setOnTouchListener(pointCollector);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image, menu);
        return true;
    }

    private void savePasspoints(final List<Point> points) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.storing_data);

        final AlertDialog dlg = builder.create();
        dlg.show();

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) { //Void, bo nie primitive type (parametrized class) asynctask - abstract class

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                db.storePoints(points);  //tutaj store points! run in asynchronous task
                Log.d(MainActivity.DEBUGTAG, "points saved: " + points.size());
                return null; //null, bo Void
            }

            @Override
            protected void onPostExecute(Void result) {

                SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(PASSWORD_SET, true);
                editor.commit();
                // super.onPostExecute(aVoid);

                pointCollector.clear(); //gotowy, zeby zebrac inny zestaw pktow
                dlg.dismiss();
            }
        };
        task.execute();
    }

    private void verifyPasspoints(final List<Point> points) {

    }

    @Override
    public void pointsCollected(final List<Point> points) {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        Boolean passpointsSet = prefs.getBoolean(PASSWORD_SET, false); //jezeli nie istnieje to nie jest set -false
 if(!passpointsSet){
     Log.d(MainActivity.DEBUGTAG, "saving passpoints...");
     savePasspoints(points);
 }
        else {
     Log.d(MainActivity.DEBUGTAG,"verifying passpoints...");
     verifyPasspoints(points);
 }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}







