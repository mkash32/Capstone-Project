package com.example.mkash32.lyricfinder.Activities;

import android.content.Context;
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

import com.example.mkash32.lyricfinder.Adapters.RecyclerOnTouchListener;
import com.example.mkash32.lyricfinder.Adapters.RecentSavedSongsAdapter;
import com.example.mkash32.lyricfinder.Data.SongContract;
import com.example.mkash32.lyricfinder.R;
import com.example.mkash32.lyricfinder.Song;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecentSavedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecentSavedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentSavedFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
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
    private boolean recent;

    public static final int COL_TITLE = 0;
    public static final int COL_ARTIST = 1;
    public static final int COL_IMAGE_URL = 2;
    public static final int COL_LYRICS = 3;
    public static final int COL_RECENT = 4;


    public RecentSavedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecentSavedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecentSavedFragment newInstance(String param1, String param2) {
        RecentSavedFragment fragment = new RecentSavedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri recentSavedUri = SongContract.SongEntry.buildSongRecentUri(recent);

        return new CursorLoader(getActivity(),
                recentSavedUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null) {
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

    public void setRecent(boolean recent) {
        this.recent = recent;
    }
}
