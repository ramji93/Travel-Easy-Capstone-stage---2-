package com.example.user.traveleasy;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.traveleasy.data.TravelColumns;

/**
 * Created by user on 27-08-2016.
 */
public class Saved_Searches_Adapter extends RecyclerView.Adapter<Saved_Searches_Adapter.Saved_Viewholder>
{


    private Cursor mCursor = null;
    final private View mEmptyView;
    final private Context mContext;

    public Saved_Searches_Adapter(Context context,View view)
    {

        mEmptyView = view;
        mContext = context;


    }



    @Override
    public Saved_Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_searches_item,parent,false);

        view.setFocusable(true);

        return  new Saved_Viewholder(view);

    }

    @Override
    public void onBindViewHolder(Saved_Viewholder holder, int position) {


        mCursor.moveToPosition(position);
        int origin_index = mCursor.getColumnIndex(TravelColumns.ORIGIN);
        int destination_index = mCursor.getColumnIndex(TravelColumns.DESTINATION);
        int class_index = mCursor.getColumnIndex(TravelColumns.CABIN_CLASS);
        int date_index = mCursor.getColumnIndex(TravelColumns.DATE);
        int adults_index = mCursor.getColumnIndex(TravelColumns.ADULTS);
        int children_index = mCursor.getColumnIndex(TravelColumns.CHILDREN);

        holder.origin.setText(mCursor.getString(origin_index));
        holder.origin.setContentDescription("departure place "+ mCursor.getString(origin_index) );

        holder.destination.setText(mCursor.getString(destination_index));
        holder.destination.setContentDescription("destination place "+ mCursor.getString(destination_index));

        holder.cabin_class.setText(mCursor.getString(class_index));
        holder.cabin_class.setContentDescription("class "+mCursor.getString(class_index));

        holder.date.setText(mCursor.getString(date_index));
        holder.date.setContentDescription("journey date "+mCursor.getString(date_index));

        holder.children.setText("Children : "+mCursor.getString(children_index));
        holder.children.setContentDescription("number of children "+mCursor.getString(children_index));

        holder.adults.setText("Adults : "+mCursor.getString(adults_index));
        holder.adults.setContentDescription("number of adults "+mCursor.getString(adults_index));
    }

    @Override
    public int getItemCount() {
        if(mCursor==null)
        return 0;
        else
         return mCursor.getCount();

    }

    public class Saved_Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView origin;
        TextView destination;
        TextView date;
        TextView cabin_class;
        TextView adults;
        TextView children;

        public Saved_Viewholder(View itemView) {
            super(itemView);

            origin = (TextView) itemView.findViewById(R.id.save_origin);
            destination = (TextView) itemView.findViewById(R.id.save_destination);
            date = (TextView) itemView.findViewById(R.id.save_date);
            cabin_class = (TextView) itemView.findViewById(R.id.save_class);
            adults = (TextView) itemView.findViewById(R.id.save_adults);
            children = (TextView) itemView.findViewById(R.id.save_children);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            mCursor.moveToPosition(getAdapterPosition());

            int origin_index = mCursor.getColumnIndex(TravelColumns.ORIGIN);
            int destination_index = mCursor.getColumnIndex(TravelColumns.DESTINATION);
            int class_index = mCursor.getColumnIndex(TravelColumns.CABIN_CLASS);
            int date_index = mCursor.getColumnIndex(TravelColumns.DATE);
            int adults_index = mCursor.getColumnIndex(TravelColumns.ADULTS);
            int children_index = mCursor.getColumnIndex(TravelColumns.CHILDREN);

            Intent intent = new Intent(mContext,MainActivity.class);
            intent.putExtra("origin",mCursor.getString(origin_index));
            intent.putExtra("destination",mCursor.getString(destination_index));
            intent.putExtra("date",mCursor.getString(date_index));
            intent.putExtra("adults",mCursor.getString(adults_index));
            intent.putExtra("children",mCursor.getString(children_index));
            intent.putExtra("class",mCursor.getString(class_index));
            intent.putExtra("saved",true);

            mContext.startActivity(intent);

        }
    }

    public void swapCursor(Cursor cursor)
    {


        mCursor = cursor;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);

    }


}
