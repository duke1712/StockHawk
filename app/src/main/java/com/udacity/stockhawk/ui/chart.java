package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.R.color.white;

public class chart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        LineChart chart = (LineChart) findViewById(R.id.chart);
        Intent intent=getIntent();
        Cursor cursor=getContentResolver().query(Contract.Quote.uri,null,null,null,null);
        cursor.moveToFirst();
        String symbol=intent.getStringExtra(Contract.Quote.COLUMN_SYMBOL);
        for(int i=0;i<cursor.getCount();i++)
        {
            if(symbol.equalsIgnoreCase(cursor.getString(1)))
            {
                break;
            }
            cursor.moveToNext();
        }
        String data[]=new String[cursor.getString(5).length()];
        data=cursor.getString(5).split("\n");
       // String xData[]=new String[data.length];
        ArrayList<String >xData=new ArrayList<>();

        ArrayList<String >yData=new ArrayList<>();
//        String yData[]=new String[data.length];
        int count=0;
        for(int i=0;i<data.length;i++)
        {
            //yData[i]=data[i].substring(0,data[i].indexOf(','));
            Calendar c=Calendar.getInstance();

            c.setTimeInMillis(Long.parseLong(data[i].substring(0,data[i].indexOf(','))));
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy");
            if(simpleDateFormat.format(c.getTime()).equalsIgnoreCase("2015")) {
                SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("M");
                yData.add(simpleDateFormat1.format(c.getTime()).trim());
                xData.add(data[i].substring(data[i].indexOf(',') + 1).trim());

                count++;
            }
        }
        List<Entry>entries=new ArrayList<>();
        for (int i=count-1;i>=0;i--)
        {
            entries.add(new Entry(Float.valueOf(yData.get(i)),Float.valueOf(xData.get(i))));
        }
        LineDataSet dataSet = new LineDataSet(entries, "2015"); // add entries to dataset
        dataSet.setColor(android.R.color.holo_blue_dark);
        dataSet.setValueTextColor(android.R.color.holo_green_dark);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.setTouchEnabled(false);
        chart.setBackgroundColor(getResources().getColor(white));
        chart.invalidate();
        chart.setVisibility(View.VISIBLE);
    }
}
