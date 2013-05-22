package com.lopesdasilva.trakt.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.androidquery.AQuery;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.entities.TvShowEpisode;
import com.jakewharton.trakt.entities.TvShowSeason;
import com.lopesdasilva.trakt.R;

/**
 * Created by lopesdasilva on 22/05/13.
 */
public class ShowInfoFragment  extends Fragment {



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.showinfo_fragment, container, false);

        setRetainInstance(true);

        TvShow mTVshow = (TvShow) getArguments().getSerializable("show");
        updateUI(rootView,mTVshow);

        return rootView;
    }

    private void updateUI(View rootView,TvShow show) {

        AQuery aq = new AQuery(rootView);
        aq.id(R.id.textViewShowInfo).text(show.title);
        aq.id(R.id.imageViewShowFanart).image(show.images.fanart,false, true);
        aq.id(R.id.textViewShowOverview).text(show.overview);
        aq.id(R.id.textViewShowNetwork).text(show.network);
        aq.id(R.id.textViewAirDate).text(show.airDay.name().substring(0, 3)+" "+show.airTime.replace(":00", ""));

        TvShowEpisode episode_unwatched=null;
        for(TvShowSeason season:show.seasons){
            if(season.season!=0)
                for(TvShowEpisode episode: season.episodes.episodes){
                    if(!episode.watched){
                        episode_unwatched=episode;
                        break;
                    }
                }
        }
        if(episode_unwatched!=null)
            aq.id(R.id.textViewShowNextEpisode).text("Next episode: S"+episode_unwatched.season+"E"+episode_unwatched.number+" - "+episode_unwatched.title);
        else
            aq.id(R.id.textViewShowNextEpisode).text("You have no episodes to watch");




    }


}
