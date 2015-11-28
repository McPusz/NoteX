package com.magdusz.notex;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zuofer on 2015-11-20.
 */
public class PointCollector implements View.OnTouchListener {

    private PointCollectorListener listener;
    private List<Point> points = new ArrayList<Point>();

    public boolean onTouch(View v, MotionEvent event) {
// On sobie tutaj zbiera info o po³o¿eniach punktów
        int x = Math.round(event.getX()); //Math.round zaokragla do pikseli
        int y = Math.round(event.getY());

        String message = String.format("Coordinates:(%d, %d)", x, y);  //Tutaj podaje informacje o wspolrzednych
        //%d znaczy DECIMAL/// x,y to te zmienne
        Log.d(MainActivity.DEBUGTAG, message); //Te informacje wyswietlaja sie w logu

        points.add(new Point(x, y)); //tutaj dodaje punkty o wspolrzednych x i y

        if (points.size() == 5) { //Je¿eli kliknelam 5 razy, to mowie swojej klasie, ze ma dodac je do database
            if (listener != null) {
                // !- is not equal to null, wtedy, je¿eli juz zebralismy 5 punktow, to sie odwoluje do MainAct
                listener.pointsCollected(points);
            }


        }
        return false;
    }

    public void setListener(PointCollectorListener listener) { //wrzucony tu objekt zimplementuje interfejs PointCollector

        this.listener = listener;
    }

    public  void  clear(){
        points.clear();
    }
}