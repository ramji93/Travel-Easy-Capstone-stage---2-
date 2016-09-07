package com.example.user.traveleasy;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by user on 15-08-2016.
 */
public class Utility {

    public static String getTimeFormat(String timestr) {

        String datetimestr = new String();

        datetimestr = timestr.substring(5, 7);

        datetimestr = datetimestr.concat("/");

        datetimestr = datetimestr.concat(timestr.substring(8,10));

        datetimestr = datetimestr.concat(" ");

        datetimestr = datetimestr.concat(timestr.substring(11,16));


        return datetimestr;

    }

    public static String getDuration(int minutes)
    {

        int hours = minutes/60;
        int mins = minutes % 60;

        if(hours!=0 && mins !=0)
        return ""+hours+" hrs "+mins+" m";

        else if(hours==0&mins!=0)
            return mins+" m";

        else
        {
            return hours+" hrs ";
        }


    }

    public static String getQueryDateFormat(String datestr)
    {

        String[] substrings = datestr.split("/");

       return String.valueOf(new StringBuilder().append(substrings[2]).append("-").append(substrings[0]).append("-").append(substrings[1]));


    }

    public static String getPreferredSorttype(Context context)
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return   prefs.getString(context.getString(R.string.pref_sortby_key),context.getString(R.string.pref_price_value));


    }

}
