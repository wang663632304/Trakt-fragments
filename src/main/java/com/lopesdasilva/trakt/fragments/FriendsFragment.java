package com.lopesdasilva.trakt.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.androidquery.AQuery;
import com.jakewharton.trakt.ServiceManager;
import com.jakewharton.trakt.entities.ActivityItem;
import com.lopesdasilva.trakt.R;
import com.lopesdasilva.trakt.extras.UserChecker;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lopesdasilva on 15/06/13.
 */
public class FriendsFragment extends Fragment {


    private View rootView;
    private FriendsTaskDownload mFriendsTask;
    private ServiceManager manager;
    private List<ActivityItem> activityList = new LinkedList<ActivityItem>();
    private LayoutInflater inflater;
    private LinearLayout mFriendsLayout;

    public FriendsFragment() {

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        rootView = inflater.inflate(R.layout.friends_fragment, container, false);
//        Log.d("Trakt", "OnCreateView");
        manager = UserChecker.checkUserLogin(getActivity());


        mFriendsTask = new FriendsTaskDownload();
        mFriendsTask.execute();

        return rootView;

    }

    public class FriendsTaskDownload extends AsyncTask<Void, Void, List<ActivityItem>> {

        private Exception e;

        @Override
        protected List<ActivityItem> doInBackground(Void... voids) {
            try {
                Date d = new Date();
                int onWeekInMIliSecconds = 604800000;
                d.setTime(d.getTime() - onWeekInMIliSecconds);
                return manager.activityService().friends().timestamp(d).fire().activity;
            } catch (Exception e) {
                this.e = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<ActivityItem> result) {
            if (e == null) {
                updateList(result);
            }
            Log.d("Trakt", "Error downloading Friends list");

        }

    }

    private void updateList(List<ActivityItem> result) {
        activityList.clear();
        activityList.addAll(result);

        mFriendsLayout = (LinearLayout) rootView.findViewById(R.id.linearLayoutFriends);

        int end;
        if (activityList.size() >= 5)
            end = 5;
        else
            end = activityList.size();
        for (ActivityItem activityItem : activityList.subList(0, end)) {

            View activityItemLayout = setActivity(activityItem);
//
            mFriendsLayout.addView(activityItemLayout);
        }


    }

    private View setActivity(ActivityItem activityItem) {
        RelativeLayout relativeLayoutFriends = (RelativeLayout) inflater.inflate(R.layout.friends_item_episode, null).findViewById(R.id.relativeLayoutFriendsItem);
        AQuery aq = new AQuery(relativeLayoutFriends);
        aq.id(R.id.textViewFriendsUsername).text(activityItem.user.username);
        aq.id(R.id.textViewFriendsAction).text(activityItem.action.toString());

        switch (activityItem.action) {

            case All:
                break;
            case Watching:
                aq.id(R.id.textViewFriendsAction).text("is "+activityItem.action.toString());
                break;
            case Scrobble:
                break;
            case Checkin:
                break;
            case Seen:
                break;
            case Collection:
                break;
            case Rating:
                aq.id(R.id.textViewFriendsAction).text("rated as "+activityItem.rating);
                break;
            case Watchlist:
                aq.id(R.id.textViewFriendsAction).text("added to their"+activityItem.action.toString());
                break;
            case Shout:
                break;
            case Created:
                break;
            case ItemAdded:
                break;
        }

        aq.id(R.id.textViewFriendsTime).text(activityItem.when.day +" "+activityItem.when.time);
        switch (activityItem.type) {

            case All:
                break;
            case Episode:
                aq.id(R.id.imageViewFriendsPoster).image(activityItem.episode.images.screen);
                aq.id(R.id.textViewFriendsItemTitle).text(activityItem.show.title + " S" + activityItem.episode.season + "E" + activityItem.episode.number);
                break;
            case Show:
                aq.id(R.id.imageViewFriendsPoster).image(activityItem.episode.images.poster);
                break;
            case Movie:
                aq.id(R.id.imageViewFriendsPoster).image(activityItem.movie.images.fanart);
                aq.id(R.id.textViewFriendsItemTitle).text(activityItem.movie.title);
                break;
            case List:
                break;
        }
        return relativeLayoutFriends;

    }


}
