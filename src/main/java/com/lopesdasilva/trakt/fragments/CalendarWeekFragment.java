package com.lopesdasilva.trakt.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.androidquery.AQuery;
import com.jakewharton.trakt.ServiceManager;
import com.jakewharton.trakt.entities.CalendarDate;
import com.lopesdasilva.trakt.R;
import com.lopesdasilva.trakt.Tasks.DownloadWeekCalendar;
import com.lopesdasilva.trakt.activities.EpisodeActivity;
import com.lopesdasilva.trakt.extras.UserChecker;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapter;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lopesdasilva on 27/05/13.
 */
public class CalendarWeekFragment extends Fragment implements DownloadWeekCalendar.OnWeekTaskCompleted {

    private ServiceManager manager;
    private DownloadWeekCalendar mTaskDownloadWeekCalendar;
    private List<CalendarDate> weekCalendar = new LinkedList<CalendarDate>();
    private int mDate = 0;
    private WeekCalendarAdapter mAdapter;

    private List<CalendarDate.CalendarTvShowEpisode> lista = new LinkedList<CalendarDate.CalendarTvShowEpisode>();
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        mDate = getArguments().getInt("calendardate");

        manager = UserChecker.checkUserLogin(getActivity());
        Log.d("Trakt", "Fragment Calendar Week Launched");
        Date d = new Date();
        if (mDate != -1) {
            d.setTime(d.getTime() + (mDate));
        }

        mTaskDownloadWeekCalendar = new DownloadWeekCalendar(this, getActivity(), manager, d);
        mTaskDownloadWeekCalendar.execute();

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.calendarweek_fragment, container, false);


        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        StickyGridHeadersGridView l = (StickyGridHeadersGridView) rootView.findViewById(R.id.listViewCalendar);
        mAdapter = new WeekCalendarAdapter(getActivity(), weekCalendar, lista);
        l.setAdapter(mAdapter);

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Bundle arguments = new Bundle();
                arguments.putString("show_imdb", lista.get(position).show.imdbId);
                arguments.putInt("show_season", lista.get(position).episode.season);
                arguments.putInt("show_episode", lista.get(position).episode.number);
                Intent i = new Intent(getActivity(), EpisodeActivity.class);
                i.putExtras(arguments);

                startActivity(i);
                getActivity().overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);


            }
        });
        l.setLongClickable(true);
        l.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(lista.get(position).show.title);
                builder.setItems(R.array.calendar_longclick, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                Toast.makeText(getActivity(), "Long click", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }


    @Override
    public void onWeekInfoComplete(List<CalendarDate> response) {
        Toast.makeText(getActivity(), "Downloading Complete", Toast.LENGTH_SHORT).show();


        lista.clear();
        weekCalendar.clear();
        weekCalendar.addAll(response);
        for (CalendarDate l : weekCalendar) {
            lista.addAll(l.episodes);
        }
        mAdapter.notifyDataSetChanged();

    }

    public class WeekCalendarAdapter extends BaseAdapter implements StickyGridHeadersBaseAdapter {

        private final List<CalendarDate> mListHeaders;
        private final List<CalendarDate.CalendarTvShowEpisode> lista;
        private LayoutInflater inflater;

        public WeekCalendarAdapter(Context context, List<CalendarDate> mList, List<CalendarDate.CalendarTvShowEpisode> lista) {
            this.mListHeaders = mList;
            this.lista = lista;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return lista.size();
        }

        @Override
        public CalendarDate.CalendarTvShowEpisode getItem(int i) {
            return lista.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.calendarweek_item_episode, parent, false);
            }

            AQuery aq = new AQuery(convertView);
            aq.id(R.id.imageViewCalendarEpisodeBackdrop).image(lista.get(position).episode.images.screen, false, true, 200, R.drawable.episode_backdrop);
            aq.id(R.id.textViewCalendarShowTitle).text(lista.get(position).show.title);
            aq.id(R.id.textViewCalendarEpisodeSeasonNumberAndEpisodeNumber).text("S" + lista.get(position).episode.season + "E" + lista.get(position).episode.number);
            aq.id(R.id.textViewCalendarEpisodeTitle).text(lista.get(position).episode.title);
            if (lista.get(position).episode.watched)
                aq.id(R.id.imageViewCalendarEpisodeSeenTag).visible();
            else
                aq.id(R.id.imageViewCalendarEpisodeSeenTag).gone();
            aq.recycle(convertView);
            return convertView;
        }

        @Override
        public int getCountForHeader(int i) {
            return mListHeaders.get(i).episodes.size();
        }

        @Override
        public int getNumHeaders() {
            return mListHeaders.size();
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.calendarweek_item, parent, false);
            }
            AQuery aq = new AQuery(convertView);
            SimpleDateFormat format = new SimpleDateFormat("E MM dd, yyyy");
            aq.id(R.id.textViewCalendarWeekDay).text(format.format(mListHeaders.get(position).date) + "");

            return convertView;
        }
    }
}
