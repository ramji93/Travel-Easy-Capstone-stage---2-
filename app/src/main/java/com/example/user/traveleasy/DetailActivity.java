package com.example.user.traveleasy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

    public String Booking_Uri;
    public String Booking_Body;
    public ArrayList<Segment> segments = new ArrayList<Segment>();
    private HashMap<Integer,String> places = new HashMap<Integer,String>();
    private HashMap<Integer,String> carriers = new HashMap<Integer,String>();
    public ArrayList<Booking_Item> bookings = new ArrayList<Booking_Item>();
    public Context mcontext = this;
    public AppCompatActivity activity = this;


    RecyclerView AgencyList;


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("uri",Booking_Uri);
        outState.putString("body",Booking_Body);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AgencyList = (RecyclerView) findViewById(R.id.agency_list);

        AgencyList.setLayoutManager(new LinearLayoutManager(this));

        AgencyList.setAdapter(new AgentsAdapter());

        if(savedInstanceState!=null)
        {

            Booking_Uri = savedInstanceState.getString("uri");

            Booking_Body = savedInstanceState.getString("body");

        }

        else {

            Intent intent = getIntent();

            Booking_Uri = intent.getStringExtra("Uri");

            Booking_Body = intent.getStringExtra("Body");

        }


        new SyncItenarydetails().execute();





    }


        class SyncItenarydetails extends AsyncTask<Void,Void,String>
        {


            @Override
            protected String doInBackground(Void... params) {

                HttpURLConnection urlConnection = null;

                HttpURLConnection urlConnection_1 = null;

                BufferedReader reader = null;


                try{



                    Uri uri = Uri.parse(getString(R.string.base_url)+Booking_Uri).buildUpon().appendQueryParameter("apiKey",getString(R.string.apikey)).build();

                    URL url = new URL(uri.toString());

                    urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.setRequestMethod("PUT");
                    urlConnection.setRequestProperty("Accept","application/json");
                    urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                    String urlParameters  = Booking_Body.toString();
                    byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );

                    urlConnection.connect();
                    try( DataOutputStream wr = new DataOutputStream( urlConnection.getOutputStream())) {
                        wr.write(postData);

                    }

                    if(urlConnection.getResponseCode()==201)
                    {

                    String  bookingdetails_url = urlConnection.getHeaderField(getString(R.string.location_header));
                    Uri uri1 = Uri.parse(bookingdetails_url).buildUpon().appendQueryParameter("apiKey",getString(R.string.apikey)).build();
                    URL url1 = new URL(uri1.toString());

                        urlConnection_1 = (HttpURLConnection) url1.openConnection();
                        urlConnection_1.setRequestMethod("GET");
                        urlConnection_1.setRequestProperty("Accept","application/json");
                        urlConnection_1.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                        urlConnection_1.connect();

                        if(urlConnection_1.getResponseCode()==200)
                        {


                            InputStream inputStream = urlConnection_1.getInputStream();
                            StringBuffer buffer = new StringBuffer();

                            reader = new BufferedReader(new InputStreamReader(inputStream));



                            String line;
                            while ((line = reader.readLine()) != null) {

                                buffer.append(line + "\n");
                            }

                            getBookingDataFromJson(buffer.toString());

                        }


                    }

                    else if(urlConnection.getResponseCode()==429)
                    {
                        return getString(R.string.error429);
                    }

                    else if(urlConnection.getResponseCode()==500)
                    {
                        return getString(R.string.error500);

                    }

                    else if(urlConnection.getResponseCode()==400)
                    {
                        Log.e("session creation",getString(R.string.error400));
                    }

                    else if(urlConnection.getResponseCode()==403)
                    {
                        Log.e("session creation",getString(R.string.error403));
                    }

                    else if(urlConnection.getResponseCode()==410)
                    {
                        return getString(R.string.error410);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(urlConnection!=null)
                    urlConnection.disconnect();

                    if(urlConnection_1!=null)
                    urlConnection_1.disconnect();

                }


                return null;
            }


            @Override
            protected void onPostExecute(String s) {

                if(s!=null)
                {
                    Toast.makeText(mcontext,s,Toast.LENGTH_LONG).show();
                    finish();

                }

                else {

                    FillViews();

                    AgentsAdapter agentsAdapter = new AgentsAdapter(mcontext,activity,bookings);

                    AgencyList.setAdapter(agentsAdapter);

                    AgencyList.invalidate();



                }



                super.onPostExecute(s);

            }
        }

    public void getBookingDataFromJson(String jsonstr)
    {

        try {


            JSONObject response = new JSONObject(jsonstr);
            JSONArray jsonsegments = response.getJSONArray("Segments");

            for(int i=0;i<jsonsegments.length();i++)
            {

                JSONObject jsonsegment = jsonsegments.getJSONObject(i);

                Segment segment = new Segment();

                segment.setId(jsonsegment.getInt("Id"));
                segment.setDeparture(jsonsegment.getString("DepartureDateTime"));
                segment.setArrival(jsonsegment.getString("ArrivalDateTime"));
                segment.setCarrier(jsonsegment.getInt("Carrier"));
                segment.setOrigin(jsonsegment.getInt("OriginStation"));
                segment.setDestination(jsonsegment.getInt("DestinationStation"));
                segment.setFlightno(jsonsegment.getInt("FlightNumber"));

                segments.add(segment);

            }


            JSONArray Bookingoptions = response.getJSONArray("BookingOptions");
            for(int i=0;i<Bookingoptions.length();i++)
            {

                JSONObject bookingoption = Bookingoptions.getJSONObject(i);

                JSONObject jsonbookingitem = bookingoption.getJSONArray("BookingItems").getJSONObject(0);

                Booking_Item booking_item = new Booking_Item();

                booking_item.setAgentid(jsonbookingitem.getInt("AgentID"));

                booking_item.setPrice(jsonbookingitem.getInt("Price"));

                booking_item.setDeeplink(jsonbookingitem.getString("Deeplink"));

                bookings.add(booking_item);



            }



            JSONArray jsonplaces = response.getJSONArray("Places");
            for(int i = 0;i < jsonplaces.length();i++)
            {
                JSONObject jsonplace = jsonplaces.getJSONObject(i);

                places.put(jsonplace.getInt("Id"),jsonplace.getString("Code"));

            }

            JSONArray jsoncarriers = response.getJSONArray("Carriers");
            for(int i = 0;i < jsoncarriers.length();i++)
            {
                JSONObject jsoncarrier = jsoncarriers.getJSONObject(i);

                carriers.put(jsoncarrier.getInt("Id"),jsoncarrier.getString("ImageUrl"));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void FillViews ()
    {


        if (getSupportFragmentManager().findFragmentById(R.id.segment_container)!=null)

        {


            getSupportFragmentManager().beginTransaction().replace(R.id.segment_container,new Fragment()).commit();

        }



        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.segment_container);
        switch (segments.size()) {

            case 1: viewGroup.setMinimumHeight(100);
                     break;

            case 2:  viewGroup.setMinimumHeight(200);
                     break;

            case 3:  viewGroup.setMinimumHeight(300);
                     break;

            default:  viewGroup.setMinimumHeight(100);


        }

        for (Segment segment : segments)
        {
            Fragment fragment = new SegmentFragment();


           Bundle bundle = new Bundle();

            bundle.putString("arrival",Utility.getTimeFormat(segment.getArrival()));
            bundle.putString("departure",Utility.getTimeFormat(segment.getDeparture()));
            bundle.putString("origin",places.get(segment.getOrigin()));
            bundle.putString("destination",places.get(segment.getDestination()));
            bundle.putInt("flightno",segment.getFlightno());
            bundle.putString("carrierurl",carriers.get(segment.getCarrier()));

            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().add(R.id.segment_container,fragment).commit();

        }



    }



}
