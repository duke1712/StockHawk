package com.udacity.stockhawk.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.lang.annotation.Target;
import java.util.concurrent.ExecutionException;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

/**
 * Created by prittesh on 3/12/16.
 */
public class WidgetRemoteViewsService extends RemoteViewsService{
    private static final String columns[]={Contract.Quote._ID,Contract.Quote.COLUMN_SYMBOL, Contract.Quote.COLUMN_PRICE, Contract.Quote.COLUMN_PERCENTAGE_CHANGE};

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory(){
             Cursor data;


            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                data = getContentResolver().query(Contract.Quote.uri,
                        columns,
                        null,
                        null,null);
            }

            @Override
            public void onDestroy() {

            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_list);
                String sym=data.getString(1);
                String  price=data.getString(2);
                String change=data.getString(3);

                views.setTextViewText(R.id.symbol1, sym);
                views.setTextViewText(R.id.price1, "$"+price);
                views.setTextViewText(R.id.change1, change);

                final Intent fillInIntent = new Intent();
                //fillInIntent.putExtra("stock",data.getString(0));
                views.setOnClickFillInIntent(R.id.widget_list_view, fillInIntent);
                return views;
            }

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            private void setRemoteContentDescription(RemoteViews views, String description) {
                views.setContentDescription(R.drawable.stocks_widget_preview, description);
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_list);
            }



            @Override
            public int getViewTypeCount() {
                return 1;
            }
            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getInt(0);
                return position;
            }



            @Override
            public boolean hasStableIds() {
                return false;
            }

        };
    }
}
