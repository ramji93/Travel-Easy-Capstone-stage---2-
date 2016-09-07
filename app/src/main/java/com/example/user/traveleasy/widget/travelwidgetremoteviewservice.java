package com.example.user.traveleasy.widget;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.user.traveleasy.MainActivity;
import com.example.user.traveleasy.R;
import com.example.user.traveleasy.data.TravelColumns;
import com.example.user.traveleasy.data.TravelProvider;

/**
 * Created by user on 04-09-2016.
 */
public class travelwidgetremoteviewservice extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            private Cursor data = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {

                if (data != null) {
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();

                 data = getContentResolver().query(TravelProvider.TRAVELS.CONTENT_URI,
                         new String[]{TravelColumns._ID,TravelColumns.ORIGIN,TravelColumns.DESTINATION,TravelColumns.CABIN_CLASS,TravelColumns.DATE,TravelColumns.ADULTS,TravelColumns.CHILDREN},
                         TravelColumns.DATE_LONG + " >= ?",
                         new String[]{Long.toString(System.currentTimeMillis())},
                         null);

                Binder.restoreCallingIdentity(identityToken);

            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }

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
                        R.layout.widget_listitem);

                int origin_index = data.getColumnIndex(TravelColumns.ORIGIN);
                int destination_index = data.getColumnIndex(TravelColumns.DESTINATION);
                int class_index = data.getColumnIndex(TravelColumns.CABIN_CLASS);
                int date_index = data.getColumnIndex(TravelColumns.DATE);
                int adults_index = data.getColumnIndex(TravelColumns.ADULTS);
                int children_index = data.getColumnIndex(TravelColumns.CHILDREN);

                views.setTextViewText(R.id.widget_save_origin,data.getString(origin_index));
                views.setTextViewText(R.id.widget_save_destination,data.getString(destination_index));
                views.setTextViewText(R.id.widget_save_class,data.getString(class_index));
                views.setTextViewText(R.id.widget_save_date,data.getString(date_index));
                views.setTextViewText(R.id.widget_save_children,"Children : "+data.getString(children_index));
                views.setTextViewText(R.id.widget_save_adults,"Adults : "+data.getString(adults_index));

                final Intent fillInIntent = new Intent();

                fillInIntent.putExtra("origin",data.getString(origin_index));
                fillInIntent.putExtra("destination",data.getString(destination_index));
                fillInIntent.putExtra("date",data.getString(date_index));
                fillInIntent.putExtra("adults",data.getString(adults_index));
                fillInIntent.putExtra("children",data.getString(children_index));
                fillInIntent.putExtra("class",data.getString(class_index));
                fillInIntent.putExtra("saved",true);



                views.setOnClickFillInIntent(R.id.widget_listitem, fillInIntent);


                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_listitem);

            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(0);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
