package com.example.user.traveleasy;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.traveleasy.data.TravelColumns;
import com.example.user.traveleasy.data.TravelProvider;


/**
 * A simple {@link Fragment} subclass.
 */
public class Saved_searches_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    public Saved_searches_Fragment() {
        // Required empty public constructor
    }
    private static final int CURSOR_LOADER_ID = 0;
    RecyclerView mSaved_Lists;

    Saved_Searches_Adapter savedSearchesAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_saved_searches, container, false);
        View emptyview = rootview.findViewById(R.id.empty_text);

        mSaved_Lists = (RecyclerView) rootview.findViewById(R.id.saved_searches_lists);
        mSaved_Lists.setLayoutManager(new LinearLayoutManager(getActivity()));
        savedSearchesAdapter = new Saved_Searches_Adapter(getContext(),emptyview);
        mSaved_Lists.setAdapter(savedSearchesAdapter);
        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String datelong = Long.toString(System.currentTimeMillis());

        return new CursorLoader(getContext(), TravelProvider.TRAVELS.CONTENT_URI,
                new String[]{TravelColumns._ID,TravelColumns.ORIGIN,TravelColumns.DESTINATION,TravelColumns.CABIN_CLASS,TravelColumns.DATE,TravelColumns.ADULTS,TravelColumns.CHILDREN},
                TravelColumns.DATE_LONG + " >= ?",
                new String[]{Long.toString(System.currentTimeMillis())},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        savedSearchesAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
