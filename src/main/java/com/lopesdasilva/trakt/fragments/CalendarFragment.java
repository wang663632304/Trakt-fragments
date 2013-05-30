package com.lopesdasilva.trakt.fragments;

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
import com.lopesdasilva.trakt.R;

import java.text.SimpleDateFormat;

/**
 * Created by lopesdasilva on 26/05/13.
 */
public class CalendarFragment extends Fragment {

    private View rootView;
    private ServiceManager manager;
    private WeekPager mWeekPagerAdapter;
    private ViewPager mViewPager;

    public CalendarFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.calendar_fragment, container, false);

        if (savedInstanceState == null) {
            setRetainInstance(true);


            mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
            mWeekPagerAdapter = new WeekPager(getActivity().getSupportFragmentManager());
            mWeekPagerAdapter.notifyDataSetChanged();
            mViewPager.setAdapter(mWeekPagerAdapter);
            mViewPager.setCurrentItem(3);
            mViewPager.setOffscreenPageLimit(0);

        }
        return rootView;
    }

    public class WeekPager extends FragmentPagerAdapter {

        private int mCount;

        public WeekPager(FragmentManager fm) {
            super(fm);
            mCount = 7;
        }

        @Override
        public Fragment getItem(int position) {
            int onWeekInMIliSecconds = 604800000;
            Bundle arguments = new Bundle();
            Fragment fragment  = new CalendarWeekFragment();
            Log.d("Trakt", "current Position: " + position);
            if (position < 3) {
                arguments.putInt("calendardate", -onWeekInMIliSecconds * (6 - position - 3));

            } else if (position > 3) {
                arguments.putInt("calendardate", onWeekInMIliSecconds * (position-3));

            }else
            arguments.putInt("calendardate",-1);
            fragment.setArguments(arguments);
            return fragment;
        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if (position == 3)
                return "this week";
            else if (position == 2)
                return "last week";
            else if (position == 4)
                return "next week";
            else {
                if (position < 3) {
                    int aux_position = 6 - position - 3;
                    return aux_position + " weeks ago";
                } else
                    return String.valueOf(position - 3) + " weeks ahead";
            }

        }
    }


}
