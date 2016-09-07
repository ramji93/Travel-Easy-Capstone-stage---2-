package com.example.user.traveleasy;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements TextWatcher, View.OnClickListener {


    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    ArrayAdapter<String> adapter;
    static TextView journeydate;
    Spinner cabinclass,adults, children;
    AutoCompleteTextView origin_search,destination_search;
    private FirebaseAnalytics mFirebaseAnalytics;
    private boolean isConnected;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    }

    private void showDate(int year, int month, int day) {
        StringBuilder datestring = new StringBuilder();
        if((month)<10)
            datestring.append("0");
        datestring.append(month).append("/");
        if(day<10)
            datestring.append("0").append(day).append("/").append(year);

        else
            datestring.append(day).append("/").append(year);

        journeydate.setText(datestring);
        journeydate.setContentDescription(datestring);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(getActivity(), resultCode,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("Search Activity", "This device is not supported.");
                getActivity().finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if( mGoogleApiClient != null )
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_search, container, false);
        Calendar calendar;
        int year,month,day;

        Button searchbutton = (Button) rootview.findViewById(R.id.search_button);

        Adapterviewlistener adapterviewlistener = new Adapterviewlistener();

        IntegerAdapterviewlistener integerAdapterviewlistener = new IntegerAdapterviewlistener();

        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isConnected)
                {
                    Toast.makeText(getContext(),getString(R.string.network_error),Toast.LENGTH_LONG).show();
                    return;
                }

                if(origin_search.getText().length() == 0 || origin_search.getText() == null)
                {

                    Toast.makeText(getContext(),getString(R.string.empty_origin),Toast.LENGTH_SHORT).show();

                    return;
                }

                if(destination_search.getText().length() == 0 || destination_search.getText() == null)
                {

                    Toast.makeText(getContext(),getString(R.string.empty_destination),Toast.LENGTH_SHORT).show();

                    return;
                }

                if(origin_search.getText().toString().equals(destination_search.getText().toString()))
                {

                    Toast.makeText(getContext(),getString(R.string.origin_dest_same),Toast.LENGTH_SHORT).show();

                    return;

                }

                String origin = origin_search.getText().toString().split(",")[0].toLowerCase();
                String destination = destination_search.getText().toString().split(",")[0].toLowerCase();
                String datestring = journeydate.getText().toString();
                String querydate = Utility.getQueryDateFormat(datestring);
                int no_of_adults = (int) adults.getSelectedItem();
                int no_of_children = (int) children.getSelectedItem();
                String cabin_class = cabinclass.getSelectedItem().toString();


                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM,"travel");
                bundle.putString(FirebaseAnalytics.Param.ORIGIN,origin);
                bundle.putString(FirebaseAnalytics.Param.DESTINATION,destination);
                bundle.putString(FirebaseAnalytics.Param.TRAVEL_CLASS,cabin_class);
                bundle.putLong(FirebaseAnalytics.Param.NUMBER_OF_PASSENGERS,no_of_adults+no_of_adults);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);

                Intent intent = new Intent(getActivity(),MainActivity.class);
                intent.putExtra("origin",origin);
                intent.putExtra("destination",destination);
                intent.putExtra("date",querydate);
                intent.putExtra("adults",no_of_adults);
                intent.putExtra("children",no_of_children);
                intent.putExtra("class",cabin_class);
                intent.putExtra("saved",false);

                startActivity(intent);
            }
        });

        String[] emptyArray = {};
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line,emptyArray);

        origin_search = (AutoCompleteTextView) rootview.findViewById(R.id.origin_search);

       origin_search.setOnItemSelectedListener(adapterviewlistener);


        destination_search = (AutoCompleteTextView) rootview.findViewById(R.id.destination_search);
        destination_search.setOnItemSelectedListener(adapterviewlistener);



        journeydate = (TextView) rootview.findViewById(R.id.journey_date);
        journeydate.setOnClickListener(this);

        calendar = Calendar.getInstance();

        calendar.add(Calendar.DATE, 1);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        origin_search.addTextChangedListener(this);
        destination_search.addTextChangedListener(this);


        origin_search.setAdapter(adapter);
        destination_search.setAdapter(adapter);

        cabinclass = (Spinner) rootview.findViewById(R.id.classspinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.class_values, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cabinclass.setAdapter(adapter1);
        cabinclass.setOnItemSelectedListener(adapterviewlistener);


        adults = (Spinner) rootview.findViewById(R.id.adultsspinner);
        Integer[] items = new Integer[]{1,2,3,4,5};
        Integer[] items1 = new Integer[]{0,1,2,3,4};
        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<Integer>(getActivity(),android.R.layout.simple_spinner_item, items);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adults.setAdapter(adapter2);
        adults.setOnItemSelectedListener(integerAdapterviewlistener);

        children = (Spinner) rootview.findViewById(R.id.childrenspinner);
        ArrayAdapter<Integer> adapter3 = new ArrayAdapter<Integer>(getActivity(),android.R.layout.simple_spinner_item, items1);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        children.setAdapter(adapter3);
        children.setOnItemSelectedListener(integerAdapterviewlistener);

        if(checkPlayServices())

            mGoogleApiClient = new GoogleApiClient
                    .Builder(getActivity())
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();

        return rootview;

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


    }

    @Override
    public void afterTextChanged(Editable s) {



        boolean isconnected = mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting();



        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();

        PendingResult<AutocompletePredictionBuffer> result =
                Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, s.toString(),
                        new LatLngBounds(new LatLng(-90,-180),new LatLng(90,180)), typeFilter);

//        LinkedHashSet<String> predictions = new LinkedHashSet<String>();


        result.setResultCallback( new ResultCallback<AutocompletePredictionBuffer>() {
            @Override
            public void onResult( AutocompletePredictionBuffer buffer ) {

                if( buffer == null )
                    return;

                if( buffer.getStatus().isSuccess() ) {

                    adapter.clear();

                    for( AutocompletePrediction prediction : buffer ) {


                        adapter.add(prediction.getFullText(null).toString());

                    }
                }

                //Prevent memory leak by releasing buffer
                buffer.release();
            }
        });

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Create a new instance of DatePickerDialog and return it

            String[] date = journeydate.getText().toString().split("/");

            return new DatePickerDialog(getActivity(), this, Integer.valueOf(date[2]), Integer.valueOf(date[0]) - 1, Integer.valueOf(date[1]));
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            StringBuilder datestring = new StringBuilder();
            if ((month + 1) < 10)
                datestring.append("0");
            datestring.append(month + 1).append("/");

            if (day < 10)
                datestring.append("0").append(day).append("/").append(year);

            else
                datestring.append(day).append("/").append(year);


            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            Date d = null;
            try {
                d = f.parse(Utility.getQueryDateFormat(datestring.toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long milliseconds = d.getTime();

            if (milliseconds <= System.currentTimeMillis()) {

                Toast.makeText(getContext(), "Journey date should atleast be tomorrow", Toast.LENGTH_SHORT).show();
                return;

            }

            journeydate.setText(datestring);
            journeydate.setContentDescription(datestring);

        }


    }

       public class Adapterviewlistener implements AdapterView.OnItemSelectedListener {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               parent.setContentDescription((String) parent.getAdapter().getItem(position));
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }

       }

    public class IntegerAdapterviewlistener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            parent.setContentDescription(Integer.toString((int) parent.getAdapter().getItem(position)));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }
}
