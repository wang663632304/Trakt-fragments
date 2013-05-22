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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.show_fragment, container, false);
        if (savedInstanceState == null) {
            setRetainInstance(true);
            show = getArguments().getString("show_imdb");

            manager = UserChecker.checkUserLogin(getActivity());


            Log.d("Trakt", "ServiceManager: " + manager);
            Log.d("Trakt", "Show_imdb received: " + show);

            mTaskDownloadShowInfo = new DownloadShowInfo(this, getActivity(), manager, show);
            mTaskDownloadShowInfo.execute();
        }
        return rootView;
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab,
                              FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        Log.d("trakt", "tab position: " + tab.getPosition());
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
        Log.d("trakt", "tab position: " + tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
        Log.d("trakt", "tab position: " + tab.getPosition());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("trakt", " Detaching ShowFragment should Remove all tabs");
    }

    @Override
    public void onShowInfoTaskComplete(TvShow response) {
        Toast.makeText(getActivity(), "Download Show info complete", Toast.LENGTH_SHORT).show();
        Log.d("Trakt", "Download show info complete");

        actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setSubtitle(response.title);

        mTVshow = response;

        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getActivity().getSupportFragmentManager());
        mSectionsPagerAdapter.notifyDataSetChanged();
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
        mViewPager.setCurrentItem(1);

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
            arguments.putSerializable("show", mTVshow);
            Log.d("trakt", "Fragment position: " + position);
            Fragment fragment = new SeasonsFragment();
            switch (position) {
                case 0:
                    fragment = new SeasonsFragment();
                    break;
                case 1:
                    fragment = new ShowInfoFragment();
                    break;
                case 2:
                    fragment = new SeasonsFragment();
                    break;

            }
            fragment.setArguments(arguments);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Shouts".toUpperCase(l);
                case 1:
                    return "Info".toUpperCase(l);
                case 2:
                    return "Seasons".toUpperCase(l);
            }
            return null;
        }


    }


}