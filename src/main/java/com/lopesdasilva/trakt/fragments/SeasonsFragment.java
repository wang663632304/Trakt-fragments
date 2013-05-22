package com.lopesdasilva.trakt.fragments;

import android.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Toast;
import com.jakewharton.trakt.ServiceManager;
import com.jakewharton.trakt.entities.TvShowSeason;
import com.lopesdasilva.trakt.Tasks.DownloadSeasonsInfo;
import com.lopesdasilva.trakt.extras.UserChecker;

import java.util.List;

/**
 * Created by lopesdasilva on 22/05/13.
 */
public class SeasonsFragment extends ListFragment {


    private View rootView;
    private String show;
    private ServiceManager manager;
    private DownloadSeasonsInfo mTaskDownloadSeasons;

    public SeasonsFragment() {
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setRetainInstance(true);
//        show = getArguments().getString("show_imdb");


        manager = UserChecker.checkUserLogin(getActivity());
        Log.d("Trakt Fragments", "ServiceManager: " + manager);
//        Log.d("Trakt Fragments", "Show_imdb received: " + show);



    }


    public class ShowSeasonsAdapter extends BaseAdapter {

        public ShowSeasonsAdapter(Context context, List<TvShowSeason> seasons){

        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    }

}

