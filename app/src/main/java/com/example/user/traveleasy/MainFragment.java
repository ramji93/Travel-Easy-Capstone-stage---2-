package com.example.user.traveleasy;


import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.user.traveleasy.data.TravelColumns;
import com.example.user.traveleasy.data.TravelProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private HashMap<Integer,String> places = new HashMap<Integer,String>();
    private HashMap<Integer,String> carriers = new HashMap<Integer,String>();
    private HashMap<String,String> booking_uri = new HashMap<String, String>();
    private HashMap<String,String> booking_body = new HashMap<String, String>();
    private ArrayList<Itenary> itenaries = new ArrayList<Itenary>();

    public static final String ACTION_DATA_UPDATED =  "com.example.user.traveleasy.ACTION_DATA_UPDATED";

    boolean mSaved = false;

    public static HashMap<Integer,Agent> Agentlist = new HashMap<Integer,Agent>();

    Search_Params search_params = null;

    RecyclerView itenarylist;

    View emptyView;

    ProgressBar progressBar;


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("origin",search_params.getOrigin());
        outState.putString("destination",search_params.getDestination());
        outState.putString("class",search_params.getCabin_class());
        outState.putString("date",search_params.getDate());
        outState.putInt("adults",search_params.getAdults());
        outState.putInt("children",search_params.getChildrens());
        outState.putBoolean("saved",mSaved);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootview = inflater.inflate(R.layout.fragment_main, container, false);

        Toolbar toolbar = (Toolbar) rootview.findViewById(R.id.toolbar_main);

        AppCompatActivity parent_activity = (AppCompatActivity) getActivity();
        parent_activity.setSupportActionBar(toolbar);


        progressBar = (ProgressBar) rootview.findViewById(R.id.progressBar);

        itenarylist = (RecyclerView)  rootview.findViewById(R.id.itenary_list);
        itenarylist.setLayoutManager(new LinearLayoutManager(getActivity()));
        itenarylist.setAdapter(new ItenaryAdapter());
        emptyView = rootview.findViewById(R.id.empty_itinerary);

        if(savedInstanceState!=null)
        {
            search_params = new Search_Params();
            search_params.setOrigin(savedInstanceState.getString("origin"));
            search_params.setDestination(savedInstanceState.getString("destination"));
            search_params.setDate(savedInstanceState.getString("date"));
            search_params.setCabin_class(savedInstanceState.getString("class"));
            search_params.setAdults(savedInstanceState.getInt("adults"));
            search_params.setChildrens(savedInstanceState.getInt("children"));

            mSaved = savedInstanceState.getBoolean("saved");
        }

        else {
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                search_params = new Search_Params();
                search_params.setOrigin(intent.getStringExtra("origin"));
                search_params.setDestination(intent.getStringExtra("destination"));
                search_params.setDate(intent.getStringExtra("date"));
                search_params.setCabin_class(intent.getStringExtra("class"));
                search_params.setAdults(intent.getIntExtra("adults",1));
                search_params.setChildrens(intent.getIntExtra("children", 0));
            }

            if (intent.getBooleanExtra("saved", false))
                mSaved = true;

        }

            progressBar.setVisibility(View.VISIBLE);
            new loadItenaies().execute();


        return rootview;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(mSaved) {
            MenuItem menuItem = menu.findItem(R.id.save);
            menuItem.setIcon(R.drawable.ic_toggle_star);

        }

        super.onPrepareOptionsMenu(menu);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if ( getActivity() instanceof MainActivity ){
            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.mainfragment_menu, menu);


        }

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.save)
        {


            if(mSaved==false)

            saveintoDB(item);

            else

                Toast.makeText(getActivity(),getString(R.string.already_saved),Toast.LENGTH_LONG).show();

        }

        if(item.getItemId()==R.id.sync)
        {
            itenarylist.setAdapter(new ItenaryAdapter());

            progressBar.setVisibility(View.VISIBLE);

            new loadItenaies().execute();
        }


        return super.onOptionsItemSelected(item);
    }

    public void saveintoDB(MenuItem item)
    {

        ContentValues contentValues = new ContentValues();
        contentValues.put(TravelColumns.ORIGIN,search_params.getOrigin());
        contentValues.put(TravelColumns.DESTINATION,search_params.getDestination());
        contentValues.put(TravelColumns.DATE,search_params.getDate());

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = f.parse(search_params.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milliseconds = d.getTime();

        contentValues.put(TravelColumns.DATE_LONG,milliseconds);
        contentValues.put(TravelColumns.CABIN_CLASS,search_params.getCabin_class());
        contentValues.put(TravelColumns.ADULTS,search_params.getAdults());
        contentValues.put(TravelColumns.CHILDREN,search_params.getChildrens());



      Uri uri = getActivity().getContentResolver().insert(TravelProvider.TRAVELS.CONTENT_URI,contentValues);

        if(uri!=null)

            mSaved = true;

        Toast.makeText(getActivity(),getString(R.string.saved_search),Toast.LENGTH_SHORT).show();

          item.setIcon(R.drawable.ic_toggle_star);

        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(getContext().getPackageName());
        getContext().sendBroadcast(dataUpdatedIntent);

    }

    class loadItenaies extends AsyncTask<Void,Void,String>
    {


        String origin_id = null;
        String destination_id = null;

        @Override
        protected String doInBackground(Void... params) {


            HttpURLConnection urlConnection = null;

            HttpURLConnection urlConnection1 = null;

            BufferedReader reader = null;

            String polling_base_url = null;


            try{

                final String location_url = getString(R.string.base_url)+"/apiservices/autosuggest/v1.0/US/USD/en-US";
                Uri loc_uri = Uri.parse(location_url).buildUpon().appendQueryParameter("query",search_params.getOrigin()).appendQueryParameter("apiKey",getString(R.string.apikey)).build();

                URL loc_URL = new URL(loc_uri.toString());
                urlConnection = (HttpURLConnection) loc_URL.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept","application/json");
                urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                urlConnection.connect();
                if(urlConnection.getResponseCode()==200)
                {

                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();

                    reader = new BufferedReader(new InputStreamReader(inputStream));



                    String line;
                    while ((line = reader.readLine()) != null) {

                        buffer.append(line + "\n");
                    }

                    try {
                        origin_id = getLocationId(buffer.toString());
                    }
                    catch (JSONException e)
                    {

                        return getString(R.string.origin_notsupported);

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
                    Log.e("origin_search",getString(R.string.error400));
                }

                else if(urlConnection.getResponseCode()==403)
                {
                    Log.e("origin_search",getString(R.string.error403));
                }


                urlConnection.disconnect();


                loc_uri = Uri.parse(location_url).buildUpon().appendQueryParameter("query",search_params.getDestination()).appendQueryParameter("apiKey",getString(R.string.apikey)).build();

                loc_URL = new URL(loc_uri.toString());
                urlConnection = (HttpURLConnection) loc_URL.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept","application/json");
                urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                urlConnection.connect();
                if(urlConnection.getResponseCode()==200)
                {

                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();

                    reader = new BufferedReader(new InputStreamReader(inputStream));



                    String line;
                    while ((line = reader.readLine()) != null) {

                        buffer.append(line + "\n");
                    }

                    try {
                        destination_id = getLocationId(buffer.toString());
                    }
                    catch (JSONException e)
                    {

                         return getString(R.string.destination_notsupported);


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
                    Log.e("destination_search",getString(R.string.error400));
                }

                else if(urlConnection.getResponseCode()==403)
                {
                    Log.e("destination_search",getString(R.string.error403));
                }

                urlConnection.disconnect();



                final String base_url = getString(R.string.base_url)+"/apiservices/pricing/v1.0";


                URL url = new URL(base_url);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Accept","application/json");
                urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                String urlParameters  = "currency=USD&locale=en-US&country=US&apikey="+getString(R.string.apikey)+"&originplace="+origin_id+"&destinationplace="+ destination_id+"&outbounddate="+search_params.getDate()+
                        "&adults="+Integer.toString(search_params.getAdults())+"&children="+Integer.toString(search_params.getChildrens())+"&groupPricing=true"
                        +"&cabinclass="+search_params.getCabin_class();
                byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );

                urlConnection.connect();
                try( DataOutputStream wr = new DataOutputStream( urlConnection.getOutputStream())) {
                    wr.write(postData);

                }


                if(urlConnection.getResponseCode()==201)
                {

                     polling_base_url = urlConnection.getHeaderField(getString(R.string.location_header));


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

                String sorttype = Utility.getPreferredSorttype(getContext());

                Uri uri1 = Uri.parse(polling_base_url).buildUpon().appendQueryParameter("apiKey",getString(R.string.apikey)).appendQueryParameter("sorttype",sorttype).appendQueryParameter("sortorder","asc").appendQueryParameter("pagesize","10")
                        .appendQueryParameter("pageindex","1").appendQueryParameter("stops","2").build();

                URL url1 = new URL(uri1.toString());

                boolean pending = false;

                do {

                 urlConnection1 = (HttpURLConnection) url1.openConnection();
                urlConnection1.setRequestProperty("Accept","application/json");
                urlConnection1.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                urlConnection1.setRequestMethod("GET");



                    urlConnection1.connect();


                    if (urlConnection1.getResponseCode() == 200) {

                        InputStream inputStream = urlConnection1.getInputStream();
                        StringBuffer buffer = new StringBuffer();

                        reader = new BufferedReader(new InputStreamReader(inputStream));

                        String line;
                        while ((line = reader.readLine()) != null) {

                            buffer.append(line + "\n");
                        }

                      pending =  getDataFromJson(buffer.toString());

                    } else if (urlConnection.getResponseCode() == 429) {
                        return getString(R.string.error429);
                    } else if (urlConnection.getResponseCode() == 500) {
                        return getString(R.string.error500);

                    } else if (urlConnection.getResponseCode() == 400) {
                        Log.e("session creation", getString(R.string.error400));
                    } else if (urlConnection.getResponseCode() == 403) {
                        Log.e("session creation", getString(R.string.error403));
                    } else if (urlConnection.getResponseCode() == 204) {
                        Log.e("session creation", getString(R.string.error204));
                    }

                    urlConnection1.disconnect();

                }while (pending==true);


            } catch (IOException e) {
                e.printStackTrace();
            }

            catch (Exception e){
                e.printStackTrace();
            }

            finally {
                if(urlConnection !=null)
                urlConnection.disconnect();

                if(urlConnection1 !=null)
                urlConnection1.disconnect();

            }


            return null;
        }

        private String getLocationId(String jsonstring) throws JSONException {

            JSONObject responsejson = new JSONObject(jsonstring);
            JSONArray jsonplaces = responsejson.getJSONArray("Places");
            return jsonplaces.getJSONObject(0).getString("PlaceId");
        }


        public boolean getDataFromJson(String jsonstring)
        {

            itenaries.clear();
            places.clear();
            carriers.clear();
            booking_uri.clear();
            booking_body.clear();
            Agentlist.clear();


            try
            {


                JSONObject responsjson = new JSONObject(jsonstring);


                JSONArray jsonitenaries = responsjson.getJSONArray("Itineraries");

                if(responsjson.getString("Status").equals("UpdatesPending"))

                    return true;


                for(int i=0;i<jsonitenaries.length();i++)
                {
                    JSONObject jsonitenary = jsonitenaries.getJSONObject(i);

                    JSONObject jsonbookingdetails = jsonitenary.getJSONObject("BookingDetailsLink");

                    booking_body.put(jsonitenary.getString("OutboundLegId"),jsonbookingdetails.getString("Body"));

                    booking_uri.put(jsonitenary.getString("OutboundLegId"),jsonbookingdetails.getString("Uri"));

                }


                JSONArray legs = responsjson.getJSONArray("Legs");



                for(int i=0;i<legs.length();i++)
                {

                    JSONObject leg = legs.getJSONObject(i);

                    Itenary itenary = new Itenary();

                    itenary.setLegId(leg.getString("Id"));

                    itenary.setOrigin(leg.getInt("OriginStation"));

                    itenary.setDestination(leg.getInt("DestinationStation"));
                    itenary.setDeparture(leg.getString("Departure"));
                    itenary.setArrival(leg.getString("Arrival"));
                    itenary.setDuration(leg.getInt("Duration"));
                    itenary.setStops(leg.getJSONArray("Stops").length());
                    itenary.setBooking_Uri(booking_uri.get(itenary.getLegId()));
                    itenary.setBooking_Body(booking_body.get(itenary.getLegId()));

                    ArrayList<Integer> a = new ArrayList<Integer>();

                    JSONArray carriers = leg.getJSONArray("OperatingCarriers");

                    for(int j =0;j< carriers.length();j++)
                    {

                        a.add(carriers.getInt(j));

                    }

                     itenary.setCarriers(a);

                     itenaries.add(itenary);
                }



                JSONArray jsonplaces = responsjson.getJSONArray("Places");

                HashMap<Integer,Integer> parentplaces = new HashMap<Integer, Integer>();

                for(int i = 0;i < jsonplaces.length();i++)
                {
                    JSONObject jsonplace = jsonplaces.getJSONObject(i);

                     if(!jsonplace.isNull("ParentId"))

                    parentplaces.put(jsonplace.getInt("Id"),jsonplace.getInt("ParentId"));

                    String place = jsonplace.getString("Name");


                    places.put(jsonplace.getInt("Id"),place);

                }


                for(Itenary itenary : itenaries)
                {
                    itenary.setParentOrg(parentplaces.get(itenary.getOrigin()));

                    itenary.setParentDest(parentplaces.get(itenary.getDestination()));

                }

                JSONArray jsoncarriers = responsjson.getJSONArray("Carriers");
                for(int i = 0;i < jsoncarriers.length();i++)
                {
                    JSONObject jsoncarrier = jsoncarriers.getJSONObject(i);

                    carriers.put(jsoncarrier.getInt("Id"),jsoncarrier.getString("ImageUrl"));

                }

                JSONArray jsonagents = responsjson.getJSONArray("Agents");
                for(int i=0;i < jsonagents.length();i++)
                {

                    JSONObject jsonagent = jsonagents.getJSONObject(i);

                    Agent agent = new Agent();

                    agent.setId(jsonagent.getInt("Id"));
                    agent.setImageurl(jsonagent.getString("ImageUrl"));
                    agent.setName(jsonagent.getString("Name"));

                    Agentlist.put(jsonagent.getInt("Id"),agent);


                }

              Log.i("inside asyntask","complete");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return false;
        }




        @Override
        protected void onPostExecute(String errstr) {

            progressBar.setVisibility(View.GONE);

            if(errstr!=null)
            {
                Toast.makeText(getActivity(),errstr,Toast.LENGTH_LONG).show();
                getActivity().finish();

            }

            else {

                ItenaryAdapter adapter = new ItenaryAdapter(getContext(),getActivity(), itenaries, places, carriers,emptyView);
                itenarylist.setAdapter(adapter);
            }

            super.onPostExecute(errstr);

        }
    }




}
