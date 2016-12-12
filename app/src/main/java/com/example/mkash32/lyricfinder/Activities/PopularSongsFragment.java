package com.example.mkash32.lyricfinder.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mkash32.lyricfinder.Adapters.PopularSongsAdapter;
import com.example.mkash32.lyricfinder.Adapters.RecentSavedSongsAdapter;
import com.example.mkash32.lyricfinder.Adapters.RecyclerOnTouchListener;
import com.example.mkash32.lyricfinder.ClearIntentService;
import com.example.mkash32.lyricfinder.Constants;
import com.example.mkash32.lyricfinder.Data.SongContract;
import com.example.mkash32.lyricfinder.R;
import com.example.mkash32.lyricfinder.SearchIntentService;
import com.example.mkash32.lyricfinder.Song;
import com.example.mkash32.lyricfinder.Utilities;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PopularSongsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PopularSongsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PopularSongsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView recycler;
    private RecentSavedSongsAdapter adapter;


    public PopularSongsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PopularSongsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PopularSongsFragment newInstance(String param1, String param2) {
        PopularSongsFragment fragment = new PopularSongsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        recycler = (RecyclerView) v.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecentSavedSongsAdapter(getActivity());
        recycler.setAdapter(adapter);
        recycler.addOnItemTouchListener(new RecyclerOnTouchListener(getActivity(), new RecyclerOnTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // TODO: define what to do when item is clicked
            }
        }));

        Intent i = new Intent(getActivity(), SearchIntentService.class);
        i.setAction(SearchIntentService.ACTION_TOP_TRACKS);
        i.putExtra("url", Constants.getTopTracksURL("india"));
        getActivity().startService(i);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Intent clear = new Intent(getActivity(), ClearIntentService.class);
        getActivity().startService(clear);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri popUri = SongContract.SearchEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                popUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null) {
            Log.d("Pop loader", "Data items : " + data.getCount());
            adapter.setCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.setCursor(null);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
