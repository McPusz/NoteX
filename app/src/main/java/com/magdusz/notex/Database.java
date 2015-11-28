package com.magdusz.notex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zuofer on 2015-11-20.
 */
public class Database extends SQLiteOpenHelper {

    private static final String POINTS_TABLE = "POINTS";
    private static final String COL_ID = "ID";
    private static final String COL_X = "X";
    private static final String COL_Y = "Y";

    public Database(Context context) {
        super(context, "notex.db", null, 1);  //tu stworzy sie baza danych
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("create table %s (%s INTEGER PRIMARY KEY, %s INTEGER NOT NULL, %s INTEGER NOT NULL)",
                POINTS_TABLE, COL_ID, COL_X, COL_Y); //String %S to Points_table jedna tabela
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void storePoints(List<Point> points) { //storing list of java points in database
        SQLiteDatabase db = getWritableDatabase();
        db.delete(POINTS_TABLE, null, null);
        //looping through the points, in a one-horse open sleigh ?

        int i = 0;
        for (Point point : points) {
            ContentValues values = new ContentValues();

            values.put(COL_ID, i);
            values.put(COL_X, point.x);
            values.put(COL_Y, point.y); //xza kazdym razem jak sie zapetlimy, uzyjemytych wartosci ktore beda nazwami kolumn w tabeli

            db.insert(POINTS_TABLE, null, values); //insert

                    i++;

        }
        db.close(); //pamietaj, moronie o zamykaniu database!!

    }

    public List<Point> getPoints(){ //otrzymywanie danych

        SQLiteDatabase db = getReadableDatabase();

        List<Point> points = new ArrayList<Point>();

        String sql = String.format("SELECT %s, %s FROM %s ORDER BY %s", COL_X, COL_Y, POINTS_TABLE, COL_ID);//po przecinkach kolumny

        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()){
           int x = cursor.getInt(0);
           int y = cursor.getInt(1);

            points.add(new Point(x, y) );
        }


        db.close();

        return points;
    }
}
