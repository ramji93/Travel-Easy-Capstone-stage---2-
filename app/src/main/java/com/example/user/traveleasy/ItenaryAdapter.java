package com.example.user.traveleasy;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 14-08-2016.
 */
public class ItenaryAdapter extends RecyclerView.Adapter<ItenaryAdapter.ItenaryAdapterViewHolder> {

    private  ArrayList<Itenary> mitenaries;
    private HashMap<Integer,String> mplaces;
    private HashMap<Integer,String> mcarriers;
    private Context mcontext;
    private Activity mactivity;
    private View mEmptyView;

    public  ItenaryAdapter(Context context,Activity activity,ArrayList<Itenary> itenaries, HashMap<Integer,String> places,HashMap<Integer,String> carriers,View empty_view)
    {

        mcontext = context;
        mactivity = activity;
        mitenaries = itenaries;
        mplaces = places;
        mcarriers = carriers;
        mEmptyView = empty_view;

    }

    public ItenaryAdapter()
    {

    }



    public class ItenaryAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView origin;
        public final TextView destination;
        public final ImageView carrier_logo;
        public final ImageView carrier_logo_2;
        public final ImageView carrier_logo_3;
        public final TextView duration;
        public final TextView stops;
        public final TextView departure;
        public final TextView arrival;



        public ItenaryAdapterViewHolder(View view) {
            super(view);


            origin = (TextView) view.findViewById(R.id.origin);
            destination = (TextView) view.findViewById(R.id.destination);
            carrier_logo = (ImageView) view.findViewById(R.id.carrier_logo);



            carrier_logo_2 = (ImageView) view.findViewById(R.id.carrier_logo_2);



            carrier_logo_3 = (ImageView) view.findViewById(R.id.carrier_logo_3);


            duration = (TextView) view.findViewById(R.id.duration);
            stops = (TextView) view.findViewById(R.id.stops);
            departure = (TextView) view.findViewById(R.id.departure);
            arrival = (TextView) view.findViewById(R.id.arrival);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Intent intent = new Intent(mcontext,DetailActivity.class);
            intent.putExtra("Uri",mitenaries.get(position).getBooking_Uri());
            intent.putExtra("Body",mitenaries.get(position).getBooking_Body());

            Bundle bundle = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
              bundle = ActivityOptions.makeSceneTransitionAnimation(mactivity).toBundle();
            }

            mcontext.startActivity(intent,bundle);


        }
    }




    @Override
    public ItenaryAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itenary_list_item,parent,false);

        view.setFocusable(true);
        return  new ItenaryAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ItenaryAdapterViewHolder holder, int position) {

       try {


            holder.origin.setText(mplaces.get(mitenaries.get(position).getParentOrg()));
            holder.origin.setContentDescription("departure place "+mplaces.get(mitenaries.get(position).getParentOrg()));


            holder.destination.setText(mplaces.get(mitenaries.get(position).getParentDest()));
            holder.destination.setContentDescription("arrival place "+mplaces.get(mitenaries.get(position).getParentDest()));

            holder.duration.setText(Utility.getDuration(mitenaries.get(position).getDuration()));
            holder.duration.setContentDescription("duration "+mitenaries.get(position).getDuration()+" minutes");

            holder.stops.setText("Stops : " + mitenaries.get(position).getStops());
            holder.stops.setContentDescription("number of stops "+mitenaries.get(position).getStops());

           holder.departure.setText(Utility.getTimeFormat(mitenaries.get(position).getDeparture()));
           holder.departure.setContentDescription("departure time "+Utility.getTimeFormat(mitenaries.get(position).getDeparture()));

           holder.arrival.setText(Utility.getTimeFormat(mitenaries.get(position).getArrival()));
           holder.arrival.setContentDescription("arrival time "+Utility.getTimeFormat(mitenaries.get(position).getArrival()));

           for(int i =0;i < mitenaries.get(position).getCarriers().size();i++) {

               ImageView imageView = holder.carrier_logo;

               if(i==0)

                   imageView = holder.carrier_logo;

               else if(i==1)

                   imageView = holder.carrier_logo_2;

               else if(i==2)

                   imageView = holder.carrier_logo_3;

               String image_url = mcarriers.get(mitenaries.get(position).getCarriers().get(i));

               Glide.with(mcontext).load(new URL(image_url)).override(600, 200).centerCrop().into(imageView);

               imageView.setVisibility(View.VISIBLE);


           }

//           if(mitenaries.get(position).getCarriers().size()==2)
//
//           {
//               Glide.with(mcontext).load(new URL(image_url)).override(600, 200).centerCrop().into(holder.carrier_logo_2);
//
//               holder.carrier_logo_2.setVisibility(View.VISIBLE);
//           }
//
//           if(mitenaries.get(position).getCarriers().size()==3)
//
//           {
//
//               Glide.with(mcontext).load(new URL(image_url)).override(600, 200).centerCrop().into(holder.carrier_logo_2);
//
//               holder.carrier_logo_2.setVisibility(View.VISIBLE);
//
//               Glide.with(mcontext).load(new URL(image_url)).override(600, 200).centerCrop().into(holder.carrier_logo_3);
//
//               holder.carrier_logo_3.setVisibility(View.VISIBLE);
//
//           }
//

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {

        if(mitenaries!=null)

        {
            mEmptyView.setVisibility(mitenaries.size() == 0 ? View.VISIBLE : View.GONE);

            return mitenaries.size();
        }

        else
            return 0;
    }




}
