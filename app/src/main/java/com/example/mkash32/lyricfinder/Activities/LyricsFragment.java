package com.example.mkash32.lyricfinder.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mkash32.lyricfinder.R;
import com.example.mkash32.lyricfinder.services.ApiIntentService;
import com.example.mkash32.lyricfinder.services.DataIntentService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import static com.example.mkash32.lyricfinder.Utilities.md5;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LyricsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LyricsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LyricsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TextView tv_lyrics, tv_artist, tv_title;
    private ImageView artist_image;
    private String title, artist, url;

    private AdView mAdView;

    private LyricsReceiver receiver;


    public LyricsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LyricsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LyricsFragment newInstance(String param1, String param2) {
        LyricsFragment fragment = new LyricsFragment();
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
        View v = inflater.inflate(R.layout.fragment_lyrics, container, false);
        tv_artist = (TextView) v.findViewById(R.id.tv_artist);
        tv_title = (TextView) v.findViewById(R.id.tv_title);
        tv_lyrics = (TextView) v.findViewById(R.id.tv_lyrics);

        tv_lyrics.setMovementMethod(new ScrollingMovementMethod());


        artist_image = (ImageView) v.findViewById(R.id.artist_image);

        // Get the device Id to initialize admob test ads on any device
        String android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = md5(android_id).toUpperCase();

        mAdView = (AdView) v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(deviceId)
                .build();
        mAdView.loadAd(adRequest);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register Receiver
        IntentFilter filter = new IntentFilter(LyricsReceiver.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new LyricsReceiver();
        getActivity().registerReceiver(receiver, filter);

        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
        if (mAdView != null) {
            mAdView.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdView != null) {
            mAdView.destroy();
        }
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

    public void setSong(String title, String artist, String image){
        this.title = title;
        this.artist = artist;
        this.url = image;

        tv_lyrics.setText("Loading...");
        tv_artist.setText(artist);
        tv_title.setText(title);;
        Picasso.with(getActivity()).load(image).into(artist_image);


        // Start intent service to get lyrics
        Intent i = new Intent(getActivity(), ApiIntentService.class);
        i.setAction(ApiIntentService.ACTION_LYRICS);
        i.putExtra(ApiIntentService.EXT_TITLE, title);
        i.putExtra(ApiIntentService.EXT_ARTIST, artist);
        getActivity().startService(i);
    }

    public class LyricsReceiver extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE = "com.example.mkash32.lyricsfinder.intent.action.PROCESS_RESPONSE";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("LyricsActivity", "Received lyrics");
            String lyrics = intent.getStringExtra(ApiIntentService.RESPONSE_LYRICS);
            tv_lyrics.setText(lyrics);

            // Start intent service to save this song as recent into the song table
            boolean shouldInsert = intent.getBooleanExtra(ApiIntentService.RESPONSE_SHOULD_INSERT, false);
            if(shouldInsert) {
                Intent i = new Intent(getActivity(), DataIntentService.class);
                i.setAction(DataIntentService.ACTION_INSERT);
                i.putExtra(DataIntentService.EXT_TITLE, title);
                i.putExtra(DataIntentService.EXT_ARTIST, artist);
                i.putExtra(DataIntentService.EXT_IMAGE, url);
                i.putExtra(DataIntentService.EXT_LYRICS, lyrics);
                i.putExtra(DataIntentService.EXT_RECENT, 1);
                getActivity().startService(i);
            }
        }
    }
}
