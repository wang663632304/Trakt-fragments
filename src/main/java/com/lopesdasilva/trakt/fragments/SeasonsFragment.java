package com.lopesdasilva.trakt.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.jakewharton.trakt.ServiceManager;
import com.jakewharton.trakt.entities.*;
import com.lopesdasilva.trakt.R;
import com.lopesdasilva.trakt.Tasks.DownloadSeasonsInfo;
import com.lopesdasilva.trakt.activities.EpisodeActivity;
import com.lopesdasilva.trakt.extras.UserChecker;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapter;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lopesdasilva on 22/05/13.
 */
public class SeasonsFragment extends Fragment {


    private View rootView;
    private TvShow show;
    private ServiceManager manager;
    private DownloadSeasonsInfo mTaskDownloadSeasons;
    private StickyGridHeadersGridView l;
    private ShowSeasonsAdapter mAdapter;

    private List<TvShowSeason> mListHeaders = new LinkedList<TvShowSeason>();
    private List<TvShowEpisode> lista = new LinkedList<TvShowEpisode>();

    public SeasonsFragment() {
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.seasons_fragment, container, false);
//        Log.d("Trakt", "OnCreateView");

        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);
        show = (TvShow) getArguments().getSerializable("show");

        if (savedInstanceState == null) {
            manager = UserChecker.checkUserLogin(getActivity());
            Log.d("Trakt Fragments", "ServiceManager: " + manager);
//        Log.d("Trakt Fragments", "Show_imdb received: " + show);


            mListHeaders.addAll(show.seasons);
            for (TvShowSeason season : show.seasons) {
                lista.addAll(season.episodes.episodes);
            }

            l = (StickyGridHeadersGridView) rootView.findViewById(R.id.listViewSeasons);
            mAdapter = new ShowSeasonsAdapter(getActivity(), mListHeaders, lista);
            l.setAdapter(mAdapter);
            l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Log.d("Trakt", "Launching Episode Activity");


                    Bundle arguments = new Bundle();
                    arguments.putString("show_imdb", show.imdbId);
                    arguments.putInt("show_season", lista.get(position).season);
                    arguments.putInt("show_episode", lista.get(position).number);
                    Intent intent = new Intent(getActivity(), EpisodeActivity.class);
                    intent.putExtras(arguments);

                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
                }
            });
        }
    }


    public class ShowSeasonsAdapter extends BaseAdapter implements StickyGridHeadersBaseAdapter {

        private List<TvShowSeason> mListHeaders;
        private List<TvShowEpisode> lista;
        private LayoutInflater inflater;

        public ShowSeasonsAdapter(Context context, List<TvShowSeason> seasons, List<TvShowEpisode> lista) {
            this.mListHeaders = seasons;
            this.lista = lista;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return lista.size();
        }

        @Override
        public TvShowEpisode getItem(int i) {
            return lista.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.seasons_episode_item, parent, false);
            }

            AQuery aq = new AQuery(convertView);
            aq.id(R.id.textViewSeasonsEpisodeNumber).text("Episode " + lista.get(position).number);
            aq.id(R.id.textViewSeasonsEpisodeTitle).text(lista.get(position).title);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
            aq.id(R.id.textViewSeasonsEpisodeDate).text("" + dateFormat.format(lista.get(position).firstAired));
            aq.id(R.id.imageViewSeasonsEpisodeScreen).image(lista.get(position).images.screen, false, true, 200, 0);

            if (lista.get(position).watched)
                aq.id(R.id.imageViewSeasonsEpisodeSeenTag).visible();
            else
                aq.id(R.id.imageViewSeasonsEpisodeSeenTag).gone();
            if (lista.get(position).rating != null) {
                switch (lista.get(position).rating) {
                    case Love:
                        aq.id(R.id.imageViewSeasonsEpisodeLoveTag).visible();
                        aq.id(R.id.imageViewSeasonsEpisodeHateTag).gone();
                        break;
                    case Hate:
                        aq.id(R.id.imageViewSeasonsEpisodeLoveTag).gone();
                        aq.id(R.id.imageViewSeasonsEpisodeHateTag).visible();
                        break;

                }
            } else {
                aq.id(R.id.imageViewSeasonsEpisodeLoveTag).gone();
                aq.id(R.id.imageViewSeasonsEpisodeHateTag).gone();

            }

            if (lista.get(position).inWatchlist)
                aq.id(R.id.imageViewSeasonsEpisodeWatchlistTag).visible();
            else
                aq.id(R.id.imageViewSeasonsEpisodeWatchlistTag).gone();

            return convertView;
        }

        @Override
        public int getCountForHeader(int i) {
            return mListHeaders.get(i).episodes.episodes.size();
        }

        @Override
        public int getNumHeaders() {
            return mListHeaders.size();
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.seasons_seasons_item, parent, false);
            }
            AQuery aq = new AQuery(convertView);

            aq.id(R.id.textViewSeasonsSeasonNumber).text("Season " + mListHeaders.get(position).season);

            return convertView;
        }
    }

}

