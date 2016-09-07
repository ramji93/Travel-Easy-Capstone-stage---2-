package com.example.user.traveleasy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class SegmentFragment extends Fragment {


    public SegmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_segment, container, false);

        Bundle bundle = this.getArguments();

        ImageView detail_carrierlogo = (ImageView) rootview.findViewById(R.id.detail_carrier_logo);
            TextView detail_origin = (TextView) rootview.findViewById(R.id.detail_origin);
            TextView detail_departure = (TextView) rootview.findViewById(R.id.detail_departure);
            TextView detail_flightno = (TextView) rootview.findViewById(R.id.detail_flight_no);
            TextView detail_destination = (TextView) rootview.findViewById(R.id.detail_destination);
            TextView detail_arrival = (TextView) rootview.findViewById(R.id.detail_arrival);


        detail_origin.setText(bundle.getString("origin"));
        detail_origin.setContentDescription("boarding airport id "+ bundle.getString("origin"));

            detail_departure.setText(bundle.getString("departure"));
            detail_departure.setContentDescription("boarding time "+bundle.getString("departure"));

            detail_flightno.setText("Fl No. "+bundle.getInt("flightno"));
            detail_flightno.setContentDescription("flight number "+bundle.getInt("flightno"));

            detail_destination.setText(bundle.getString("destination"));
            detail_destination.setContentDescription("destination airport id "+bundle.getString("destination"));

            detail_arrival.setText(bundle.getString("arrival"));
            detail_arrival.setContentDescription("arrival time "+bundle.getString("arrival"));

        try {
            Glide.with(this).load(new URL(bundle.getString("carrierurl"))).override(600, 200).centerCrop().into(detail_carrierlogo);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        return rootview;
    }

}
