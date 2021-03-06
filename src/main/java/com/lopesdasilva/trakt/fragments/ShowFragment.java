package com.lopesdasilva.trakt.fragments;

import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.jakewharton.trakt.ServiceManager;
import com.jakewharton.trakt.entities.TvShow;
import com.lopesdasilva.trakt.R;
import com.lopesdasilva.trakt.Tasks.DownloadShowInfo;
import com.lopesdasilva.trakt.extras.UserChecker;

public class ShowFragment extends Fragment implements ActionBar.TabListener, DownloadShowInfo.onShowInfoTaskComplete {

    private View rootView;
    private ServiceManager manager;
    private DownloadShowInfo mTaskDownloadShowInfo;
    private ActionBar actionBar;
    private TvShow mTVshow;

    public ShowFragment() {

    }


    SectionsPagerAdapter mSectionsPagerAdapter;


    ViewPager mViewPager;


    public String show;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            setRetainInstance(true);
            show = getArguments().getString("show_imdb");

            manager = UserChecker.checkUserLogin(getActivity());


            Log.d("Trakt", "ServiceManager: " + manager);
            Log.d("Trakt", "Show_imdb received: " + show);

            mTaskDownloadShowInfo = new DownloadShowInfo(this, getActivity(), manager, show);
            mTaskDownloadShowInfo.execute();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.show_fragment, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,
                              FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        Log.d("trakt", "tab selected position: " + tab.getPosition());
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
        Log.d("trakt", "tab unselected position: " + tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
        Log.d("trakt", "tab reselected position: " + tab.getPosition());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (actionBar != null)
            actionBar.removeAllTabs();
        Log.d("trakt", " Detaching ShowFragment should Remove all tabs");
    }

    @Override
    public void onShowInfoTaskComplete(TvShow response) {

        Log.d("Trakt", "Download show info complete");

        updateShow(response);


    }

    private void updateShow(TvShow response) {
        if(getActivity()!=null){
            actionBar = getActivity().getActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle(response.title);

        mTVshow = response;

        mViewPager = (ViewPager) rootView.findViewById(R.id.show_pager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getActivity().getSupportFragmentManager());
//        mSectionsPagerAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mSectionsPagerAdapter);

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab()
                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }

        mViewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });

        }
    }

    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle arguments = new Bundle();

            Log.d("trakt", "Fragment position: " + position);
            Fragment fragment = null;
            switch (position) {
                case 0:
                    arguments.putSerializable("show", mTVshow);
                    fragment = new ShowInfoFragment();
                    break;
                case 1:
                    arguments.putSerializable("show", mTVshow);
                    fragment = new SeasonsFragment();
                    break;
                case 2:
                    arguments.putSerializable("show", mTVshow);
                    fragment = new ShowCommentsFragment();
                    break;
                case 3:
                    arguments.putSerializable("showpeople", mTVshow.people);
                    fragment = new ShowCastFragment();
                    break;

            }
            fragment.setArguments(arguments);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Info".toUpperCase(l);
                case 1:
                    return "Seasons".toUpperCase(l);
                case 2:
                    return "Shouts".toUpperCase(l);
                case 3:
                    return "Cast".toUpperCase(l);
            }
            return null;
        }


    }


}
