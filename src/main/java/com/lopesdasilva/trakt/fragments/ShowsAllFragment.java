package com.lopesdasilva.trakt.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import com.androidquery.AQuery;
import com.jakewharton.trakt.ServiceManager;
import com.jakewharton.trakt.entities.TvShow;
import com.lopesdasilva.trakt.R;
import com.lopesdasilva.trakt.Tasks.DownloadTrendingShows;
import com.lopesdasilva.trakt.activities.ShowActivity;
import com.lopesdasilva.trakt.extras.UserChecker;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lopesdasilva on 04/06/13.
 */
public class ShowsAllFragment extends Fragment implements DownloadTrendingShows.onTrendingShowListTaskComplete {

    private View rootView;
    private GridView mGridView;
    private List<TvShow> mShowsList = new LinkedList<TvShow>();
    private List<TvShow> mShowsListShowing = new LinkedList<TvShow>();
    private DownloadTrendingShows mTrendingShowsTask;
    private ServiceManager manager;
    private ShowsGridAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState == null) {

            manager = UserChecker.checkUserLogin(getActivity());
            mTrendingShowsTask = new DownloadTrendingShows(this, getActivity(), manager);
            mTrendingShowsTask.execute();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.shows_fragment, container, false);
//        updateView(mShowsList);

        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);

//        mGridView = (GridView) rootView.findViewById(R.id.gridLayoutShows);
        mAdapter = new ShowsGridAdapter(getActivity(), mShowsListShowing);
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle arguments = new Bundle();
                arguments.putString("show_imdb", mShowsList.get(i).imdbId);

                Intent intent = new Intent(getActivity(), ShowActivity.class);
                intent.putExtras(arguments);

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
            }
        });

    }

    @Override
    public void onTrendingShowListTaskComplete(List<TvShow> response) {
        this.mShowsList = response;
        updateView(mShowsList);
    }

    public void updateView(List<TvShow> response) {
//        DisplayMetrics metrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//        int widthPixels = metrics.widthPixels;
//        Log.d("Trakt", response.size() + " size" + (widthPixels / 240));
//        if (response.size() >= (widthPixels / 250)) {
//
//            int numColumns=(widthPixels / 250);
            mShowsListShowing.clear();
            mShowsListShowing.addAll(response);
////            mGridView.setNumColumns(numColumns);
            mAdapter.notifyDataSetChanged();
//        }

    }

    public class ShowsGridAdapter extends BaseAdapter {

        private final List<TvShow> mList;
        private final LayoutInflater inflater;

        public ShowsGridAdapter(Context context, List<TvShow> mList) {

            this.mList = mList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.shows_grid_item, parent, false);
            }

            AQuery aq = new AQuery(convertView);
            aq.id(R.id.imageViewShowsPoster).progress(R.id.progress).image(mList.get(position).images.poster, true, true, 100, R.drawable.poster, null, AQuery.FADE_IN);
            aq.id(R.id.textViewShowsShowTitle).text(mList.get(position).title);
            aq.id(R.id.textViewShowsShowPercentage).text(mList.get(position).ratings.percentage+"%");
            return convertView;
        }
    }
}
